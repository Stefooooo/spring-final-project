package com.example.spring_final_project.Condition.repository;

import com.example.spring_final_project.Condition.model.Condition;
import com.example.spring_final_project.Doctor.model.Departament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConditionRepository extends JpaRepository<Condition, UUID> {
    Optional<Condition> findByName(String name);

    Optional<List<Condition>> findAllByDepartament(Departament departament);

    Optional<List<Condition>> findAllByDepartamentIn(List<Departament> departaments);

    Optional<List<Condition>> findAllByIsActive(boolean b);
}
