package WBPackage.BotPack;

import UserJSON.DataBase;   //добавляем кастомные классы
import UserJSON.User;
import lombok.SneakyThrows; //немного читерства для сокращения кода, все увиденные исключения были проверены, если будут проблемы, пишите в issues на GitHub
import org.telegram.telegrambots.bots.TelegramLongPollingBot;   //пачка импортов для Telegram API
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;


public class Bot extends TelegramLongPollingBot
{
    SendMessage sendMessage;    //создаём заранее все варианты отправляемого контента, ниже опишем их по мере необходимости
    SendSticker sendSticker;
    SendAudio sendMusic;
    SendVoice sendVoice;
    SendPhoto sendPhoto;
    Message lastHelp;
    String dataBasePath = "database.txt";
    String configPath = "config.txt";
    boolean fileRead = false;   //для единоразовой загрузки настроек пользователей с диска
    DataBase dataBase = null;    //инициализируем базу данных сразу, ниже опишем её

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update)
    {
        if (!fileRead)  //загружаем базу данных с диска
        {
            dataBase = takeJSON();
            fileRead = true;
        }

        if (update.hasMessage())
        {
            update.getUpdateId();
            sendMessage = new SendMessage().setChatId(update.getMessage().getChatId());
            sendSticker = new SendSticker().setChatId(update.getMessage().getChatId());
            sendMusic = new SendAudio().setChatId(update.getMessage().getChatId());
            sendVoice = new SendVoice().setChatId((update.getMessage().getChatId()));
            sendPhoto = new SendPhoto().setChatId((update.getMessage().getChatId()));
            String user = update.getMessage().getFrom().getFirstName() + " " + update.getMessage().getFrom().getLastName(); //получим имя юзера для логгирования
            String curDate = new Date().toString(); //для этого же и время обработки сообщения

            if (!(DataBase.checkUser(update.getMessage(), dataBase)))   //проверяем наличие юзера у нас в базе, если его нет, то создаём ему "учётку" и обновляем базу
            {
                sendMessage.setText("Видимо, ты впервые написал мне(либо Олег опять потерял всю мою базу данных -_-).\nЯ создала тебе профиль с настройками, можешь редактировать и пользоваться ими с помощью команды Дефолт");
                execute(sendMessage);
                DataBase.refreshBase(dataBase, curDate);    //обновление базы
                DataBase.overWrite(dataBase, dataBasePath); //перезапись на диске
            }

            if (update.getMessage().hasSticker())   //просто ламповая фича
            {
                SlashOptions.appendLog("[USER]" + update.getMessage().getText(), curDate, user); //здесь и далее используется для логгирования
                sendSticker.setSticker("CAACAgIAAxkBAAO5X_uh9OZW2YeSp59mOxJzfZt0Gq4AAgIAA80GggdFY1HBL8mAoR4E"); //[USER] для сообщений от пользователей, [ADMIN] для слэш-команд от владельца
                execute(sendSticker);
                return;
            }

            if (update.getMessage().hasLocation())  //в случае, если прислали координаты вложением
            {
                float longitude = update.getMessage().getLocation().getLongitude(); //получаем долготу
                float latitude = update.getMessage().getLocation().getLatitude();   //и широту
                SlashOptions.appendLog("Coordinates: long: " + longitude + " lat: " + latitude + " ", curDate, user);
                AskingAPI.getWeather(longitude, latitude, sendMessage); //получаем погоду, подробнее в классе AskingAPI
                execute(sendMessage);
                return;
            }

            if (update.getMessage().hasText())  //в случае, если в сообщении есть текст
            {
                String lowCase = update.getMessage().getText().toLowerCase();   //для удобства перегоняем всё в нижний регистр
                switch(lowCase) //свич, который разбирает, какая команда нужна пользователю
                {
                    case ("музяка"):
                        SlashOptions.appendLog("[USER]" + update.getMessage().getText(), curDate, user);
                        MusicFeatures.randPlayer(sendMessage, sendMusic);   //получаем музыку, подробнее в классе MusicFeatures
                        execute(sendMusic);
                        execute(sendMessage);
                        return;
                    case ("анек"):
                        SlashOptions.appendLog("[USER]" + update.getMessage().getText(), curDate, user);
                        execute(sendMessage.setText("Внимание, анекдот!")); //тут задержки просто для драматичости xD
                        Thread.sleep(2000);
                        sendMessage.setText(BAneks.getAnek());  //получаем анекдот, подробнее в классе BAneks
                        execute(sendMessage);
                        Thread.sleep(2000);
                        execute(sendMessage.setText("Ахахаххахахахахха"));
                        return;
                    case ("анек войсом"):
                        SlashOptions.appendLog("[USER]" + update.getMessage().getText(), curDate, user);
                        BAneks.getVoice(sendVoice); //получаем голосовое, подброее в классе BAneks
                        execute(sendVoice);
                        return;
                    case("пикча"):
                        SlashOptions.appendLog("[USER]" + update.getMessage().getText(), curDate, user);
                        PictureFeatures.getPic(sendPhoto, sendMessage); //получаем картинку, подробнее в классе PictureFeatures
                        execute(sendMessage);
                        execute(sendPhoto);
                        return;
                    case ("дефолт"):
                        SlashOptions.appendLog("[USER]" + update.getMessage().getText(), curDate, user);
                        //получаем список городов пользователя, от которого пришло сообщение, все методы в подробностях в классе DataBase
                        String[] cList = DataBase.getUserList()[DataBase.findUserNum(update.getMessage().getChatId(), DataBase.getUserList())].getCitiesList();
                        if (cList == null)  //если городов нет
                        {
                            sendMessage.setText("У тебя нет городов по умолчанию, попробуй их добавить");
                            execute(sendMessage);
                            return;
                        }
                        for (String s : cList)  //если есть, то отправляем погоду во всех городах по порядку
                        {
                            AskingAPI.getWeather(s, sendMessage);
                            execute(sendMessage);
                        }
                        return;
                    case ("дефолт все"):
                        SlashOptions.appendLog("[USER]" + update.getMessage().getText(), curDate, user);    //в этом кейсе всё аналогично предыдущему
                        cList = DataBase.getUserList()[DataBase.findUserNum(update.getMessage().getChatId(), DataBase.getUserList())].getCitiesList();
                        if (cList == null)
                        {
                            sendMessage.setText("У тебя нет городов по умолчанию :(");
                            execute(sendMessage);
                            return;
                        }
                        sendMessage.setText("Твои города по умолчанию:");
                        execute(sendMessage);
                        for (String s : cList)
                        {
                            sendMessage.setText(s);
                            execute(sendMessage);
                        }
                        return;
                    case ("дефолт сброс"):
                        SlashOptions.appendLog("[USER]" + update.getMessage().getText(), curDate, user);
                        DataBase.getUserList()[DataBase.findUserNum(update.getMessage().getChatId(), DataBase.getUserList())].setCitiesList(null);  //чистим список городов
                        DataBase.overWrite(dataBase, dataBasePath); //и записываем
                        sendMessage.setText("Города по умолчанию очищены");
                        execute(sendMessage);
                        return;
                    case ("/start"):
                    case ("/help"):
                        SlashOptions.appendLog("[USER]" + update.getMessage().getText(), curDate, user);
                        lastHelp = execute(KeyboardAdding.setButtons(update.getMessage().getChatId())); //выставляем экранные кнопки и выводим справку, подробнее в классе KeyboardAdding
                        return;
                    case ("/uplog"):
                        SlashOptions.appendLog("[ADMIN]" + update.getMessage().getText(), curDate, user);
                        SlashOptions.getReport(sendMessage);    //получаем последние 30 записей лога, подробнее в классе SlashOptions
                        execute(sendMessage);
                        return;
                    case ("/uplogfile"):
                        SlashOptions.appendLog("[ADMIN]" + update.getMessage().getText(), curDate, user);
                        SendDocument sendDocument = new SendDocument().setChatId(update.getMessage().getChatId());
                        SlashOptions.getFile(sendDocument); //получаем целый файл лога, т.к. телеграм не позволяет слать такие большие сообщения
                        execute(sendDocument);
                        return;
                    case ("/getup"):
                        SlashOptions.appendLog("[ADMIN]" + update.getMessage().getText(), curDate, user);
                        SlashOptions.getUp(sendMessage);    //получаем аптайм
                        execute(sendMessage);
                        return;
                    case ("/upbase"):
                        SlashOptions.appendLog("[ADMIN]" + update.getMessage().getText(), curDate, user);
                        SlashOptions.upBase(sendMessage);   //получаем нашу базу данных в виде сообщения...
                        execute(sendMessage);
                        return;
                    case ("/upbasefile"):
                        SlashOptions.appendLog("[ADMIN]" + update.getMessage().getText(), curDate, user);
                        sendDocument = new SendDocument().setChatId(update.getMessage().getChatId());
                        SlashOptions.upBaseFile(sendDocument);  //... и в виде файла
                        execute(sendDocument);
                        return;
                    case ("отмена"):
                        SlashOptions.appendLog("[USER]" + update.getMessage().getText(), curDate, user);
                        return;
                    case ("спасибо"):
                        SlashOptions.appendLog("[USER]" + update.getMessage().getText(), curDate, user);
                        sendSticker.setSticker("CAACAgIAAxkBAAO5X_uh9OZW2YeSp59mOxJzfZt0Gq4AAgIAA80GggdFY1HBL8mAoR4E"); //ещё одна ламповая фича
                        execute(sendSticker);
                        return;
                    default:    //здесь и далее - код для сообщений с аргументами
                        if (lowCase.contains("анеки войсом"))
                        {
                            SlashOptions.appendLog("[USER]" + update.getMessage().getText(), curDate, user);
                            String[] GOT = update.getMessage().getText().split(" ", 3); //делим сообщение на слова, чтобы получить аргумент, с которым будем работать
                            for (int i = 0; i < Integer.parseInt(GOT[2]); i++)  //и i раз кидаем озвучку анекдота
                            {
                                BAneks.getVoice(sendVoice);
                                execute(sendVoice);
                            }
                            return;
                        }
                        if (lowCase.contains("анеки"))
                        {
                            SlashOptions.appendLog("[USER]" + update.getMessage().getText(), curDate, user);
                            String[] GOT = update.getMessage().getText().split(" ", 2); //всё аналогично предыдущему if'у, но текстом
                            for (int i = 0; i < Integer.parseInt(GOT[1]); i++)
                            {
                                sendMessage.setText(BAneks.getAnek());
                                execute(sendMessage);
                            }
                            return;
                        }

                        if (lowCase.contains("пикча"))
                        {
                            SlashOptions.appendLog("[USER]" + update.getMessage().getText(), curDate, user);
                            sendMessage.setText("Один момент, уже ищу...");
                            execute(sendMessage);
                            PictureFeatures.getPic(sendPhoto, sendMessage, update.getMessage().getText());  //получаем картинку, подробнее в классе PictureFeatures
                            execute(sendMessage);
                            if (sendPhoto.getPhoto() != null)
                            {
                                execute(sendPhoto);
                            }
                            return;
                        }
                        if (lowCase.contains("ютуб"))
                        {
                            SlashOptions.appendLog("[USER]" + update.getMessage().getText(), curDate, user);
                            sendMessage.setText("Один момент, уже ищу...");
                            execute(sendMessage);
                            MusicFeatures.getYouTube(sendMessage, sendMusic, update.getMessage().getText());    //получаем mp3 с Ютуба, подробнее в классе MusicFeatures
                            execute(sendMessage);
                            execute(sendMusic);
                            return;
                        }
                        if (lowCase.contains("рандом"))
                        {
                            SlashOptions.appendLog("[USER]" + update.getMessage().getText(), curDate, user);
                            sendMessage.setText("Зароллено: " + RandomTools.replyRand(update.getMessage().getText()));  //тут возможен запуск с аргументами, смотрим класс RandomTools
                            execute(sendMessage);
                            return;
                        }
                        if (lowCase.contains("дефолт город"))
                        {
                            SlashOptions.appendLog("[USER]" + update.getMessage().getText(), curDate, user);
                            String[] GOT = update.getMessage().getText().split(" ", 3); //получаем аргумент-название города
                            if (User.cityUpdate(GOT[2], Objects.requireNonNull(DataBase.findUser(update.getMessage().getChatId(), DataBase.getUserList())))) //если cityUpdate вернул true, то это значит, что город был добавлен
                            {
                                sendMessage.setText("Город " + GOT[2] + " был добавлен к твоим стандартным, используй команду Дефолт, чтобы получить прогноз по всем интересующим тебя местам");
                                execute(sendMessage);
                                DataBase.overWrite(dataBase, dataBasePath);
                                return;
                            }
                            sendMessage.setText("Город " + GOT[2] + " был удалён из твоих стандартных");    //если же false, то город был удалён из списка, подробнее в классе User
                            execute(sendMessage);
                            //overWrite нужен после всех изменений, как оказалось, это не просаживает производительность, как минимум, на небольших объёмах dataBase, в другом случае может понадобиться некий counter для перезаписи после n обращений к боту
                            DataBase.overWrite(dataBase, dataBasePath);
                            return;
                        }
                        //сработает в случае несовпадения с любыми другими кейсами, буквально, все команды, кроме прописанных выше, воспринимаются, как названия городов
                        if (!update.getMessage().getText().equals(""))
                        {
                            SlashOptions.appendLog("[USER]" + update.getMessage().getText(), curDate, user);
                            String cityName = update.getMessage().getText();
                            AskingAPI.getWeather(cityName, sendMessage);
                            execute(sendMessage);
                        }
                }
            }
        }
    }

    @SneakyThrows
    public DataBase takeJSON()  //функция чтения из файла database.txt в объект класса DataBase
    {
        StringBuilder buff;
        FileReader fr = new FileReader(dataBasePath);
        BufferedReader reader = new BufferedReader(fr);
        String line = reader.readLine();
        buff = new StringBuilder(line);

        while (line != null)
        {
            buff.append(line).append("\n");
            line = reader.readLine();
        }
        return DataBase.getJSON(buff.toString());   //перегоняем полученный String в наш итоговый объект
    }

    @SneakyThrows
    public void reportOleg()    //функция для уведомления владельца бота о включении и отключении бота, позволяет отслеживать падения удалённого сервера и любые дисконнекты
    {
        String chatId;
        List<String> buff;
        buff = Files.readAllLines(Path.of("config.txt"));   //читаем из конфига Ваш chatId
        chatId = buff.get(0);
        Random random = new Random();
        SendMessage reportOleg = new SendMessage().setChatId(Long.parseLong(chatId, 10));
        String[] comms = new String[] {"Олеж, я в работе :3", "К работе готова!", "Лучший бот в мире снова в строю!"};  //тут можете забить любые приветствия, которые вам понравятся
        reportOleg.setText(comms[random.nextInt(3)]);   //приветствия рандомятся
        execute(reportOleg);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> { //создание хука-потока, который будет выполняться при остановке JVM
            Random random1 = new Random();
            SendMessage reportOleg1 = new SendMessage().setChatId(Long.parseLong(chatId, 10));
            String[] comms1 = new String[] {"Ливаю", "Я устал, я мухожук", "*dies from cringe*"};   //и прощания
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
        List<String> buff = null;
        try
        {
            buff = Files.readAllLines(Path.of(configPath));   //читаем из конфига никнейм бота
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        assert buff != null;
        return buff.get(1);
    }

    @Override
    public String getBotToken()
    {
        List<String> buff = null;
        try
        {
            buff = Files.readAllLines(Path.of(configPath));   //читаем из конфига токен бота
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        assert buff != null;
        return buff.get(2);
    }
}