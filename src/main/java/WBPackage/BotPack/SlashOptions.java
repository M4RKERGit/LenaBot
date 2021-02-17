package WBPackage.BotPack;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class SlashOptions {
    @SneakyThrows
    public static void getReport(SendMessage sendMessage) {
        List<String> buff;
        buff = Files.readAllLines(Path.of("botlog.txt"));
        StringBuilder normalized = new StringBuilder();
        for (int i = buff.size() - 30; i < buff.size(); i++) {
            normalized.append(buff.get(i)).append("\n\n");
        }
        sendMessage.setText(normalized.toString());
    }

    public static void getFile(SendDocument sendDocument) {
        File botLogFile = new File(String.valueOf(Path.of("botlog.txt")));
        sendDocument.setDocument(botLogFile);
    }

    public static void getUp(SendMessage sendMessage) {
        RuntimeMXBean rT = ManagementFactory.getRuntimeMXBean();
        long upTime = rT.getUptime() / 1000;
        String additional, buf;
        buf = String.valueOf(upTime);
        switch (buf.charAt(buf.length() - 1)) {
            case ('1'):
                additional = " секунду";
                break;
            case ('2'):
            case ('3'):
            case ('4'):
                additional = " секунды";
                break;
            default:
                additional = " секунд";
                break;
        }
        sendMessage.setText("Ебашу уже " + upTime + additional);
        if (upTime > 600) {
            upTime = upTime / 60;
            buf = String.valueOf(upTime);
            switch (buf.charAt(buf.length() - 1)) {
                case ('1'):
                    additional = " минуту";
                    break;
                case ('2'):
                case ('3'):
                case ('4'):
                    additional = " минуты";
                    break;
                default:
                    additional = " минут";
                    break;
            }
            sendMessage.setText("Ебашу уже " + upTime + additional);
        }
        if (upTime > 600) {
            upTime = upTime / 60;
            buf = String.valueOf(upTime);
            switch (buf.charAt(buf.length() - 1)) {
                case ('1'):
                    additional = " час";
                    break;
                case ('2'):
                case ('3'):
                case ('4'):
                    additional = " часа";
                    break;
                default:
                    additional = " часов";
                    break;
            }
            sendMessage.setText("Ебашу уже " + upTime + additional);
        }
    }

    @SneakyThrows
    public static void appendLog(Message message, String curDate, String user) {
        String log = curDate + " Received: " + message.getText() + " " + "From: " + user;
        System.out.println(log);
        Files.write(Path.of("botlog.txt"), (log + "\n").getBytes(), new StandardOpenOption[]{StandardOpenOption.APPEND});
    }
}
