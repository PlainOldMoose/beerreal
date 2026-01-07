package com.beerreal.beerreal.service;

import com.beerreal.beerreal.dto.BeerEntryRequest;
import com.beerreal.beerreal.dto.BeerEntryResponse;
import com.beerreal.beerreal.dto.LeaderboardEntry;
import com.beerreal.beerreal.model.BeerEntry;
import com.beerreal.beerreal.model.User;
import com.beerreal.beerreal.repository.BeerEntryRepository;
import com.beerreal.beerreal.repository.UserRepository;
import com.beerreal.beerreal.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BeerEntryService {

    @Autowired
    private BeerEntryRepository beerEntryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileStorageService fileStorageService;

    public BeerEntryResponse createBeerEntry(String username, MultipartFile photo, BeerEntryRequest request) {
        // Get the user
        User user = userRepository.findByUsername(
                username).orElseThrow(
                () -> new RuntimeException(
                        "Username not found"));

        // Store the photo
        String photoFilename = fileStorageService.storeFile(
                photo);
        String photoUrl = "/api/files/" + photoFilename;

        // Create entry
        BeerEntry entry = new BeerEntry();
        entry.setUser(user);
        entry.setPhotoUrl(photoUrl);
        entry.setBeerName(
                request.getBeerName());
        entry.setLocation(
                request.getLocation());
        entry.setNotes(
                request.getNotes());

        BeerEntry beerEntry = beerEntryRepository.save(
                entry);

        return mapToResponse(beerEntry);
    }

    public List<BeerEntryResponse> getUserEntries(String username) {
        User user = userRepository.findByUsername(
                username).orElseThrow(
                () -> new RuntimeException(
                        "Username not found"));

        return beerEntryRepository.findByUserOrderByCreatedAtDesc(
                        user).stream()
                .map(this::mapToResponse)
                .collect(
                        Collectors.toList());
    }

    public List<BeerEntryResponse> getAllEntries(Integer year) {
        if (year == null) {
            year = LocalDate.now()
                    .getYear();
        }

        return beerEntryRepository.findByYearOrderByCreatedAtDesc(
                        year).stream()
                .map(this::mapToResponse)
                .collect(
                        Collectors.toList());
    }

    public List<LeaderboardEntry> getLeaderboard(Integer year) {
        if (year == null) {
            year = LocalDate.now()
                    .getYear();
        }

        List<Object[]> results = beerEntryRepository.findLeaderboardByYear(
                year);
        List<LeaderboardEntry> leaderboard = new ArrayList<>();

        int rank = 1;
        for (Object[] result : results) {
            User user = (User) result[0];
            Long count = (Long) result[1];
            leaderboard.add(
                    new LeaderboardEntry(
                            user.getUsername(),
                            count,
                            rank++));
        }

        return leaderboard;
    }

    public long getUserEntryCount(String username, Integer year) {
        User user = userRepository.findByUsername(
                username).orElseThrow(
                () -> new RuntimeException(
                        "Username not found"));

        if (year == null) {
            year = LocalDate.now()
                    .getYear();
        }

        return beerEntryRepository.countByUserAndYear(
                user, year);
    }

    private BeerEntryResponse mapToResponse(BeerEntry entry) {
        return new BeerEntryResponse(
                entry.getId(),
                entry.getUser().getUsername(),
                entry.getPhotoUrl(),
                entry.getBeerName(),
                entry.getLocation(),
                entry.getNotes(),
                entry.getCreatedAt()
        );
    }
}
