package de.htwsaar.verwaltung_ms.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RobotRepository extends JpaRepository<Robot, Long> {

    Robot findRobotByMacAdr(String mac_adr);

    Robot findRobotByIdAndHscAndMacAdr(Long id, String hsc, String mac_adr);
}
