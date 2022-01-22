package com.gmail.gregorysalsbery.treevolution.app.util;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

    public static List<List<String>> readCsvToList(String path) throws FileNotFoundException {
        List<List<String>> records = new ArrayList<List<String>>();
        try (CSVReader csvReader = new CSVReader(new FileReader(path));) {
            String[] values = null;
            while ((values = csvReader.readNext()) != null) {
                records.add(Arrays.asList(values));
            }
        } catch (CsvValidationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }
}
