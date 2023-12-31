package com.example.financialtracker.cron;
import org.springframework.scheduling.support.CronTrigger;

public class CustomCronTrigger extends CronTrigger {
    public CustomCronTrigger(String expression) {
        super(expression);
    }
}
