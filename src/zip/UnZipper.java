/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zip;

import Utils.FileDelete;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

/**
 *
 * @author root
 */
public class UnZipper {

    String sourceZipFile = "ZipFileNAme.zip";
    String outputFolder = "OutputFolderName";

    public UnZipper() {

//        doUnzip(sourceZipFile, outputFolder);
    }

    //unzip can be done only if this application zipped it
    public void doUnzip(String zipFile, String outputFolder, JLabel jLabel, JProgressBar jProgressBar) throws FileNotFoundException, IOException, InterruptedException {

        jLabel.setText("Unzipping : ");
        String zipFileName = zipFile;
//                System.out.println("zipFile : "+zipFile);
//                System.out.println("outputFolder : "+outputFolder); 6
        String destDirectory = outputFolder;
        File destDirectoryFolder = new File(destDirectory);
        if (!destDirectoryFolder.exists()) {
//                    System.out.println("Created Destination Directory : "+destDirectory); 5
            destDirectoryFolder.mkdir();
        }
//                System.out.println("Destination Directory exist : "+destDirectoryFolder.exists() + " path : "+destDirectoryFolder.getAbsolutePath()); 4

        byte[] buffer = new byte[1024];
        FileInputStream fileInputStream = new FileInputStream(zipFileName);
        ZipInputStream zis = new ZipInputStream(fileInputStream);
        ZipEntry zipEntry = zis.getNextEntry();
//                System.out.println("ZipEntry : "+zipEntry); 3
        while (zipEntry != null) {
            String zipEntryFileName = zipEntry.getName();
            String filePath = destDirectory + File.separator + zipEntry.getName();
            final long currentProgress = (int) ((((double) fileInputStream.getChannel().position()) / ((double) fileInputStream.getChannel().size())) * 100d);

            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    jProgressBar.setValue((int) currentProgress);
                    jLabel.setText("<html>Unzipping : "+ destDirectory + "<br>"+File.separator +zipEntryFileName+"</html>");

                }
            });
//                        System.out.println("Zip position"+fileInputStream.getChannel().position() + " File Size :"+fileInputStream.getChannel().size());
//			System.out.println("Unzipping "+filePath+" zipEntry-Name : "+zipEntry.getName()); 1
            if (!zipEntry.isDirectory()) {
//                            System.out.println("ZipEntry is direcotry: "+zipEntry.isDirectory()); 2
                File currentFile = new File(filePath);
                new File(currentFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(currentFile);

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();
            } else {
                File dir = new File(filePath);
//                                System.out.println("Created Dir: "+filePath); 7
                dir.mkdir();
            }
            zis.closeEntry();
            zipEntry = zis.getNextEntry();
            System.out.println("Zip position : " + fileInputStream.getChannel().position() + " File Size : " + fileInputStream.getChannel().size() + " Avaialble Bytes : " + fileInputStream.available());

        }
        final long currentProgress = (int) ((((double) fileInputStream.getChannel().position() + fileInputStream.available()) / ((double) fileInputStream.getChannel().size())) * 100d);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                jProgressBar.setValue((int) currentProgress);
                jLabel.setText("Installed !!");

            }
        });

        System.out.println("last Zip position : " + fileInputStream.getChannel().position() + " File Size : " + fileInputStream.getChannel().size() + " Avaialble Bytes : " + fileInputStream.available());

        zis.closeEntry();
        zis.close();


//		System.out.println("Unzipping complete"); 8

    }

}
