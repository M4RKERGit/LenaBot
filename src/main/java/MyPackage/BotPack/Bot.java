package MyPackage.BotPack;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.IOException;

public class Bot extends TelegramLongPollingBot
{
    SendMessage sendMessage;
    SendSticker sendSticker;
    SendAudio sendMusic;
    Message lastHelp;
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

            if (update.getMessage().hasSticker())
            {
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

                String GOT = null;
                try
                {
                    GOT = askingAPI.getWeather(longitude, latitude);
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
                System.out.println("Received:" + update.getMessage().getText());
                if (update.getMessage().getText().equals("Музяка"))
                {
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
                }

                if (update.getMessage().getText().equals("Анек"))
                {
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
                    sendMessage.setText(bAneks.getAnek());
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
                }

                if (update.getMessage().getText().equals("/help"))
                {
                    try
                    {
                        lastHelp = execute(keyboardAdding.setButtons(update.getMessage().getChatId()));
                    }
                    catch (TelegramApiException e)
                    {
                        e.printStackTrace();
                    }
                    return;
                }

                if (update.getMessage().getText().equals("Отмена"))
                {
                    return;
                }

                if (!update.getMessage().getText().equals(""))
                {
                    cityName = update.getMessage().getText();
                    String GOT = null;
                    try
                    {
                        GOT = askingAPI.getWeather(cityName);
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

    @Override
    public String getBotUsername()
    {
        return "weather_test228_bot";
    }

    @Override
    public String getBotToken()
    {
        return "1461054874:AAEOtBLWMx9OQbBH75nG7iFFSJ9sD-giSCk";
    }
}