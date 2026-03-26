package com.PJDM;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.mongodb.autoconfigure.MongoAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class})
@MapperScan("com.PJDM.mapper")
@EnableScheduling
@EnableAsync
public class PeacefulJourneyInMedicineApplication {
    public static void main(String[] args) {
        SpringApplication.run(PeacefulJourneyInMedicineApplication.class, args);
    }
}
