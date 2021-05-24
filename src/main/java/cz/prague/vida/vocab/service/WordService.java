package cz.prague.vida.vocab.service;

import cz.prague.vida.vocab.model.Word;
import cz.prague.vida.vocab.repository.WordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class WordService {

    private final WordRepository wordRepository;

    @Autowired
    public WordService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    public List<Word> getAllWords() {
        return wordRepository.findAll();
    }


    public Optional<Word> getWord(Long id) {
        return wordRepository.findById(id);
    }

    public void updateWordCount(Long id, Date date, boolean correct) {
        if (correct) {
            wordRepository.updateWordCountTrue(id, date);
        } else {
            wordRepository.updateWordCountFalse(id, date);
        }
    }

    public Word findWord(String trim, String cs) {
        return wordRepository.findWord(trim, cs);
    }

    public Number getWordTotalCorrectCount() {
        return wordRepository.getWordTotalCorrectCount();
    }

    public Number getWordTotalCount() {
        return wordRepository.getWordTotalCount();
    }

    public Number getDistinctCount() {
        return wordRepository.getDistinctCount();
    }
}
