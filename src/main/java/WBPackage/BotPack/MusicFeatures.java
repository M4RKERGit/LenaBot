package WBPackage.BotPack;


import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Random;

public class MusicFeatures
{
    public static void randPlayer(SendMessage sendMessage, SendAudio sendMusic) //функция отправки случайной песни с диска
    {
        Random random = new Random();
        String path;
        path = "music/" + random.nextInt(72) + ".mp3";  //в моём случае у меня было 72 трека из любимых альбомов
        System.out.println(path);
        java.io.File hmmm = new java.io.File(path);
        InputFile theSong = new InputFile();
        theSong.setMedia(hmmm, "Трек");
        sendMusic.setAudio(theSong);

        String[] comms = new String[] {"Это из моей особой коллеции", "Попробуй послушать это", "Вот только вчера нашла, знатно качает"};   //эти комментарии по желанию
        sendMessage.setText(comms[random.nextInt(3)]);  //рандомим их
    }

    @SneakyThrows
    public static void getYouTube(SendMessage sendMessage, SendAudio sendMusic, String received)    //фукнция скачивания песен с YouTube
    {
        sendMessage.setText("Нашла по твоему запросу: ");
        String[] GOT = received.split(" ", 3);  //получаем аргумент
        StringBuilder requestBuilder = new StringBuilder();
        for (int i = 0; i < GOT.length; i++)    //убираем ключевое слово из запроса, оставляя только поисковую часть
        {
            if (GOT[i].equals("ютуб") | GOT[i].equals("Ютуб"))
            {
                GOT[i] = "";
            }
            else requestBuilder.append(GOT[i]);
        }
        String request = requestBuilder.toString(); //сформировали конечный запрос на ютуб
        if (request.length() > 3)
        {
            System.out.println("Конечный запрос: " + request);
            List<String> buff;
            buff = Files.readAllLines(Path.of("config.txt"));   //читаем из конфига путь к скриптам
            String scriptPath = buff.get(3);
            Process process = Runtime.getRuntime().exec("/usr/bin/python3 " + scriptPath + "yamusic.py " + request); //запуск скрипта на python(код ниже)
            process.waitFor();
            System.out.println("exe finished");
            if(!(new File("res_sound.mp3").exists()))   //проверяем, справился ли скрипт
            {
                sendMessage.setText("Извини, длина видео превышает 15 минут или такого видео не существует");
                return;
            }
            sendMusic.setAudio(new File("res_sound.mp3"));
        }

        /*
        Текст скрипта yamusic.py:
        import os
        import sys
        import json
        from youtubesearchpython import *
        import pafy
        import eyed3
        from moviepy.editor import *

        if os.path.exists('res.mp4'):
           os.remove('res.mp4')

        if os.path.exists('res_sound.mp3'):
          os.remove('res_sound.mp3')

        music = ""
        for i in sys.argv:
            if sys.argv[0] == i:
                continue
            music = music + i + " "
        videosSearch = VideosSearch(music, limit = 1, region = 'RU')
        todos = json.loads(videosSearch.result( mode = ResultMode.json))
        video = pafy.new(todos['result'][0]['link'])
        print(todos['result'][0]['duration'])
        print(todos['result'][0]['link'])
        nm = video.title
        print(video.title)

        naming = nm.split("-")

        if len(todos['result'][0]['duration']) <= 5:
            temp = todos['result'][0]['duration'].split(":",1)
            print(temp)
            if int(temp[0]) <= 15:
                bestaudio = video.getbest(preftype="mp4")
                bestaudio.download("res.mp4")
                video = VideoFileClip("res.mp4")
                video.audio.write_audiofile("res_sound.mp3")

                audiofile = eyed3.load("res_sound.mp3")
                audiofile.tag.title = naming[1].strip(" ")
                audiofile.tag.artist = naming[0].strip(" ")

                audiofile.tag.save()*/
    }
}
