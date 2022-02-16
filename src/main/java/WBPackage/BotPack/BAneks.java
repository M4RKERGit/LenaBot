package WBPackage.BotPack;

import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.telegram.telegrambots.meta.api.methods.send.SendVoice;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.net.*;
import java.io.*;

public class BAneks
{
    public static String getHTML() throws IOException   //получаем HTML-страницу
    {
        Random random = new Random();
        int rolled = 1 + random.nextInt(1142);  //рандомим номер анекдота
        URL anekURL = new URL("https://baneks.ru/" + (rolled)); //я ипользовал этот сайт из-за его удобства и наличия библиотеки в 1142 анекдота)

        BufferedReader html = new BufferedReader(new InputStreamReader(anekURL.openConnection().getInputStream())); //здесь и далее считываем HTML страницу
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = html.readLine()) != null)
        {
            response.append(inputLine);
        }
        return response.toString(); //конвертируем в понятный String
    }

    public static String getAnek()  //функция парсинга HTML
    {
        String returnAnek;
        Document parsed = new Document("");
        try
        {
            parsed = Jsoup.parse(getHTML());    //пытаемся составить распарсенный документ
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        returnAnek = parsed.select(".anek-view p").text();  //ищем анекдот через теги таблицы стилей CSS
        return returnAnek;
    }

    @SneakyThrows
    public static void getVoice(SendVoice sendVoice)    //функция озвучки анекдотов
    {
        Random random = new Random();
        int rolled = 1 + random.nextInt(1142);
        List<String> buff;
        buff = Files.readAllLines(Paths.get("config.txt"));   //читаем из конфига путь к скриптам
        String scriptPath = buff.get(3);
        Process process = Runtime.getRuntime().exec("/usr/bin/python3 " + scriptPath + "anek.py " + rolled); //вызов скрипта на python и ожидание конца его выполнения(код скрипта приведён ниже)
        process.waitFor();
        System.out.println("exe finished");
        sendVoice.setVoice(new InputFile("tts_output.ogg")); //это временный файл, который перезаписывается при каждом вызове скрипта
    }

    /*
    Привожу текст скрипта для python
    from bs4 import BeautifulSoup
    import requests
    from gtts import gTTS
    import sys, os

    var = sys.argv[1]

    url = 'https://baneks.ru/' +  var
    page = requests.get(url)
    soup = BeautifulSoup(page.content, "html.parser")
    genres = soup.find('p').getText()
    tts = gTTS(genres, lang="ru", slow=False)
    if os.path.exists('tts_output.ogg'):
       os.remove('tts_output.ogg')
    tts.save('tts_output.ogg')
    */
}
