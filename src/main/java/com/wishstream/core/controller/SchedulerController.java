package com.wishstream.core.controller;


import com.wishstream.core.scheduler.BatchEventProcessor;
import com.wishstream.core.dto.ComprehensiveEventDTO;
import com.wishstream.core.service.UserRelationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/scheduler")
public class SchedulerController {

    @Autowired
    private BatchEventProcessor batchEventProcessor;

    @Autowired
    private UserRelationServiceImpl userRelationService;

    @PostMapping("/process-upcoming-events")
    public ResponseEntity<String> triggerUpcomingEventsProcessing() {
        CompletableFuture.runAsync(() -> batchEventProcessor.processUpcomingEvents());
        return ResponseEntity.ok("Processing of upcoming events has been triggered.");
    }

    @PostMapping("/process-events-with-gpt")
    public ResponseEntity<String> triggerGPTEventProcessing() {
        CompletableFuture.runAsync(() -> batchEventProcessor.processEventsWithGPT());
        return ResponseEntity.ok("Processing of events with GPT has been triggered.");
    }

    @GetMapping("/events/{date}")
    public ResponseEntity<List<ComprehensiveEventDTO>> getEventsByDate(@PathVariable String date) {
        LocalDate requestedDate = LocalDate.parse(date);
        String tableName = "events_" + requestedDate.format(DateTimeFormatter.ofPattern("yyyy_MM_dd"));
        List<ComprehensiveEventDTO> events = userRelationService.fetchComprehensiveEvents(tableName);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/events/count/{date}")
    public ResponseEntity<Integer> getEventCountByDate(@PathVariable String date) {
        LocalDate requestedDate = LocalDate.parse(date);
        String tableName = "events_" + requestedDate.format(DateTimeFormatter.ofPattern("yyyy_MM_dd"));
        List<ComprehensiveEventDTO> events = userRelationService.fetchComprehensiveEvents(tableName);
        return ResponseEntity.ok(events.size());
    }
}
