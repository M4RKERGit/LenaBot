package WBPackage.BotPack;


import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Random;

public class MusicFeatures
{
    public static void randPlayer(SendMessage sendMessage, SendAudio sendMusic)
    {
        Random random = new Random();
        String path;
        path = "C:\\Music\\" + random.nextInt(72) + ".mp3";
        System.out.println(path);
        java.io.File hmmm = new java.io.File(path);
        InputFile theSong = new InputFile();
        theSong.setMedia(hmmm, "Трек");
        sendMusic.setAudio(theSong);

        String comms[] = new String[] {"Это из моей особой коллеции", "Попробуй послушать это", "Вот только вчера нашёл, знатно качает"};

        sendMessage.setText(comms[random.nextInt(3)]);
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
}
