package com.example.IrrigationSystem.Service;

import com.example.IrrigationSystem.Entity.CropDetails;
import com.example.IrrigationSystem.Entity.PlotDetails;
import com.example.IrrigationSystem.Repository.CropRepository;
import com.example.IrrigationSystem.Repository.PlotRepository;
import com.example.IrrigationSystem.Request.AddPlotRequest;
import com.example.IrrigationSystem.Request.ConfigurePlotRequest;
import com.example.IrrigationSystem.Response.ConfigurePlotResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class PlotService {

    private Logger logger = LoggerFactory.getLogger(PlotService.class);

    private final PlotRepository plotRepository;

    private final CropRepository cropRepository;

    private final String validCropName;

    public PlotService(@Value("${valid.crop.name}") String validCropName, CropRepository cropRepository,
                       PlotRepository plotRepository) {
        this.plotRepository = plotRepository;
        this.validCropName = validCropName;
        this.cropRepository = cropRepository;
    }

    public String addPlot(AddPlotRequest addPlotRequest) throws Exception {

        logger.info("PlotService.addPlot Starts");

        Optional<PlotDetails> plot = plotRepository.findByPlotId(addPlotRequest.getPlotId());

        if (plot.isPresent()) {
            throw new Exception("Plot Id is already Present in the system please register a new plot");
        }

        PlotDetails plotDetails = new PlotDetails();
        plotDetails.setPlotId(addPlotRequest.getPlotId());
        plotDetails.setLengthOfPlot(addPlotRequest.getLength());
        plotDetails.setWidthOfPlot(addPlotRequest.getWidth());

        plotRepository.save(plotDetails);

        logger.info("PlotService.addPlot Ends");

        return "Successfully Added the Plot";
    }

    public ConfigurePlotResponse configure(int plotId, ConfigurePlotRequest configurePlotRequest) throws Exception {

        logger.info("PlotService.configure Starts {}", plotId);

        Optional<PlotDetails> plot = plotRepository.findByPlotId(plotId);
        if (!plot.isPresent()) {
            throw new Exception("Plot is not present. Enter valid plot id to configure.");
        } else if (!Arrays.asList(validCropName.split(",")).contains(configurePlotRequest.getCropName().toLowerCase())) {
            throw new Exception("Invalid Crop Name. Plot cannot be configured");
        }

        int areaOfPlotConfigured = plot.get().getLengthOfPlot() * plot.get().getWidthOfPlot();
        int waterRequiredInPlot = areaOfPlotConfigured * configurePlotRequest.getWaterNeededPerMeter();

        saveCropDetails(configurePlotRequest);
        updatePlotDetails(plot.get(), configurePlotRequest.getIntervalHours(), waterRequiredInPlot, configurePlotRequest.getCropName());

        ConfigurePlotResponse configurePlotResponse = new ConfigurePlotResponse();
        configurePlotResponse.setPlotId(plotId);
        configurePlotResponse.setArea(areaOfPlotConfigured);
        configurePlotResponse.setWaterRequired(waterRequiredInPlot);
        configurePlotResponse.setWaterInterval(configurePlotRequest.getIntervalHours());

        logger.info("PlotService.addPlot Ends");

        return configurePlotResponse;
    }

    public String editPlot(int plotId, int length, int width) throws Exception {
        logger.info("PlotService.editPlot Starts");
        Optional<PlotDetails> plot = plotRepository.findByPlotId(plotId);

        if (!plot.isPresent()) {
            throw new Exception("Invalid Plot Id is passed. Plot cannot be updated.");
        }

        PlotDetails plotDetails = plot.get();
        plotDetails.setLengthOfPlot(length);
        plotDetails.setWidthOfPlot(width);

        if (!plotDetails.getCropName().isEmpty()) {
            Optional<CropDetails> crop = cropRepository.findByCropName(plotDetails.getCropName());
            plotDetails.setWaterAmount(length * width * crop.get().getWaterPerMeter());
        }

        plotRepository.save(plotDetails);

        logger.info("PlotService.editPlot Ends");

        return "Plot is successfully updated";
    }

    public List<PlotDetails> list() {
        return plotRepository.findAll();
    }

    private void saveCropDetails(ConfigurePlotRequest configurePlotRequest) {

        Optional<CropDetails> crop = cropRepository.findByCropName(configurePlotRequest.getCropName().toLowerCase());
        CropDetails cropDetails;

        if (crop.isPresent()) {
            cropDetails = crop.get();
            logger.info("Crop Data is Updated in DB {}", cropDetails.getCropName());
        } else {
            cropDetails = new CropDetails();
            cropDetails.setCropName(configurePlotRequest.getCropName().toLowerCase());
            logger.info("Crop Data is Created in DB {}", cropDetails.getCropName());
        }

        cropDetails.setTimeInterval(configurePlotRequest.getIntervalHours());
        cropDetails.setWaterPerMeter(configurePlotRequest.getWaterNeededPerMeter());

        cropRepository.save(cropDetails);

    }

    private void updatePlotDetails(PlotDetails plot, int intervalHours, int waterRequiredInPlot, String cropName) {

        plot.setCropName(cropName.toLowerCase());
        plot.setWaterAmount(waterRequiredInPlot);
        plot.setTimeSlot(intervalHours);
        plot.setNextDueRun(LocalDateTime.now().plusHours(intervalHours));

        plotRepository.save(plot);

    }

}
