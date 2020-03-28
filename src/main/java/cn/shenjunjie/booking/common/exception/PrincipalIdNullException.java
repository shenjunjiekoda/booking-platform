package cn.shenjunjie.booking.common.exception;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/3/9 21:55
 */
public class PrincipalIdNullException extends BaseException {
    private static final String MESSAGE = "Principal Id shouldn't be null!";

    public PrincipalIdNullException(Class clazz, String idMethodName) {
        super(clazz + " id field: " +  idMethodName + ", value is null\n" + MESSAGE);
    }
}
