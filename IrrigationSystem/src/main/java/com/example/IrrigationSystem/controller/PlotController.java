package com.example.IrrigationSystem.controller;

import com.example.IrrigationSystem.request.AddPlotRequest;
import com.example.IrrigationSystem.request.ConfigurePlotRequest;
import com.example.IrrigationSystem.service.PlotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

public class PlotController {

    private Logger logger = LoggerFactory.getLogger(PlotController.class);

    @Autowired
    PlotService plotService;

    public ResponseEntity<String> addPlot(@Valid @RequestBody AddPlotRequest addPlotRequest) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(plotService.addPlot(addPlotRequest));
        } catch (Exception e) {
            logger.error("Exception Caught in PlotController.addPlot()  " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    public ResponseEntity configure(@PathVariable int plotId, @Valid @RequestBody ConfigurePlotRequest configurePlotRequest) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(plotService.configure(plotId, configurePlotRequest));
        } catch (Exception e) {
            logger.error("Exception Caught in PlotController.configure()  " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
