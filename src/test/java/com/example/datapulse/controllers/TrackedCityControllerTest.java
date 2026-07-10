package com.example.datapulse.controllers;

import com.example.datapulse.weather.dto.TrackedCityDTO;
import com.example.datapulse.weather.models.TrackedCityEntity;
import com.example.datapulse.weather.services.TrackedCityServices;
import com.example.datapulse.weather.controllers.TrackedCityController;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(TrackedCityController.class)
class TrackedCityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TrackedCityServices trackedCityService;

    @Test
    void shouldReturnOkWhenGettingTrackedCities() throws Exception {
        Page<TrackedCityDTO.TrackedCityOutput> emptyPage = new PageImpl<>(List.of());

        when(trackedCityService.getAllTrackedCitiesByUser(
                eq("test@test.fr"),
                any(Pageable.class)
        )).thenReturn(emptyPage);

        mockMvc.perform(get("/api/tracked-cities/all-tracked")
                        .param("userEmail", "test@test.fr")
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().isOk());
    }

    @Test
    void verifyServiceCallForAllTrackedCities() throws Exception {

        Page<TrackedCityDTO.TrackedCityOutput> emptyPage = new PageImpl<>(List.of());

        when(trackedCityService.getAllTrackedCitiesByUser(
                eq("test@test.fr"),
                any(Pageable.class)
        )).thenReturn(emptyPage);

        mockMvc.perform(get("/api/tracked-cities/all-tracked")
                        .param("userEmail", "test@test.fr")
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().isOk());

        verify(trackedCityService).getAllTrackedCitiesByUser(
                eq("test@test.fr"),
                any(Pageable.class)
        );
    }

    @Test
    void shouldReturnOkWhenCreateTrackedCity() throws Exception {

        TrackedCityEntity TrackedCityEntity = new TrackedCityEntity();

        when(trackedCityService.createTrackedCity(
                eq("Lille"),
                eq("France"),
                eq("AutreTest@Test.fr")
        )).thenReturn(TrackedCityEntity);

        String jsonBody = """
            {
                "name": "Lille",
                "country": "France",
                "userEmail": "AutreTest@Test.fr"
            }
            """;

        mockMvc.perform(post("/api/tracked-cities/create-tracked-city")
                        .contentType("application/json")
                        .content(jsonBody))
                .andExpect(status().isCreated());

    }
}
