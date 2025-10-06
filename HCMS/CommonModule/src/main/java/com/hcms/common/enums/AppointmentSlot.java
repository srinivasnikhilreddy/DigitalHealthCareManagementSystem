package com.hcms.common.enums;

public enum AppointmentSlot
{
    SLOT_9_10("9:00 AM - 10:00 AM"),
    SLOT_10_11("10:00 AM - 11:00 AM"),
    SLOT_11_12("11:00 AM - 12:00 PM"),
    SLOT_4_5("4:00 PM - 5:00 PM"),
    SLOT_5_6("5:00 PM - 6:00 PM");

    private final String displayValue;

    AppointmentSlot(String displayValue)
    {
        this.displayValue = displayValue;
    }

    public String getDisplayValue()
    {
        return displayValue;
    }
}
