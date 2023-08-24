package com.w3wBeTechTest.emergencyservice.models;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ThreeWASuggestion {
    private String country;
    private String nearestPlace;
    private String words;
}
