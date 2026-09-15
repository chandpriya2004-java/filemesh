package com.filemesh.user.appContent;

public enum APPSTATUSCODE {
    SUCCESS(1),FAIL(-1),ERROR(-2), SOFT_DELETED(2),NOT_DELETED(4),HARD_DELETE(3),PAYMENT_INITIATED_BY_OTHER(4),PAYMENT_UNDER_PROCESS(5),
    ALREADY_EXIST(6), REQUEST_ACCEPTED(7);

    public final int label;

    private APPSTATUSCODE(int label) {
        this.label = label;
    }
}
