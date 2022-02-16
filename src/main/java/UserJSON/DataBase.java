package UserJSON;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

@JsonAutoDetect
@Data
public class DataBase
{
    private long totalUsers;
    private String curDate;
    private User[] userList;

    public User findUser(long chatId, User[] userList)
    {
        if (userList == null) return null;
        for (User user : userList)
            if (user.getChatId() == chatId) return user;
        return null;
    }

    public int findUserNum(long chatId, User[] userList) //находит порядковый номер записи о пользователе в БД
    {
        if (userList == null) return -1;
        for (int i = 0; i < userList.length; i++)
            if (userList[i].getChatId() == chatId) return i;
        return -1;
    }

    public DataBase getJSON(String receivedJSON) throws IOException
    {
        ObjectMapper JSONMapper = new ObjectMapper();
        return JSONMapper.readValue(receivedJSON, DataBase.class);
    }

    public String writeToJSON(DataBase dataBase) throws JsonProcessingException //перегонка объекта DataBase в JSON
    {
        return "";
    }

    public void refreshBase(DataBase dataBase, String curDate)   //фукнция обновления полей даты последних изменений и общего количества учётных записей
    {
        dataBase.setCurDate(curDate);
        dataBase.setTotalUsers(getUserList().length);
    }

    public void overWrite(DataBase dataBase, String path) throws IOException    //функция перезаписи БД на диске
    {
        refreshBase(dataBase, curDate);    //запускаем обновление для установки текущей даты
        String s = writeToJSON(dataBase);  //перегоняем в JSON
        FileWriter fileWriter = new FileWriter(path, false);    //и записываем всё в файл
        fileWriter.write(s);
        fileWriter.flush();
        fileWriter.close();
    }

    public boolean checkUser(Message recMessage, DataBase dataBase)  //функция создания новой учётки для юзера, возвращает true, если запись уже есть
    {
        if (findUser(recMessage.getChatId(), userList) == null)
        {
            ArrayList<User> list = new ArrayList<>(Arrays.asList(userList));
            User buff = new User();
            buff.setUsername(recMessage.getFrom().getFirstName() + " " + recMessage.getFrom().getLastName());
            buff.setChatId(recMessage.getChatId());
            list.add(buff);
            dataBase.setUserList(list.toArray(new User[0]));
            return false;
        }
        return true;
    }
}
