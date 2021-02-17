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
import java.util.Random;
import java.util.Scanner;

public class PictureFeatures {
    public static void getPic(SendPhoto sendPhoto, SendMessage sendMessage) {
        Random random = new Random();
        String path = null;
        File file = new File("photos/");
        if (file.exists()) {
            String[] dir = file.list();
            assert dir != null;
            int rndDir = random.nextInt(dir.length);
            path = "photos/" + dir[rndDir];
        }
        System.out.println(path);
        assert path != null;
        java.io.File hmmm = new java.io.File(path);
        sendPhoto.setPhoto(hmmm);
        String[] comms = new String[]{"Красота же!", "Мне очень нравится это фото", "Доставлено!"};
        sendMessage.setText(comms[random.nextInt(3)]);
    }

    @SneakyThrows
    public static void getPic(SendPhoto sendPhoto, SendMessage sendMessage, String received) {
        String[] GOT = received.split(" ", 3);
        StringBuilder requestBuilder = new StringBuilder();
        for (int i = 0; i < GOT.length; i++) {
            if (GOT[i].equals("пикча") | GOT[i].equals("Пикча")) {
                GOT[i] = "";
            } else requestBuilder.append(GOT[i]).append(" "); //опасно
        }
        String request = requestBuilder.toString();
        if (request.length() > 3) {
            System.out.println("Конечный запрос: " + request);
            String outPut;
            Scanner rec;
            String linkAddr = "https://pixabay.com/api/?key=20129846-7b8836a9a63b2f2b6fa405f5f&q=" + request + "&image_type=photo";
            HttpURLConnection conTar = (HttpURLConnection) new URL(linkAddr).openConnection();
            conTar.connect();
            try {
                rec = new Scanner(conTar.getInputStream());
                outPut = rec.nextLine();
            } catch (FileNotFoundException exception) {
                outPut = "Сорри, но я не нашла таких картинок, попробуй указать другие теги";
                System.out.println(request + "\n" + outPut);
                sendMessage.setText(outPut);
            }
            conTar.disconnect();
            System.out.println(outPut);
            PicJSON outJSON = getJSON(outPut);
            File bufPhoto = getURLPic(outJSON, sendMessage);
            if (bufPhoto == null) {
                sendMessage.setText("Сорри, но я не нашла таких картинок, попробуй указать другие теги");
                return;
            }
            sendPhoto.setPhoto(bufPhoto);
            String[] comms = new String[]{"Красота же!", "Мне очень нравится это фото", "Доставлено!"};
            Random random = new Random();
            sendMessage.setText(comms[random.nextInt(3)]);
        }
    }


    @SneakyThrows
    public static PicJSON getJSON(String receivedJSON) {
        ObjectMapper JSONMapper = new ObjectMapper();
        return JSONMapper.readValue(receivedJSON, PicJSON.class);
    }


    public static File getURLPic(PicJSON receivedJSON, SendMessage sendMessage) {
        String path = "picturebuff.jpg";
        Random random = new Random();
        URL picUrl = null;
        if (receivedJSON.getHits().length < 1) {
            sendMessage.setText("Сорри, но я не нашла таких картинок, попробуй указать другие теги");
            return null;
        }
        try {
            picUrl = new URL(receivedJSON.getHits()[random.nextInt(receivedJSON.getHits().length)].getLargeImageURL());
        } catch (MalformedURLException e) {
            sendMessage.setText("Сорри, но я не нашла таких картинок, попробуй указать другие теги");
        }
        InputStream is = null;
        try {
            is = picUrl.openStream();
        } catch (IOException e) {
            sendMessage.setText("Сорри, но я не нашла таких картинок, попробуй указать другие теги");
        }
        OutputStream os = null;
        try {
            os = new FileOutputStream(path);
        } catch (FileNotFoundException exception) {
            sendMessage.setText("Сорри, но я не нашла таких картинок, попробуй указать другие теги");
        }
        byte[] b = new byte[2048];
        int length;
        try {
            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }
            is.close();
            os.close();
        } catch (IOException e) {
            sendMessage.setText("Сорри, но я не нашла таких картинок, попробуй указать другие теги");
        }

        return new File(path);
    }
}
