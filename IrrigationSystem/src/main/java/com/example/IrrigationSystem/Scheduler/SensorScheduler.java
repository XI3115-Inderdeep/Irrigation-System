package com.example.IrrigationSystem.Scheduler;


import com.example.IrrigationSystem.Entity.CropDetails;
import com.example.IrrigationSystem.Entity.PlotDetails;
import com.example.IrrigationSystem.Exception.RetryOnException;
import com.example.IrrigationSystem.Repository.CropRepository;
import com.example.IrrigationSystem.Repository.PlotRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class SensorScheduler {

    private Logger logger = LoggerFactory.getLogger(SensorScheduler.class);

    @Autowired
    PlotRepository plotRepository;

    @Autowired
    CropRepository cropRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    //@Scheduled(cron = "0 8 3 * * *")
    @Scheduled(cron = "0 0/10 * * * *")
    public void runSensorScheduler() throws Exception {

        logger.info("runSensorScheduler Starts");

        List<PlotDetails> plotDetails = plotRepository.findByNextDueRunNotNull();
        for(PlotDetails plotDetail : plotDetails){
            LocalDateTime nextDueRun = plotDetail.getNextDueRun();
            if(LocalDateTime.now().isAfter(nextDueRun) && "201 CREATED".equals(hitSensorForIrrigation(plotDetail))){
                logger.info("Updating next RUN for Plot {}", plotDetail.getPlotId());
                Optional<CropDetails> cropDetails = cropRepository.findByCropName(plotDetail.getCropName());
                plotDetail.setNextDueRun(LocalDateTime.now().plusHours(cropDetails.get().getTimeInterval()));
                plotRepository.save(plotDetail);
            }
        }

        logger.info("runSensorScheduler Ends");

    }

    private String hitSensorForIrrigation(PlotDetails plotDetail) throws Exception {
        logger.info("Plot Needs to be Irrigated is {}", plotDetail.getPlotId());
        String sensorURL = "http://localhost:8866/sensor";

        RetryOnException retryHandler = new RetryOnException(3, 10000);
        String sensorRequest = null;
        ResponseEntity<String> sensorResponse;
        while(true) {
            try {
                sensorRequest = objectMapper.writeValueAsString("sensorRequest");
                sensorResponse = restTemplate.postForEntity(sensorURL, sensorRequest, String.class);
                break;
            } catch (JsonProcessingException je) {
                retryHandler.exceptionOccurred();
                logger.error("JsonProcessingException occurred in hitSensorForIrrigation:", je);
                continue;
            } catch (Exception ex) {
                retryHandler.exceptionOccurred();
                logger.error("Exception occurred in hitSensorForIrrigation:", ex);
                continue;
            }
        }
        return sensorResponse.getStatusCode().toString();
    }

}
