package MyPackage.BotPack;


import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import java.util.Random;

public class MusicFeatures
{

    public static void randPlayer(SendMessage sendMessage, SendAudio sendMusic)
    {
        Random random = new Random();
        String path;
        path = "C:\\Music\\" + random.nextInt(18) + ".mp3";
        System.out.println(path);
        java.io.File hmmm = new java.io.File(path);
        InputFile theSong = new InputFile();
        theSong.setMedia(hmmm, "Трек");
        sendMusic.setAudio(theSong);

        String comms[] = new String[] {"Это из моей особой коллеции", "Попробуй послушать это", "Вот только вчера нашёл, знатно качает"};

        sendMessage.setText(comms[random.nextInt(3)]);
    }
}
