package org.example.bot.settingsFORkeyboard;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.quartz.CronScheduleBuilder.dailyAtHourAndMinute;
import static org.quartz.TriggerBuilder.newTrigger;

public class SendMessageOnTime {

    public Scheduler scheduler;
    JobDetail job;

    public  void sendMessageByTime(int hour, Long chatId) throws SchedulerException {
        scheduler = StdSchedulerFactory.getDefaultScheduler();
        job = JobBuilder.newJob(Message.class)
                .withIdentity(String.valueOf(chatId), "daily-jobs")
                .usingJobData("chatId", chatId)
                .build();

        Trigger trigger = newTrigger()
                .withIdentity("myTrigger", "myTriggerGroup")
                .withSchedule(dailyAtHourAndMinute(hour, 04))
                .build();
        Date date = scheduler.scheduleJob(job, trigger);


        final Map<String, JobDetail> jobDetailsMap = new HashMap<>();
        jobDetailsMap.put(String.valueOf(chatId), job);
        scheduler.start();

        System.out.println(date);
    }

    public  void deleteJob() throws SchedulerException {
        scheduler.deleteJob(job.getKey());
        scheduler.shutdown(true);
    }
}
