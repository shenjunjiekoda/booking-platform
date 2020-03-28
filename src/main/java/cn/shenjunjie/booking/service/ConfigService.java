package cn.shenjunjie.booking.service;

import cn.shenjunjie.booking.common.rest.RestBody;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/3/27 8:43
 */
public interface ConfigService {

    RestBody switchEmailConfig();

    Integer getEmailConfig();

    void updateBooks();

    void scheduleUpdateBooks();

    void stopScheduled();

    Integer getBookScheduleStatus();
}
