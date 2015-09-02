#!/usr/bin/env php
<?php

if (!isset($argv[1])) {
	throw new Exception("File not specified as argument");
}

$xlsFilename = $argv[1];

if (!file_exists($xlsFilename)) {
	throw new Exception("File does not exist");
}

$xlsContent = file_get_contents($xlsFilename);

$xml = simplexml_load_string(utf8_encode($xlsContent));

$csvData = [
	['Date','Description','Credit Amount','Debit Amount','Balance'],
];

$i = 0;
foreach ($xml->body->table->tr as $row) {
	$i++;

	// Skip rows 1-5, they have nonsense/blankness in
	if (in_array($i, [1, 2, 3, 4, 5])) continue;

	$csvRow = [];

	$c = 0;
	foreach ($row->td as $col) {
		$c++;

		// Skip these columns, they are blank
		if (in_array($c, [1, 3, 5, 9])) continue;
		$value = trim((string)$col->font);

		// Format currency columns as numbers correctly
		if (in_array($c, [6, 7, 8])) {
			$value = str_replace('£ ', '', $value);
			$value = str_replace(',', '', $value);
		}

		$csvRow[] = $value;
	}

	// Only keep this row if it has content
	if ($csvRow[0] != '') {
		$csvData[] = $csvRow;
	}
}

$csvFilename = str_replace(".xls", ".csv", $xlsFilename);

$fh = fopen($csvFilename, 'w');
foreach ($csvData as $fields) {
	fputcsv($fh, $fields);
}
fclose($fh);

unlink($xlsFilename);
