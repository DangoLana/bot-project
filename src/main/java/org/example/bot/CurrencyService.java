package org.example.bot;

import org.example.model.UserSettings;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class CurrencyService {

    private final CurrencyTelegramBot currencyTelegramBot;
    private UserSettings userSettings;
    private final Set<UserSettings.Currency> selectedCurrencies;

    public CurrencyService(CurrencyTelegramBot currencyTelegramBot) {
        this.currencyTelegramBot = currencyTelegramBot;
        this.selectedCurrencies = new HashSet<>();
    }

    public void addCurrency(UserSettings.Currency currency) {
        selectedCurrencies.add(currency);
    }

    public void removeCurrency(UserSettings.Currency currency) {
        selectedCurrencies.remove(currency);
    }

    public String chooseCurrenciesForSettingsPB(StringBuilder stringBuilder, String s) {
        for (UserSettings.Currency currency : selectedCurrencies) {
            if (s.contains(currency.toString())) {
                stringBuilder.append(s).append("\n");
                break;
            }
        }
        return stringBuilder.toString();
    }

    public String chooseCurrenciesForSettingsMono(StringBuilder sbRates, String s) {
        for (UserSettings.Currency currency : selectedCurrencies) {
            if (s.contains(currency.toString())) {
                sbRates.append(s).append("\n");
                break;
            }
        }
        return sbRates.toString();
    }

    public String chooseCurrenciesForSettings(StringBuilder sb, JSONObject obj, String currencyCode) {
        for (UserSettings.Currency currency : selectedCurrencies) {
            if (currency.toString().equals(currencyCode)) {
                float rate = obj.getFloat("rate");
                String formattedString = String.format("%s: %f\n", currencyCode, rate);
                sb.append(formattedString);
                break;
            }
        }
        return sb.toString();
    }

    public Set<UserSettings.Currency> getHashSetCurrencies() {
        return selectedCurrencies;
    }

}
