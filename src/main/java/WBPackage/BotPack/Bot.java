package WBPackage.BotPack;

import UserJSON.DataBase;
import UserJSON.UserModel;
import WBPackage.ProjectConfiguration;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;


public class Bot extends TelegramLongPollingBot
{
    private Message lastHelp;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String dataBasePath = "database.json";
    private DataBase dataBase;
    private final ProjectConfiguration config = new ProjectConfiguration();
    private final WeatherApiService weatherService = new WeatherApiService(config.getWeatherToken(), config.getLanguage());
    private final MusicService musicService = new MusicService(config.getPythonScripts());
    private final AnekService anekService = new AnekService();
    private final ObjectMapper jsonMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private final ObjectWriter jsonWriter = jsonMapper.writer(new DefaultPrettyPrinter());

    private void loadBase()
    {
        try
        {
            dataBase = jsonMapper.readValue(Paths.get(dataBasePath).toFile(), DataBase.class);
            logger.info("Base loaded");
        }
        catch (Exception e)
        {
            dataBase = new DataBase(0L, "", new ArrayList<>());
            logger.info("Database lost, creating new one...");
        }
    }

    private void dumpBase() throws IOException
    {
        dataBase.setCurDate(LocalDateTime.now().toString());
        dataBase.setTotalUsers(dataBase.getUserList().size());
        jsonWriter.writeValue(new File(dataBasePath), dataBase);
        logger.info("Base dumped");
    }

    @Override
    public void onUpdateReceived(Update update)
    {
        try
        {
            if (dataBase == null) {loadBase();}
            if (update.hasMessage())
            {
                update.getUpdateId();
                String chatId = String.valueOf(update.getMessage().getChatId());
                String messageText = update.getMessage().getText();
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                String user = update.getMessage().getFrom().getFirstName() + " " + update.getMessage().getFrom().getLastName(); //получим имя юзера для логгирования
                String curDate = new Date().toString(); //для этого же и время обработки сообщения

                if (messageText.contains("/")) SlashOptions.appendLog("[ADMIN]" + messageText, curDate, user);
                else SlashOptions.appendLog("[USER]" + messageText, curDate, user);

                if (!dataBase.getUserList().contains(new UserModel(user, chatId, new ArrayList<>())))   //проверяем наличие юзера у нас в базе, если его нет, то создаём ему "учётку" и обновляем базу
                {
                    sendMessage.setText("Видимо, ты впервые написал мне (либо Олег опять потерял всю мою базу данных -_-).\nЯ создала тебе профиль с настройками, можешь редактировать и пользоваться ими с помощью команды Дефолт");
                    execute(sendMessage);
                    dataBase.getUserList().add(new UserModel(user, chatId, new ArrayList<>()));
                    dumpBase();
                }
//            if (update.getMessage().hasSticker())   //просто ламповая фича
//            {
//                SlashOptions.appendLog("[USER]" + messageText, curDate, user); //здесь и далее используется для логгирования
//                sendSticker.setSticker("CAACAgIAAxkBAAO5X_uh9OZW2YeSp59mOxJzfZt0Gq4AAgIAA80GggdFY1HBL8mAoR4E"); //[USER] для сообщений от пользователей, [ADMIN] для слэш-команд от владельца
//                execute(sendSticker);
//                return;
//            }

                if (update.getMessage().hasLocation())  //в случае, если прислали координаты вложением
                {
                    Double longitude = update.getMessage().getLocation().getLongitude(); //получаем долготу
                    Double latitude = update.getMessage().getLocation().getLatitude();   //и широту
                    SlashOptions.appendLog("Coordinates: long: " + longitude + " lat: " + latitude + " ", curDate, user);
                    weatherService.getWeather(longitude, latitude, sendMessage); //получаем погоду, подробнее в классе AskingAPI
                    execute(sendMessage);
                    return;
                }

                if (update.getMessage().hasText())  //в случае, если в сообщении есть текст
                {
                    String lowCase = messageText.toLowerCase();   //для удобства перегоняем всё в нижний регистр
                    switch(lowCase) //свич, который разбирает, какая команда нужна пользователю
                    {
                        case ("музяка"):
                            SendAudio sendAudio = new SendAudio();
                            sendAudio.setChatId(chatId);
                            musicService.randPlayer(sendMessage, sendAudio);   //получаем музыку, подробнее в классе MusicFeatures
                            execute(sendAudio);
                            execute(sendMessage);
                            return;
                        case ("анек"):
                            sendMessage.setText("Внимание, анекдот!");
                            execute(sendMessage); //тут задержки просто для драматичости xD
                            Thread.sleep(2000);
                            sendMessage.setText(anekService.getAnek());  //получаем анекдот, подробнее в классе BAneks
                            execute(sendMessage);
                            Thread.sleep(2000);
                            sendMessage.setText("Ахахаххахахахахха");
                            execute(sendMessage);
                            return;
                        case ("анек войсом"):
                            SendVoice sendVoice = new SendVoice();
                            sendVoice.setChatId(chatId);
                            anekService.getVoice(sendVoice); //получаем голосовое, подброее в классе BAneks
                            execute(sendVoice);
                            return;
                        case("пикча"):
                            SendPhoto sendPhoto = new SendPhoto();
                            sendPhoto.setChatId(chatId);
                            PictureFeatures.getPic(sendPhoto, sendMessage); //получаем картинку, подробнее в классе PictureFeatures
                            execute(sendMessage);
                            execute(sendPhoto);
                            return;
                        case ("дефолт"):
                            //получаем список городов пользователя, от которого пришло сообщение, все методы в подробностях в классе DataBase
                            UserModel curUser = dataBase.getUserList().stream().filter(mod -> mod.getChatId().equals(chatId)).findFirst().get();
                            if (curUser.getCitiesList().isEmpty())
                            {
                                sendMessage.setText("У тебя нет городов по умолчанию, попробуй их добавить");
                                execute(sendMessage);
                                return;
                            }
                            for (String s : curUser.getCitiesList())  //если есть, то отправляем погоду во всех городах по порядку
                            {
                                weatherService.getWeather(s, sendMessage);
                                execute(sendMessage);
                            }
                            return;
                        case ("дефолт все"):
                            curUser = dataBase.getUserList().stream().filter(mod -> mod.getChatId().equals(chatId)).findFirst().get();
                            if (curUser.getCitiesList().isEmpty())
                            {
                                sendMessage.setText("У тебя нет городов по умолчанию :(");
                                execute(sendMessage);
                                return;
                            }
                            sendMessage.setText("Твои города по умолчанию:");
                            execute(sendMessage);
                            for (String s : curUser.getCitiesList())
                            {
                                sendMessage.setText(s);
                                execute(sendMessage);
                            }
                            return;
                        case ("дефолт сброс"):
                            int index = dataBase.getUserList().indexOf(new UserModel(user, chatId, null));
                            dataBase.getUserList().get(index).getCitiesList().clear();
                            sendMessage.setText("Города по умолчанию очищены");
                            execute(sendMessage);
                            return;
                        case ("/start"):
                        case ("/help"):
                            lastHelp = execute(KeyboardAdding.setButtons(update.getMessage().getChatId())); //выставляем экранные кнопки и выводим справку, подробнее в классе KeyboardAdding
                            return;
                        case ("/uplog"):
                            SlashOptions.getReport(sendMessage);    //получаем последние 30 записей лога, подробнее в классе SlashOptions
                            execute(sendMessage);
                            return;
                        case ("/uplogfile"):
                            SendDocument sendDocument = new SendDocument();
                            sendDocument.setChatId(chatId);
                            SlashOptions.getFile(sendDocument); //получаем целый файл лога, т.к. телеграм не позволяет слать такие большие сообщения
                            execute(sendDocument);
                            return;
                        case ("/getup"):
                            SlashOptions.getUp(sendMessage);    //получаем аптайм
                            execute(sendMessage);
                            return;
                        case ("/upbase"):
                            SlashOptions.upBase(sendMessage);   //получаем нашу базу данных в виде сообщения...
                            execute(sendMessage);
                            return;
                        case ("/upbasefile"):
                            sendDocument = new SendDocument();
                            sendDocument.setChatId(chatId);
                            SlashOptions.upBaseFile(sendDocument);  //... и в виде файла
                            execute(sendDocument);
                            return;
                        case ("отмена"):
                            return;
                        case ("спасибо"):
//                            SlashOptions.appendLog("[USER]" + messageText, curDate, user);
//                            sendSticker.setSticker("CAACAgIAAxkBAAO5X_uh9OZW2YeSp59mOxJzfZt0Gq4AAgIAA80GggdFY1HBL8mAoR4E"); //ещё одна ламповая фича
//                            execute(sendSticker);
                            return;
                        default:    //здесь и далее - код для сообщений с аргументами
                            if (lowCase.contains("анеки войсом"))
                            {
                                String[] GOT = messageText.split(" ", 3); //делим сообщение на слова, чтобы получить аргумент, с которым будем работать
                                for (int i = 0; i < Integer.parseInt(GOT[2]); i++)  //и i раз кидаем озвучку анекдота
                                {
                                    SendVoice sendMultVoice = new SendVoice();
                                    sendMultVoice.setChatId(chatId);
                                    anekService.getVoice(sendMultVoice);
                                    execute(sendMultVoice);
                                }
                                return;
                            }
                            if (lowCase.contains("анеки"))
                            {
                                String[] GOT = messageText.split(" ", 2); //всё аналогично предыдущему if'у, но текстом
                                for (int i = 0; i < Integer.parseInt(GOT[1]); i++)
                                {
                                    sendMessage.setText(anekService.getAnek());
                                    execute(sendMessage);
                                }
                                return;
                            }

                            if (lowCase.contains("пикча"))
                            {
                                sendMessage.setText("Один момент, уже ищу...");
                                execute(sendMessage);
                                sendPhoto = new SendPhoto();
                                sendPhoto.setChatId(chatId);
                                PictureFeatures.getPic(sendPhoto, sendMessage, messageText);  //получаем картинку, подробнее в классе PictureFeatures
                                execute(sendMessage);
                                sendPhoto.getPhoto();
                                execute(sendPhoto);
                                return;
                            }
                            if (lowCase.contains("ютуб"))
                            {
                                sendMessage.setText("Один момент, уже ищу...");
                                execute(sendMessage);
                                sendAudio = new SendAudio();
                                sendAudio.setChatId(chatId);
                                musicService.getYouTube(sendMessage, sendAudio, messageText);    //получаем mp3 с Ютуба, подробнее в классе MusicFeatures
                                execute(sendMessage);
                                execute(sendAudio);
                                return;
                            }
                            if (lowCase.contains("рандом"))
                            {
                                sendMessage.setText("Зароллено: " + RandomTools.replyRand(messageText));  //тут возможен запуск с аргументами, смотрим класс RandomTools
                                execute(sendMessage);
                                return;
                            }
                            if (lowCase.contains("дефолт город"))
                            {
                                String city = messageText.split(" ", 3)[2]; //получаем аргумент-название города
                                curUser = dataBase.getUserList().stream().filter(mod -> mod.getChatId().equals(chatId)).findFirst().get();
                                if (curUser.getCitiesList().contains(city))
                                {
                                    curUser.getCitiesList().remove(city);
                                    sendMessage.setText("Город " + city + " был удалён из твоих стандартных");    //если же false, то город был удалён из списка, подробнее в классе User
                                    execute(sendMessage);
                                }
                                else
                                {
                                    curUser.getCitiesList().add(city);
                                    sendMessage.setText("Город " + city + " был добавлен к твоим стандартным, " +
                                            "используй команду Дефолт, чтобы получить прогноз по всем интересующим тебя местам");
                                    execute(sendMessage);
                                }
                                dumpBase();
                                return;
                            }
                            //сработает в случае несовпадения с любыми другими кейсами, буквально, все команды, кроме прописанных выше, воспринимаются, как названия городов
                            if (!messageText.equals(""))
                            {
                                weatherService.getWeather(messageText, sendMessage);
                                execute(sendMessage);
                            }
                    }
                }
            }
        }
        catch (Exception e) {e.printStackTrace();}
    }

    public void reportOleg() throws IOException, TelegramApiException    //функция для уведомления владельца бота о включении и отключении бота, позволяет отслеживать падения удалённого сервера и любые дисконнекты
    {
        Random random = new Random();
        SendMessage reportOleg = new SendMessage();
        reportOleg.setChatId(config.getAdminID());
        String[] comms = new String[] {"Олеж, я в работе :3", "К работе готова!", "Лучший бот в мире снова в строю!"};  //тут можете забить любые приветствия, которые вам понравятся
        reportOleg.setText(comms[random.nextInt(3)]);   //приветствия рандомятся
        execute(reportOleg);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> //создание хука-потока, который будет выполняться при остановке бота
        {
            Random random1 = new Random();
            SendMessage reportOleg1 = new SendMessage();
            reportOleg1.setChatId(config.getAdminID());
            String[] comms1 = new String[] {"Ливаю", "Я устал, я мухожук", "*dies from cringe*"};   //и прощания
            reportOleg1.setText(comms1[random1.nextInt(3)]);
            try {execute(reportOleg1);} catch (TelegramApiException e) {e.printStackTrace();}
        }));
    }

    @Override
    public String getBotUsername() {return config.getBotUserName();}

    @Override
    public String getBotToken() {return config.getBotToken();}
}