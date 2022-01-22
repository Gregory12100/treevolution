package com.gmail.gregorysalsbery.treevolution.tree.dna;

import com.gmail.gregorysalsbery.treevolution.app.util.Util;

import java.io.FileNotFoundException;
import java.util.List;

public class TreeNA {

    // read in treena file
    // assign a growing order to each tree part
    // during the sim, the tree just follows the predetermined build instructions
    // first part built is always top leaf and then works way down trunk
    // when multiple things could be built, just choose randomly



    public void loadFromCsvFile(String path) throws FileNotFoundException {
        List<List<String>> csvData = Util.readCsvToList(path);
    }
}
