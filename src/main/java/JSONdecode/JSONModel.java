package JSONdecode;

import lombok.Data;

@Data
public class JSONModel
{
    private CoordModel Coord;
    private WeatherModel[] WeatherModel;
    private String Base;
    private MainModel MainModel;
    private long Visibility;
    private WindModel WindModel;
    private CloudsModel Clouds;
    private RainModel RainModel;
    private SnowModel SnowModel;
    private long Dt;
    private SysModel SysModel;
    private int Timezone;
    private long Id;
    private String Name;
    private int Cod;
}

