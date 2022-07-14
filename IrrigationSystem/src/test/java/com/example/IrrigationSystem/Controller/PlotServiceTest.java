package com.example.IrrigationSystem.Controller;

import com.example.IrrigationSystem.Entity.PlotDetails;
import com.example.IrrigationSystem.Repository.CropRepository;
import com.example.IrrigationSystem.Repository.PlotRepository;
import com.example.IrrigationSystem.Request.AddPlotRequest;
import com.example.IrrigationSystem.Request.ConfigurePlotRequest;
import com.example.IrrigationSystem.Response.ConfigurePlotResponse;
import com.example.IrrigationSystem.Service.PlotService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlotServiceTest {

    private Logger logger = LoggerFactory.getLogger(PlotServiceTest.class);

    @InjectMocks
    PlotService plotService;

    @Mock
    PlotRepository plotRepository;

    @Mock
    CropRepository cropRepository;

    @Test
    void add_plot_test() throws Exception {

        AddPlotRequest addPlotRequest = new AddPlotRequest();
        addPlotRequest.setPlotId(123);
        addPlotRequest.setLength(55);
        addPlotRequest.setWidth(231);

        PlotDetails plotDetails = new PlotDetails();
        plotDetails.setPlotId(addPlotRequest.getPlotId());
        plotDetails.setLengthOfPlot(addPlotRequest.getLength());
        plotDetails.setWidthOfPlot(addPlotRequest.getWidth());

        when(plotRepository.save(any())).thenReturn(plotDetails);

        String response = plotService.addPlot(addPlotRequest);
        assertThat(response).isNotNull();
        assertThat(plotDetails).isNotNull();
        logger.info(response);
        logger.info("Crop Data is Updated in DB {}, {} and {}",plotDetails.getPlotId(),plotDetails.getLengthOfPlot(),plotDetails.getWidthOfPlot());
    }

    @Test
    void configure_plot_test() throws Exception {

        PlotDetails plotDetails = new PlotDetails();
        plotDetails.setPlotId(22);
        plotDetails.setLengthOfPlot(11);
        plotDetails.setWidthOfPlot(33);

        when(plotRepository.save(any(PlotDetails.class))).thenReturn(plotDetails);
        plotRepository.save(plotDetails);

        ConfigurePlotRequest configurePlotRequest = new ConfigurePlotRequest();
        configurePlotRequest.setCropName("ROCE");
        configurePlotRequest.setWaterNeededPerMeter(3);
        configurePlotRequest.setIntervalHours(2);

        ConfigurePlotResponse response = plotService.configure(plotDetails.getPlotId(),configurePlotRequest);
        assertThat(response).isNotNull();
    }

}
