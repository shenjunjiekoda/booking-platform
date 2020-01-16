package cn.shenjunjie.booking.common.exception;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/16 11:20
 */
public class JsoupException extends BaseException {
    public JsoupException(String message){
        super(message);
    }

    public JsoupException(Long errorCode){
        this("JsoupException exception");
        setErrorCode(errorCode);
    }

    public JsoupException(Long errorCode, Object[] args){
        super(errorCode,args);
    }

}
