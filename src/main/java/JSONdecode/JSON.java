package JSONdecode;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

public class JSON
{
    private coord Coord;
    private weather Weather[];
    private String Base;
    private main Main;
    private long Visibility;
    private wind Wind;
    private clouds Clouds;
    private rain Rain;
    private snow Snow;
    private long Dt;
    private sys Sys;
    private int Timezone;
    private long Id;
    private String Name;
    private int Cod;


    public coord getCoord() {
        return Coord;
    }

    public void setCoord(coord coord) {
        Coord = coord;
    }

    public weather[] getWeather() {
        return Weather;
    }

    public void setWeather(weather[] weather) {
        Weather = weather;
    }

    public String getBase() {
        return Base;
    }

    public void setBase(String base) {
        Base = base;
    }

    public main getMain() {
        return Main;
    }

    public void setMain(main main) {
        Main = main;
    }

    public long getVisibility() {
        return Visibility;
    }

    public void setVisibility(long visibility) {
        Visibility = visibility;
    }

    public wind getWind() {
        return Wind;
    }

    public void setWind(wind wind) {
        Wind = wind;
    }

    public clouds getClouds() {
        return Clouds;
    }

    public void setClouds(clouds clouds) {
        Clouds = clouds;
    }

    public rain getRain() {
        return Rain;
    }

    public void setRain(rain rain) {
        Rain = rain;
    }

    public snow getSnow() {
        return Snow;
    }

    public void setSnow(snow snow) {
        Snow = snow;
    }

    public long getDt() {
        return Dt;
    }

    public void setDt(long dt) {
        Dt = dt;
    }

    public sys getSys() {
        return Sys;
    }

    public void setSys(sys sys) {
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

