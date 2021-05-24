package cz.prague.vida.vocab.service;

import cz.prague.vida.vocab.model.LessonCheck;
import cz.prague.vida.vocab.repository.LessonCheckRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class LessonCheckService {

    private final LessonCheckRepository lessonCheckRepository;

    @Autowired
    public LessonCheckService(LessonCheckRepository lessonCheckRepository) {
        this.lessonCheckRepository = lessonCheckRepository;
    }

    public List<LessonCheck> findByLessonId(Long lessonId) {
        return lessonCheckRepository.findByLessonId(lessonId);
    }


    public void persist(LessonCheck lessonCheck) {
        lessonCheckRepository.save(lessonCheck);
    }
}
