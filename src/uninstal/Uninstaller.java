/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uninstal;

import Utils.FileDelete;
import static Utils.StringUtils.stripExtension;
import appstoretrial.Configuration;
import appstoretrial.FilePath;
import appstoretrial.LocalConfig;
import java.io.File;
import java.io.IOException;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author root
 */
public class Uninstaller {

    LocalConfig localAppListConfig = Configuration.getLocalConfig();
    String appZipFileName = "";
    int appId;
    JProgressBar uninstalJProgressBar=null;
    JLabel progressbarStatus, progressbarDetails =null;

    public Uninstaller(String appZipFileName,int appId, JProgressBar uninstalJProgressBar,JLabel progressbarStatus,JLabel progressbarDetails) {
        this.appZipFileName = appZipFileName;
        this.appId = appId;
        this.uninstalJProgressBar = uninstalJProgressBar;
        this.progressbarStatus = progressbarStatus;
        this.progressbarDetails = progressbarDetails;
    }

    public void run() throws IOException {
        File appfolderToDelete = new File(FilePath.Local_AppStore_Directory + "\\" + stripExtension(appZipFileName)); //String filename = appZipFileName;
        
        //for jprogressBar
        long appFolderSize = FileDelete.folderSize(appfolderToDelete);
        System.out.println("AppFolderSize "+appFolderSize);
        
        FileDelete.deleteDirectory(appfolderToDelete,appFolderSize,0,uninstalJProgressBar,progressbarStatus,progressbarDetails);
        appfolderToDelete.delete();
        removeJsonDataFromConfigFile();
    }

    public void removeJsonDataFromConfigFile() throws IOException {
        localAppListConfig.loadLocalConfig(FilePath.Local_AppStore_Directory + FilePath.Local_AppList_Config_File);
        JSONObject localConfigJSONObject = localAppListConfig.getJSONObject();


        JSONArray jsonArray = localConfigJSONObject.getJSONArray("Apps");
        int len = jsonArray.length();
        if (jsonArray != null) {
            for (int i = 0; i < len; i++) {
                JSONObject appLocalObject = jsonArray.getJSONObject(i);
                //Excluding the item at position
                if (appLocalObject.getInt("appId") == appId) {
      
                    jsonArray.remove(i);
                }
            }
        }
         Configuration.getLocalConfig().saveLocalCofig(FilePath.Local_AppStore_Directory + FilePath.Local_AppList_Config_File, localConfigJSONObject.toString());
    }

}
