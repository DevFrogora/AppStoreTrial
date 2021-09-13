/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appstoretrial;

import org.json.JSONObject;

/**
 *
 * @author root
 */
public class AppStoreListConfig {

    JSONObject jSONObject = null;
    int a=0;

    //call saveJsonString one time;
    public void saveJsonString(String json) {
        jSONObject = new JSONObject(json);
    }

    public JSONObject getJSONObject() {
        return jSONObject;
    }
    
    public int doSomething(){
        System.out.println(a);
        return a++;
    }
    
    public void printJson(){
        System.out.println(jSONObject.toString());
    }
}
