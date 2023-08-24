package com.w3wBeTechTest.emergencyservice.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.w3wBeTechTest.emergencyservice.constants.Constants;
import com.w3wBeTechTest.emergencyservice.enumeration.Country;
import com.w3wBeTechTest.emergencyservice.enumeration.Language;
import com.w3wBeTechTest.emergencyservice.models.ThreeWASuggestion;
import com.w3wBeTechTest.emergencyservice.response.CoordinateResponse;
import com.w3wBeTechTest.emergencyservice.response.LanguageConvertResponse;
import com.w3wBeTechTest.emergencyservice.response.ThreeWAResponse;
import com.w3wBeTechTest.emergencyservice.services.interfaces.IEmergencyService;
import com.what3words.javawrapper.What3WordsV3;
import com.what3words.javawrapper.request.Coordinates;
import com.what3words.javawrapper.response.Autosuggest;
import com.what3words.javawrapper.response.ConvertTo3WA;
import com.what3words.javawrapper.response.ConvertToCoordinates;

@Service
public class EmergencyServiceImpl implements IEmergencyService {

    @Value("${external.w3w.secrets.apiKey}") // pulling API key from properties
    private String w3wApiKey;
    
    @Override
    public ThreeWAResponse get3WAFromCoordinates(double latitude, double longitude, String language) {
        try {
            What3WordsV3 w3wObj = new What3WordsV3(w3wApiKey); // Instantiating What3Words wrapper
            ConvertTo3WA threeWAMeta = w3wObj.convertTo3wa(new Coordinates(latitude, longitude)) // Call w3w wrapper to convert coordinates to 3wa
                .language(language == null ? Language.ENGLISH.label : language)
                .execute();
            String threeWAString = threeWAMeta.getWords(); // extract actual 3wa from the meta
            ThreeWAResponse threeWAResponse = ThreeWAResponse.builder()
                .threeWA(threeWAString)
                .message(threeWAString == null ? Constants.INVALID_COORDINATES : null) // Keeping only message in case of bad 3wa
                .build();
            return threeWAResponse;
        }
        catch(Exception e) {
            return ThreeWAResponse.builder()
                .message(Constants.INVALID_COORDINATES)
                .build();
        }
    }

    public CoordinateResponse getCoordinatesFrom3WA(String threeWA) {
        String message = null;
        List <ThreeWASuggestion> suggestions = new ArrayList<ThreeWASuggestion>();
        Double latitude = null;
        Double longitude = null;
        try {
            What3WordsV3 w3wObj = new What3WordsV3(w3wApiKey); // Instantiating What3Words wrapper
            ConvertToCoordinates coordinatesMeta = w3wObj.convertToCoordinates(threeWA) // Call w3w wrapper to convert 3wa to coordinates
                .execute();
            if(coordinatesMeta.getCoordinates() == null || !coordinatesMeta.getCountry().equals(Country.UNITED_KINGDOM.label)) { // checking for invalidation
                Autosuggest suggestedCoordinates = w3wObj.autosuggest(threeWA) // Call w3w wrapper to get autosuggested addresses
                    .clipToCountry(Country.UNITED_KINGDOM.label)
                    .nResults(3)
                    .execute();
                message = (suggestedCoordinates.getSuggestions() == null || suggestedCoordinates.getSuggestions().isEmpty()) ? Constants.INVALID_3WA : null; // case of bad coordinates
                if(!suggestedCoordinates.getSuggestions().isEmpty()) {
                    message = Constants.UNRECOGNISED_3WA + threeWA;
                    suggestedCoordinates.getSuggestions().stream().forEach((suggestion) -> {
                        suggestions.add(ThreeWASuggestion.builder() // pushing suggestions in internal list
                            .country(suggestion.getCountry())
                            .nearestPlace(suggestion.getNearestPlace())
                            .words(suggestion.getWords())
                            .build()
                        );
                    });
                }
            }
            else {
                latitude = coordinatesMeta.getCoordinates().getLat();
                longitude = coordinatesMeta.getCoordinates().getLng();
            }
            CoordinateResponse coordinateResponse = CoordinateResponse.builder() // creating final object
                .latitude(latitude)
                .longitude(longitude)
                .message(message)
                .suggestions(suggestions)
                .build();
            return coordinateResponse;
        }
        catch(Exception e) {
            return CoordinateResponse.builder()
                .message(Constants.INVALID_3WA)
                .build();
        }
    }

    public LanguageConvertResponse convertToTargetLanguage(String threeWA, String targetLanguage) {
        try {
            CoordinateResponse coordinateResponse = this.getCoordinatesFrom3WA(threeWA); // converting to coordinates
            ThreeWAResponse threeWAResponse = new ThreeWAResponse("", null);
            if(coordinateResponse.getLatitude() != null && coordinateResponse.getLongitude() != null) { // checking for correct coordinates
                threeWAResponse = this.get3WAFromCoordinates(coordinateResponse.getLatitude(), coordinateResponse.getLongitude(), targetLanguage); // converting to 3wa again with language param
            }
            LanguageConvertResponse languageConvertResponse = LanguageConvertResponse.builder() // creating final object
                .threeWA(threeWAResponse.getThreeWA())
                .message(coordinateResponse.getMessage() != null ? coordinateResponse.getMessage() : (threeWAResponse.getThreeWA() == null ? Constants.UNIDENTIFIED_LANGUAGE : null))
                .suggestions(coordinateResponse.getSuggestions())
                .build();
            return languageConvertResponse;
        }
        catch(Exception e) {
            return LanguageConvertResponse.builder()
                .message(Constants.INVALID_3WA)
                .build();
        }
    }
}
