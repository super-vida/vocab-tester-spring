package cz.prague.vida.vocab.repository;

import cz.prague.vida.vocab.model.LessonGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonGroupRepository extends JpaRepository<LessonGroup, Long> {
}
