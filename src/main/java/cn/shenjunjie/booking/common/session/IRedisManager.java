package cn.shenjunjie.booking.common.session;

import java.util.Set;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/3/9 21:59
 */
public interface IRedisManager {

    /**
     * get value from redis
     * @param key key
     * @return value
     */
    byte[] get(byte[] key);

    /**
     * set value
     * @param key  key
     * @param value value
     * @param expire expire
     * @return value
     */
    byte[] set(byte[] key, byte[] value, int expire);

    /**
     * del
     * @param key key
     */
    void del(byte[] key);

    /**
     * dbsize
     * @param pattern pattern
     * @return key-value size
     */
    Long dbSize(byte[] pattern);

    /**
     * keys
     * @param pattern key pattern
     * @return key set
     */
    Set<byte[]> keys(byte[] pattern);
}
