package de.htwsaar.backend_service.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RobotRepository extends JpaRepository<Robot, Long> {

    Robot findRobotByMacAdr(String mac_adr);

    Robot findRobotByIdAndHscAndMacAdr(Long id, String hsc, String mac_adr);

    Optional<Robot> findRobotById(Long id);

    @Query(value = "SELECT r from Robot r WHERE r.isActive = TRUE")
    List<Robot> findActiveRobots();
}
