package com.example.financialtracker.cron;

import com.example.financialtracker.expensepending.PendingExpense;
import com.example.financialtracker.expensepending.PendingExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class CustomTaskSchedule {
    private final CronService cronService;
    private final PendingExpenseService pendingExpenseService;

//    @Bean
//    public TaskScheduler taskScheduler() {
//        return new ThreadPoolTaskScheduler();
//    }

    private final TaskScheduler taskScheduler;

    public void scheduleTasks() {
        System.out.println("Called task Scheduler !");
        List<Cron> crons = cronService.getAllCron();
        for (Cron cron : crons) {
            System.out.println(cron.toString());
            taskScheduler.schedule(() -> {
                PendingExpense pendingExpense = new PendingExpense(cron);
                pendingExpenseService.createPendingExpense(pendingExpense);
            }, new CustomCronTrigger(cron.getCronDate()));
        }
    }
}