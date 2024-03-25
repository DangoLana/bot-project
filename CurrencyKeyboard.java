package org.example.bot;

import org.example.model.UserSettings;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CurrencyKeyboard {

    private final Set<UserSettings.Currency> selectedCurrencies = new HashSet<>();
    private final CurrencyTelegramBot bot;

    public CurrencyKeyboard(CurrencyTelegramBot bot) {
        this.bot = bot;
    }

    public void sendCurrencySettings(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Select the currency");
        message.setReplyMarkup(createCurrencyKeyboard());
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private ReplyKeyboardMarkup createCurrencyKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();

        // Define the check emoji
        String checkMarkEmoji = "\u2705";

        // Append the check emoji to "USD" if it is selected
        String usdLabel = selectedCurrencies.contains(UserSettings.Currency.USD) ? "USD " + checkMarkEmoji : "USD";
        row1.add(usdLabel);

        String eurLabel = selectedCurrencies.contains(UserSettings.Currency.EUR) ? "EUR " + checkMarkEmoji : "EUR";
        row1.add(eurLabel);

        row2.add("Settings Menu");
        row2.add("Get Info");
        keyboard.add(row1);
        keyboard.add(row2);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    public void toggleCurrencySelection(Update update, String chatId) {
        String text = update.getMessage().getText();
        if ("USD".equals(text) || "EUR".equals(text)) {
            UserSettings.Currency currency = UserSettings.Currency.valueOf(text);
            if (selectedCurrencies.contains(currency)) {
                selectedCurrencies.remove(currency);
            } else {
                selectedCurrencies.add(currency);
            }

            sendCurrencySettings(chatId);
        }
    }


}
