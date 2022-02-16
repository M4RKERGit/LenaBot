package JSONdecode;

import lombok.Data;

@Data
public class WeatherModel
{
    private int id;
    private String main;
    private String description;
    private String icon;
}
