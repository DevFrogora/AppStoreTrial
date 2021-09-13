/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package launch;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author root
 */
public class Launcher {
    

    public void run(String batchFilePath) {
        System.out.println(batchFilePath);
        //https://stackoverflow.com/questions/17063947/get-current-batchfile-directory/17064031#17064031
        ProcessBuilder pb = new ProcessBuilder(batchFilePath);
        pb.redirectError();
        try {
            Process p = pb.start();
            try (InputStream inputStream = p.getInputStream()) {
                int in = -1;
                while ((in = inputStream.read()) != -1) {
                    System.out.print((char) in);
                }
            }
            System.out.println("Exited with " + p.waitFor());
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }

    }

}
