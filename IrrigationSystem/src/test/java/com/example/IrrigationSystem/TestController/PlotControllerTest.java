package com.example.IrrigationSystem.TestController;

import com.example.IrrigationSystem.Entity.PlotDetails;
import com.example.IrrigationSystem.Repository.PlotRepository;
import com.example.IrrigationSystem.Request.AddPlotRequest;
import com.example.IrrigationSystem.Request.ConfigurePlotRequest;
import com.example.IrrigationSystem.Response.ConfigurePlotResponse;
import com.example.IrrigationSystem.Service.PlotService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PlotControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PlotService plotService;

    @Mock
    private PlotRepository plotRepository;

    @Test
    void make_add_call() throws Exception {

        AddPlotRequest addPlotRequest = new AddPlotRequest();
        addPlotRequest.setPlotId(11);
        addPlotRequest.setLength(40);
        addPlotRequest.setWidth(90);
        when(plotService.addPlot(any())).thenReturn("SUCCESS");

        this.mockMvc.perform(post("/plot/add")
                        .contentType(MediaType.APPLICATION_JSON).
                        content(new ObjectMapper().writeValueAsString(addPlotRequest))).
                andDo(print()).andExpect(status().isOk());


        this.mockMvc.perform(post("/plot/add")
                        .contentType(MediaType.APPLICATION_JSON).
                        content(new ObjectMapper().writeValueAsString(addPlotRequest))).
                andDo(print()).andExpect(status().isOk());

    }

    @Test
    void make_configure_call() throws Exception {

        ConfigurePlotRequest configurePlotRequest = new ConfigurePlotRequest();
        configurePlotRequest.setIntervalHours(4);
        configurePlotRequest.setWaterNeededPerMeter(2);
        configurePlotRequest.setCropName("Tea");

        PlotDetails plotDetails = new PlotDetails();
        plotDetails.setPlotId(22);
        plotDetails.setLengthOfPlot(11);
        plotDetails.setWidthOfPlot(33);

        when(plotService.configure(3, configurePlotRequest)).thenReturn(new ConfigurePlotResponse());
        when(plotRepository.findByPlotId(any(Integer.class))).thenReturn(Optional.of(plotDetails));

        this.mockMvc.perform(post("/plot/configure/1")
                        .contentType(MediaType.APPLICATION_JSON).
                        content(new ObjectMapper().writeValueAsString(configurePlotRequest))).
                andDo(print()).andExpect(status().isOk());

    }

    @Test
    void make_edit_call() throws Exception {

        PlotDetails plotDetails = new PlotDetails();
        plotDetails.setPlotId(22);
        plotDetails.setLengthOfPlot(11);
        plotDetails.setWidthOfPlot(33);

        when(plotRepository.findByPlotId(any(Integer.class))).thenReturn(Optional.of(plotDetails));

        this.mockMvc.perform(put("/plot/edit/10/40/50")
                        .contentType(MediaType.APPLICATION_JSON)).
                andDo(print()).andExpect(status().isOk());

    }

    @Test
    void make_list_call() throws Exception {

        this.mockMvc.perform(get("/plot/list")
                        .contentType(MediaType.APPLICATION_JSON)).
                andDo(print()).andExpect(status().isOk());

    }

}
