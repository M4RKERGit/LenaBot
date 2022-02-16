package JSONdecode;

import lombok.Data;

@Data
public class JSONModel
{
    private CoordModel coord;
    private WeatherModel[] weather;
    private String base;
    private MainModel main;
    private long visibility;
    private WindModel wind;
    private CloudsModel clouds;
    private RainModel rain;
    private SnowModel snow;
    private long dt;
    private SysModel sys;
    private int timezone;
    private long id;
    private String name;
    private int cod;
}

