package com.example.IrrigationSystem.Response;

import lombok.Data;

@Data
public class ConfigurePlotResponse {

    private int plotId;
    private int area;
    private int waterRequired;
    private int waterInterval;

}
