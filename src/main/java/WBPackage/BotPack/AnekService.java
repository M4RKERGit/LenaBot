package WBPackage.BotPack;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.telegram.telegrambots.meta.api.methods.send.SendVoice;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.net.*;
import java.io.*;

public class AnekService
{
    private final Random random = new Random();
    private final String host = "https://baneks.ru/";

    public String getHTML() throws IOException   //получаем HTML-страницу
    {
        Random random = new Random();
        URL anekURL = new URL(host + (1 + random.nextInt(1142)));

        HttpURLConnection conTar = (HttpURLConnection) anekURL.openConnection();
        conTar.connect();
        if (conTar.getResponseCode() != 200) throw new IOException();
        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(conTar.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) content.append(inputLine);
            in.close();
            conTar.disconnect();
            return content.toString();
        }
        catch (FileNotFoundException e) {e.printStackTrace(); return "";}
    }

    public String getAnek()  //функция парсинга HTML
    {
        Document parsed = new Document("");

        try {parsed = Jsoup.parse(getHTML());}
        catch (IOException e) {e.printStackTrace(); return "";}

        return parsed.select(".anek-view p").text();
    }

    public void getVoice(SendVoice sendVoice) throws IOException, InterruptedException    //функция озвучки анекдотов
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
}
