package org.banking.transaction.common.constants;

public enum TransactionType {
    DEPOSIT("存款"),
    WITHDRAW("取款"),
    TRANSFER("转账"),
    REFUND("退款");
    private String displayName;
    TransactionType(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
