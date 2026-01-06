package com.beerreal.beerreal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BeerEntryResponse {
    private Long id;
    private String username;
    private String photoUrl;
    private String beerName;
    private String location;
    private String notes;
    private LocalDateTime createdAt;
}
