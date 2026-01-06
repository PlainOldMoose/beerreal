package com.beerreal.beerreal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LeaderboardEntry {
    private String username;
    private Long entryCount;
    private Integer rank;
}
