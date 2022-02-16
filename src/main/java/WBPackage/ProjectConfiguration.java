package WBPackage;

import lombok.Data;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@Data
public class ProjectConfiguration
{
    private String adminID;
    private String botUserName;
    private String botToken;
    private String pythonScripts;
    private String weatherToken;
    private String pixabayToken;
    private String language;

    public ProjectConfiguration()
    {
        Properties properties = new Properties();
        try
        {
            properties.load(new FileReader("config.properties"));
            adminID = properties.getProperty("adminID");
            botUserName = properties.getProperty("botUserName");
            botToken = properties.getProperty("botToken");
            pythonScripts = properties.getProperty("pythonScripts");
            weatherToken = properties.getProperty("weatherToken");
            pixabayToken = properties.getProperty("pixabayToken");
            language = properties.getProperty("language");
        }
        catch (IOException e) {e.printStackTrace();}
    }
}
