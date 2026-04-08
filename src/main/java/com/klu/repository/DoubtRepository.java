package com.klu.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.klu.model.Doubt;

public interface DoubtRepository extends JpaRepository<Doubt, Long> {
    List<Doubt> findByCourseIdIn(List<Long> courseIds);

    void deleteByCourseId(Long courseId);
}
