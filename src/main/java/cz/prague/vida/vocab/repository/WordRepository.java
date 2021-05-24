package cz.prague.vida.vocab.repository;

import cz.prague.vida.vocab.model.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

    @Transactional
    @Modifying
    @Query(value = "update Word w set w.correctCount = w.correctCount + 1, w.correctTime =:date where w.id = :id")
    void updateWordCountTrue(Long id, Date date);

    @Transactional
    @Modifying
    @Query(value = "update Word w set w.incorrectCount = w.incorrectCount + 1, w.incorrectTime =:date where w.id = :id")
    void updateWordCountFalse(Long id, Date date);

    @Query(value = "select w from Word w where w.text = :text and w.language = :language")
    Word findWord(String text, String language);

    @Query(value = "select sum(l.correctCount) from Lesson l")
    Number getWordTotalCorrectCount();

    @Query(value = "select sum(l.totalCount) from Lesson l")
    Number getWordTotalCount();

    @Query(value = "select count(w.id) from Word w where w.language = 'en'")
    Number getDistinctCount();
}
