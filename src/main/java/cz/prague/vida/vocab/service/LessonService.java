package cz.prague.vida.vocab.service;

import cz.prague.vida.vocab.model.Lesson;
import cz.prague.vida.vocab.repository.LessonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class LessonService {

    private final LessonRepository lessonRepository;

    @Autowired
    public LessonService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll(Sort.by("name"));
    }

    public Lesson findLesson(String lessonName) {
        return lessonRepository.findLesson(lessonName);
    }


    public void deleteLesson(Lesson lesson) {
        lessonRepository.delete(lesson);
    }

    public void persist(Lesson lessonNew) {
        lessonRepository.save(lessonNew);
    }

    public Number getLessonCount() {
        return lessonRepository.getLessonCount();
    }
}
