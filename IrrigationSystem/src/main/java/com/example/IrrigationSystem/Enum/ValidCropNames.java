package com.example.IrrigationSystem.Enum;

public enum ValidCropNames {
    RICE("RICE"),
    WHEAT("WHEAT"),
    SUGARCANE("SUGARCANE"),
    TEA("TEA"),
    COFFEE("COFFEE"),
    PULSES("PULSES");


    private final String value;

    ValidCropNames(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
