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

public class SlashOptions
{
    @SneakyThrows
    public static void getReport(SendMessage sendMessage)   //получаем из лога последние 30 записей и отсылаем в сообщении
    {
        List<String> buff;
        buff = Files.readAllLines(Path.of("botlog.txt"));
        StringBuilder normalized = new StringBuilder();
        for (int i = buff.size() - 30; i < buff.size(); i++)
        {
            normalized.append(buff.get(i)).append("\n\n");
        }
        sendMessage.setText(normalized.toString());
    }

    public static void getFile(SendDocument sendDocument)   //получаем полный логфайл
    {
        File botLogFile = new File(String.valueOf(Path.of("botlog.txt")));
        sendDocument.setDocument(botLogFile);
    }

    public static void getUp(SendMessage sendMessage)   //отображение аптайма в удобном человеческом формате
    {
        RuntimeMXBean rT = ManagementFactory.getRuntimeMXBean();
        long upTime = rT.getUptime()/1000;  //сначала получаем его в секундах
        String additional, buf;
        buf = String.valueOf(upTime);
        switch (buf.charAt(buf.length() - 1))   //свич выбора падежа
        {
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
        if (upTime <= 20L)  //на случай чисел 10-20
        {
            additional = " секунд";
        }
        sendMessage.setText("Онлайн уже " + upTime + additional);
        if (upTime > 600)   //через 10 минут преобразуем в минуты
        {
            upTime = upTime/60;
            buf = String.valueOf(upTime);
            switch (buf.charAt(buf.length() - 1))
            {
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
            if (upTime <= 20L)
            {
                additional = " минут";
            }
            sendMessage.setText("Онлайн уже " + upTime + additional);
        }
        if (upTime > 600)//через 10 часов - в часы, далее исчисление ведётся в часах
        {
            upTime = upTime/60;
            buf = String.valueOf(upTime);
            switch (buf.charAt(buf.length() - 1))
            {
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
            if (upTime <= 20L)
            {
                additional = " часов";
            }
            sendMessage.setText("Онлайн уже " + upTime + additional);
        }
    }

    @SneakyThrows
    public static void upBase(SendMessage sendMessage)  //получаем БД сообщением
    {
        List<String> buff;
        buff = Files.readAllLines(Path.of("database.txt"));
        StringBuilder normalized = new StringBuilder();
        for (int i = 0; i < buff.size(); i++)
        {
            normalized.append(buff.get(i)).append("\n\n");
        }
        sendMessage.setText(normalized.toString());
    }

    public static void upBaseFile(SendDocument sendDocument)    //получаем БД файлом на случай редактирования
    {
        File botLogFile = new File(String.valueOf(Path.of("database.txt")));
        sendDocument.setDocument(botLogFile);
    }

    @SneakyThrows
    public static void appendLog(String text, String curDate, String user)  //фукнция логгирования
    {
        String log = curDate + " Received: " + text + " " + "From: " + user;
        System.out.println(log);
        Files.write(Path.of("botlog.txt"), (log + "\n").getBytes(), new StandardOpenOption[]{StandardOpenOption.APPEND});
    }
}
