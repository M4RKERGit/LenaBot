package WBPackage.BotPack;

import JSONdecode.Json;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

public class AskingAPI {
    @SneakyThrows
    public static void getWeather(String cityName, SendMessage sendMessage) {
        String outPut;
        Scanner rec;
        String linkAddr = "http://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&APPID=06d0d10ec4b1f25282e0ea1f8788bcb2&lang=ru";
        HttpURLConnection conTar = (HttpURLConnection) new URL(linkAddr).openConnection();
        conTar.connect();
        try {
            rec = new Scanner(conTar.getInputStream());
            outPut = rec.nextLine();
        } catch (FileNotFoundException exception) {
            outPut = "Сорри, но я не нашла такого города/страны, попробуй проверить правильность написания";
            System.out.println(cityName + "\n" + outPut);
            sendMessage.setText(outPut);
        }
        conTar.disconnect();
        System.out.println(outPut);
        Json outJSON = getJSON(outPut);
        sendMessage.setText(getReport(outJSON));
    }

    @SneakyThrows
    public static void getWeather(float longitude, float latitude, SendMessage sendMessage) {
        String outPut = "";
        Scanner rec;
        String linkAddr = "http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&APPID=06d0d10ec4b1f25282e0ea1f8788bcb2&lang=ru";
        try {
            HttpURLConnection conTar = (HttpURLConnection) new URL(linkAddr).openConnection();
            conTar.connect();
            try {
                rec = new Scanner(conTar.getInputStream());
                outPut = rec.nextLine();
            } catch (FileNotFoundException exception) {
                sendMessage.setText("Сорри, но я не нашла такого города/страны, попробуй проверить правильность написания");
                System.out.println(latitude + "\n" + longitude + "\n" + outPut);
                return;
            }
            conTar.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(outPut);
        Json outJSON = AskingAPI.getJSON(outPut);
        sendMessage.setText(AskingAPI.getReport(outJSON));
    }

    public static Json getJSON(String receivedJSON) throws IOException {
        ObjectMapper JSONMapper = new ObjectMapper();
        return JSONMapper.readValue(receivedJSON, Json.class);
    }

    public static String getReport(Json receivedJSON) {
        String toReturn = "";
        if (receivedJSON.getName() != null) {
            toReturn += "Город: " + receivedJSON.getName() + "\n";
        }
        toReturn += "Погода: " + receivedJSON.getWeather()[0].getDescription() + "\n";
        toReturn += "Температура: " + String.format("%.1f", receivedJSON.getMain().getTemp() - 273.15) + " C\n";
        toReturn += "Ощущается как: " + String.format("%.1f", receivedJSON.getMain().getFeels_like() - 273.15) + " C\n";
        toReturn += "Давление: " + receivedJSON.getMain().getPressure() + " мм рт. ст.\n";
        toReturn += "Скорость ветра: " + receivedJSON.getWind().getSpeed() + " м/c\n";

        if (receivedJSON.getRain() != null) {
            toReturn += "Объём дождя в ближайший час: " + receivedJSON.getRain().getHour() + "\n";
        }

        if (receivedJSON.getSnow() != null) {
            toReturn += "Объём снега в ближайший час: " + receivedJSON.getSnow().getHour() + "\n";
        }
        return toReturn;
    }

    public static void appendLog(Message message, String curDate, String user) {
        String log = curDate + " Received: " + message.getText() + " " + "From: " + user;
        System.out.println(log);
        try {
            Files.write(Path.of("botlog.txt"), (log + "\n").getBytes(), new StandardOpenOption[]{StandardOpenOption.APPEND});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void appendLog(String curDate, String user, float latitude, float longitude) {
        String log = curDate + " Received: Coordinates: " + "long: " + longitude + " lat: " + latitude + " " + "From: " + user;
        System.out.println(log);
        try {
            Files.write(Path.of("botlog.txt"), (log + "\n").getBytes(), new StandardOpenOption[]{StandardOpenOption.APPEND});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
