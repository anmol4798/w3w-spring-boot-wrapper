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

public class LanguageConvertResponse extends BaseResponse {
    @JsonProperty(Constants.THREE_WA)
    private String threeWA;

    private List <ThreeWASuggestion> suggestions;

    @Builder
    public LanguageConvertResponse(String message, String threeWA, List <ThreeWASuggestion> suggestions) {
        super(message);
        this.threeWA = threeWA;
        this.suggestions = suggestions;
    }
}
