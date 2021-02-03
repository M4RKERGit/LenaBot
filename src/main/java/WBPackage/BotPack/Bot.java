package WBPackage.BotPack;

import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Date;
import java.util.Random;


public class Bot extends TelegramLongPollingBot
{
    SendMessage sendMessage;
    SendSticker sendSticker;
    SendAudio sendMusic;
    SendVoice sendVoice;
    SendPhoto sendPhoto;
    Message lastHelp;

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update)
    {
        String curDate = new Date().toString();
        if (update.hasMessage())
        {
            update.getUpdateId();
            sendMessage = new SendMessage().setChatId(update.getMessage().getChatId());
            sendSticker = new SendSticker().setChatId(update.getMessage().getChatId());
            sendMusic = new SendAudio().setChatId(update.getMessage().getChatId());
            sendVoice = new SendVoice().setChatId((update.getMessage().getChatId()));
            sendPhoto = new SendPhoto().setChatId((update.getMessage().getChatId()));
            String cityName;
            String user = update.getMessage().getFrom().getFirstName() + " " + update.getMessage().getFrom().getLastName();

            if (update.getMessage().hasSticker())
            {
                SlashOptions.appendLog(update.getMessage(), curDate, user);
                sendSticker.setSticker("CAACAgIAAxkBAAO5X_uh9OZW2YeSp59mOxJzfZt0Gq4AAgIAA80GggdFY1HBL8mAoR4E");
                execute(sendSticker);
                return;
            }

            if (update.getMessage().hasLocation())
            {
                float longitude = update.getMessage().getLocation().getLongitude();
                float latitude = update.getMessage().getLocation().getLatitude();
                AskingAPI.appendLog(curDate, user, latitude, longitude);
                AskingAPI.getWeather(longitude, latitude, sendMessage);
                execute(sendMessage);
                return;
            }

            if (update.getMessage().hasText())
            {
                String lowCase = update.getMessage().getText().toLowerCase();
                switch(lowCase)
                {
                    case ("музяка"):
                        MusicFeatures.appendLog(update.getMessage(), curDate, user);
                        MusicFeatures.randPlayer(sendMessage, sendMusic);
                        execute(sendMusic);
                        execute(sendMessage);
                        return;
                    case ("анек"):
                        BAneks.appendLog(update.getMessage(), curDate, user);
                        execute(sendMessage.setText("Внимание, анекдот!"));
                        Thread.sleep(2000);
                        sendMessage.setText(BAneks.getAnek());
                        execute(sendMessage);
                        Thread.sleep(2000);
                        execute(sendMessage.setText("Ахахаххахахахахха"));
                        return;
                    case ("анек войсом"):
                        BAneks.appendLog(update.getMessage(), curDate, user);
                        BAneks.getVoice(sendVoice);
                        execute(sendVoice);
                        return;
                    case("пикча"):
                        SlashOptions.appendLog(update.getMessage(), curDate, user);
                        PictureFeatures.getPic(sendPhoto, sendMessage);
                        execute(sendMessage);
                        execute(sendPhoto);
                        return;
                    case ("/start"):
                    case ("/help"):
                        SlashOptions.appendLog(update.getMessage(), curDate, user);
                        lastHelp = execute(KeyboardAdding.setButtons(update.getMessage().getChatId()));
                        return;
                    case ("/uplog"):
                        SlashOptions.appendLog(update.getMessage(), curDate, user);
                        SlashOptions.getReport(sendMessage);
                        execute(sendMessage);
                        return;
                    case ("/uplogfile"):
                        SlashOptions.appendLog(update.getMessage(), curDate, user);
                        SendDocument sendDocument = new SendDocument().setChatId(update.getMessage().getChatId());
                        SlashOptions.getFile(sendDocument);
                        execute(sendDocument);
                        return;
                    case ("/getup"):
                        SlashOptions.appendLog(update.getMessage(), curDate, user);
                        SlashOptions.getUp(sendMessage);
                        execute(sendMessage);
                        return;
                    case ("отмена"):
                        SlashOptions.appendLog(update.getMessage(), curDate, user);
                        return;
                    case ("спасибо"):
                        SlashOptions.appendLog(update.getMessage(), curDate, user);
                        sendSticker.setSticker("CAACAgIAAxkBAAO5X_uh9OZW2YeSp59mOxJzfZt0Gq4AAgIAA80GggdFY1HBL8mAoR4E");
                        execute(sendSticker);
                        return;
                    default:
                        if (lowCase.contains("пикча"))
                        {
                            sendMessage.setText("Один момент, уже ищу...");
                            execute(sendMessage);
                            MusicFeatures.appendLog(update.getMessage(), curDate, user);
                            PictureFeatures.getPic(sendPhoto, sendMessage, update.getMessage().getText());
                            execute(sendMessage);
                            execute(sendPhoto);
                            return;
                        }
                        if (lowCase.contains("ютуб"))
                        {
                            sendMessage.setText("Один момент, уже ищу...");
                            execute(sendMessage);
                            MusicFeatures.appendLog(update.getMessage(), curDate, user);
                            MusicFeatures.getYouTube(sendMessage, sendMusic, update.getMessage().getText());
                            execute(sendMessage);
                            execute(sendMusic);
                            return;
                        }
                        if (lowCase.contains("рандом"))
                        {
                            RandomTools.appendLog(update.getMessage(), curDate, user);
                            sendMessage.setText("Зароллено: " + RandomTools.replyRand(update.getMessage().getText()));
                            execute(sendMessage);
                            return;
                        }
                        if (!update.getMessage().getText().equals(""))
                        {
                            AskingAPI.appendLog(update.getMessage(), curDate, user);
                            cityName = update.getMessage().getText();
                            AskingAPI.getWeather(cityName, sendMessage);
                            execute(sendMessage);
                        }
                }
            }
        }
    }

    @SneakyThrows
    public void reportOleg()
    {
        Random random = new Random();
        SendMessage reportOleg = new SendMessage().setChatId();	  //функция для уведомления о включении/выключении бота вам в ЛС, необходимо указать ваш ID
        String[] comms = new String[] {"Олеж, я в работе :3", "Готова ебашить!", "Лучший бот в мире снова в строю!"};
        reportOleg.setText(comms[random.nextInt(3)]);
        execute(reportOleg);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Random random1 = new Random();
            SendMessage reportOleg1 = new SendMessage().setChatId();
            String[] comms1 = new String[] {"Ливаю", "Я устал, я мухожук", "*dies from cringe*"};
            reportOleg1.setText(comms1[random1.nextInt(3)]);
            try {
                execute(reportOleg1);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        )
        );
    }

    @Override
    public String getBotUsername()
    {
        return "";	//сюда вписать юзернейм бота
    }

    @Override
    public String getBotToken()
    {
        return "";	//сюда вписать токен
    }
}