package com.example.IrrigationSystem.Controller;

import com.example.IrrigationSystem.Request.AddPlotRequest;
import com.example.IrrigationSystem.Request.ConfigurePlotRequest;
import com.example.IrrigationSystem.Service.PlotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/plot")
public class PlotController {

    private Logger logger = LoggerFactory.getLogger(PlotController.class);

    @Autowired
    PlotService plotService;

    @PostMapping("/add")
    public ResponseEntity<String> addPlot(@Valid @RequestBody AddPlotRequest addPlotRequest) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(plotService.addPlot(addPlotRequest));
        } catch (Exception e) {
            logger.error("Exception Caught in PlotController.addPlot()  " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/configure/{plotId}")
    public ResponseEntity configure(@PathVariable int plotId, @Valid @RequestBody ConfigurePlotRequest configurePlotRequest) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(plotService.configure(plotId, configurePlotRequest));
        } catch (Exception e) {
            logger.error("Exception Caught in PlotController.configure()  " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/edit/{plotId}/{length}/{width}")
    public ResponseEntity editPlot(@PathVariable int plotId, @PathVariable int length, @PathVariable int width) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(plotService.editPlot(plotId, length, width));
        } catch (Exception e) {
            logger.error("Exception Caught in PlotController.editPlot()  " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseEntity list() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(plotService.list());
        } catch (Exception e) {
            logger.error("Exception Caught in PlotController.list()  " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
