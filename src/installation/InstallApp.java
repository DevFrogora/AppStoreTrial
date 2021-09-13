/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package installation;

import Utils.FileDelete;
import appstoretrial.Configuration;
import appstoretrial.FilePath;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import org.json.JSONArray;
import org.json.JSONObject;
import org.riversun.promise.Func;
import org.riversun.promise.Promise;
import update.IUpdateCommunicator;
import zip.UnZipper;

/**
 *
 * @author root
 */
public class InstallApp {

    JLabel jLabel1 = null;
    JLabel jLabel2 = null;

    JProgressBar jProgressBar1 = null;
    String appZipFileName;
    JSONObject localConfigJSONObject, appJSONObject = null;
    IInstalCommunicator instalCommunicator = null;
    boolean isItForUpdate = false;
    IUpdateCommunicator iUpdateCommunicator = null;

    public InstallApp(JLabel jLabel1, JLabel jLabel2, JProgressBar jProgressBar1, String appZipFileName, JSONObject localConfigJSONObject, JSONObject appJSONObject, IInstalCommunicator iInstalCommunicator) {
        this.jLabel1 = jLabel1;
        this.jLabel2 = jLabel2;
        this.jProgressBar1 = jProgressBar1;
        this.appZipFileName = appZipFileName;
        this.localConfigJSONObject = localConfigJSONObject;
        this.appJSONObject = appJSONObject;
        this.instalCommunicator = iInstalCommunicator;
    }

    public InstallApp(JLabel jLabel1, JLabel jLabel2, JProgressBar jProgressBar1, String appZipFileName, JSONObject localConfigJSONObject, JSONObject appJSONObject, IInstalCommunicator iInstalCommunicator, IUpdateCommunicator iUpdateCommunicator) {
        this.jLabel1 = jLabel1;
        this.jLabel2 = jLabel2;
        this.jProgressBar1 = jProgressBar1;
        this.appZipFileName = appZipFileName;
        this.localConfigJSONObject = localConfigJSONObject;
        this.appJSONObject = appJSONObject;
        this.instalCommunicator = iInstalCommunicator;
        this.iUpdateCommunicator = iUpdateCommunicator;
    }

    public static class Checker {

        public static boolean isdownloaded = false;
        public static String filePath = null;
        public static boolean isUnzipped = false;

    }
    int i;

    public void setIsItForUpdate(boolean isItForUpdate) {
        this.isItForUpdate = isItForUpdate;
    }

    public int run() {

        Func function1 = (action, argData) -> {
            new Thread(() -> {
                try {
                    final String filename = appZipFileName;
                    URL url = new URL(FilePath.APP_STORE_REPO + "/" + filename);
                    HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
                    final long completeFileSize = httpConnection.getContentLength();

                    java.io.BufferedInputStream in = new java.io.BufferedInputStream(httpConnection.getInputStream());
                    String filepath = FilePath.Local_AppStore_Directory + "\\" + filename;
                    Checker.filePath = filepath;
                    java.io.FileOutputStream fos = new java.io.FileOutputStream(filepath);
                    java.io.BufferedOutputStream bout = new BufferedOutputStream(
                            fos, 1024);
                    byte[] data = new byte[1024];
                    long downloadedFileSize = 0;
                    int x = 0;
                    while ((x = in.read(data, 0, 1024)) >= 0) {
                        downloadedFileSize += x;

                        // calculate progress
                        final long currentProgress = (int) ((((double) downloadedFileSize) / ((double) completeFileSize)) * 100d);
                        final long downloadedSize = downloadedFileSize;
                        // update progress bar
                        SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                jLabel1.setText("Downloading " + filename + "  :  " + humanReadableByteCountBin(downloadedSize) + "/" + humanReadableByteCountBin(completeFileSize));
                                jLabel2.setText(String.valueOf(currentProgress) + "%");
                                jProgressBar1.setValue((int) currentProgress);
                            }
                        });

                        bout.write(data, 0, x);
                    }
                    bout.close();
                    in.close();
                    Checker.isdownloaded = true;

                } catch (FileNotFoundException e) {
                } catch (IOException e) {
                }
                action.resolve();
            }).start();
        };

        Func function2 = (action, argData) -> {
            new Thread(() -> {

                if (Checker.isdownloaded) {
                    jLabel1.setText("Installing ");
                    unzip(Checker.filePath);
                    Checker.isUnzipped = true;
                }
//                unzip();
                action.resolve();
            }).start();
        };

        Func function3 = (action, argData) -> {
            new Thread(() -> {

                if (Checker.isUnzipped) {
                    jLabel1.setText("Unzipped Done ");
                    if (!isItForUpdate) {
                        addAppInList(localConfigJSONObject, appJSONObject);
                        afterInstall(appZipFileName);
                        instalCommunicator.uiUpdate();

                    }
                    if (isItForUpdate) {
                        afterInstall(appZipFileName);
                        addAppInList(localConfigJSONObject, appJSONObject);
                        System.out.println("Ui Config Update Is going to going to write");
                        iUpdateCommunicator.uiUpdate();
                    }

                }
//                unzip();
                i = 1;
                action.resolve();
            }).start();
        };

        Promise.resolve()
                .then(new Promise(function1))
                .then(new Promise(function2))
                .then(new Promise(function3))
                .start();// start Promise operation
        return i;

    }

    public void afterInstall(String appZipFileName) {
        String zipPath = FilePath.Local_AppStore_Directory + "\\" + appZipFileName;
        System.out.println("Deleting Zip Path  : " + zipPath);
        File fileToDelete = new File(zipPath);
        FileDelete.deleteFile(fileToDelete);

    }

    public void addAppInList(JSONObject mainJSONObject, JSONObject appObject) {
        JSONArray appList = mainJSONObject.getJSONArray("Apps");
        appList.put(appObject);
        System.out.println("Adding" + mainJSONObject);
        Configuration.getLocalConfig().saveLocalCofig(FilePath.Local_AppStore_Directory + FilePath.Local_AppList_Config_File, mainJSONObject.toString());

    }

    void unzip(String filePath) {

        File f = new File(filePath);
        if (f != null) {
            String filename = f.getName();
            String filepath = f.getPath();
            jLabel1.setText("Unzipping : " + filepath);
            try {

                UnZipper unZipper = new UnZipper();
//                System.out.println("File path");
                System.out.println("filepath : " + filepath + "\n absolute path : " + stripExtension(filepath) + "\n Mydir Parent Path :" + f.getParent());
//                unZipper.doUnzip(filepath, stripExtension(filename));
//                unZipper.doUnzip(filepath, stripExtension(f.getParent()));
                unZipper.doUnzip(filepath, f.getParent() + File.separator + stripExtension(filename), jLabel2, jProgressBar1);

                jLabel1.setText("Installed");
//        zip_one_file(filepath, f.getParent()+"\\"+stripExtension(filename)+".zip", filename);
                System.out.println("filepath " + filepath + "\nFileParent :" + f.getParent() + "\\" + stripExtension(filename) + ".zip" + "\nfilename : " + stripExtension(filename));
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

    }

    public String stripExtension(String str) {
        // Handle null case specially.

        if (str == null) {
            return null;
        }

        // Get position of last '.'.
        int pos = str.lastIndexOf(".");

        // If there wasn't any '.' just return the string as is.
        if (pos == -1) {
            return str;
        }

        // Otherwise return the string, up to the dot.
        return str.substring(0, pos);
    }

    public static String humanReadableByteCountBin(long bytes) {
        long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        if (absB < 1024) {
            return bytes + " B";
        }
        long value = absB;
        CharacterIterator ci = new StringCharacterIterator("KMGTPE");
        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            ci.next();
        }
        value *= Long.signum(bytes);
        return String.format("%.1f %ciB", value / 1024.0, ci.current());
    }

}
