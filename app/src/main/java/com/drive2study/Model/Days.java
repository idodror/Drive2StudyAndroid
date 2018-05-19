package com.drive2study.Model;

public enum Days {
    SUNDAY(1),
    MONDAY(2),
    TUESDAY(3),
    WEDNESDAY(4),
    THURSDAY(5),
    FRIDAY(6),
    SATURDAY(7);

    private final int value;

    Days(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}