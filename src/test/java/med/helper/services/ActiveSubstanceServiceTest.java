package med.helper.services;

import med.helper.dtos.*;
import med.helper.entitys.ActiveSubstance;
import med.helper.entitys.ActiveSubstanceInteraction;
import med.helper.enums.ElementStatus;
import med.helper.repository.ActiveSubstanceInteractionRepository;
import med.helper.repository.ActiveSubstanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ActiveSubstanceServiceTest {
    private ActiveSubstanceService activeSubstanceService;

    @MockBean
    private ActiveSubstanceRepository activeSubstanceRepository;
    @MockBean
    private ActiveSubstanceInteractionRepository activeSubstanceInteractionRepository;

    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        modelMapper = new ModelMapper();
        activeSubstanceService = new ActiveSubstanceService(activeSubstanceRepository, activeSubstanceInteractionRepository, modelMapper);
    }

    @Test
    public void testGetAllActiveSubstance() {
        List<ActiveSubstance> substances = Arrays.asList(
                new ActiveSubstance(1L, "substance1", ElementStatus.ACTIVE.getName()),
                new ActiveSubstance(2L, "substance2", ElementStatus.ACTIVE.getName()),
                new ActiveSubstance(3L, "substance3", ElementStatus.DELETED.getName())
        );

        when(activeSubstanceRepository.findAll()).thenReturn(substances);

        Set<ActiveSubstanceDto> result = activeSubstanceService.getAllActiveSubstance();
        assertEquals(2, result.size());
    }

    @Test
    public void testAddNewActiveSubstance() {
        NewActiveSubstanceDto newSubstance = new NewActiveSubstanceDto("substance1", ElementStatus.ACTIVE.getName());

        when(activeSubstanceRepository.findByName(newSubstance.getName())).thenReturn(Optional.empty());
        when(activeSubstanceRepository.save(any(ActiveSubstance.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<ActiveSubstanceDto> result = activeSubstanceService.addNewActiveSubstance(newSubstance);

        assertTrue(result.isPresent());
        assertEquals(newSubstance.getName(), result.get().getName());
        assertEquals(newSubstance.getStatus(), result.get().getStatus());
    }

    @Test
    public void testDeactivateActiveSubstance() {
        ActiveSubstance substance = new ActiveSubstance(1L, "substance1", ElementStatus.ACTIVE.getName());

        when(activeSubstanceRepository.findById(1L)).thenReturn(Optional.of(substance));
        when(activeSubstanceRepository.save(any(ActiveSubstance.class))).thenAnswer(invocation -> {
            ActiveSubstance updatedSubstance = invocation.getArgument(0);
            substance.setStatus(updatedSubstance.getStatus());
            return updatedSubstance;
        });

        boolean result = activeSubstanceService.deactivateActiveSubstance(1L);

        assertTrue(result);
        assertEquals(ElementStatus.DELETED.getName(), substance.getStatus());
    }
    @Test
    public void testAddNewSubInteraction() {
        List<NewActiveSubstanceInteractionDto> interactions = Arrays.asList(
                new NewActiveSubstanceInteractionDto(1L, "substance2", new Date()),
                new NewActiveSubstanceInteractionDto(1L, "substance3", new Date()),
                new NewActiveSubstanceInteractionDto(2L, "substance4", new Date())
        );

        when(activeSubstanceRepository.findById(1L)).thenReturn(Optional.of(new ActiveSubstance(1L, "substance1", ElementStatus.ACTIVE.getName())));
        when(activeSubstanceRepository.findByName("substance2")).thenReturn(Optional.of(new ActiveSubstance(2L, "substance2", ElementStatus.ACTIVE.getName())));
        when(activeSubstanceRepository.findByName("substance3")).thenReturn(Optional.of(new ActiveSubstance(3L, "substance3", ElementStatus.ACTIVE.getName())));
        when(activeSubstanceRepository.findByName("substance4")).thenReturn(Optional.of(new ActiveSubstance(4L, "substance4", ElementStatus.ACTIVE.getName())));
        when(activeSubstanceInteractionRepository.save(any(ActiveSubstanceInteraction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<ActiveSubstanceDto> result = activeSubstanceService.addNewSubInteraction(interactions);

        assertTrue(result.isPresent());
        verify(activeSubstanceInteractionRepository, times(4)).save(any(ActiveSubstanceInteraction.class));
    }

    @Test
    public void testGetAllInteractionsWithSubstanceById() {
        ActiveSubstance activeSubstance1 = new ActiveSubstance(1L, "Substance1", ElementStatus.ACTIVE.getName());
        ActiveSubstance activeSubstance2 = new ActiveSubstance(2L, "Substance2", ElementStatus.ACTIVE.getName());
        ActiveSubstance activeSubstance3 = new ActiveSubstance(3L, "Substance3", ElementStatus.ACTIVE.getName());

        ActiveSubstanceInteraction interaction1 = new ActiveSubstanceInteraction(1L, activeSubstance1, activeSubstance2, new Date(), ElementStatus.ACTIVE.getName());
        ActiveSubstanceInteraction interaction2 = new ActiveSubstanceInteraction(2L, activeSubstance1, activeSubstance3, new Date(), ElementStatus.ACTIVE.getName());
        ActiveSubstanceInteraction interaction3 = new ActiveSubstanceInteraction(3L, activeSubstance2, activeSubstance3, new Date(), ElementStatus.ACTIVE.getName());

        List<ActiveSubstanceInteraction> interactions = Arrays.asList(interaction1, interaction2, interaction3);

        when(activeSubstanceInteractionRepository.findAll()).thenReturn(interactions);

        Set<ActiveSubstanceInteractionDto> result = activeSubstanceService.getAllInteractionsWithSubstanceById(1L);

        assertEquals(2, result.size());
    }

    @Test
    public void testDeactivateActiveSubstanceInteraction() {
        // Create some mock interactions
        ActiveSubstance activeSubstance1 = new ActiveSubstance(1L, "Substance1", ElementStatus.ACTIVE.getName());
        ActiveSubstance activeSubstance2 = new ActiveSubstance(2L, "Substance2", ElementStatus.ACTIVE.getName());

        ActiveSubstanceInteraction interaction1 = new ActiveSubstanceInteraction(1L, activeSubstance1, activeSubstance2, new Date(), ElementStatus.ACTIVE.getName());
        ActiveSubstanceInteraction interaction2 = new ActiveSubstanceInteraction(2L, activeSubstance1, activeSubstance2, new Date(), ElementStatus.ACTIVE.getName());

        Set<Long> ids = new HashSet<>(Arrays.asList(1L, 2L));

        when(activeSubstanceInteractionRepository.findById(1L)).thenReturn(Optional.of(interaction1));
        when(activeSubstanceInteractionRepository.findById(2L)).thenReturn(Optional.of(interaction2));
        when(activeSubstanceInteractionRepository.save(any(ActiveSubstanceInteraction.class))).thenAnswer(invocation -> {
            ActiveSubstanceInteraction saved = invocation.getArgument(0);
            assertEquals(ElementStatus.DELETED.getName(), saved.getStatus());
            return saved;
        });

        boolean result = activeSubstanceService.deactivateActiveSubstanceInteraction(ids);

        assertTrue(result);
        verify(activeSubstanceInteractionRepository, times(2)).save(any(ActiveSubstanceInteraction.class));
    }



}
