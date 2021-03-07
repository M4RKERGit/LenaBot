package WBPackage.BotPack;

import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Random;

public class RandomTools
{
    public static int replyRand(String received)    //дфеолтная функция рандома в случае получения двух аргументов
    {
        int lowBorder, highBorder;
        String[] GOT = received.split(" ", 3);
        if (GOT.length == 1)
        {
            return getRand();   //если аргументы не получены
        }
        if (GOT.length == 2 && !GOT[1].equals(""))  //если есть один целочисленный аргумент
        {
            int singleBorder = Integer.parseInt(GOT[1]);
            return getRand(0, singleBorder);
        }
        lowBorder = Integer.parseInt(GOT[1]);   //здесь и далее - если есть оба аргумента
        highBorder = Integer.parseInt(GOT[2]);
        if (highBorder < lowBorder) //на случай, если границы указаны не в том порядке, ставим их по местам
        {
            lowBorder = lowBorder + highBorder;
            highBorder = highBorder - lowBorder;
            highBorder = -highBorder;
            lowBorder = lowBorder - highBorder;
        }
        return getRand(lowBorder, highBorder);
    }

    public static int getRand(int lowBorder, int highBorder) //функция рандома в случае получения одного аргумента
    {
        Random random = new Random();
        int range = highBorder - lowBorder + 1;
        return lowBorder + random.nextInt(range);
    }

    public static int getRand() //рандом без аргументов
    {
        Random random = new Random();
        int range = 99;
        return 1 + random.nextInt(range);
    }
}
