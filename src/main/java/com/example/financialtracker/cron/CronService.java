package com.example.financialtracker.cron;

import com.example.financialtracker.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CronService {

    private final CronRepository cronRepository;

    static final String SERVER_ERROR_MESSAGE = "Internal Server Error !";
    static final int SERVER_ERROR_CODE = 500;

    private final String[] WEEK_DAY = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};

    public List<Cron> getAllCron(){
        try {
            return cronRepository.findAll();
        } catch (Exception ex) {
            throw new CustomException(SERVER_ERROR_MESSAGE, SERVER_ERROR_CODE);
        }
    }

    public List<String> getAllCronDate(){
        try {
            return cronRepository.getAllCronDate();
        } catch (Exception ex) {
            throw new CustomException(SERVER_ERROR_MESSAGE, SERVER_ERROR_CODE);
        }
    }

    public void addCron(long userId, long expenseId, String cronFor) {
        try {
            String cronDate = "";
            LocalDate today = LocalDate.now();

            if(cronFor.equals("WEEK")){
                cronDate = "0 0 18 * * " + WEEK_DAY[today.getDayOfWeek().getValue() - 1];
            } else if (cronFor.equals("MONTH")) {
                cronDate = "0 0 18 L * ?";
            }else{
                cronDate ="0 0 18 * * ?";
            }

            Cron cron = new Cron(userId, expenseId, cronDate);
            cronRepository.save(cron);
        } catch (Exception ex) {
            throw new CustomException(SERVER_ERROR_MESSAGE, SERVER_ERROR_CODE);
        }
    }
}
