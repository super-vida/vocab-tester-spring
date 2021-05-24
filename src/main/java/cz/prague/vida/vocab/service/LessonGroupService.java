package cz.prague.vida.vocab.service;

import cz.prague.vida.vocab.model.LessonGroup;
import cz.prague.vida.vocab.repository.LessonGroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class LessonGroupService {

    private final LessonGroupRepository lessonGroupRepository;

    @Autowired
    public LessonGroupService(LessonGroupRepository lessonGroupRepository) {
        this.lessonGroupRepository = lessonGroupRepository;
    }

    public List<LessonGroup> getAllLessonGroups() {
        return lessonGroupRepository.findAll();
    }


}
