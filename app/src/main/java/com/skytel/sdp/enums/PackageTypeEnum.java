package com.skytel.sdp.enums;

import com.skytel.sdp.utils.Constants;

public enum PackageTypeEnum implements Constants{
    UNIT_PACKAGE(CONST_UNIT_PACKAGE),
    DATA_PACKAGE(CONST_DATA_PACKAGE),
    IP_CARD(CONST_IP_CARD),
    SMART_CARD(CONST_SMART_CARD);

    private final int value;

    PackageTypeEnum(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }
}
