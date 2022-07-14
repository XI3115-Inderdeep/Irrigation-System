package com.example.IrrigationSystem.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "crop_details")
public class CropDetails {
    @Id
    @Column(name = "crop_name")
    private String cropName;
    @Column(name = "time_interval")
    private int timeInterval;
    @Column(name = "water_per_meter")
    private int waterPerMeter;
}
