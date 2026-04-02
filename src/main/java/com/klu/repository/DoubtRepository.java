package com.klu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.klu.model.Doubt;

public interface DoubtRepository extends JpaRepository<Doubt, Long> {
}