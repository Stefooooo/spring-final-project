package com.example.spring_final_project.Condition.service;

import com.example.spring_final_project.Condition.model.Condition;
import com.example.spring_final_project.Condition.repository.ConditionRepository;
import com.example.spring_final_project.exception.DomainException;
import com.example.spring_final_project.web.dto.ConditionAddRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public Condition getConditionById(UUID uuid){
        Optional<Condition> condition = conditionRepository.findById(uuid);

        if (condition.isEmpty()){
            throw new DomainException("Condition with ID [%s] cannot be found!".formatted(uuid));
        } else {
            return condition.get();
        }
    }

    @Cacheable("conditions")
    public List<Condition> getAllConditions(){
        return conditionRepository.findAll();
    }

    @Cacheable("active-conditions")
    public List<Condition> getAllActiveConditions(){

        Optional<List<Condition>> conditions = conditionRepository.findAllByIsActive(true);

        return conditions.orElseGet(ArrayList::new);
    }

    @CacheEvict(value = {"conditions", "active-conditions"}, allEntries = true)
    public Condition saveCondition(ConditionAddRequest conditionAddRequest) {

        Condition condition = initializeNewCondition(conditionAddRequest);

        conditionRepository.save(condition);

        log.info("A new condition with name [%s] and id [%s] was created!".formatted(condition.getName(), condition.getId()));

        return condition;
    }

    @CacheEvict(value = {"conditions", "active-conditions"}, allEntries = true)
    private Condition initializeNewCondition(ConditionAddRequest conditionAddRequest) {

        LocalDateTime now = LocalDateTime.now();

        return Condition.builder()
                .name(conditionAddRequest.getName())
                .departament(conditionAddRequest.getDepartament())
                .isActive(true)
                .createdOn(now)
                .updatedOn(now)
                .build();
    }

    @CacheEvict(value = {"conditions", "active-conditions"}, allEntries = true)
    public void deleteCondition(UUID id) {
        Condition condition = getConditionById(id);

        condition.setActive(false);

        conditionRepository.save(condition);

    }
}
