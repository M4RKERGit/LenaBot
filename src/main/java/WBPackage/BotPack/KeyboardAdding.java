package WBPackage.BotPack;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import java.util.ArrayList;
import java.util.List;

public class KeyboardAdding
{
    public static SendMessage setButtons(long chatId)   //стандартная процедура создания клавиатуры, можно смело копипастить
    {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();   //задаём первый ряд кнопок
        keyboardFirstRow.add("Музяка");
        keyboardFirstRow.add("Анек");
        keyboardFirstRow.add("Рандом");

        KeyboardRow keyboardSecondRow = new KeyboardRow();  //и второй
        keyboardSecondRow.add("Анек войсом");
        keyboardSecondRow.add("Пикча");
        keyboardSecondRow.add("Отмена");

        keyboard.add(keyboardFirstRow); //всё это добавляем
        keyboard.add(keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboard);

        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.setText("Привет!\n" +
                "Я погодный бот, отправь в этот чат название города (или даже страны, если она небольшая :3) на RU/EN языке и я дам тебе краткую справку по погоде в этом городе.\n" +
                "Если хочешь, могу поделиться с тобой музыкой - просто напиши \"Музяка\"\n" +
                "Могу рассказать анекдот - напиши \"Анек\"\n" +
                "Для удобства пользования ботом можешь воспользоваться клавиатурой бота\n" +
                "-------------------------------------------------------------------------------------\n" +
                "Полный список команд: \n" +
                "Название города/страны - вывод краткой справки о погоде в этом месте на ближайший час\n" +
                "Альтернатива этой функции - пришли мне координаты вложением, я их тоже понимаю)\n" +
                "-------------------------------------------------------------------------------------\n" +
                "Анек - расскажу тебе отбитый анекдот из моей коллекции\n" +
                "Анек войсом - тож самое, но с озвучкой\n" +
                "Анеки + <число> или Анеки войсом + <число> - несколько за раз\n" +
                "-------------------------------------------------------------------------------------\n" +
                "Музяка - пришлю тебе случайную песню из моей памяти с поддержкой плеера\n" +
                "Ютуб <твой запрос> - скомунизжу для тебя mp3 любого видео с ютуба\n" +
                "-------------------------------------------------------------------------------------\n" +
                "Пикча - пришлю тебе случайную картинку(обязательно красивую, хоть на обои ставь)\n" +
                "Пикча <твой запрос> - найду тебе картинку по запросу(720p)\n" +
                "-------------------------------------------------------------------------------------\n" +
                "Рандом <1-е число> <2-е число> - случайное число в указанном диапазоне\n" +
                "Если получу одно число - зароллю тебе от 0 до этого числа\n" +
                "Рандом без чисел - ролл от 1 до 100\n" +
                "-------------------------------------------------------------------------------------\n" +
                "Дефолт - вывод погоды во всех городах, которые ты выбрал\n" +
                "Дефолт город <название города> - добавить/убрать указанный город из списка\n" +
                "Дефолт все - вывод всех городов по умолчанию\n" +
                "Дефолт сброс - очистка списка выбранных городов\n" +
                "-------------------------------------------------------------------------------------\n" +
                "Если есть какие-то вопросы, напиши моему хозяину в личку @Miku\\_Tyan\n" +
                "-------------------------------------------------------------------------------------\n" +
                "Надеюсь, я тебе понравлюсь!\n" +
                "(ﾉ◕ヮ◕)ﾉ\\*\\:\\･\\ﾟ\\✧");
        return sendMessage;
    }
}
