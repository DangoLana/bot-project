package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class UserSettings {
    private int decimalPlaces;
    private List<String> currencies;
    private List<String> banks;
    private int notificationTime;

    public void UserSetting() {
        // Ініціалізація полів за замовчуванням, якщо юзер ше не вибрав
        decimalPlaces = 2;
        currencies = new ArrayList<>();
        banks = new ArrayList<>();
        notificationTime = 9;
    }

    public int getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(int decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public List<String> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<String> currencies) {
        this.currencies = currencies;
    }

    public List<String> getBanks() {
        return banks;
    }

    public void setBanks(List<String> banks) {
        this.banks = banks;
    }

    public int getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(int notificationTime) {
        this.notificationTime = notificationTime;
    }
}