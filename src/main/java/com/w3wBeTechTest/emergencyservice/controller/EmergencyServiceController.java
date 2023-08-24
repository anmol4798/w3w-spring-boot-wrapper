package com.w3wBeTechTest.emergencyservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.w3wBeTechTest.emergencyservice.constants.Constants;
import com.w3wBeTechTest.emergencyservice.response.ThreeWAResponse;
import com.w3wBeTechTest.emergencyservice.response.BaseResponse;
import com.w3wBeTechTest.emergencyservice.response.CoordinateResponse;
import com.w3wBeTechTest.emergencyservice.response.LanguageConvertResponse;
import com.w3wBeTechTest.emergencyservice.services.interfaces.IEmergencyService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping({"/emergencyapi/"})
public class EmergencyServiceController {

    @Autowired
    private IEmergencyService emergencyService;

    /**
     * API to get 3wa from provided coordinates
     *
     * @param latitude                  - Latitude value sent by client
     * @param longitude                 - Longitude value sent by client
     * @param httpServletRequest        - Http Request
     * @param httpServletResponse       - Http response
     * @return - {@link ThreeWAResponse}
     */

    @RequestMapping(value = "/coord-to-3wa", method = RequestMethod.GET)
    @ResponseBody
    public ThreeWAResponse get3WAFromCoordinates(
        @RequestParam(value = Constants.LATITUDE, required = true) double latitude,
        @RequestParam(value = Constants.LONGITUDE, required = true) double longitude,
        HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse
    ) throws Exception {
        try {
            ThreeWAResponse threeWAResponse = emergencyService.get3WAFromCoordinates(latitude, longitude, null);
            return threeWAResponse;
        }
        catch (final Exception e) {
            throw e;
        }
    }


    /**
     * API to get coordinates from provided 3wa
     *
     * @param threeWA                   - 3wa sent by client
     * @param httpServletRequest        - Http Request
     * @param httpServletResponse       - Http response
     * @return - {@link ThreeWAResponse}
     */

    @RequestMapping(value = "/3wa-to-coord", method = RequestMethod.GET)
    @ResponseBody
    public CoordinateResponse getCoordinatesFrom3WA(
        @RequestParam(value = Constants.THREE_WA, required = true) String threeWA,
        HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse
    ) throws Exception {
        try {
            CoordinateResponse coordinateResponse = emergencyService.getCoordinatesFrom3WA(threeWA);
            return coordinateResponse;
        }
        catch (final Exception e) {
            throw e;
        }
    }


    /**
     * API to convert 3wa in provided language
     *
     * @param threeWA                   - 3wa sent by client
     * @param targetLanguage           - language sent by client
     * @param httpServletRequest        - Http Request
     * @param httpServletResponse       - Http response
     * @return - {@link ThreeWAResponse}
     */

    @RequestMapping(value = "/language-convert", method = RequestMethod.GET)
    @ResponseBody
    public LanguageConvertResponse convertToTargetLanguage(
        @RequestParam(value = Constants.THREE_WA, required = true) String threeWA,
        @RequestParam(value = Constants.TARGET_LANGUAGE, required = true) String targetLanguage,
        HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse
    ) throws Exception {
        try {
            LanguageConvertResponse languageConvertResponse = emergencyService.convertToTargetLanguage(threeWA, targetLanguage);
            return languageConvertResponse;
        }
        catch (final Exception e) {
            throw e;
        }
    }


    /**
     * Exception handler for handling controller level exceptions
     *
     * @param e                         - the caught exception
     * @param httpServletRequest        - parent Http Request
     * @return - {@link BaseResponse}
     */

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public BaseResponse handleException(Exception e, HttpServletRequest httpServletRequest) {
        switch(httpServletRequest.getRequestURI()) {
            case Constants.CONVERT_TO_3WA_URI:
                return new BaseResponse(Constants.INVALID_COORDINATES);
            case Constants.CONVERT_TO_COORDINATES_URI:
                return new BaseResponse(Constants.INVALID_3WA);
            default:
                return new BaseResponse(Constants.BAD_REQUEST);
        }
    }
}
