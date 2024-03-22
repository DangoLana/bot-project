package org.example.model;

import lombok.Data;

@Data
public class UserSettings  {
    private int decimalPlaces;
    private Currency currency;
    private ChoiceBank banks;
    private int notificationTime;


    // Ініціалізація полів за замовчуванням, якщо юзер ше не вибрав
    public UserSettings() {
        // Ініціалізуємо всі поля дефолтними значеннями
        decimalPlaces = 2;
        currency = Currency.USD;
        banks = ChoiceBank.NBU;
        notificationTime = 9;
    }


    public ChoiceBank getSelectedBank() {
        return banks;
    }


    public Currency getSelectedCurrency() {
        return currency;
    }

    public enum Currency {
        USD,
        EUR
    }

    public enum ChoiceBank {
        NBU,
        PrivatBank,
        MonoBank


    }
}