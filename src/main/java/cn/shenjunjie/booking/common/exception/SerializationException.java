package cn.shenjunjie.booking.common.exception;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/3/9 21:55
 */
public class SerializationException extends BaseException {
    public SerializationException(String msg) {
        super(msg);
    }
    public SerializationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
