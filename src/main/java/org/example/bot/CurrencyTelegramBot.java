package org.example.bot;



import org.example.settingsFORkeyboard.SettingsForKeyboard;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
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

@EqualsAndHashCode(callSuper = true)
@Data
public class CurrencyTelegramBot extends TelegramLongPollingBot {

    private final SettingsForKeyboard settingsForKeyboard;
    private final CurrencyService currencyService;
    private final BankService bankService;
    private final NumberForDecimalPlaces numberForDecimalPlaces;

    public CurrencyTelegramBot() {
        this.settingsForKeyboard = new SettingsForKeyboard(this);
        this.currencyService = new CurrencyService(this);
        this.bankService = new BankService(this);
        this.numberForDecimalPlaces = new NumberForDecimalPlaces(this);
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        SendMessage message = new SendMessage();
        String chatId = update.getMessage().getChatId().toString();
        message.setChatId(chatId);
        if (isMessagePresent(update) && update.getMessage().getText().equalsIgnoreCase(BOT_COMMAND_START)) {
            message.setText(BOT_COMMAND_GREETING);
            message.setReplyMarkup(setupBeginButton());
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        } else if (isMessagePresent(update)) {

            String text = update.getMessage().getText();
            System.out.println("text = " + text);
            switch (text) {

                case "Get Info": {
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

               {
                    settingsForKeyboard.sendBankSettings(chatId);
                    break;
                }
                case "NBU":
                case "PrivatBank":
                case "MonoBank":{
                    settingsForKeyboard.sendExchangeRates(chatId);
                    break;}
                case "Currency": {
                    settingsForKeyboard.sendCurrencySettings(chatId);
                    break;
                }
                case "USD": {
                    settingsForKeyboard.addCurrency(UserSettings.Currency.USD);
                    settingsForKeyboard.sendExchangeRates(chatId);
                    break;
                }
                case "EUR": {
                  settingsForKeyboard.addCurrency(UserSettings.Currency.EUR);
                    settingsForKeyboard.sendExchangeRates(chatId);
                    break;
                }
                case "Remove USD": {
                    settingsForKeyboard.removeCurrency(UserSettings.Currency.USD);
                    settingsForKeyboard.sendExchangeRates(chatId);
                    break;
                }
                case "Remove EUR": {
                   settingsForKeyboard.removeCurrency(UserSettings.Currency.EUR);
                    settingsForKeyboard.sendExchangeRates(chatId);                   
                    settingsForKeyboard.sendBankSettings(chatId);
                    break;

                case "Currency":
                    settingsForKeyboard.sendCurrencySettings(chatId);
                     break;
                case "USD": {
                    currencyService.addCurrency(UserSettings.Currency.USD);
                    UserSettings newSettings = new UserSettings();
                    newSettings.setCurrency(UserSettings.Currency.USD);
                    settingsForKeyboard.updateSettings(chatId, newSettings);
                    break;
                }
                case "EUR": {
                    currencyService.addCurrency(UserSettings.Currency.EUR);
                    UserSettings newSettings = new UserSettings();
                    newSettings.setCurrency(UserSettings.Currency.EUR);
                    settingsForKeyboard.updateSettings(chatId, newSettings);
                    break;
                }
                case "Remove USD": {
                    currencyService.removeCurrency(UserSettings.Currency.USD);
                    UserSettings newSettings = new UserSettings();
                    settingsForKeyboard.updateSettings(chatId, newSettings);
                    break;
                }
                case "Remove EUR": {
                    currencyService.removeCurrency(UserSettings.Currency.EUR);
                    UserSettings newSettings = new UserSettings();
                    settingsForKeyboard.updateSettings(chatId, newSettings);
                    break;
                }
                case "PrivatBank": {
                    bankService.addBank(UserSettings.ChoiceBank.PRIVATBANK);
                    UserSettings newSettings = new UserSettings();
                    newSettings.setBanks(UserSettings.ChoiceBank.PRIVATBANK);
                    settingsForKeyboard.updateSettings(chatId, newSettings);
                    break;
                }
                case "MonoBank": {
                    bankService.addBank(UserSettings.ChoiceBank.MONOBANK);
                    UserSettings newSettings = new UserSettings();
                    newSettings.setBanks(UserSettings.ChoiceBank.MONOBANK);
                    settingsForKeyboard.updateSettings(chatId, newSettings);
                    break;

                }
                case "NBU": {
                    bankService.addBank(UserSettings.ChoiceBank.NBU);
                    UserSettings newSettings = new UserSettings();
                    newSettings.setBanks(UserSettings.ChoiceBank.NBU);
                    settingsForKeyboard.updateSettings(chatId, newSettings);
                    break;
                }
                case "Remove MonoBank": {
                    bankService.removeBank(UserSettings.ChoiceBank.MONOBANK);
                    UserSettings newSettings = new UserSettings();
                    settingsForKeyboard.updateSettings(chatId, newSettings);
                    break;
                }
                case "Remove PrivatBank": {
                    bankService.removeBank(UserSettings.ChoiceBank.PRIVATBANK);
                    UserSettings newSettings = new UserSettings();
                    settingsForKeyboard.updateSettings(chatId, newSettings);
                    break;
                }
                case "Remove NBU": {
                    bankService.removeBank(UserSettings.ChoiceBank.NBU);
                    UserSettings newSettings = new UserSettings();
                    settingsForKeyboard.updateSettings(chatId, newSettings);
                    break;
                }
                case "Notification time": {
                    settingsForKeyboard.sendNotificationTimeSettings(chatId);
                    break;
                }
                case "2": {
                    numberForDecimalPlaces.addDecimalPlaces(UserSettings.DecimalPlaces.TWO);
                    UserSettings newSettings = new UserSettings();
                    newSettings.setDecimalPlaces(UserSettings.DecimalPlaces.TWO);
                    settingsForKeyboard.updateSettings(chatId, newSettings);
                    break;
                }
                case "3": {
                    numberForDecimalPlaces.addDecimalPlaces(UserSettings.DecimalPlaces.THREE);
                    UserSettings newSettings = new UserSettings();
                    newSettings.setDecimalPlaces(UserSettings.DecimalPlaces.THREE);
                    settingsForKeyboard.updateSettings(chatId, newSettings);
                    break;
                }
                case "4": {
                    numberForDecimalPlaces.addDecimalPlaces(UserSettings.DecimalPlaces.FOUR);
                    UserSettings newSettings = new UserSettings();
                    newSettings.setDecimalPlaces(UserSettings.DecimalPlaces.FOUR);
                    settingsForKeyboard.updateSettings(chatId, newSettings);
                    break;

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
                    settingsForKeyboard.sendNotificationTimeSettings(chatId);
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

    public static boolean isMessagePresent(Update update) {
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
