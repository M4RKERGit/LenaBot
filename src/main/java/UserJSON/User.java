package UserJSON;


import java.util.ArrayList;
import java.util.Arrays;

public class User
{
    private String username;
    private long chatId;
    private String[] citiesList;

    public static boolean cityUpdate(String city, User recUser) //функция добавления/удаления города из списка
    {
        String[] curList = recUser.citiesList;
        if (curList == null)    //если список пуст, то добавляем город
        {
            curList = new String[1];
            curList[0] = city;
            recUser.setCitiesList(curList);
            return true;
        }
        ArrayList<String> list = new ArrayList<>(Arrays.asList(curList));   //кастуем в ArrayList для удобства удаления
        for (byte i = 0; i < curList.length; i++)   //ищем город в списке
        {
            if (city.equals(curList[i]))
            {
                list.remove(city);  //удаляем в случае нахождения
                curList = list.toArray(new String[list.size()]);    //и кастуем обратно
                recUser.setCitiesList(curList);
                return false;   //false в случае удаления
            }
        }
        list.add(city);
        curList = list.toArray(new String[list.size()]);
        recUser.setCitiesList(curList);
        return true;    //true в случае добавления
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String[] getCitiesList() {
        return citiesList;
    }

    public void setCitiesList(String[] citiesList) {
        this.citiesList = citiesList;
    }
}
