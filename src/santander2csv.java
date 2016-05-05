/**
 * Tool to convert Santander XLS to Clever Accounts CSV files
 */
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.*;

public class santander2csv {
    public static void main(String[] args)
    {
        try {
            File inputFile = null;

            if (args.length >= 1) {
                String argsStr = "";
                for(String s : args) {
                    argsStr = argsStr.concat(s);
                }
                inputFile = new File(argsStr);
                if (!inputFile.exists()) {
                    inputFile = null;
                }
            }

            if (null == inputFile) {
                JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
                fileChooser.setFileFilter(new FileNameExtensionFilter("Excel spreadsheet (.xls)", "xls"));

                if (fileChooser.showDialog(null, "Convert") != JFileChooser.APPROVE_OPTION) {
                    System.exit(0);
                }

                inputFile = fileChooser.getSelectedFile();
            }

            File outputFile = new File(inputFile.getAbsolutePath().replace(".xls", ".csv"));

            FileWriter writer = new FileWriter(outputFile);
            writer.write(toCsvString(parse(inputFile)));
            writer.close();

            JOptionPane.showMessageDialog(
                    null,
                    "Complete!\n\nThe converted file has been saved as \""
                            .concat(outputFile.getName())
                            .concat("\" in the same directory as the original file."),
                    "santander2csv Information",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(
                    null,
                    exception.getMessage(),
                    "santander2csv Failure",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        System.exit(0);
    }

    private static List<List<String>> parse(File file) throws Exception
    {
        Document document = Jsoup.parse(file, "ISO-8859-1");

        Element tableElement = document.getElementsByTag("body").first().getElementsByTag("table").first();

        List<List<String>> csv = new ArrayList<>();

        List<String> csvHeader = new ArrayList<>(Arrays.asList("Date","Description","Credit Amount","Debit Amount","Balance"));
        csv.add(csvHeader);

        int row = 0;
        for (Element trElement : tableElement.getElementsByTag("tr")) {
            row++;

            if (row <= 5) {
                continue;
            }

            List<String> csvRowItems = new ArrayList<>();

            int col = 0;
            for (Element tdElement : trElement.getElementsByTag("td")) {
                col++;

                if (col == 1 || col == 3 || col == 5 || col == 9) {
                    continue;
                }

                if (tdElement.getElementsByTag("font").size() == 0) {
                    continue;
                }

                String value = tdElement.getElementsByTag("font").first().text();

                if (col == 6 || col == 7 || col == 8) {
                    value = value.replace("£ ", "");
                    value = value.replace(",", "");
                }

                csvRowItems.add(value);
            }

            if (csvRowItems.size() > 0) {
                csv.add(csvRowItems);
            }
        }
        return csv;
    }

    private static String toCsvString(List<List<String>> csvItems)
    {
        String out = "";
        for (List<String> row : csvItems) {
            for (String col : row) {
                out = out.concat("\"").concat(col).concat("\",");
            }
            out = out.substring(0, out.length()-1).concat("\n");
        }
        return out;
    }
}
