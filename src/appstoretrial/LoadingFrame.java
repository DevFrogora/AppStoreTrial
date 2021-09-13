/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appstoretrial;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.riversun.promise.Func;
import org.riversun.promise.Promise;

/**
 *
 * @author root
 */
public class LoadingFrame extends javax.swing.JFrame {

    AppStoreListConfig appStoreListConfig = null;

    /**
     * Creates new form LoadingFrame
     */
    public LoadingFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jProgressBar1 = new javax.swing.JProgressBar();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("jLabel1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(91, 91, 91)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(95, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(97, 97, 97)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(105, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    void sleep(long milisecond) {
        try {
            Thread.sleep(milisecond);
        } catch (InterruptedException ex) {
            Logger.getLogger(LoadingFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void loading() {

        long sleepTime = 200;
        Func function1 = (action, data) -> {
            new Thread(() -> {
                if (!Skipper.SKIP_DOWNLOADING_JSON) {
                    try {
                        Configuration.getAppStoreListConfig().saveJsonString(
                                new JSONManager().readJsonUrl(FilePath.APP_STORE_REPO + FilePath.Online_AppList_Config_File)
                        );
                        appStoreListConfig = Configuration.getAppStoreListConfig();
                    } catch (IOException ex) {
                        Logger.getLogger(LoadingFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

//                jProgressBar1.setVisible(true);
                sleep(sleepTime);
                jProgressBar1.setBackground(Color.WHITE);
                jProgressBar1.setForeground(Color.decode("#C7FF33"));
                jProgressBar1.setString("20%");
                jProgressBar1.setStringPainted(true);
                jProgressBar1.setValue(20);

                action.resolve();
            }).start();
        };
        Func function2 = (action, data) -> {
            sleep(sleepTime);
            if (!Skipper.SKIP_LODING_LOCAL_JSON) {

                Configuration.getLocalConfig().loadLocalConfig(FilePath.Local_AppStore_Directory + FilePath.Local_AppList_Config_File);
            }
            Configuration.getAppStoreListConfig().doSomething();
            jProgressBar1.setString("40%");
            jProgressBar1.setValue(40);
            action.resolve();
        };
        Func function3 = (action, data) -> {
            sleep(sleepTime);
            Configuration.getAppStoreListConfig().doSomething();
            jProgressBar1.setString("60%");
            jProgressBar1.setValue(60);
            action.resolve();
        };
        Func function4 = (action, data) -> {
            sleep(sleepTime);
            Configuration.getAppStoreListConfig().doSomething();
            jProgressBar1.setString("80%");
            jProgressBar1.setValue(80);
            action.resolve();
        };
        Func function5 = (action, data) -> {
            sleep(sleepTime);
            Configuration.getAppStoreListConfig().doSomething();
            jProgressBar1.setString("100%");
            jProgressBar1.setValue(100);
            this.dispose();
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    JSONObject jsonObj = appStoreListConfig.getJSONObject();

//extracting data array from json string
                    JSONArray ja_data = jsonObj.getJSONArray("Apps");
                    int length = ja_data.length();
                    System.out.println("Config : " + jsonObj.toString());
                    System.out.println("array : " + ja_data.toString());
                    System.out.println("length : " + length);
                    for (int i = 0; i < length; i++) {
                        //{
//   "AppStoreName":"lms",
//   "Apps":[
//      {
//         "appId": "1",
//         "appName":"Chef App",
//         "Description":"It will cook Momo For you",
//         "AppIcon":"https://cdn.discordapp.com/emojis/600065479910096925.png?v=1",
//         "appZipFileName":"https://cdn.discordapp.com/emojis/600065479910096925.png?v=1",
//         "appVersionNumber":"1"
//      }
//   ]
//}
                        JSONObject jObj = ja_data.getJSONObject(i);
                        int appId = jObj.getInt("appId");
                        String appName = jObj.getString("appName");
                        String appDescription = jObj.getString("Description");
                        String appIconUrl = jObj.getString("AppIcon");
                        String appZipFileName = jObj.getString("appZipFileName");
                        String appVersionNumber = jObj.getString("appVersionNumber");
                        String startPath = jObj.getString("startPath");

                        AppStoreFrame appStoreFrame = new AppStoreFrame();
                        appStoreFrame.pack();
                        appStoreFrame.setVisible(true);
                        URL url;
                        Image image;
                        try {
                            url = new URL(appIconUrl);
                            image = ImageIO.read(url);

                            //added App
                            appStoreFrame.addAppInPanel(appId, appName, appDescription, image, appZipFileName, appVersionNumber, startPath);

                        } catch (MalformedURLException ex) {
                            Logger.getLogger(LoadingFrame.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(LoadingFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }

                }
            }
            );
            action.resolve();
        };

        Promise.resolve()
                .then(new Promise(function1))
                .then(new Promise(function2))
                .then(new Promise(function3))
                .then(new Promise(function4))
                .then(new Promise(function5))
                .start();// start Promise operation

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LoadingFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoadingFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoadingFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoadingFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoadingFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JProgressBar jProgressBar1;
    // End of variables declaration//GEN-END:variables
}