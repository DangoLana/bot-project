package org.example.bot.settingsFORkeyboard;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.example.notify.SendMessageOnTime;
import org.example.json.JsonMB;
import org.example.bot.CurrencyTelegramBot;
import org.example.json.JsonPB;
import org.example.model.UserSettings;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.quartz.SchedulerException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import java.lang.reflect.Type;
import java.util.*;
import java.util.List;




public class SettingsForKeyboard  {
    private final CurrencyTelegramBot bot;

    public SettingsForKeyboard(CurrencyTelegramBot bot) {
        this.bot = bot;
    }


    private UserSettings.ChoiceBank selectedBank = UserSettings.ChoiceBank.NBU;
    private final UserSettings.Currency selectedCurrency = UserSettings.Currency.USD;
    private final Set<UserSettings.Currency> selectedCurrencies = new HashSet<>();

    private final SendMessageOnTime sendMessageOnTime = new SendMessageOnTime();

    UserSettings userSettings = new UserSettings();


    private String fetchDataFromUrl(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).string();
        }
    }

    private String getPrivatBankExchangeRates(String data) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<JsonPB>>() {
        }.getType();
        List<JsonPB> privatBankList = gson.fromJson(data, listType);

        StringBuilder sb = new StringBuilder();
        for (JsonPB pb : privatBankList) {
            sb.append("Currency: ").append(pb.getCcy()).append("\n");
            sb.append("Buy rate: ").append(pb.getBuy()).append("\n");
            sb.append("Sell rate: ").append(pb.getSale()).append("\n\n");
        }
        return sb.toString();
    }

    private String getNbuExchangeRates(String data) {
        StringBuilder sb = new StringBuilder();
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String currencyCode = obj.getString("cc");
                if (currencyCode.equals("USD") || currencyCode.equals("EUR")) {
                    float rate = obj.getFloat("rate");

                    sb.append("Currency: ").append(currencyCode).append("\n");
                    sb.append("Buy rate: ").append(rate).append("\n");
                    sb.append("Sell rate: ").append(rate).append("\n\n");

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    public ReplyKeyboard sendSettingsMenu(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Menu");
        message.setReplyMarkup(createSettingsMenuKeyboard());
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return null;
    }



    private ReplyKeyboardMarkup createCurrencyKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        row1.add("USD");
        row1.add("EUR");
        row2.add("Settings Menu");
        row2.add("Get Info");
        keyboard.add(row1);
        keyboard.add(row2);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    public void sendNotificationTimeSettings(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Get currency rates at " + text + ":00");

        try {
            sendMessageOnTime.sendMessageByTime(Integer.parseInt(text), Long.valueOf(chatId));
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }


        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void deletJob(String chatId){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Turn off the message");

        try {
            sendMessageOnTime.deleteJob();
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    public void createKeyBordTime(String chatId){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Select notification time");
        message.setReplyMarkup(createNotificationTimeKeyboard());

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public ReplyKeyboardMarkup createNotificationTimeKeyboard() {
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

    private String getMonoBankExchangeRates(String monoBankData) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<JsonMB>>() {
        }.getType();
        List<JsonMB> monoBankList = gson.fromJson(monoBankData, listType);

        StringBuilder sb = new StringBuilder();
        for (JsonMB mb : monoBankList) {
            sb.append("Buy rate: ").append(mb.getRateBuy()).append("\n");
            sb.append("Sell rate: ").append(mb.getRateSell()).append("\n\n");
        }
        return sb.toString();
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
    public void sendStartMessage(String chatId) {
        UserSettings defaultSettings = new UserSettings();

        // Встановлення тільки PrivatBank та USD за замовчуванням
        selectedBank = defaultSettings.getBanks();
        selectedCurrencies.clear(); // Очистимо обрані валюти
        selectedCurrencies.add(UserSettings.Currency.USD); // Додамо USD

        SendMessage answer = new SendMessage();
        answer.setChatId(String.valueOf(chatId));
        answer.setText("Choose");
        answer.setReplyMarkup(bot .setupBeginButton());
        try {
            bot.execute(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void sendExchangeRates(String chatId) {
        try {
            String data;
            String bankUrl;
            if (selectedBank == UserSettings.ChoiceBank.PrivatBank) {
                bankUrl = "https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5";
            } else {
                bankUrl = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json";
            }
            data = fetchDataFromUrl(bankUrl);

            String rates;
            if (selectedBank == UserSettings.ChoiceBank.PrivatBank) {
                rates = getPrivatBankExchangeRates(data);
            } else {
                rates = getNbuExchangeRates(data);
            }

            if (selectedBank == UserSettings.ChoiceBank.NBU && selectedCurrency == UserSettings.Currency.USD) {
                Set<UserSettings.Currency> currenciesForNBU = new HashSet<>();
                currenciesForNBU.add(UserSettings.Currency.USD);
                currenciesForNBU.add(UserSettings.Currency.EUR);
                for (UserSettings.Currency currency : currenciesForNBU) {
                    if (currency.equals(UserSettings.Currency.USD)) {
                        rates = filterExchangeRatesByCurrency(rates, currency);
                    }
                }
            }
            if (selectedBank == UserSettings.ChoiceBank.NBU && selectedCurrency == UserSettings.Currency.EUR) {
                Set<UserSettings.Currency> currenciesForNBU = new HashSet<>();
                currenciesForNBU.add(UserSettings.Currency.USD);
                currenciesForNBU.add(UserSettings.Currency.EUR);
                for (UserSettings.Currency currency : currenciesForNBU) {
                    if (currency.equals(UserSettings.Currency.EUR)) {
                        rates = filterExchangeRatesByCurrency(rates, currency);
                    }
                }
            }
            if (selectedBank ==UserSettings. ChoiceBank.PrivatBank  && selectedCurrency == UserSettings.Currency.USD) {
                Set<UserSettings.Currency> currenciesForPB = new HashSet<>();
                currenciesForPB.add(UserSettings.Currency.USD);
                currenciesForPB.add(UserSettings.Currency.EUR);
                for (UserSettings.Currency currency : currenciesForPB) {
                    if (currency.equals(UserSettings.Currency.USD)) {
                        rates = filterExchangeRatesByCurrency(rates, currency);
                    }
                }
            }

            if (selectedBank == UserSettings.ChoiceBank.PrivatBank && selectedCurrency == UserSettings.Currency.EUR) {
                Set<UserSettings.Currency> currenciesForPB = new HashSet<>();
                currenciesForPB.add(UserSettings.Currency.USD);
                currenciesForPB.add(UserSettings.Currency.EUR);
                for (UserSettings.Currency currency : currenciesForPB) {
                    if (currency.equals(UserSettings.Currency.EUR)) {
                        rates = filterExchangeRatesByCurrency(rates, currency);
                    }
                }
            }
            // Якщо умова не виконується, виводимо всі курси без фільтрації
            String limitedRates = limitExchangeRates(rates);
            String bankName = (selectedBank == UserSettings.ChoiceBank.PrivatBank) ? "PrivatBank" : "NBU";

            String messageText = "Exchange rates data:\n\nBank: " + bankName + "\n\n" + limitedRates;

            SendMessage message = new SendMessage();
            message.setText(messageText);
            message.setChatId(String.valueOf(chatId));

            bot.execute(message);

        } catch (IOException | TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String sendExchangeRatesWithStr(String chatId) {
        String result = null;
            String data;
            String bankUrl;
            if (selectedBank == UserSettings.ChoiceBank.PrivatBank) {
                bankUrl = "https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5";
            } else {
                bankUrl = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json";
            }
        try {
            data = fetchDataFromUrl(bankUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String rates;
            if (selectedBank == UserSettings.ChoiceBank.PrivatBank) {
                rates = getPrivatBankExchangeRates(data);
            } else {
                rates = getNbuExchangeRates(data);
            }

            if (selectedBank == UserSettings.ChoiceBank.NBU && selectedCurrency == UserSettings.Currency.USD) {
                Set<UserSettings.Currency> currenciesForNBU = new HashSet<>();
                currenciesForNBU.add(UserSettings.Currency.USD);
                currenciesForNBU.add(UserSettings.Currency.EUR);
                for (UserSettings.Currency currency : currenciesForNBU) {
                    if (currency.equals(UserSettings.Currency.USD)) {
                        rates = filterExchangeRatesByCurrency(rates, currency);
                    }
                }
            }
            if (selectedBank == UserSettings.ChoiceBank.NBU && selectedCurrency == UserSettings.Currency.EUR) {
                Set<UserSettings.Currency> currenciesForNBU = new HashSet<>();
                currenciesForNBU.add(UserSettings.Currency.USD);
                currenciesForNBU.add(UserSettings.Currency.EUR);
                for (UserSettings.Currency currency : currenciesForNBU) {
                    if (currency.equals(UserSettings.Currency.EUR)) {
                        rates = filterExchangeRatesByCurrency(rates, currency);
                    }
                }
            }
            if (selectedBank ==UserSettings. ChoiceBank.PrivatBank  && selectedCurrency == UserSettings.Currency.USD) {
                Set<UserSettings.Currency> currenciesForPB = new HashSet<>();
                currenciesForPB.add(UserSettings.Currency.USD);
                currenciesForPB.add(UserSettings.Currency.EUR);
                for (UserSettings.Currency currency : currenciesForPB) {
                    if (currency.equals(UserSettings.Currency.USD)) {
                        rates = filterExchangeRatesByCurrency(rates, currency);
                    }
                }
            }

            if (selectedBank == UserSettings.ChoiceBank.PrivatBank && selectedCurrency == UserSettings.Currency.EUR) {
                Set<UserSettings.Currency> currenciesForPB = new HashSet<>();
                currenciesForPB.add(UserSettings.Currency.USD);
                currenciesForPB.add(UserSettings.Currency.EUR);
                for (UserSettings.Currency currency : currenciesForPB) {
                    if (currency.equals(UserSettings.Currency.EUR)) {
                        rates = filterExchangeRatesByCurrency(rates, currency);
                    }
                }
            }
            // Якщо умова не виконується, виводимо всі курси без фільтрації
            String limitedRates = limitExchangeRates(rates);
            String bankName = (selectedBank == UserSettings.ChoiceBank.PrivatBank) ? "PrivatBank" : "NBU";

        result =  "Exchange rates data:Bank: " + bankName + limitedRates;
        return result;
    }

    private String filterExchangeRatesByCurrency(String exchangeRates, UserSettings.Currency currency) {
        StringBuilder filteredRates = new StringBuilder();
        String[] lines = exchangeRates.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            String currencyString = "Currency: " + currency.toString();
            if (line.startsWith(currencyString)) {
                filteredRates.append(line).append("\n");
                if (i + 1 < lines.length) {
                    filteredRates.append(lines[++i]).append("\n");
                }
                if (i + 1 < lines.length) {
                    filteredRates.append(lines[++i]).append("\n\n");
                }
            }
        }
        return filteredRates.toString();
    }

    private String limitExchangeRates(String exchangeRates) {
        String[] lines = exchangeRates.split("\n");
        StringBuilder limitedRates = new StringBuilder();
        for (int i = 0; i < Math.min(lines.length, 10); i++) {
            limitedRates.append(lines[i]).append("\n");
        }
        return limitedRates.toString();
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
        row1.add("NBU");
        row1.add("PrivatBank");
        row1.add("Bank.Monobank");
        row2.add("Settings Menu");
        row2.add("Get Info");
        keyboard.add(row1);
        keyboard.add(row2);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    // Метод для створення клавіатури з відміченими налаштуваннями
    public ReplyKeyboardMarkup createSettingsMenuKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        KeyboardRow row4 = new KeyboardRow();
        row1.add("Number of decimal places");
        row2.add("Bank");
        row2.add("Currency");
        row3.add("Notification time");
        row4.add("Settings Menu");
        row4.add("Get Info");
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
        row2.add("Settings Menu");
        row2.add("Get Info");
        keyboard.add(row1);
        keyboard.add(row2);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }
}


