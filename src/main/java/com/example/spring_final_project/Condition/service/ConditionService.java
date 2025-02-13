package com.example.spring_final_project.Condition.service;

import com.example.spring_final_project.Condition.model.Condition;
import com.example.spring_final_project.Condition.repository.ConditionRepository;
import com.example.spring_final_project.Doctor.model.Departament;
import com.example.spring_final_project.exception.DomainException;
import com.example.spring_final_project.web.dto.ConditionRegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class ConditionService {

    private final ConditionRepository conditionRepository;

    @Autowired
    public ConditionService(ConditionRepository conditionRepository) {
        this.conditionRepository = conditionRepository;
    }

    public Condition registerNewCondition(ConditionRegisterRequest conditionRegisterRequest){
        Optional<Condition> conditionRequest = conditionRepository.findByName(conditionRegisterRequest.getName());

        if (conditionRequest.isPresent()){
            throw new DomainException("[%s] already exists as a condition!".formatted(conditionRegisterRequest.getName()));
        }

        Condition condition = conditionRepository.save(initializeCondition(conditionRegisterRequest));

        log.info("Successfully created condition with name [%s] and id [%s]!".formatted(condition.getName(), condition.getId()));

        return condition;
    }

    private Condition initializeCondition(ConditionRegisterRequest conditionRegisterRequest) {

        LocalDateTime now = LocalDateTime.now();

        return Condition.builder()
                .name(conditionRegisterRequest.getName())
//                .departament(conditionRegisterRequest.getDepartament())
                .createdOn(now)
                .updatedOn(now)
                .build();
    }

    private Condition getConditionById(UUID uuid){
        Optional<Condition> condition = conditionRepository.findById(uuid);

        if (condition.isEmpty()){
            throw new DomainException("Condition with ID [%s] cannot be found!".formatted(uuid));
        } else {
            return condition.get();
        }
    }

    private List<Condition> getAllConditions(){
        return conditionRepository.findAll();
    }

    private List<Condition> getAllConditionsByDepartment(Departament departament){
        Optional<List<Condition>> conditions = conditionRepository.findAllByDepartament(departament);

        if (conditions.isEmpty()){
            throw new DomainException("Conditions for the [%s] Department cannot be found!".formatted(departament.name()));
        } else {
            return conditions.get();
        }

    }

    private List<Condition> getAllConditionsByDepartment(List<Departament> departaments){
        Optional<List<Condition>> conditions = conditionRepository.findAllByDepartamentIn(departaments);

        if (conditions.isEmpty()){
            throw new DomainException("Conditions with the selected departments cannot be found!");
        } else {
            return conditions.get();
        }

    }

}
