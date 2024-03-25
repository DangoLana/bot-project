package org.example.notify;

import org.example.bot.CurrencyTelegramBot;
import org.example.bot.settingsFORkeyboard.SettingsForKeyboard;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import java.util.Date;
import static org.quartz.CronScheduleBuilder.dailyAtHourAndMinute;
import static org.quartz.TriggerBuilder.newTrigger;

public class SendMessageOnTime {
    public Scheduler scheduler;

    JobDetail job;

    public  void sendMessageByTime(int hour, Long chatId) throws SchedulerException {
        CurrencyTelegramBot bot = new CurrencyTelegramBot();
        SettingsForKeyboard settingsForKeyboard = new SettingsForKeyboard(bot);


        scheduler = StdSchedulerFactory.getDefaultScheduler();
        job = JobBuilder.newJob(Message.class)
                .withIdentity(String.valueOf(chatId), "daily-jobs")
                .usingJobData("chatId", chatId)
                .usingJobData("rate", settingsForKeyboard.sendExchangeRatesWithStr(String.valueOf(chatId)))
                .build();

        Trigger trigger = newTrigger()
                .withIdentity("myTrigger", "myTriggerGroup")
                .withSchedule(dailyAtHourAndMinute(hour, 00))
                .build();
        Date date = scheduler.scheduleJob(job, trigger);
        scheduler.start();
        System.out.println(date);
    }

    public  void deleteJob() throws SchedulerException {
        scheduler.deleteJob(job.getKey());
        scheduler.shutdown(true);
    }
}
