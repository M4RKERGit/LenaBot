package WBPackage.BotPack;


import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Random;

public class MusicFeatures {
    public static void randPlayer(SendMessage sendMessage, SendAudio sendMusic) {
        Random random = new Random();
        String path;
        path = "music/" + random.nextInt(72) + ".mp3";
        System.out.println(path);
        java.io.File hmmm = new java.io.File(path);
        InputFile theSong = new InputFile();
        theSong.setMedia(hmmm, "Трек");
        sendMusic.setAudio(theSong);

        String[] comms = new String[]{"Это из моей особой коллеции", "Попробуй послушать это", "Вот только вчера нашла, знатно качает"};

        sendMessage.setText(comms[random.nextInt(3)]);
    }

    @SneakyThrows
    public static void getYouTube(SendMessage sendMessage, SendAudio sendMusic, String received) {
        sendMessage.setText("Нашла по твоему запросу: ");
        String[] GOT = received.split(" ", 3);
        StringBuilder requestBuilder = new StringBuilder();
        for (int i = 0; i < GOT.length; i++) {
            if (GOT[i].equals("ютуб") | GOT[i].equals("Ютуб")) {
                GOT[i] = "";
            } else requestBuilder.append(GOT[i]);
        }
        String request = requestBuilder.toString();
        if (request.length() > 3) {
            System.out.println("Конечный запрос: " + request);
            Process process = Runtime.getRuntime().exec("/usr/bin/python3 /root/MT/yamusic.py " + request);
            process.waitFor();
            System.out.println("exe finished");
            if (!(new File("res_sound.mp3").exists())) {
                sendMessage.setText("Извини, длина видео превышает 15 минут или такого видео не существует");
                return;
            }
            sendMusic.setAudio(new File("res_sound.mp3"));
        }
    }

    public static void appendLog(Message message, String curDate, String user) {
        String log = curDate + " Received: " + message.getText() + " " + "From: " + user;
        System.out.println(log);
        try {
            Files.write(Path.of("botlog.txt"), (log + "\n").getBytes(), new StandardOpenOption[]{StandardOpenOption.APPEND});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
