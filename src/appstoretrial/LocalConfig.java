/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appstoretrial;

import java.io.File;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author root
 */
public class LocalConfig {

    JSONObject jSONObject = null;
    JSONManager jSONManager=null;
    int a = 0;

    public LocalConfig() {
        jSONManager  = new JSONManager();
    }

    //call saveJsonString one time;
    public void loadLocalConfig(String filePath) throws IOException {

        File f = new File(filePath);
        if (f.exists()) {
            String json = jSONManager.readJsonFile(filePath);
            jSONObject = new JSONObject(json);
            System.out.println("----Local Config File Already Present and Loaded-----(Config_Loaded)");
        } else {
            JSONObject defaultJson = new JSONObject();
//             {"AppStoreName":"EASy setUp","Apps":[{"appVersion":"1.0","appID":"1"},{"appVersion":"1.0","appID":"2"}]}
            defaultJson.put("AppStoreName", "EASy setUp");
            JSONArray appsArray = new JSONArray();
//            appsArray.put(new JSONObject().put("", ""));
            defaultJson.put("Apps", appsArray);
            System.out.println("--Local Config is created ----" + filePath + "\n" + defaultJson);
            jSONManager.writeJsonToFile(filePath, defaultJson.toString());
            jSONObject = defaultJson;
        }

    }
    
    public void saveLocalCofig(String filePath, String jsonString){
         jSONManager.writeJsonToFile(filePath, jsonString);
         jSONObject = new JSONObject(jsonString);
    }

    public JSONObject getJSONObject() {
        return jSONObject;
    }

    public int doSomething() {
        System.out.println(a);
        return a++;
    }

    public void printJson() {
        System.out.println(jSONObject.toString());
    }

}
