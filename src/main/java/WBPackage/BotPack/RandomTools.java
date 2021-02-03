package WBPackage.BotPack;

import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Random;

public class RandomTools
{
    public static int replyRand(String received)
    {
        int lowBorder, highBorder;
        String[] GOT = received.split(" ", 3);
        if (GOT.length == 1)
        {
            return getRand();
        }
        if (GOT.length == 2 && !GOT[1].equals(""))
        {
            int singleBorder = Integer.parseInt(GOT[1]);
            return getRand(0, singleBorder);
        }
        lowBorder = Integer.parseInt(GOT[1]);
        highBorder = Integer.parseInt(GOT[2]);
        if (highBorder < lowBorder)
        {
            lowBorder = lowBorder + highBorder;
            highBorder = highBorder - lowBorder;
            highBorder = -highBorder;
            lowBorder = lowBorder - highBorder;
        }
        return getRand(lowBorder, highBorder);
    }

    public static int getRand(int lowBorder, int highBorder)
    {
        Random random = new Random();
        int range = highBorder - lowBorder;
        return lowBorder + random.nextInt(range);
    }

    public static int getRand()
    {
        Random random = new Random();
        int range = 99;
        return 1 + random.nextInt(range);
    }

    public static void appendLog(Message message, String curDate, String user)
    {
        String log = curDate + " Received: " + message.getText() + " " + "From: " + user;
        System.out.println(log);
        try {
            Files.write(Path.of("botlog.txt"), (log + "\n").getBytes(), new StandardOpenOption[]{StandardOpenOption.APPEND});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
