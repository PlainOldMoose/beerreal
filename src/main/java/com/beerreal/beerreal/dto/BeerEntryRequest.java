package com.beerreal.beerreal.dto;

import lombok.Data;

@Data
public class BeerEntryRequest {
    private String beerName;
    private String location;
    private String notes;
}
