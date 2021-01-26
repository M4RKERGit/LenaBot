package WBPackage.BotPack;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class SlashOptions
{
    public static SendMessage getReport(SendMessage sendMessage)
    {
        List<String> buff = null;
        try {
            buff = Files.readAllLines(Path.of("E://botlog.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String normalized = "";
        for (int i = 0; i < buff.size(); i++)
        {
            normalized += (buff.get(i) + "\n\n");
        }
        sendMessage.setText(normalized);
        return sendMessage;
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
