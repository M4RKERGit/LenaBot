package MyPackage;

import java.io.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.Audio;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;


import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.*;
import java.util.List;

import JSONdecode.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;



public class Bot extends TelegramLongPollingBot {
    String comms[] = new String[] {"��� �� ���� ������ ��������", "�������� ��������� ���", "��� ������ ����� �����, ������ ������"};
    @Override
    public void onUpdateReceived(Update update)
    {
        if (update.hasMessage())
        {
            update.getUpdateId();

            SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId());
            SendSticker sendSticker = new SendSticker().setChatId(update.getMessage().getChatId());
            SendAudio sendMusic = new SendAudio().setChatId(update.getMessage().getChatId());
            String cityName = "";




            /*else if (update.hasCallbackQuery())
            {
                randPlayer(sendMessage, sendMusic);
            }*/

            if (update.getMessage().hasSticker())
            {
                Sticker stickToSend = new Sticker();
                sendSticker.setSticker("CAACAgIAAxkBAAO5X_uh9OZW2YeSp59mOxJzfZt0Gq4AAgIAA80GggdFY1HBL8mAoR4E");
                try {
                    execute(sendSticker);
                } catch (TelegramApiException e) {
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
                    GOT = getWeather(longitude, latitude);
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
                if (update.getMessage().getText().equals("������"))
                {
                    randPlayer(sendMessage, sendMusic);
                    return;
                }

                if (update.getMessage().getText().equals("/help"))
                {
                    //sendMessage.setText("������!\n� �������� ���, ������� � ���� ��� �������� ������ (��� ���� ������, ���� ��� ��������� :3) �� RU/EN ����� � � ��� ���� ������� ������� �� ������ � ���� ������.\n���� ������, ���� ���������� � ����� ������� - ������ ������ \"������\"\n(????)?*:???");
                    try {
                        //execute(sendMessage);
                        execute(setButtons(update.getMessage().getChatId()));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                if (!update.getMessage().getText().equals(""))
                {
                    cityName = update.getMessage().getText();
                    String GOT = null;
                    try {
                        GOT = getWeather(cityName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    sendMessage.setText(GOT);
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    @Override
    public String getBotUsername() {
        return "weather_test228_bot";
    }

    @Override
    public String getBotToken() {
        return "1461054874:AAEOtBLWMx9OQbBH75nG7iFFSJ9sD-giSCk";
    }

    public String getWeather(String cityName) throws IOException {
        String outPut = "";
        String decOut = "";
        Scanner rec;
        String linkAddr = "http://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&APPID=06d0d10ec4b1f25282e0ea1f8788bcb2&lang=ru";
        try
        {
            HttpURLConnection conTar = (HttpURLConnection) new URL(linkAddr).openConnection();
            conTar.connect();
            try
            {
                rec = new Scanner(conTar.getInputStream());
                outPut = rec.nextLine();
            }
            catch (FileNotFoundException exception)
            {
                outPut = "�����, �� � �� ����� ������ ������/������, �������� ��������� ������������ ���������";
                System.out.println(cityName + "\n" + outPut);
                return outPut;
            }
            conTar.disconnect();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


        System.out.println(outPut);

        JSON outJSON = getJSON(outPut);
        decOut = getReport(outJSON);

        return decOut;
    }

    public String getWeather(float longitude, float latitude) throws IOException {
        String outPut = "";
        String decOut = "";
        Scanner rec;
        String linkAddr = "http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&APPID=06d0d10ec4b1f25282e0ea1f8788bcb2&lang=ru";
        try
        {
            HttpURLConnection conTar = (HttpURLConnection) new URL(linkAddr).openConnection();
            conTar.connect();
            try
            {
                rec = new Scanner(conTar.getInputStream());
                outPut = rec.nextLine();
            }
            catch (FileNotFoundException exception)
            {
                outPut = "�����, �� � �� ����� ������ ������/������, �������� ��������� ������������ ���������";
                System.out.println(latitude + "\n" + longitude + "\n" + outPut);
                return outPut;
            }
            conTar.disconnect();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


        System.out.println(outPut);

        JSON outJSON = getJSON(outPut);
        decOut = getReport(outJSON);

        return decOut;
    }


    public JSON getJSON(String receivedJSON) throws IOException {
        ObjectMapper JSONMapper = new ObjectMapper();
        JSON unlocked = JSONMapper.readValue(receivedJSON, JSON.class);
        return unlocked;
    }

    public String getReport(JSON receivedJSON)
    {
        String toReturn = "";
        if (receivedJSON.getName() != null)
        {
            toReturn += "�����: " + receivedJSON.getName() + "\n";
        }
        toReturn += "������: " + receivedJSON.getWeather()[0].getDescription() + "\n";
        toReturn += "�����������: " + String.format("%.1f", receivedJSON.getMain().getTemp() - 273.15) + " C\n";
        toReturn += "��������� ���: " + String.format("%.1f", receivedJSON.getMain().getFeels_like() - 273.15) + " C\n";
        toReturn += "��������: " + receivedJSON.getMain().getPressure() + " �� ��. ��.\n";
        toReturn += "�������� �����: " + receivedJSON.getWind().getSpeed() + " �/c\n";

        if (receivedJSON.getRain() != null)
        {
            toReturn += "����� ����� � ��������� ���: " + receivedJSON.getRain().getHour() + "\n";
        }

        if (receivedJSON.getSnow() != null)
        {
            toReturn += "����� ����� � ��������� ���: " + receivedJSON.getSnow().getHour() + "\n";
        }
        return toReturn;
    }

    public SendMessage setButtons(long chatId)
    {
        InlineKeyboardMarkup inlineKeyboardMarkup =new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText("������");
        inlineKeyboardButton.setCallbackData("������");
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton);
        List<List<InlineKeyboardButton>> rowList= new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return new SendMessage().setChatId(chatId).setText("������!\n� �������� ���, ������� � ���� ��� �������� ������ (��� ���� ������, ���� ��� ��������� :3) �� RU/EN ����� � � ��� ���� ������� ������� �� ������ � ���� ������.\n���� ������, ���� ���������� � ����� ������� - ������ ������ \"������\"\n(????)?*:???").setReplyMarkup(inlineKeyboardMarkup);
    }

    public void randPlayer(SendMessage sendMessage, SendAudio sendMusic)
    {
        Random random = new Random();
        String path = "file:///C:/Music/14.mp3";
        path = "C:\\Music\\" + random.nextInt(18) + ".mp3";
        System.out.println(path);
        java.io.File hmmm = new java.io.File(path);
        InputFile theSong = new InputFile();
        theSong.setMedia(hmmm, "����");
        sendMusic.setAudio(theSong);

        sendMessage.setText(comms[random.nextInt(3)]);
        try
        {
            execute(sendMusic);
            execute(sendMessage);
        }
        catch (TelegramApiException e)
        {
            e.printStackTrace();
        }
    }
}