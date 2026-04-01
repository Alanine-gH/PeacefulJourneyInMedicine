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
        System.out.println("""
                 医路安心 Peaceful Journey In Medicine 启动成功
                 ____        _   ___   __  __
                |  _ \\      | | |_ _| |  \\/  |
                | |_) |  _  | |  | |  | |\\/| |
                |  __/  | |_| |  | |  | |  | |
                |_|      \\___/  |___| |_|  |_|
                
                """);
    }
}
