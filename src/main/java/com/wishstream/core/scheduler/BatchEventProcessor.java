package com.wishstream.core.scheduler;

import com.wishstream.core.dto.ComprehensiveEventDTO;
import com.wishstream.core.service.GPTService;
import com.wishstream.core.service.UserRelationServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class BatchEventProcessor {



    @Autowired
    private GPTService gptService;


    @Autowired
    private UserRelationServiceImpl userRelationService;

    private Logger log = LoggerFactory.getLogger(BatchEventProcessor.class);

    @Scheduled(cron = "0 0 0 * * ?")
    public void processUpcomingEvents() {

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        String sourceTable = "events_master_" + tomorrow.format(DateTimeFormatter.ofPattern("MM_dd"));

        String destTable = "events_" + tomorrow.format(DateTimeFormatter.ofPattern("yyyy_MM_dd"));

        // Fetch stored events
        int rowsAffected = userRelationService.cloneTable(sourceTable, destTable);


        if (rowsAffected > 0) {
            log.info("Cloned {} rows from {} to {}", rowsAffected, sourceTable, destTable);
        } else {
            log.info("No events found for tomorrow, {}", tomorrow);
        }
    }

        @Scheduled(cron = "0 0 2 * * *", zone = "UTC")
        public void processEventsWithGPT() {
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            String tableName = "events_" + tomorrow.format(DateTimeFormatter.ofPattern("yyyy_MM_dd"));

            // Fetch stored events
            List<ComprehensiveEventDTO> events = userRelationService.fetchComprehensiveEvents(tableName);


            // Process each event with GPT
            for (ComprehensiveEventDTO event : events) {
                ComprehensiveEventDTO updatedDTO = gptService.generateEmailDetails(event);
                userRelationService.updateComprehensiveEvent(updatedDTO,tableName);
            }

            log.info("Processed {} events for {}", events.size(), tomorrow);
        }

    }

