/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appstoretrial;

/**
 *
 * @author root
 */
public class Configuration {

    private static AppStoreListConfig appStoreListConfig;
    private static LocalConfig localConfig;

    private Configuration() {
    }

    public static AppStoreListConfig getAppStoreListConfig() {
        if (appStoreListConfig == null) {
            synchronized (AppStoreListConfig.class) {
                if (appStoreListConfig == null) {

                    appStoreListConfig = new AppStoreListConfig();
                }
            }

        }
        return appStoreListConfig;
    }
    
        public static LocalConfig getLocalConfig() {
        if (localConfig == null) {
            synchronized (LocalConfig.class) {
                if (localConfig == null) {

                    localConfig = new LocalConfig();
                }
            }

        }
        return localConfig;
    }

}
