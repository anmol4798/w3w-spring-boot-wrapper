package com.w3wBeTechTest.emergencyservice.services.interfaces;

import com.w3wBeTechTest.emergencyservice.response.CoordinateResponse;
import com.w3wBeTechTest.emergencyservice.response.LanguageConvertResponse;
import com.w3wBeTechTest.emergencyservice.response.ThreeWAResponse;

public interface IEmergencyService {
    ThreeWAResponse get3WAFromCoordinates(double latitude, double longitude, String language) throws Exception;
    CoordinateResponse getCoordinatesFrom3WA(String threeWA) throws Exception;
    LanguageConvertResponse convertToTargetLanguage(String threeWA, String targetLanguage) throws Exception;
}
