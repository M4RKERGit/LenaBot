package WBPackage.BotPack;

import PictureJSON.PicJSON;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class PictureFeatures
{
    static String token;    //здесь, как и в AskingAPI, необходимы токены
    static String tokenPath = "tokens.txt";

    public static void getPic(SendPhoto sendPhoto, SendMessage sendMessage) //функция отправки фото с диска
    {
        Random random = new Random();
        String path = null;
        File file = new File("photos/");    //фото в этом каталоге
        if (file.exists())
        {
            String[] dir = file.list();
            assert dir != null;
            int rndDir = random.nextInt(dir.length);
            path = "photos/" + dir[rndDir];
        }
        System.out.println(path);
        assert path != null;
        java.io.File hmmm = new java.io.File(path);
        sendPhoto.setPhoto(hmmm);
        String[] comms = new String[] {"Красота же!", "Мне очень нравится это фото", "Доставлено!"};
        sendMessage.setText(comms[random.nextInt(3)]);
    }

    @SneakyThrows
    public static void getPic(SendPhoto sendPhoto, SendMessage sendMessage, String received)    //поиск фото по тегам и отправка
    {
        List<String> buff;
        buff = Files.readAllLines(Path.of(tokenPath));
        token = buff.get(1);
        String[] GOT = received.split(" ", 3);
        StringBuilder requestBuilder = new StringBuilder();
        for (int i = 0; i < GOT.length; i++)    //избавляемся от ключевых слов, оставляя только запрос
        {
            if (GOT[i].equals("пикча") | GOT[i].equals("Пикча"))
            {
                GOT[i] = "";
            }
            else requestBuilder.append(GOT[i]).append(" "); //опасно
        }
        String request = requestBuilder.toString(); //конечный запрос
        if (request.length() > 3) {
            System.out.println("Конечный запрос: " + request);
            String outPut;
            Scanner rec;
            String linkAddr = "https://pixabay.com/api/?key=" + token + "&q=" + request + "&image_type=photo";  //формируем URL для получения JSON
            HttpURLConnection conTar = (HttpURLConnection) new URL(linkAddr).openConnection();  //здесь и далее - получение JSON'a
            conTar.connect();
            try
            {
                rec = new Scanner(conTar.getInputStream());
                outPut = rec.nextLine();
            }
            catch (FileNotFoundException exception)
            {
                outPut = "Сорри, но я не нашла таких картинок, попробуй указать другие теги";
                System.out.println(request + "\n" + outPut);
                sendMessage.setText(outPut);
            }
            conTar.disconnect();
            System.out.println(outPut);
            PicJSON outJSON = getJSON(outPut);  //перегоняем полученный JSON в наш объект
            File bufPhoto = getURLPic(outJSON, sendMessage);    //получаем фото по ссылке
            if (bufPhoto == null)   //на случай, если у функции ничего не вышло
            {
                sendMessage.setText("Сорри, но я не нашла таких картинок, попробуй указать другие теги");
                return;
            }
            sendPhoto.setPhoto(bufPhoto);
            String[] comms = new String[] {"Красота же!", "Мне очень нравится это фото", "Доставлено!"};
            Random random = new Random();
            sendMessage.setText(comms[random.nextInt(3)]);
        }
    }


    @SneakyThrows
    public static PicJSON getJSON(String receivedJSON)  //перегонка JSON в наш объект
    {
        ObjectMapper JSONMapper = new ObjectMapper();
        return JSONMapper.readValue(receivedJSON, PicJSON.class);
    }


    public static File getURLPic(PicJSON receivedJSON, SendMessage sendMessage) //"расчленение" JSON'a и получение ссылки на случайную картинку из результатов поиска
    {
        String path = "picturebuff.jpg";    //временный файл, обновляется каждый вызов скрипта
        Random random = new Random();
        URL picUrl = null;
        if (receivedJSON.getHits().length < 1)  //Hits - количество результатов поиска
        {
            sendMessage.setText("Сорри, но я не нашла таких картинок, попробуй указать другие теги");
            return null;
        }
        try
        {
            picUrl = new URL(receivedJSON.getHits()[random.nextInt(receivedJSON.getHits().length)].getLargeImageURL());
        }
        catch (MalformedURLException e)
        {
            sendMessage.setText("Сорри, но я не нашла таких картинок, попробуй указать другие теги");
        }
        InputStream is = null;
        try
        {
            assert picUrl != null;
            is = picUrl.openStream();   //здесь и далее - получение картинки как файла по ссылке
        }
        catch (IOException e)
        {
            sendMessage.setText("Сорри, но я не нашла таких картинок, попробуй указать другие теги");
        }
        OutputStream os = null;
        try
        {
            os = new FileOutputStream(path);
        }
        catch (FileNotFoundException exception)
        {
            sendMessage.setText("Сорри, но я не нашла таких картинок, попробуй указать другие теги");
        }
        byte[] b = new byte[2048];
        int length;
        try
        {
            while (true)
            {
                assert is != null;
                if ((length = is.read(b)) == -1) break;
                assert os != null;
                os.write(b, 0, length);
            }
            is.close();
            assert os != null;
            os.close();
        }
        catch (IOException e) {
            sendMessage.setText("Сорри, но я не нашла таких картинок, попробуй указать другие теги");
        }
        return new File(path);  //возвращаем файл по старому пути, т.к. перезаписали его
    }
}
