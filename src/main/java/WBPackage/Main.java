package WBPackage;

import WBPackage.BotPack.Bot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.Locale;

public class Main   //в этом файле всё дефолтно, кроме строк с комментами
{
    public static void main(String[] args)
    {
        Locale.setDefault(new Locale("ru", "RU"));
        try
        {
            Bot osnBot = new Bot();
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(osnBot);
            osnBot.reportOleg();    //отчёт админу о запуске
            System.out.println("Registered, ready to work");    //
            String curDate = new Date().toString(); //
            String log = curDate + " Bot started! ";    //
            System.out.println(log);    //
            try
            {
                Files.write(Paths.get("botlog.txt"), ("------------------------------------------------------------\n").getBytes(), new StandardOpenOption[]{StandardOpenOption.APPEND}); //
                Files.write(Paths.get("botlog.txt"), (log + "\n").getBytes(), new StandardOpenOption[]{StandardOpenOption.APPEND});   //
            }
            catch (IOException e)   //
            {
                e.printStackTrace();    //
            }
        }
        catch (IOException | TelegramApiException e)
        {
            e.printStackTrace();
        }
    }
}
