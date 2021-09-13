/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appstoretrial;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author root
 */
public class JSONManager {

    public String readJsonUrl(String urlPath) throws IOException {
        String url = urlPath;
        String json = getJSON(url);
        return json;
    }

    public String getJSON(String url) throws IOException {
        HttpsURLConnection con = null;
        try {
            URL u = new URL(url);
            con = (HttpsURLConnection) u.openConnection();

            con.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            return sb.toString();

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

    public String readJsonFile(String filePath) throws IOException {

        JSONObject jsonObject = null;

        // filepath = "/Users/User/Desktop/course.json"
//            Object obj = parser.parse(new FileReader(filePath));
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();
        return sb.toString();
//            String name = (String) jsonObject.get("Name");
//            String course = (String) jsonObject.get("Course");
//            JSONArray subjects = (JSONArray) jsonObject.get("Subjects");
//            System.out.println("Name: " + name);
//            System.out.println("Course: " + course);
//            System.out.println("Subjects:");
//
//            for (int i = 0, size = subjects.length(); i < size; i++) {
//                System.out.println();
//            }

    }

    public void writeJsonToFile(String filePath, String jsonString) {

        try {

            FileWriter writer = new FileWriter(filePath);
            writer.write(jsonString);
            writer.close();
        } catch (IOException e) {
            System.out.println("exception " + e.getMessage());
            e.printStackTrace();
        }
    }

}
