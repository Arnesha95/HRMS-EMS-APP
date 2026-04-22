package com.vvdn.ems_backend.repository;

import com.vvdn.ems_backend.dtos.EmployeeEventDto;
import com.vvdn.ems_backend.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface EmpRepository extends JpaRepository<Employee, UUID> {

    List<Employee> findByIsActiveTrue();

    long count();

    long countByIsActiveTrue();

    long countByIsActiveFalse();

    long countByCreatedOnBetween(Instant start, Instant end);

    long countByCreatedOnBefore(Instant date);


//    @Query("SELECT e FROM Employee e WHERE e.isActive = true AND " +
//            "FUNCTION('MONTH', e.dateOfBirth) = :month AND FUNCTION('DAY', e.dateOfBirth) = :day")
//    List<Employee> findTodaysBirthdays(@Param("month") int month,
//                                       @Param("day") int day);
//
//
//    @Query("SELECT e FROM Employee e WHERE e.isActive = true AND " +
//            "FUNCTION('MONTH', e.joinDate) = :month AND FUNCTION('DAY', e.joinDate) = :day")
//    List<Employee> findTodaysAnniversaries(@Param("month") int month,
//                                           @Param("day") int day);

}
