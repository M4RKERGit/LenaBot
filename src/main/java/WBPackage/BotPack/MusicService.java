package WBPackage.BotPack;

import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

public class MusicService
{
    private String pythonScripts;

    public MusicService(String path)
    {
        pythonScripts = path;
    }
    public void randPlayer(SendMessage sendMessage, SendAudio sendMusic)
    {
        Random random = new Random();
        InputFile theSong = new InputFile();
        theSong.setMedia(new File("music/" + random.nextInt(72) + ".mp3"), "Трек");
        sendMusic.setAudio(theSong);

        String[] comms = new String[] {"Это из моей особой коллеции", "Попробуй послушать это", "Вот только вчера нашла, знатно качает"};   //эти комментарии по желанию
        sendMessage.setText(comms[random.nextInt(3)]);
    }

    public void getYouTube(SendMessage sendMessage, SendAudio sendMusic, String received) throws IOException, InterruptedException    //фукнция скачивания песен с YouTube
    {
        sendMessage.setText("Нашла по твоему запросу: ");
        String request = received.toLowerCase().replaceAll("ютуб", "").trim();
        if (request.length() > 3)
        {
            List<String> buff;
            buff = Files.readAllLines(Paths.get("config.txt"));   //читаем из конфига путь к скриптам
            String scriptPath = buff.get(3);
            Process process = Runtime.getRuntime().exec("/usr/bin/python3 " + scriptPath + "yamusic.py " + request); //запуск скрипта на python(код ниже)
            process.waitFor();
            if(!(new File("res_sound.mp3").exists()))   //проверяем, справился ли скрипт
            {
                sendMessage.setText("Извини, длина видео превышает 15 минут или такого видео не существует");
                return;
            }
            sendMusic.setAudio(new InputFile("res_sound.mp3"));
        }
    }
}
