package WBPackage;

import WBPackage.BotPack.Bot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.Locale;

public class Main
{
    public static void main(String[] args)
    {
        Logger logger = LoggerFactory.getLogger(Main.class);
        Locale.setDefault(new Locale("ru", "RU"));
        try
        {
            Bot osnBot = new Bot();
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(osnBot);
            osnBot.reportOleg();
            logger.info("Registered, ready to work");
            String curDate = new Date().toString();
            String log = curDate + " Bot started! ";
            logger.info(log);
            if (!new File("botlog.txt").exists()) Files.createFile(Paths.get("botlog.txt"));
            Files.write(Paths.get("botlog.txt"), ("------------------------------------------------------------\n").getBytes(), new StandardOpenOption[]{StandardOpenOption.APPEND}); //
            Files.write(Paths.get("botlog.txt"), (log + "\n").getBytes(), new StandardOpenOption[]{StandardOpenOption.APPEND});   //
        }
        catch (IOException | TelegramApiException e) {e.printStackTrace();}
    }
}
