package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
    private static Configuration instance;
    private Properties configProps = new Properties();
    private String url;
    public static synchronized Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }
    private Configuration() {

        InputStream is = null;
        try {
            is = ClassLoader.getSystemResourceAsStream("config.properties");
            configProps.load(is);
            this.url = configProps.getProperty("url");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {

            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}
