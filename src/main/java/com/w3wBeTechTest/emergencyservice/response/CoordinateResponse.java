package com.w3wBeTechTest.emergencyservice.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.w3wBeTechTest.emergencyservice.constants.Constants;
import com.w3wBeTechTest.emergencyservice.models.ThreeWASuggestion;

import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)

public class CoordinateResponse extends BaseResponse {
    @JsonProperty(Constants.LATITUDE)
    private Double latitude;

    @JsonProperty(Constants.LONGITUDE)
    private Double longitude;

    private List <ThreeWASuggestion> suggestions;

    @Builder
    public CoordinateResponse(String message, Double latitude, Double longitude, List <ThreeWASuggestion> suggestions) {
        super(message);
        this.latitude = latitude;
        this.longitude = longitude;
        this.suggestions = suggestions;
    }
}
