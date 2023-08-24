package com.w3wBeTechTest.emergencyservice.constants;

public class Constants {
    public final static String LATITUDE = "lat";
    public final static String LONGITUDE = "lng";
    public final static String THREE_WA = "3wa";
    public final static String TARGET_LANGUAGE = "target_language";
    public final static String INVALID_COORDINATES = "Coordinates supplied do not convert to a 3wa";
    public final static String INVALID_3WA = "3wa address supplied has invalid format";
    public final static String UNRECOGNISED_3WA = "3wa not recognised : ";
    public final static String UNIDENTIFIED_LANGUAGE = "This language is not identified";
    public final static String BAD_REQUEST = "Incomplete request";
    public final static String CONVERT_TO_3WA_URI = "/emergencyapi/coord-to-3wa";
    public final static String CONVERT_TO_COORDINATES_URI = "/emergencyapi/3wa-to-coord";
    public final static String CHANGE_LANGUAGE_URI = "/emergencyapi/language-convert";
}
