package cn.shenjunjie.booking.enums;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/3/27 9:29
 */
public enum OperationType {
    /**
     * 添加
     */
    ADD("add"),
    /**
     *
     */
    UPDATE("update"),
    /**
     * 提交
     */
    SUBMIT("submit"),

    UNKNOWN("unknwon");

    private String type;

    public String getType(){
        return type;
    }

    OperationType(String type){
        this.type = type;
    }

}
