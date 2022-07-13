package com.example.IrrigationSystem.Repository;

import com.example.IrrigationSystem.Entity.CropDetails;
import com.example.IrrigationSystem.Entity.PlotDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CropRepository extends JpaRepository<CropDetails, Long> {

    Optional<CropDetails> findByCropName(String cropName);

}
