package com.example.IrrigationSystem.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "plot_details")
public class PlotDetails {
        @Id
        @Column(name="plot_id")
        private int plotId;
        @Column(name="length" )
        private int lengthOfPlot;
        @Column(name="width")
        private int widthOfPlot;
        @Column(name="crop_name")
        private String cropName;
        @Column(name="time_slot")
        private int timeSlot;
        @Column(name="water_amount")
        private int waterAmount;
        @Column(name="next_due_run")
        private LocalDateTime nextDueRun;
}
