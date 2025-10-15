package seohee.dividend.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ThreadPoolTestScheduler {

    @Scheduled(fixedDelay = 1000)
    public void test1() throws InterruptedException {
        Thread.sleep(5000);
        System.out.println(Thread.currentThread().getName() + " 테스트 1 : " + LocalDateTime.now());
    }

    @Scheduled(fixedDelay = 1000)
    public void test2() {
        System.out.println(Thread.currentThread().getName() + " 테스트 2 : " + LocalDateTime.now());
    }

}
