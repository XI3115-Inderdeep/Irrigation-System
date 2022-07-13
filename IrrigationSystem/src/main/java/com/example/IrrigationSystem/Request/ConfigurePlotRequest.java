package com.example.IrrigationSystem.Request;

import lombok.Data;

@Data
public class ConfigurePlotRequest {

    private String cropName;
    private int intervalHours;
    private int waterNeededPerMeter;

}
