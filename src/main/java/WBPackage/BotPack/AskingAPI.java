package WBPackage.BotPack;

import JSONdecode.Json;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

public class AskingAPI
{
    public static String getWeather(String cityName) throws IOException {
        String outPut = "";
        String decOut = "";
        Scanner rec;
        String linkAddr = "http://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&APPID=/OpenWeatherAPI Token here\&lang=ru";		//не забудьте вставить сюда свой токен
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

        Json outJSON = getJSON(outPut);
        decOut = getReport(outJSON);

        return decOut;
    }

    public static String getWeather(float longitude, float latitude) throws IOException {
        String outPut = "";
        String decOut = "";
        Scanner rec;
        String linkAddr = "http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&APPID=/OpenWeatherAPI Token here\&lang=ru";		//и сюда
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

        Json outJSON = AskingAPI.getJSON(outPut);
        decOut = AskingAPI.getReport(outJSON);

        return decOut;
    }

    public static Json getJSON(String receivedJSON) throws IOException {
        ObjectMapper JSONMapper = new ObjectMapper();
        Json unlocked = JSONMapper.readValue(receivedJSON, Json.class);
        return unlocked;
    }

    public static String getReport(Json receivedJSON)
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

    public static void appendLog(Message message, String curDate, String user)
    {
        String log = curDate + " Received: " + message.getText() + " " + "From: " + user;
        System.out.println(log);
        try {
            Files.write(Path.of("E://botlog.txt"), (log + "\n").getBytes(), new StandardOpenOption[]{StandardOpenOption.APPEND});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void appendLog(String curDate, String user, float latitude, float longitude)
    {
        String log = curDate + " Received: Coordinates: " + "long: " + longitude + " lat: " + latitude + " " + "From: " + user;
        System.out.println(log);
        try {
            Files.write(Path.of("E://botlog.txt"), (log + "\n").getBytes(), new StandardOpenOption[]{StandardOpenOption.APPEND});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
