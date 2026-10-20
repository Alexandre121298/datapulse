package com.example.datapulse.controllers;

import com.example.datapulse.weather.dto.TrackedCityDTO;
import com.example.datapulse.weather.models.TrackedCityEntity;
import com.example.datapulse.weather.services.TrackedCityServices;
import com.example.datapulse.weather.controllers.TrackedCityController;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(TrackedCityController.class)
@WithMockUser(username = "test@test.fr")
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
    void shouldReturnCreatedWhenCreateTrackedCity() throws Exception {

        TrackedCityEntity TrackedCityEntity = new TrackedCityEntity();

        when(trackedCityService.createTrackedCity(
                eq("Lille"),
                eq("France"),
                eq("test@test.fr")
        )).thenReturn(TrackedCityEntity);

        String jsonBody = """
            {
                "name": "Lille",
                "country": "France",
                "userEmail": "test@test.fr"
            }
            """;

        mockMvc.perform(post("/api/tracked-cities/create-tracked-city")
                        .contentType("application/json")
                        .content(jsonBody))
                .andExpect(status().isCreated());

    }

    //todo : Tester avec a chaque fois une valeur vide ou null pour le Create

    @Test
    void shouldReturnBadRequestWhenCreateCityNameIsEmpty() throws Exception {

        String jsonBody = """
            {
                "name": "",
                "country": "France",
                "userEmail": "test@test.fr"
            }
            """;

        mockMvc.perform(post("/api/tracked-cities/create-tracked-city")
                        .contentType("application/json")
                        .content(jsonBody))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(trackedCityService);
    }

    @Test
    void shouldReturnBadRequestWhenCreateCountryIsEmpty() throws Exception {

        String jsonBody = """
            {
                "name": "Lille",
                "country": "",
                "userEmail": "test@test.fr"
            }
            """;

        mockMvc.perform(post("/api/tracked-cities/create-tracked-city")
                        .contentType("application/json")
                        .content(jsonBody))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(trackedCityService);
    }


    @Test
    void shouldDeleteTrackedCity() throws Exception {

        doNothing().when(trackedCityService).deleteTrackedCity(
                eq("test@test.fr"),
                eq("Lille"),
                eq("France")
        );

        mockMvc.perform(delete("/api/tracked-cities/delete")
                        .param("userEmail", "test@test.fr")
                        .param("cityName", "Lille")
                        .param("cityCountry", "France"))
                .andExpect(status().isNoContent());

        verify(trackedCityService).deleteTrackedCity(
                eq("test@test.fr"),
                eq("Lille"),
                eq("France")
        );
    }


    @Test
    void shouldReturnBadRequestWhenDeleteCityNameIsEmpty() throws Exception {

        doThrow(new BadRequestException("Le nom de la ville ne peut pas être vide"))
                .when(trackedCityService)
                .deleteTrackedCity(
                        eq("test@test.fr"),
                        eq(""),
                        eq("France")
                );

        mockMvc.perform(delete("/api/tracked-cities/delete")
                        .param("userEmail", "test@test.fr")
                        .param("cityName", "")
                        .param("cityCountry", "France"))
                .andExpect(status().isBadRequest());

        verify(trackedCityService).deleteTrackedCity(
                eq("test@test.fr"),
                eq(""),
                eq("France")
        );
    }

    @Test
    void shouldReturnBadRequestWhenDeleteCityNameIsMissing() throws Exception {

        mockMvc.perform(delete("/api/tracked-cities/delete")
                        .param("userEmail", "test@test.fr")
                        .param("cityCountry", "France"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(trackedCityService);
    }

    @Test
    void shouldReturnBadRequestWhenDeleteCityCountryIsEmpty() throws Exception {

        doThrow(new BadRequestException("Le pays de la ville ne peut pas être vide"))
                .when(trackedCityService)
                .deleteTrackedCity(
                        eq("test@test.fr"),
                        eq("Lille"),
                        eq("")
                );

        mockMvc.perform(delete("/api/tracked-cities/delete")
                        .param("userEmail", "test@test.fr")
                        .param("cityName", "Lille")
                        .param("cityCountry", ""))
                .andExpect(status().isBadRequest());

        verify(trackedCityService).deleteTrackedCity(
                eq("test@test.fr"),
                eq("Lille"),
                eq("")
        );
    }

    @Test
    void shouldReturnBadRequestWhenDeleteCityCountryIsMissing() throws Exception {

        mockMvc.perform(delete("/api/tracked-cities/delete")
                        .param("userEmail", "test@test.fr")
                        .param("cityName", "Lille"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(trackedCityService);
    }

    @Test
    void shouldUseConnectedUserWhenAnotherEmailIsProvided() throws Exception {

        when(trackedCityService.getAllTrackedCitiesByUser(
                eq("test@test.fr"),
                any(Pageable.class)
        )).thenReturn(Page.empty());

        mockMvc.perform(get("/api/tracked-cities/all-tracked")
                        .param("userEmail", "autre@example.com"))
                .andExpect(status().isOk());

        verify(trackedCityService).getAllTrackedCitiesByUser(
                eq("test@test.fr"),
                any(Pageable.class)
        );
    }

    @Test
    void shouldDeleteOnlyForConnectedUserWhenAnotherEmailIsProvided()
            throws Exception {

        mockMvc.perform(delete("/api/tracked-cities/delete")
                        .param("userEmail", "autre@example.com")
                        .param("cityName", "Lille")
                        .param("cityCountry", "France"))
                .andExpect(status().isNoContent());

        verify(trackedCityService).deleteTrackedCity(
                "test@test.fr",
                "Lille",
                "France"
        );
    }

}
