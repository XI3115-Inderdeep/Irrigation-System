package com.example.IrrigationSystem.Repository;

import com.example.IrrigationSystem.Entity.PlotDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlotRepository extends JpaRepository<PlotDetails, Long> {

    Optional<PlotDetails> findByPlotId(int id);
    List<PlotDetails> findByNextDueRunNotNull();
}
