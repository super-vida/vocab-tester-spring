package cz.prague.vida.vocab.repository;

import cz.prague.vida.vocab.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    @Query(value = "select l from Lesson l where l.name = :lessonName")
    Lesson findLesson(String lessonName);

    @Query(value = "select count(l.id) from Lesson l")
    Number getLessonCount();
}
