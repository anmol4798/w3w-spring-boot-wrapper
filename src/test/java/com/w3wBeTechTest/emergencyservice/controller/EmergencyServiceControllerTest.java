package com.w3wBeTechTest.emergencyservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.w3wBeTechTest.emergencyservice.constants.Constants;
import com.w3wBeTechTest.emergencyservice.services.impl.EmergencyServiceImpl;

@ContextConfiguration(classes = {EmergencyServiceController.class, EmergencyServiceImpl.class})
@WebMvcTest
public class EmergencyServiceControllerTest {

    @Autowired
	private MockMvc mockMvc;

    @Test
    public void get3WAFromCoordinatesTests() throws Exception {

        // Testing OK case with correct request and response
            mockMvc.perform(MockMvcRequestBuilders.get(Constants.CONVERT_TO_3WA_URI + "?lat=52.599329&lng=-2.083533")).andExpect(status().isOk()).andExpect(jsonPath("$.3wa").value("filled.count.slap"));
        // Testing bad request case
            mockMvc.perform(MockMvcRequestBuilders.get(Constants.CONVERT_TO_3WA_URI + "?lat=52.599329")).andExpect(status().isOk()).andExpect(jsonPath("$.message").value(Constants.INVALID_COORDINATES));
        // Testing latitutde being out of bounds [-90, 90] case
            mockMvc.perform(MockMvcRequestBuilders.get(Constants.CONVERT_TO_3WA_URI + "?lat=92.599329&lng=-2.083533")).andExpect(status().isOk()).andExpect(jsonPath("$.message").value(Constants.INVALID_COORDINATES));
    }

    @Test
    public void getCoordinatesFrom3WA() throws Exception {
    
        // Testing OK case with correct request and response
            mockMvc.perform(MockMvcRequestBuilders.get(Constants.CONVERT_TO_COORDINATES_URI + "?3wa=daring.lion.race")).andExpect(status().isOk()).andExpect(jsonPath("$.lat").value(51.508341)).andExpect(jsonPath("$.lng").value(-0.125499));
        // Testing bad request case
            mockMvc.perform(MockMvcRequestBuilders.get(Constants.CONVERT_TO_COORDINATES_URI + "?3wa=daring.lion")).andExpect(status().isOk()).andExpect(jsonPath("$.message").value(Constants.INVALID_3WA));
        // Testing suggestion case
            mockMvc.perform(MockMvcRequestBuilders.get(Constants.CONVERT_TO_COORDINATES_URI + "?3wa=filled.count.snap")).andExpect(status().isOk()).andExpect(jsonPath("$.message").value(Constants.UNRECOGNISED_3WA + "filled.count.snap")).andExpect(jsonPath("$.suggestions").isArray());
    }

    @Test
    public void convertToTargetLanguage() throws Exception {
        // Testing OK case with correct request and response
            mockMvc.perform(MockMvcRequestBuilders.get(Constants.CHANGE_LANGUAGE_URI + "?3wa=daring.lion.race&target_language=cy")).andExpect(status().isOk()).andExpect(jsonPath("$.3wa").value("sychach.parciau.lwmpyn"));
        // Testing bad request case
            mockMvc.perform(MockMvcRequestBuilders.get(Constants.CHANGE_LANGUAGE_URI + "?3wa=daring.lion&target_language=cy")).andExpect(status().isOk()).andExpect(jsonPath("$.message").value(Constants.INVALID_3WA));
        // Testing suggestion case
            mockMvc.perform(MockMvcRequestBuilders.get(Constants.CHANGE_LANGUAGE_URI + "?3wa=filled.count.snap&target_language=cy")).andExpect(status().isOk()).andExpect(jsonPath("$.message").value(Constants.UNRECOGNISED_3WA + "filled.count.snap")).andExpect(jsonPath("$.suggestions").isArray());
        // Testing incomplete request case
            mockMvc.perform(MockMvcRequestBuilders.get(Constants.CHANGE_LANGUAGE_URI + "?3wa=filled.count.snap")).andExpect(status().isOk()).andExpect(jsonPath("$.message").value(Constants.BAD_REQUEST));
        // Testing unidentified language case
            mockMvc.perform(MockMvcRequestBuilders.get(Constants.CHANGE_LANGUAGE_URI + "?3wa=daring.lion.race&target_language=ab")).andExpect(status().isOk()).andExpect(jsonPath("$.message").value(Constants.UNIDENTIFIED_LANGUAGE));
    }
}
