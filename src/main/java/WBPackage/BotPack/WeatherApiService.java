package WBPackage.BotPack;

import JSONdecode.JSONModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class WeatherApiService
{
    private String token;
    private String language;

    public WeatherApiService(String token, String language)
    {
        this.token = token;
        this.language = language;
    }
    public void getWeather(String cityName, SendMessage sendMessage) throws IOException
    {
        String outPut;
        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&APPID=" + token + "&lang=" + language);
        HttpURLConnection conTar = (HttpURLConnection) url.openConnection();
        conTar.connect();
        if (conTar.getResponseCode() != 200) return;
        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(conTar.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) content.append(inputLine);
            in.close();
            outPut = content.toString();
        }
        catch (FileNotFoundException exception) {outPut = "Сорри, но я не нашла город " + cityName + ", попробуй проверить правильность написания";}
        conTar.disconnect();
        JSONModel outJSONModel = getJSON(outPut);
        sendMessage.setText(getReport(outJSONModel));
    }

    public void getWeather(Double longitude, Double latitude, SendMessage sendMessage) throws IOException //тут всё аналогично фукнции выше
    {
        String outPut = "";
        URL url = new URL(String.format("http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&APPID=%s&lang=%s", latitude, longitude, token, language));
        HttpURLConnection conTar = (HttpURLConnection) url.openConnection(); //TODO: аргументы
        conTar.connect();
        if (conTar.getResponseCode() != 200) return;
        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(conTar.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) content.append(inputLine);
            in.close();
            outPut = content.toString();
        }
        catch (FileNotFoundException exception) {outPut = "Сорри, но я не нашла город по твоим координатам, попробуй проверить правильность написания";}
        conTar.disconnect();
        JSONModel outJSONModel = getJSON(outPut);
        sendMessage.setText(getReport(outJSONModel));
    }

    public JSONModel getJSON(String receivedJSON) throws IOException //мини-функция для получения объекта погоды из JSON - текста
    {
        ObjectMapper JSONMapper = new ObjectMapper();
        return JSONMapper.readValue(receivedJSON, JSONModel.class);
    }

    public String getReport(JSONModel receivedJSONModel)   //функция составления справки о погоде на основе полученного JSON'а
    {
        String toReturn = "";
        if (receivedJSONModel.getName() != null)
        {
            toReturn += "Город: " + receivedJSONModel.getName() + "\n";
        }
        toReturn += "Погода: " + receivedJSONModel.getWeather()[0].getDescription() + "\n";
        toReturn += "Температура: " + String.format("%.1f", receivedJSONModel.getMain().getTemp() - 273.15) + " C\n";    //температуру API присылает в Фаренгейтах, поэтому "кастуем" в Цельсий
        toReturn += "Ощущается как: " + String.format("%.1f", receivedJSONModel.getMain().getFeels_like() - 273.15) + " C\n";
        toReturn += "Давление: " + receivedJSONModel.getMain().getPressure() + " мм рт. ст.\n";
        toReturn += "Скорость ветра: " + receivedJSONModel.getWind().getSpeed() + " м/c\n";

        if (receivedJSONModel.getRain() != null)
        {
            toReturn += "Объём дождя в ближайший час: " + receivedJSONModel.getRain().getOneHour() + "\n";
        }

        if (receivedJSONModel.getSnow() != null)
        {
            toReturn += "Объём снега в ближайший час: " + receivedJSONModel.getSnow().getOneHour() + "\n";
        }
        return toReturn;
    }
}
