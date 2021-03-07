package UserJSON;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@JsonAutoDetect
public class DataBase
{
    private static long totalUsers;
    private static String curDate;
    private static User[] userList;

    public static User findUser(long chatId, User[] userList)   //функция поиска пользователя в БД, возвращает его экземпляр
    {
        if (userList == null)   //если список вовсе пустой
        {
            return null;
        }
        for (User user : userList)  //ищем юзера
        {
            if (user.getChatId() == chatId)
                return user;
        }
        return null;    //если не найден
    }

    public static int findUserNum(long chatId, User[] userList) //находит порядковый номер записи о пользователе в БД
    {
        if (userList == null)   //если список вовсе пустой
        {
            return -1;  //код возврата, если пользователь не найден
        }
        for (int i = 0; i < userList.length; i++)
        {
            if (userList[i].getChatId() == chatId)
                return i;
        }
        return -1;
    }

    public static DataBase getJSON(String receivedJSON) throws IOException {    //перегонка JSON в объект DataBase
        ObjectMapper JSONMapper = new ObjectMapper();
        return JSONMapper.readValue(receivedJSON, DataBase.class);
    }

    @SneakyThrows
    public static String writeToJSON(DataBase dataBase) //перегонка объекта DataBase в JSON
    {
        //Алярм! Тут пришлось прибегнуть к хитрости и вместо использования DataBase со статическими полями, с которыми удобно работать в основной части программы,
        //я создаю копию нашей dataBase, но в классе DataBaseJSON без статических полей, других отличий между ними нет
        //(хитрость нужна, т.к. иначе Jackson не может работать со статическим массивом)
        DataBaseJSON fake = new DataBaseJSON(); //создаём копию
        fake.setCurDate(dataBase.getCurDate());
        fake.setUserList(getUserList());
        fake.setTotalUsers(dataBase.getTotalUsers());
        ObjectMapper JSONMapper = new ObjectMapper();
        String s = JSONMapper.writeValueAsString(fake); //перегоняем в JSON
        return s;
    }

    public static void refreshBase(DataBase dataBase, String curDate)   //фукнция обновления полей даты последних изменений и общего количества учётных записей
    {
        dataBase.setCurDate(curDate);
        dataBase.setTotalUsers(getUserList().length);
    }

    @SneakyThrows
    public static void overWrite(DataBase dataBase, String path)    //функция перезаписи БД на диске
    {
        DataBase.refreshBase(dataBase, curDate);    //запускаем обновление для установки текущей даты
        String s = DataBase.writeToJSON(dataBase);  //перегоняем в JSON
        FileWriter fileWriter = new FileWriter(path, false);    //и записываем всё в файл
        fileWriter.write(s);
        fileWriter.flush();
        fileWriter.close();
    }

    public static boolean checkUser(Message recMessage, DataBase dataBase)  //функция создания новой учётки для юзера, возвращает true, если запись уже есть
    {
        userList = getUserList();
        if (DataBase.findUser(recMessage.getChatId(), userList) == null)
        {
            ArrayList<User> list = new ArrayList<>(Arrays.asList(userList));
            User buff = new User();
            buff.setUsername(recMessage.getFrom().getFirstName() + " " + recMessage.getFrom().getLastName());
            buff.setChatId(recMessage.getChatId());
            list.add(buff);
            dataBase.setUserList(list.toArray(new User[list.size()]));
            return false;
        }
        return true;
    }


    public static User[] getUserList() {
        return userList;
    }

    public void setUserList(User[] userList) {
        DataBase.userList = userList;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        DataBase.totalUsers = totalUsers;
    }

    public String getCurDate() {
        return curDate;
    }

    public void setCurDate(String curDate) {
        DataBase.curDate = curDate;
    }
}
