/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import acq.*;
import java.util.ArrayList;

/**
 *
 * @author Bruger
 */
public class DataFacade implements IData {
    FileManager fm;

    public DataFacade() {
        fm = new FileManager();
    }
    public void writeData(ArrayList<String> data, String filePath) {
        fm.writeToFile(data, "test");
    }
}
