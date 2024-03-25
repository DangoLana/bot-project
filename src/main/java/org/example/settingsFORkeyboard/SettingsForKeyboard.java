package org.example.settingsFORkeyboard;


import lombok.Data;
import org.example.bot.CurrencyTelegramBot;
import org.example.model.UserSettings;
import org.example.utils.ConstantData;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.*;
import java.util.List;


@Data
public class SettingsForKeyboard {


    private final CurrencyTelegramBot bot;
    private UserSettings.ChoiceBank selectedBank;
    private UserSettings.Currency selectedCurrency;
    private UserSettings.DecimalPlaces decimalPlaces;

    public SettingsForKeyboard(CurrencyTelegramBot bot) {
        this.bot = bot;
    }

    public void updateSettings(String chatId, UserSettings newSettings) {
        // Оновлюємо внутрішні дані на основі нових налаштувань
        this.selectedBank = newSettings.getBanks();
        this.selectedCurrency = newSettings.getCurrency();
        this.decimalPlaces = newSettings.getDecimalPlaces();
        // Викликаємо метод для виведення оновлених налаштувань у консоль (для перевірки)
        System.out.println("Updated settings:");
        System.out.println("Selected bank: " + bot.getBankService().getHashSetBank());
        System.out.println("Selected currency: " + bot.getCurrencyService().getHashSetCurrencies());
        System.out.println("Decimal places: " + decimalPlaces);

    }

    public void sendExchangeRates(String chatId) {
        try {
            StringBuilder messageText = bot.getBankService().getFinalStringBuilder();
            SendMessage message = new SendMessage();
            message.setText(messageText.toString());
            message.setChatId(chatId);
            bot.execute(message);
        } catch (TelegramApiException | IOException e) {
            e.printStackTrace();

        }
    }

    public void sendSettingsMenu(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Menu");
        message.setReplyMarkup(createSettingsMenuKeyboard());
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
        KeyboardRow row3 = new KeyboardRow();
        row1.add("USD");
        row1.add("EUR");
        row2.add("Remove USD");
        row2.add("Remove EUR");
        row3.add("Settings Menu");
        row3.add("Get Info");
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }


    public void sendNotificationTimeSettings(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Select notification time");
        message.setReplyMarkup(createNotificationTimeKeyboard());

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private ReplyKeyboardMarkup createNotificationTimeKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Додавання кнопок для вибору часу нотифікації від 9 до 18 годин
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        KeyboardRow row4 = new KeyboardRow();
        KeyboardRow row5 = new KeyboardRow();
        row1.add("9");
        row1.add("10");
        row1.add("11");
        row2.add("12");
        row2.add("13");
        row2.add("14");
        row3.add("15");
        row3.add("16");
        row3.add("17");
        row4.add("18");
        row4.add("off");
        row5.add("Settings Menu");
        row5.add("Get Info");
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);
        keyboard.add(row5);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }


    public void sendSignAfterCommaSettings(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Select the number of decimal places");
        message.setReplyMarkup(createSignAfterCommaKeyboard());

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
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

    public void sendBankSettings(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Select the bank");
        message.setReplyMarkup(createBankKeyboard());

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private ReplyKeyboardMarkup createBankKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        row1.add("NBU");
        row1.add("PrivatBank");
        row1.add("MonoBank");
        row2.add("Remove NBU");
        row2.add("Remove PrivatBank");
        row2.add("Remove MonoBank");
        row3.add("Settings Menu");
        row3.add(ConstantData.GET_INFO);
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    public ReplyKeyboardMarkup createSettingsMenuKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        KeyboardRow row4 = new KeyboardRow();
        row1.add(ConstantData.DECIMAL_PLACES);
        row2.add(ConstantData.BANK);
        row2.add(ConstantData.CURRENCY);
        row3.add(ConstantData.NOTIFICATION_TIME);
        row4.add(ConstantData.SETTNGS);
        row4.add(ConstantData.GET_INFO);
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    public ReplyKeyboardMarkup createSignAfterCommaKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        row1.add("2");
        row1.add("3");
        row1.add("4");
        row2.add(ConstantData.SETTNGS);
        row2.add(ConstantData.GET_INFO);
        keyboard.add(row1);
        keyboard.add(row2);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }
}




