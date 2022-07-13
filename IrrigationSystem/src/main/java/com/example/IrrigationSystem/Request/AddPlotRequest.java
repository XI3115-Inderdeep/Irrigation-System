package com.example.IrrigationSystem.Request;

import lombok.Data;

@Data
public class AddPlotRequest {
    private int plotId;
    private int length;
    private int width;
}
