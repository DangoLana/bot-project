package org.example.bot.settingsFORkeyboard;

import org.example.bot.CurrencyTelegramBot;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.URI;

public class Message implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        CurrencyTelegramBot currencyTelegramBot = new CurrencyTelegramBot();
        org.telegram.telegrambots.meta.api.methods.send.SendMessage sendMessage = new SendMessage();
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        Long chatId = jobDataMap.getLong("chatId");

        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.enableMarkdown(true);
        String s = null;
        /*try {
            s = CurrencyUtil.sendGetByCurrencyPrivat(URI.create(PRIVAT_URI));// get currency rate, send text in bot
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        sendMessage.setText(s);

         */

        try {
            currencyTelegramBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Hal");

    }
}
