package cn.shenjunjie.booking.enums;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/2/27 18:58
 */
public enum BookStatus {
    /**
     * 创建，还未提交
     */
    NEW("new"),

    /**
     * 已提交
     */
    SUBMITTED("submitted"),
    /**
     * 有货
     */
    IN_STOCK("instock"),

    /**
     * 无货
     */
    NOT_IN_STOCK("notinstock");

    private String status;

    public String getStatus(){
        return status;
    }

    BookStatus(String status){
        this.status = status;
    }
}
