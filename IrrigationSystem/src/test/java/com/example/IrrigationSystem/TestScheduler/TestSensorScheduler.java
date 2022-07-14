package com.example.IrrigationSystem.TestScheduler;

import com.example.IrrigationSystem.Entity.CropDetails;
import com.example.IrrigationSystem.Entity.PlotDetails;
import com.example.IrrigationSystem.Repository.CropRepository;
import com.example.IrrigationSystem.Repository.PlotRepository;
import com.example.IrrigationSystem.Scheduler.SensorScheduler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestSensorScheduler {

    @InjectMocks
    private SensorScheduler sensorScheduler;

    @Mock
    private PlotRepository plotRepository;

    @Mock
    private CropRepository cropRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private RestTemplate restTemplate;


    @Test
    void invoke_sensor_scheduler() throws Exception {

        List<PlotDetails> plotDetailsList = new ArrayList<PlotDetails>();
        PlotDetails plotDetails = new PlotDetails();
        plotDetails.setPlotId(32);
        plotDetails.setLengthOfPlot(70);
        plotDetails.setWidthOfPlot(90);
        plotDetails.setCropName("rice");
        plotDetails.setWaterAmount(3312);
        plotDetails.setNextDueRun(LocalDateTime.now().minusHours(2));
        plotDetailsList.add(plotDetails);

        CropDetails cropDetails = new CropDetails();
        cropDetails.setCropName("tea");
        cropDetails.setTimeInterval(5);
        cropDetails.setWaterPerMeter(2);

        when(plotRepository.findByNextDueRunNotNull()).thenReturn(plotDetailsList);
        when(cropRepository.findByCropName(anyString())).thenReturn(Optional.of(cropDetails));
        ResponseEntity<String> r = ResponseEntity.created(null).build();
        when(restTemplate.postForEntity("http://localhost:8866/sensor", null, String.class)).thenReturn(r);
        sensorScheduler.runSensorScheduler();

    }

}
