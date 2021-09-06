package com.itechart.peopleflow.repository;

import com.itechart.peopleflow.entity.EmployeeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends CrudRepository<EmployeeEntity, Long> {
 EmployeeEntity findByUserId(String userId);
}