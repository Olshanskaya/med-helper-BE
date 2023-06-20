package med.helper.controllers;

import static org.junit.jupiter.api.Assertions.*;

import med.helper.dtos.ActiveSubstanceDto;
import med.helper.dtos.ActiveSubstanceInteractionDto;
import med.helper.services.ActiveSubstanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class ActiveSubstanceControllerTest {

    @Mock
    private ActiveSubstanceService activeSubstanceService;

    @InjectMocks
    private ActiveSubstanceController activeSubstanceController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(activeSubstanceController).build();
    }

    @Test
    void testGetAllActiveSubstance() throws Exception {
        ActiveSubstanceDto dto = new ActiveSubstanceDto(); // setup dto

        when(activeSubstanceService.getAllActiveSubstance()).thenReturn(Collections.singleton(dto));

        mockMvc.perform(get("/admin/sub/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllInteractionsWithSubstanceById() throws Exception {
        ActiveSubstanceInteractionDto dto = new ActiveSubstanceInteractionDto(); // setup dto

        when(activeSubstanceService.getAllInteractionsWithSubstanceById(1L)).thenReturn(Collections.singleton(dto));

        mockMvc.perform(get("/admin/sub/interaction/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
}
