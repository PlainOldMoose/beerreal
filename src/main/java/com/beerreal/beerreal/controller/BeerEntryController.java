package com.beerreal.beerreal.controller;

import com.beerreal.beerreal.dto.BeerEntryRequest;
import com.beerreal.beerreal.dto.BeerEntryResponse;
import com.beerreal.beerreal.dto.LeaderboardEntry;
import com.beerreal.beerreal.model.BeerEntry;
import com.beerreal.beerreal.repository.BeerEntryRepository;
import com.beerreal.beerreal.service.BeerEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/entries")
public class BeerEntryController {

    @Autowired
    private BeerEntryService beerEntryService;

    @PostMapping
    public ResponseEntity<BeerEntryResponse> createEntry(
            Authentication authentication,
            @RequestParam("photo") MultipartFile photo,
            @RequestParam(value = "beerName", required = false) String beerName,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "notes", required = false) String notes) {

        BeerEntryRequest request = new BeerEntryRequest();
        request.setBeerName(beerName);
        request.setLocation(location);
        request.setNotes(notes);

        BeerEntryResponse resposne = beerEntryService.createBeerEntry(
                authentication.getName(),
                photo,
                request
        );

        return ResponseEntity.ok(
                resposne);
    }

    @GetMapping("/my-entries")
    public ResponseEntity<List<BeerEntryResponse>> getMyEntries(Authentication authentication) {
        List<BeerEntryResponse> entries = beerEntryService.getUserEntries(authentication.getName());
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/all")
    public ResponseEntity<List<BeerEntryResponse>> getAllEntries(@RequestParam(required = false) Integer year) {
        List<BeerEntryResponse> entries = beerEntryService.getAllEntries(year);
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<LeaderboardEntry>> getLeaderboard(@RequestParam(required = false) Integer year) {
        List<LeaderboardEntry> leaderboard = beerEntryService.getLeaderboard(year);
        return ResponseEntity.ok(leaderboard);
    }

    @GetMapping("/my-count")
    public ResponseEntity<Long> getMyCount(Authentication authentication, @RequestParam(required = false) Integer year) {
        long count = beerEntryService.getUserEntryCount(authentication.getName(), year);
        return ResponseEntity.ok(count);
    }
}
