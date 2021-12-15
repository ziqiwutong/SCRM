package com.scrm.service.schedule;

import com.scrm.service.service.WeimobService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@EnableScheduling
@EnableAsync
public class ThreadScheduleTask {

    @Resource
    private WeimobService weimobService;

    @Async
    @Scheduled(cron = "0 30 2 * * ?")
    public void configureTasks() {
        for (int i = 0; i < 5; i++) {
            String result = weimobService.syncProduct();
            if (result == null) break;
            try {
                Thread.sleep(30 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < 5; i++) {
            String result = weimobService.syncOrder();
            if (result == null) break;
            try {
                Thread.sleep(30 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
