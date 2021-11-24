package de.htwsaar.steuer_info_service.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RobotInfoRepository extends JpaRepository<Robot_Info, Long> {

     Robot_Info getRobot_InfoByRobot(Robot r);
}
