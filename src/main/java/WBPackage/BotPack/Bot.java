package WBPackage.BotPack;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Date;

public class Bot extends TelegramLongPollingBot
{
    SendMessage sendMessage;
    SendSticker sendSticker;
    SendAudio sendMusic;
    Message lastHelp;
    String curDate = new Date().toString();
    String log;


    @Override
    public void onUpdateReceived(Update update)
    {
        if (update.hasMessage())
        {
            update.getUpdateId();
            sendMessage = new SendMessage().setChatId(update.getMessage().getChatId());
            sendSticker = new SendSticker().setChatId(update.getMessage().getChatId());
            sendMusic = new SendAudio().setChatId(update.getMessage().getChatId());
            String cityName;
            String user = update.getMessage().getFrom().getFirstName() + " " + update.getMessage().getFrom().getLastName();

            if (update.getMessage().hasSticker())
            {
                log = curDate + " Received: " + update.getMessage().getSticker() + " " + "From: " + user;
                System.out.println(log);
                try {
                    Files.write(Path.of("E://botlog.txt"), (log + "\n").getBytes(), new StandardOpenOption[]{StandardOpenOption.APPEND});
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sendSticker.setSticker("CAACAgIAAxkBAAO5X_uh9OZW2YeSp59mOxJzfZt0Gq4AAgIAA80GggdFY1HBL8mAoR4E");
                try
                {
                    execute(sendSticker);
                }
                catch (TelegramApiException e)
                {
                    e.printStackTrace();
                }
                return;
            }

            if (update.getMessage().hasLocation())
            {
                float longitude = update.getMessage().getLocation().getLongitude();
                float latitude = update.getMessage().getLocation().getLatitude();

                AskingAPI.appendLog(curDate, user, latitude, longitude);

                String GOT = null;
                try
                {
                    GOT = AskingAPI.getWeather(longitude, latitude);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                sendMessage.setText(GOT);
                try
                {
                    execute(sendMessage);
                }
                catch (TelegramApiException e)
                {
                    e.printStackTrace();
                }
                return;
            }

            if (update.getMessage().hasText())
            {
                switch(update.getMessage().getText())
                {
                    case ("Музяка"):
                        MusicFeatures.appendLog(update.getMessage(), curDate, user);
                        MusicFeatures.randPlayer(sendMessage, sendMusic);
                        try
                        {
                            execute(sendMusic);
                            execute(sendMessage);
                        }
                        catch (TelegramApiException e)
                        {
                            e.printStackTrace();
                        }
                        return;
                    case ("Анек"):
                        BAneks.appendLog(update.getMessage(), curDate, user);
                        try
                        {
                            execute(sendMessage.setText("Внимание, анекдот!"));
                        }
                        catch (TelegramApiException e)
                        {
                            e.printStackTrace();
                        }
                        try
                        {
                            Thread.sleep(2000);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        sendMessage.setText(BAneks.getAnek());
                        try
                        {
                            execute(sendMessage);
                        }
                        catch (TelegramApiException e)
                        {
                            e.printStackTrace();
                        }
                        try
                        {
                            Thread.sleep(2000);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        try
                        {
                            execute(sendMessage.setText("Ахахаххахахахахха"));
                        }
                        catch (TelegramApiException e)
                        {
                            e.printStackTrace();
                        }
                        return;
                    case ("/help"):
                        SlashOptions.appendLog(update.getMessage(), curDate, user);
                        try
                        {
                            lastHelp = execute(KeyboardAdding.setButtons(update.getMessage().getChatId()));
                        }
                        catch (TelegramApiException e)
                        {
                            e.printStackTrace();
                        }
                        return;
                    case ("/uplog"):
                        SlashOptions.appendLog(update.getMessage(), curDate, user);
                        SlashOptions.getReport(sendMessage);
                        try
                        {
                            execute(sendMessage);
                        }
                        catch (TelegramApiException e)
                        {
                            e.printStackTrace();
                        }
                        return;
                    case ("Отмена"):
                        log = curDate + " Received: " + update.getMessage().getText() + " " + "From: " + user;
                        System.out.println(log);
                        try {
                            Files.write(Path.of("E://botlog.txt"), (log + "\n").getBytes(), new StandardOpenOption[]{StandardOpenOption.APPEND});
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return;
                    case ("Рандом"):
                        RandomTools.appendLog(update.getMessage(), curDate, user);
                        sendMessage.setText("Зароллено: " + RandomTools.replyRand(update.getMessage().getText()));
                        try
                        {
                            execute(sendMessage);
                        }
                        catch (TelegramApiException e)
                        {
                            e.printStackTrace();
                        }
                        return;
                    default:
                        if (!update.getMessage().getText().equals(""))
                        {
                            AskingAPI.appendLog(update.getMessage(), curDate, user);
                            cityName = update.getMessage().getText();
                            String GOT = null;
                            try
                            {
                                GOT = AskingAPI.getWeather(cityName);
                            }
                            catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                            sendMessage.setText(GOT);
                            try
                            {
                                execute(sendMessage);
                            }
                            catch (TelegramApiException e)
                            {
                                e.printStackTrace();
                            }
                        }
                }
            }
        }
    }

    @Override
    public String getBotUsername()
    {
        return "";								//не забудьте вписать логин вашего бота
    }

    @Override
    public String getBotToken()
    {
        return "";								//и его токен
    }
}