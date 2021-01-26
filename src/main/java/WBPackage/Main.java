package WBPackage;

import WBPackage.BotPack.Bot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.ApiContextInitializer;
import java.util.Locale;

public class Main
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
        }
        catch (TelegramApiRequestException e)
        {
            e.printStackTrace();
        }
    }
}
