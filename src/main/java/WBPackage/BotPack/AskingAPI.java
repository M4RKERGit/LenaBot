package WBPackage.BotPack;

import JSONdecode.Json;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Scanner;

public class AskingAPI
{
    static String token;
    static String language;
    static String tokenPath = "tokens.txt";
    @SneakyThrows
    public static void getWeather(String cityName, SendMessage sendMessage) {
        List<String> buff;
        buff = Files.readAllLines(Path.of(tokenPath));
        token = buff.get(0);    //получение токена из текстового файла для доступа к OpenWeatherAPI
        language = buff.get(2); //получение языка прогноза
        String outPut;
        Scanner rec;
        String linkAddr = "http://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&APPID=" + token + "&lang=" + language;  //формирование URL, можно протестить в строке браузера
            HttpURLConnection conTar = (HttpURLConnection) new URL(linkAddr).openConnection();  //создаём соединение
            conTar.connect();
            try
            {
                rec = new Scanner(conTar.getInputStream()); //получаем поток данных от API
                outPut = rec.nextLine();
            }
            catch (FileNotFoundException exception) //в случае ошибки соединения или внутренней ошибки сервера
            {
                outPut = "Сорри, но я не нашла город " + cityName + ", попробуй проверить правильность написания";
                System.out.println(cityName + "\n" + outPut);
                sendMessage.setText(outPut);
                return;
            }
            conTar.disconnect();
        System.out.println(outPut);
        Json outJSON = getJSON(outPut); //перегоняем полученный JSON в объект прогноза(класс JSONDecode.Json
        sendMessage.setText(getReport(outJSON));
    }

    @SneakyThrows
    public static void getWeather(float longitude, float latitude, SendMessage sendMessage) {   //тут всё аналогично фукнции выше
        List<String> buff;
        buff = Files.readAllLines(Path.of(tokenPath));
        token = buff.get(0);
        language = buff.get(2);
        String outPut = "";
        Scanner rec;
        String linkAddr = "http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&APPID=" + token + "&lang=" + language;   //но запрос формируется немного иначе
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
                sendMessage.setText("Сорри, но я не нашла города поблизости с этим местом, попробуй ещё раз");
                System.out.println(latitude + "\n" + longitude + "\n" + outPut);
                return;
            }
            conTar.disconnect();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        System.out.println(outPut);
        Json outJSON = AskingAPI.getJSON(outPut);
        sendMessage.setText(AskingAPI.getReport(outJSON));
    }

    public static Json getJSON(String receivedJSON) throws IOException {    //мини-функция для получения объекта погоды из JSON - текста
        ObjectMapper JSONMapper = new ObjectMapper();
        return JSONMapper.readValue(receivedJSON, Json.class);
    }

    public static String getReport(Json receivedJSON)   //функция составления справки о погоде на основе полученного JSON'а
    {
        String toReturn = "";
        if (receivedJSON.getName() != null)
        {
            toReturn += "Город: " + receivedJSON.getName() + "\n";
        }
        toReturn += "Погода: " + receivedJSON.getWeather()[0].getDescription() + "\n";
        toReturn += "Температура: " + String.format("%.1f", receivedJSON.getMain().getTemp() - 273.15) + " C\n";    //температуру API присылает в Фаренгейтах, поэтому "кастуем" в Цельсий
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
