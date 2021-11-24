package de.htwsaar.verwaltung_ms.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RobotRepository extends JpaRepository<Robot, Long> {

    Optional<Robot> findRobotByMacAdr(String mac_adr);

    Optional<Robot> findRobotByIdAndHscAndMacAdr(Long id, String hsc, String mac_adr);

    Robot findRobotById(Long id);

    @Query(value = "SELECT r from Robot r WHERE r.isActive = TRUE")
    List<Robot> findActiveRobots();
}
