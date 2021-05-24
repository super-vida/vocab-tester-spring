package cz.prague.vida.vocab.service;

import cz.prague.vida.vocab.model.WordGroup;
import cz.prague.vida.vocab.repository.WordGroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class WordGroupService {

    private final WordGroupRepository wordGroupRepository;

    @Autowired
    public WordGroupService(WordGroupRepository wordGroupRepository) {
        this.wordGroupRepository = wordGroupRepository;
    }

    public List<WordGroup> findWordGroups(long lessonId) {
        return wordGroupRepository.findWordGroups(lessonId);
    }

    public void persist(WordGroup wordGroup) {
        wordGroupRepository.save(wordGroup);
    }
}
