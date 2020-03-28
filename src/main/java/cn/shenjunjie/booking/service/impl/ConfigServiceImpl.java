package cn.shenjunjie.booking.service.impl;

import cn.shenjunjie.booking.common.rest.RestBody;
import cn.shenjunjie.booking.entity.Book;
import cn.shenjunjie.booking.repo.BookRepo;
import cn.shenjunjie.booking.service.ConfigService;
import cn.shenjunjie.booking.utils.JsoupUtil;
import cn.shenjunjie.booking.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/3/27 8:44
 */
@Slf4j
@Service
public class ConfigServiceImpl implements ConfigService {

    @Resource
    private RedisUtil redisUtil;
    @Resource(name = "scheduledExecutor")
    private ScheduledExecutorService scheduledExecutorService;
    @Resource
    private BookRepo bookRepo;


    private static final String EMAIL_CONFIG = "booking:email";
    private static final String BOOK_CONFIG = "booking:book";
    private static final Integer OPEN = 1;
    private static final Integer CLOSE = 0;
    private static final Integer NUM = 3;

    @Override
    public RestBody switchEmailConfig() {
        Object config = redisUtil.get(EMAIL_CONFIG);
        if (config != null && config.equals(CLOSE)) {
            redisUtil.set(EMAIL_CONFIG, OPEN);
        } else {
            redisUtil.set(EMAIL_CONFIG, CLOSE);
        }
        return RestBody.succeed();
    }

    @Override
    public Integer getEmailConfig() {
        Object config = redisUtil.get(EMAIL_CONFIG);
        if (config == null) {
            redisUtil.set(EMAIL_CONFIG, CLOSE);
            return CLOSE;
        }
        return (Integer) config;
    }

    @Override
    public void updateBooks() {
        Long count = bookRepo.count();
        if (count > NUM) {
            for (int i = 1; i <= NUM; i++) {
                int finalI = i;
                scheduledExecutorService.schedule(
                        () -> updateTask((long) finalI, finalI == NUM ? (NUM - finalI + 1) : count / NUM)
                        , 10, TimeUnit.SECONDS);
            }
        }
    }

    public void updateTask(Long startId, Long range) {
        //分片执行更新操作
        updateTask(startId, range, false);
    }

    public void updateTask(Long startId, Long range, boolean check) {
        //分片执行更新操作
        if (check) {
            Object config = redisUtil.get(BOOK_CONFIG);
            if (config == null) {
                return;
            } else if (config.equals(CLOSE)) {
                return;
            }
        }
        try {
            for (Long i = startId; i < startId + range; i++) {
                doUpdate(i);
            }
        } catch (Exception e) {
            log.error("分片任务遇到异常, e:{}", e.getMessage());
        }
    }

    private void doUpdate(Long id) {
        Book book = bookRepo.selectById(id);
        if (book != null) {
            List<Book> webBooks = JsoupUtil.getBookListByIsbn(book.getIsbn());
            if (CollectionUtils.isNotEmpty(webBooks)) {
                for (Book webBook : webBooks) {
                    webBook.setId(book.getId());
                    webBook.setEdition(book.getEdition());
                    int i = bookRepo.updateByPrimaryKey(webBook);
                    if (i > 0) {
                        break;
                    }
                }
            }
        }
    }


    @Override
    public void scheduleUpdateBooks() {
        redisUtil.set(BOOK_CONFIG, OPEN);
        startSchedule();
    }

    private void startSchedule() {
        Long count = bookRepo.count();
        if (count > NUM) {
            for (int i = 1; i <= NUM; i++) {
                int finalI = i;
                scheduledExecutorService.scheduleAtFixedRate(
                        () -> updateTask((long) finalI, finalI == NUM ? (NUM - finalI + 1) : count / NUM, true),
                        0, 30, TimeUnit.DAYS);
            }
        }
    }

    @Override
    public void stopScheduled() {
        redisUtil.set(BOOK_CONFIG, CLOSE);
    }

    @Override
    public Integer getBookScheduleStatus() {
        Object config = redisUtil.get(BOOK_CONFIG);
        if (config == null) {
            redisUtil.set(BOOK_CONFIG, CLOSE);
            return CLOSE;
        }
        return (Integer) config;
    }

}
