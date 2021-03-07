package WBPackage;

import WBPackage.BotPack.Bot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.ApiContextInitializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.Locale;

public class Main   //в этом файле всё дефолтно, кроме строк с комментами
{
    public static void main(String[] args)
    {
        Locale.setDefault(new Locale("ru", "RU"));
        ApiContextInitializer.init();
        TelegramBotsApi telegram = new TelegramBotsApi();

        Bot osnBot = new Bot();
        try
        {
            telegram.registerBot(osnBot);
            osnBot.reportOleg();    //отчёт админу о запуске
            System.out.println("Registered, ready to work");    //
            String curDate = new Date().toString(); //
            String log = curDate + " Bot started! ";    //
            System.out.println(log);    //
            try
            {
                Files.write(Path.of("botlog.txt"), ("------------------------------------------------------------\n").getBytes(), new StandardOpenOption[]{StandardOpenOption.APPEND}); //
                Files.write(Path.of("botlog.txt"), (log + "\n").getBytes(), new StandardOpenOption[]{StandardOpenOption.APPEND});   //
            }
            catch (IOException e)   //
            {
                e.printStackTrace();    //
            }
        }
        catch (TelegramApiRequestException e)
        {
            e.printStackTrace();
        }
    }
}
