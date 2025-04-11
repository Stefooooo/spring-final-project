package com.example.spring_final_project.Condition;

import com.example.spring_final_project.Appointment.model.Appointment;
import com.example.spring_final_project.Appointment.model.AppointmentStatus;
import com.example.spring_final_project.Condition.model.Condition;
import com.example.spring_final_project.Condition.repository.ConditionRepository;
import com.example.spring_final_project.Condition.service.ConditionService;
import com.example.spring_final_project.Doctor.model.Departament;
import com.example.spring_final_project.Doctor.model.Doctor;
import com.example.spring_final_project.User.model.User;
import com.example.spring_final_project.exception.DomainException;
import com.example.spring_final_project.web.dto.ConditionAddRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConditionServiceTest {

    @Mock
    private ConditionRepository conditionRepository;

    @InjectMocks
    private ConditionService conditionService;

    @Test
    void givenNoActualCondition_throwException_whenGetConditionById() {
        // Given
        when(conditionRepository.findById(any())).thenReturn(Optional.empty());

        //When & Then
        assertThrows(DomainException.class, () -> conditionService.getConditionById(UUID.randomUUID()));
    }

    @Test
    void givenHappyPath_whenGetConditionById() {

        // Given
        Condition condition = Condition.builder()
                .id(UUID.randomUUID())
                .build();
        when(conditionRepository.findById(any())).thenReturn(Optional.of(condition));

        //When
        Condition result = conditionService.getConditionById(UUID.randomUUID());

        assertEquals(condition.getId(), result.getId());
    }

    @Test
    void givenExistingConditionsInDatabase_whenGetAllConditions_thenReturnThemAll() {
        List<Condition> conditions = List.of(new Condition(), new Condition());
        when(conditionRepository.findAll()).thenReturn(conditions);

        List<Condition> result = conditionService.getAllConditions();
        assertThat(result).hasSize(2);
    }

    @Test
    void givenExistingConditionsInDatabase_whenGetAllActiveConditions_thenReturnThemAll() {
        List<Condition> conditions = List.of(new Condition(), new Condition());
        when(conditionRepository.findAllByIsActive(true)).thenReturn(Optional.of(conditions));

        List<Condition> result = conditionService.getAllActiveConditions();
        assertThat(result).hasSize(2);
    }

    @Test
    void givenNoExistingConditionsInDatabase_whenGetAllActiveConditions_thenReturnZero() {
        List<Condition> conditions = new ArrayList<>();
        when(conditionRepository.findAllByIsActive(true)).thenReturn(Optional.of(conditions));

        List<Condition> result = conditionService.getAllActiveConditions();
        assertThat(result).hasSize(0);
    }

    @Test
    void givenHappyPath_whenSaveCondition() {

        ConditionAddRequest nastinka = ConditionAddRequest.builder()
                .name("nastinka")
                .departament(Departament.ENT)
                .build();

        Condition condition = conditionService.saveCondition(nastinka);

        verify(conditionRepository, times(1)).save(condition);
    }

    @Test
    void givenHappyPath_whenDeleteCondition() {
        UUID conditionId = UUID.randomUUID();
        Condition condition = Condition.builder()
                .id(conditionId)
                .isActive(true)
                .build();

        when(conditionRepository.findById(any())).thenReturn(Optional.of(condition));

        conditionService.deleteCondition(conditionId);

        verify(conditionRepository, times(1)).save(condition);
        assertFalse(condition.isActive());
    }

    @Test
    void testGettersAndSetters() {
        LocalDateTime now = LocalDateTime.now();

        Condition condition = new Condition();
        condition.setActive(true);
        condition.setName("nastinka");
        condition.setDepartament(Departament.Endocrinology);
        condition.setUpdatedOn(now);
        condition.setCreatedOn(now);


        assertEquals("nastinka", condition.getName());
        assertEquals(Departament.Endocrinology, condition.getDepartament());
        assertTrue(condition.isActive());
        assertEquals(now, condition.getCreatedOn());
        assertEquals(now, condition.getUpdatedOn());
    }
}