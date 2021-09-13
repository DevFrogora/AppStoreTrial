/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.io.File;
import java.net.URI;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

/**
 *
 * @author root
 */
public class FileDelete {

    // function to delete subdirectories and files
    public static long deleteDirectory(File file, long appFolderSize, long deletedSize, JProgressBar uninstalJProgressBar, JLabel progressbarStatus, JLabel progressbarDetails) {
        // store all the paths of files and folders present
        // inside directory
        System.out.println("Init DeletedSize" + deletedSize);
        for (File subfile : file.listFiles()) {

            // if it is a subfolder,e.g Rohan and Ritik,
            // recursiley call function to empty subfolder
            deletedSize = deletedSize + subfile.length();
            if (subfile.isDirectory()) {
                deletedSize = deleteDirectory(subfile, appFolderSize, deletedSize, uninstalJProgressBar, progressbarStatus, progressbarDetails);
            }
            
            //for jlabel relative path
            URI path1 = subfile.toURI();
            URI path2 = file.toURI();
            // create a relative path from the two paths
            URI relativePath = path2.relativize(path1);
            // convert the URI to string
            String path = relativePath.getPath();

            long currentProgress = (int) ((((double) deletedSize) / ((double) appFolderSize)) * 100d);
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    progressbarStatus.setText("Uninstalling : " + currentProgress + " %");
                    progressbarDetails.setText(path);
                    uninstalJProgressBar.setValue((int) currentProgress);

                }
            });

            System.out.println("deleted size : " + deletedSize + "/" + appFolderSize);
            // delete files and empty subfolders
            subfile.delete();
        }
        return deletedSize;
    }

    public static long folderSize(File directory) {
        long length = 0;
        for (File file : directory.listFiles()) {
            if (file.isFile()) {
                length += file.length();
            } else {
                length += folderSize(file);
            }
        }
        return length;
    }

    public static void deleteFile(File file) {

        if (file.delete()) {
            System.out.println("File deleted successfully");
        } else {
            System.out.println("Failed to delete the file");
        }

    }

}
