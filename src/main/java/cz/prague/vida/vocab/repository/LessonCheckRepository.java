package cz.prague.vida.vocab.repository;

import cz.prague.vida.vocab.model.LessonCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonCheckRepository extends JpaRepository<LessonCheck, Long> {


    @Query(value = "select lc from LessonCheck lc " +
            "where lc.lessonId = :lessonId order by lc.time desc")
    List<LessonCheck> findByLessonId(@Param("lessonId") Long lessonId);
}
