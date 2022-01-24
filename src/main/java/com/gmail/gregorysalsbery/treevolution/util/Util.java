package com.gmail.gregorysalsbery.treevolution.util;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Util {

    public static int gridXToScreenX(int gridX) {
        return gridX * Config.CELL_SIZE;
    }

    public static int gridYToScreenY(int gridY) {
        return Config.SCREEN_SIZE_Y - (gridY + 1) * Config.CELL_SIZE;
    }

    public static List<List<String>> readCsvToList(String path) throws IOException, CsvValidationException {
        List<List<String>> records = new ArrayList<List<String>>();
        CSVReader csvReader = new CSVReader(new FileReader(path));
            String[] values = null;
            while ((values = csvReader.readNext()) != null) {
                records.add(Arrays.asList(values));
            }
        return records;
    }

    public static void writeListToCsv(String path, List<List<String>> writeList) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(path));
        for(List<String> row : writeList) {
            String[] rowArray = new String[row.size()];
            rowArray = row.toArray(rowArray);
            writer.writeNext(rowArray);
        }
        writer.close();
    }
}
