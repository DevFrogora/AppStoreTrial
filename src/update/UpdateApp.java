/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package update;

import appstoretrial.Configuration;
import appstoretrial.FilePath;
import gui.AppTemplateCard;
import installation.IInstalCommunicator;
import installation.InstallApp;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import org.json.JSONObject;
import org.riversun.promise.Func;
import org.riversun.promise.Promise;
import uninstal.Uninstaller;

/**
 *
 * @author root
 */
public class UpdateApp {

    JLabel jLabel1 = null;
    JLabel jLabel2 = null;

    JProgressBar jProgressBar1 = null;
    String appZipFileName;
    JSONObject localConfigJSONObject, appJSONObject = null;
    int appId;
    JLabel progressbarStatus, progressbarDetails = null;
    IInstalCommunicator instalCommunicator = null;
    IUpdateCommunicator iUpdateCommunicator = null;

    public UpdateApp(String appZipFileName,
            int appId, JProgressBar jProgressBar1,
            JLabel progressbarStatus, JLabel progressbarDetails,
            JSONObject localConfigJSONObject, JSONObject appJSONObject,
            IInstalCommunicator iInstalCommunicator,
            IUpdateCommunicator iUpdateCommunicator) {
        this.appZipFileName = appZipFileName;
        this.appId = appId;
        this.jProgressBar1 = jProgressBar1;
        this.progressbarStatus = progressbarStatus;
        this.progressbarDetails = progressbarDetails;
        this.localConfigJSONObject = localConfigJSONObject;
        this.appJSONObject = appJSONObject;
        this.instalCommunicator = iInstalCommunicator;
        this.iUpdateCommunicator = iUpdateCommunicator;
    }

    public void run() {
        //To update -->uninstall then --->install
        Func function1 = (action, data) -> {
            new Thread(() -> {
                try {
                    // download(ziplinkUrl);

                    new Uninstaller(appZipFileName, appId, jProgressBar1, progressbarStatus, progressbarDetails).run();
                } catch (IOException ex) {
                    Logger.getLogger(AppTemplateCard.class.getName()).log(Level.SEVERE, null, ex);
                }
                action.resolve();
            }).start();
        };

        Func function2 = (action, data) -> {
            new Thread(() -> {
                // download(ziplinkUrl);
                try {
                    // TODO add your handling code here:
                    Configuration.getLocalConfig().loadLocalConfig(FilePath.Local_AppStore_Directory + FilePath.Local_AppList_Config_File);
                    this.localConfigJSONObject = Configuration.getLocalConfig().getJSONObject();
                } catch (IOException ex) {
                    Logger.getLogger(AppTemplateCard.class.getName()).log(Level.SEVERE, null, ex);
                }
                InstallApp installApp = new InstallApp(progressbarStatus, progressbarDetails, jProgressBar1, appZipFileName, localConfigJSONObject, appJSONObject, instalCommunicator, iUpdateCommunicator);
                installApp.setIsItForUpdate(true);
                installApp.run();
//                iUpdateCommunicator.uiUpdate();
                System.out.println("Update Ui executed");
                action.resolve();
            }).start();
        };

// update the app;      
        Func function3 = (action, data) -> {
            new Thread(() -> {

                action.resolve();
            }).start();
        };
        Promise.resolve()
                .then(new Promise(function1))
                .then(new Promise(function2))
                .then(new Promise(function3))
                .start();// start Promise operation

    }

}
