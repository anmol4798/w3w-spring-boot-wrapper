package com.w3wBeTechTest.emergencyservice.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.w3wBeTechTest.emergencyservice.constants.Constants;

import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)

public class ThreeWAResponse extends BaseResponse {
    @JsonProperty(Constants.THREE_WA)
    private String threeWA;

    @Builder
    public ThreeWAResponse(String message, String threeWA) {
        super(message);
        this.threeWA = threeWA;
    }
}
