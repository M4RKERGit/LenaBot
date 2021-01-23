package MyPackage.BotPack;

import JSONdecode.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class askingAPI
{
    public static String getWeather(String cityName) throws IOException {
        String outPut = "";
        String decOut = "";
        Scanner rec;
        String linkAddr = "http://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&APPID=06d0d10ec4b1f25282e0ea1f8788bcb2&lang=ru";
        try
        {
            HttpURLConnection conTar = (HttpURLConnection) new URL(linkAddr).openConnection();
            conTar.connect();
            try
            {
                rec = new Scanner(conTar.getInputStream());
                outPut = rec.nextLine();
            }
            catch (FileNotFoundException exception)
            {
                outPut = "Сорри, но я не нашёл такого города/страны, попробуй проверить правильность написания";
                System.out.println(cityName + "\n" + outPut);
                return outPut;
            }
            conTar.disconnect();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


        System.out.println(outPut);

        JSON outJSON = getJSON(outPut);
        decOut = getReport(outJSON);

        return decOut;
    }

    public static String getWeather(float longitude, float latitude) throws IOException {
        String outPut = "";
        String decOut = "";
        Scanner rec;
        String linkAddr = "http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&APPID=06d0d10ec4b1f25282e0ea1f8788bcb2&lang=ru";
        try
        {
            HttpURLConnection conTar = (HttpURLConnection) new URL(linkAddr).openConnection();
            conTar.connect();
            try
            {
                rec = new Scanner(conTar.getInputStream());
                outPut = rec.nextLine();
            }
            catch (FileNotFoundException exception)
            {
                outPut = "Сорри, но я не нашёл такого города/страны, попробуй проверить правильность написания";
                System.out.println(latitude + "\n" + longitude + "\n" + outPut);
                return outPut;
            }
            conTar.disconnect();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


        System.out.println(outPut);

        JSON outJSON = askingAPI.getJSON(outPut);
        decOut = askingAPI.getReport(outJSON);

        return decOut;
    }

    public static JSON getJSON(String receivedJSON) throws IOException {
        ObjectMapper JSONMapper = new ObjectMapper();
        JSON unlocked = JSONMapper.readValue(receivedJSON, JSON.class);
        return unlocked;
    }

    public static String getReport(JSON receivedJSON)
    {
        String toReturn = "";
        if (receivedJSON.getName() != null)
        {
            toReturn += "Город: " + receivedJSON.getName() + "\n";
        }
        toReturn += "Погода: " + receivedJSON.getWeather()[0].getDescription() + "\n";
        toReturn += "Температура: " + String.format("%.1f", receivedJSON.getMain().getTemp() - 273.15) + " C\n";
        toReturn += "Ощущается как: " + String.format("%.1f", receivedJSON.getMain().getFeels_like() - 273.15) + " C\n";
        toReturn += "Давление: " + receivedJSON.getMain().getPressure() + " мм рт. ст.\n";
        toReturn += "Скорость ветра: " + receivedJSON.getWind().getSpeed() + " м/c\n";

        if (receivedJSON.getRain() != null)
        {
            toReturn += "Объём дождя в ближайший час: " + receivedJSON.getRain().getHour() + "\n";
        }

        if (receivedJSON.getSnow() != null)
        {
            toReturn += "Объём снега в ближайший час: " + receivedJSON.getSnow().getHour() + "\n";
        }
        return toReturn;
    }
}
