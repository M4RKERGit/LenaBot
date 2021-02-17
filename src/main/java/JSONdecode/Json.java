package JSONdecode;

public class Json {
    private JSONdecode.Coord Coord;
    private JSONdecode.Weather Weather[];
    private String Base;
    private JSONdecode.Main Main;
    private long Visibility;
    private JSONdecode.Wind Wind;
    private JSONdecode.Clouds Clouds;
    private JSONdecode.Rain Rain;
    private JSONdecode.Snow Snow;
    private long Dt;
    private JSONdecode.Sys Sys;
    private int Timezone;
    private long Id;
    private String Name;
    private int Cod;


    public JSONdecode.Coord getCoord() {
        return Coord;
    }

    public void setCoord(JSONdecode.Coord coord) {
        Coord = coord;
    }

    public JSONdecode.Weather[] getWeather() {
        return Weather;
    }

    public void setWeather(JSONdecode.Weather[] weather) {
        Weather = weather;
    }

    public String getBase() {
        return Base;
    }

    public void setBase(String base) {
        Base = base;
    }

    public JSONdecode.Main getMain() {
        return Main;
    }

    public void setMain(JSONdecode.Main main) {
        Main = main;
    }

    public long getVisibility() {
        return Visibility;
    }

    public void setVisibility(long visibility) {
        Visibility = visibility;
    }

    public JSONdecode.Wind getWind() {
        return Wind;
    }

    public void setWind(JSONdecode.Wind wind) {
        Wind = wind;
    }

    public JSONdecode.Clouds getClouds() {
        return Clouds;
    }

    public void setClouds(JSONdecode.Clouds clouds) {
        Clouds = clouds;
    }

    public JSONdecode.Rain getRain() {
        return Rain;
    }

    public void setRain(JSONdecode.Rain rain) {
        Rain = rain;
    }

    public JSONdecode.Snow getSnow() {
        return Snow;
    }

    public void setSnow(JSONdecode.Snow snow) {
        Snow = snow;
    }

    public long getDt() {
        return Dt;
    }

    public void setDt(long dt) {
        Dt = dt;
    }

    public JSONdecode.Sys getSys() {
        return Sys;
    }

    public void setSys(JSONdecode.Sys sys) {
        Sys = sys;
    }

    public int getTimezone() {
        return Timezone;
    }

    public void setTimezone(int timezone) {
        Timezone = timezone;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getCod() {
        return Cod;
    }

    public void setCod(int cod) {
        Cod = cod;
    }

}

