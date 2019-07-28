package Servers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Log2 {
    private File file;

    protected Log2(String fileName){
        file = new File(fileName);
    }

    public void writeToFile(String line){
        try (FileWriter fw = new FileWriter(file, true)) {
            fw.write(line + "\n" + "==========================\n");
            fw.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }



    }



}
