# santander2csv

Quick and dirty Java JAR that takes an XLS downloaded from Santander UK online banking and simplifies it into a basic
CSV (conveniently suitable for Clever Accounts).

Usage: 

## Linux

 * Clone or download this project (e.g. to `/home/user/santander2csv`)
 * Make it global: `sudo ln -s /home/user/santander2csv/santander2csv.sh /usr/local/bin/santander2csv`
 * Using Nautilus-Actions configuration, create an action to invoke `/usr/local/bin/santander2csv`
 * When you download the XLS from Santander, you can now right click > santander2csv
 * It will replace it with a .csv file you can upload to Clever Accounts

You can also run directly on the shell, e.g.:

```shell
$ santander2csv your-downloaded-statement.xls
```

## Windows

 * Clone or download this project (e.g. to `C:\Program Files\santander2csv`)
 * Open an Explorer window
 * Type `shell:sendto`
 * Create a shortcut to the `start.bat` (e.g. `C:\Program Files\santander2csv\start.bat`), call it "santander2csv"
 * When you download the XLS from Santander, you can now right click > Send To > santander2csv
 * It will replace it with a .csv file you can upload to Clever Accounts
