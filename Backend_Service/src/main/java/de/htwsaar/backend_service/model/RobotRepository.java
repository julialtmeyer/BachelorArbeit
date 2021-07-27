package de.htwsaar.backend_service.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RobotRepository extends JpaRepository<Robot, Long> {

    Robot findRobotByMacAdr(String mac_adr);

    Robot findRobotByIdAndHscAndMacAdr(Long id, String hsc, String mac_adr);

    @Query(value = "SELECT r from Robot r WHERE r.isActive = TRUE")
    List<Robot> findActiveRobots();

    @Query(value = "SELECT r from Robot r WHERE r.roboterName = :name")
    Robot findRobotByName(@Param("name")String name);
}
