package org.example.bot;


import org.example.bot.settingsFORkeyboard.SettingsForKeyboard;
import org.example.model.UserSettings;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import static org.example.utils.ConstantData.*;
public class CurrencyTelegramBot extends TelegramLongPollingBot {

    private final SettingsForKeyboard settingsForKeyboard;

    public CurrencyTelegramBot() {
        this.settingsForKeyboard = new SettingsForKeyboard(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage message = new SendMessage();
        String chatId = update.getMessage().getChatId().toString();
        message.setChatId(chatId);
        if (isMessagePresent(update) && update.getMessage().getText().equalsIgnoreCase(BOT_COMMAND_START)) {
            message.setText(BOT_COMMAND_GREETING);
            message.setReplyMarkup(setupBeginButton());
            UserSettings userSettings = new UserSettings();
            userSettings.setNotificationTime(9);
            settingsForKeyboard.sendNotificationTimeSettings(chatId, String.valueOf(userSettings.getNotificationTime()));
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        } else if (isMessagePresent(update)) {
            String text = update.getMessage().getText();

            switch (text) {
                case "Get Info": {
                    settingsForKeyboard.sendStartMessage(chatId);
                    settingsForKeyboard.sendExchangeRates(chatId);
                    break;
                }
                case "Settings":
                case "Settings Menu": {
                    settingsForKeyboard.sendSettingsMenu(chatId);
                    break;
                }
                case "Number of decimal places": {
                    message.setReplyMarkup(settingsForKeyboard.createSignAfterCommaKeyboard());
                    settingsForKeyboard.sendSignAfterCommaSettings(chatId);
                    break;
                }
                case "Bank":
                case "NBU":
                case "PrivatBank":
                case "MonoBank": {
                    settingsForKeyboard.sendBankSettings(chatId);
                    break;
                }
                case "Currency":
                case "USD":
                case "EUR": {
                    settingsForKeyboard.sendCurrencySettings(chatId);
                    break;
                }
                case "Notification time": {
                    settingsForKeyboard.deletJob(chatId);
                    settingsForKeyboard.createKeyBordTime(chatId);

                    break;
                }
                case "2":
                case "3":
                case "4": {

                    settingsForKeyboard.createSignAfterCommaKeyboard();
                    break;
                }
                case "off": {
                    settingsForKeyboard.deletJob(chatId);

                }
                case "9":
                case "10":
                case "11":
                case "12":
                case "13":
                case "14":
                case "15":
                case "16":
                case "17":
                case "18": {
                    UserSettings userSettings = new UserSettings();
                    userSettings.setNotificationTime(Integer.parseInt(text));
                    settingsForKeyboard.sendNotificationTimeSettings(chatId, text);
                    break;
                }

                default: {
                    break;
                }
            }

        }
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    public static boolean isMessagePresent(Update update){
        return update.hasMessage() && update.getMessage().hasText();
    }

    public ReplyKeyboard setupBeginButton() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(GET_INFO);
        KeyboardRow row2 = new KeyboardRow();
        row2.add(SETTNGS);

        keyboard.add(row1);
        keyboard.add(row2);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }
}