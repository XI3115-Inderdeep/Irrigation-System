package com.example.IrrigationSystem.TestService;

import com.example.IrrigationSystem.Entity.CropDetails;
import com.example.IrrigationSystem.Entity.PlotDetails;
import com.example.IrrigationSystem.Repository.CropRepository;
import com.example.IrrigationSystem.Repository.PlotRepository;
import com.example.IrrigationSystem.Request.AddPlotRequest;
import com.example.IrrigationSystem.Request.ConfigurePlotRequest;
import com.example.IrrigationSystem.Response.ConfigurePlotResponse;
import com.example.IrrigationSystem.Service.PlotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlotServiceTest {

    @InjectMocks
    private PlotService plotService;

    @Mock
    private PlotRepository plotRepository;

    @Mock
    private CropRepository cropRepository;

    @BeforeEach
    public void setUp() {
        plotService = new PlotService("wheat,rice,sugarcane,coffee,tea,pulses,spices"
                , cropRepository, plotRepository);
    }


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
    }

    @Test
    void configure_plot_test() throws Exception {

        PlotDetails plotDetails = new PlotDetails();
        plotDetails.setPlotId(22);
        plotDetails.setLengthOfPlot(11);
        plotDetails.setWidthOfPlot(33);

        when(plotRepository.save(any(PlotDetails.class))).thenReturn(plotDetails);
        when(plotRepository.findByPlotId(any(Integer.class))).thenReturn(Optional.of(plotDetails));

        ConfigurePlotRequest configurePlotRequest = new ConfigurePlotRequest();
        configurePlotRequest.setCropName("RICE");
        configurePlotRequest.setWaterNeededPerMeter(3);
        configurePlotRequest.setIntervalHours(2);

        ConfigurePlotResponse response = plotService.configure(plotDetails.getPlotId(), configurePlotRequest);
        assertThat(response).isNotNull();
    }

    @Test
    void edit_plot_test() throws Exception {

        PlotDetails plotDetails = new PlotDetails();
        plotDetails.setCropName("pulses");

        CropDetails cropDetails = new CropDetails();
        cropDetails.setCropName("tea");
        cropDetails.setTimeInterval(5);
        cropDetails.setWaterPerMeter(2);

        when(plotRepository.findByPlotId(any(Integer.class))).thenReturn(Optional.of(plotDetails));
        when(cropRepository.findByCropName(anyString())).thenReturn(Optional.of(cropDetails));

        String response = plotService.editPlot(22, 20, 30);
        assertThat(response).isNotNull();
    }

    @Test
    void list_plot_test() throws Exception {

        List<PlotDetails> response = plotService.list();
        assertThat(response).isNotNull();
    }

}
