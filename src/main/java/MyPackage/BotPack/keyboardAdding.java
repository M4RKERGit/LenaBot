package MyPackage.BotPack;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import java.util.ArrayList;
import java.util.List;

public class keyboardAdding
{
    public static SendMessage setButtons(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add("Музяка");
        keyboardFirstRow.add("Анек");

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardFirstRow.add("Отмена");

        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboard);

        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.setText("Привет!\nЯ погодный бот, отправь в этот чат название города (или даже страны, если она небольшая :3) на RU/EN языке и я дам тебе краткую справку по погоде в этом городе.\nЕсли хочешь, могу поделиться с тобой музыкой - просто напиши \"Музяка\"\nМогу рассказать анекдот - напиши \"Анек\"\nДля удобства пользования ботом можешь вопспользоваться клавиатурой бота\n(ﾉ◕ヮ◕)ﾉ");

        return sendMessage;
    }
}
