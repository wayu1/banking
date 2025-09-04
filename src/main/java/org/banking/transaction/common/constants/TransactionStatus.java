package org.banking.transaction.common.constants;

public enum TransactionStatus {
    SUCCESS("成功"),
    FAILED("失败"),
    DELETED("已删除"),
    PENDING("处理中");
    private String displayName;
    TransactionStatus(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
