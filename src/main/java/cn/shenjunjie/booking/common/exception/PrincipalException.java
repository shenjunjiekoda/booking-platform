package cn.shenjunjie.booking.common.exception;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/13 11:14
 */
public class PrincipalException extends BaseException {
    public PrincipalException(String message){
        super(message);
    }

    public PrincipalException(Long errorCode){
        this("PrincipalException exception");
        setErrorCode(errorCode);
    }

    public PrincipalException(Long errorCode, Object[] args){
        super(errorCode,args);
    }
}
