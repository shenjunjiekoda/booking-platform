package cn.shenjunjie.booking.common.session.serializer;


import cn.shenjunjie.booking.common.exception.SerializationException;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/3/9 21:54
 */
public interface RedisSerializer<T> {

    byte[] serialize(T t) throws SerializationException;

    T deserialize(byte[] bytes) throws SerializationException;
}
