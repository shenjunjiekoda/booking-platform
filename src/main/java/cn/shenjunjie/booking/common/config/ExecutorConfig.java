package cn.shenjunjie.booking.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author JunjieShen
 */
@EnableAsync
@Configuration
public class ExecutorConfig {

    private int corePoolSize = 50;

    private int maxPoolSize = 200;

    private int queueCapacity = 500;

    private int keepAlive = 120;

//    public AsyncTaskExecutor taskExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(corePoolSize);
//        executor.setMaxPoolSize(maxPoolSize);
//        executor.setQueueCapacity(queueCapacity);
//        executor.setThreadNamePrefix("deployment-taskExecutor-");
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
//        executor.setKeepAliveSeconds(keepAlive);
//        executor.initialize();
//        return executor;
//    }

    @Bean("scheduledExecutor")
    public ScheduledExecutorService scheduleTaskExecutor() {
        return Executors.newScheduledThreadPool(100);
    }
}
