package cz.prague.vida.vocab.repository;

import cz.prague.vida.vocab.model.*;
import cz.prague.vida.vocab.service.LessonCheckService;
import cz.prague.vida.vocab.service.LessonService;
import cz.prague.vida.vocab.service.WordGroupService;
import cz.prague.vida.vocab.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.*;

/**
 * The Class VocabHolder.
 */
@Service
public class VocabHolderDB implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private int index = 0;

    private int totalWords = 0;
    private int totalAnswers;
    private int totalCorrectAnswers;
    private String fileName;
    private Lesson lesson;
    private long startCheckTime;

    /**
     * The Constant CHARSET.
     */
    public static final String CHARSET = "UTF-8";

    private transient List<TestedWord> testedWords;
    private transient Set<TestedWord> incorrectWords;

    private final LessonService lessonService;
    private final WordGroupService wordGroupService;
    private final WordService wordService;
    private final LessonCheckService lessonCheckService;

    @Autowired
    public VocabHolderDB(LessonService lessonService, WordGroupService wordGroupService, WordService wordService, LessonCheckService lessonCheckService) {
        this.lessonService = lessonService;
        this.wordGroupService = wordGroupService;
        this.wordService = wordService;
        this.lessonCheckService = lessonCheckService;
    }

    /**
     * Gets the current test word.
     *
     * @return the current test word
     */
    public TestedWord getCurrentTestWord() {
        if (testedWords == null || testedWords.isEmpty()) {
            return new TestedWord(null, null);
        }
        return testedWords.get(index);
    }

    /**
     * Shuffle words.
     */
    public void shuffleWords() {
        Collections.shuffle(testedWords);
    }

    /**
     * Prepare next pair.
     *
     * @return true, if successful
     */
    public boolean prepareNextPair() {
        shuffleWords();
        if (testedWords.isEmpty()) {
            return false;
        }

        if (testedWords.size() > index + 1) {
            ++index;
            return true;
        } else {
            index = 0;
            return true;
        }

    }

    /**
     * Load vocabulary file.
     *
     * @param lessonName the lesson name
     */
    public void loadVocabularyFile(String lessonName, Lesson less) {
        this.totalWords = 0;
        if (lesson == null) {
            this.lesson = lessonService.findLesson(lessonName);
        } else {
            this.lesson = less;
        }
        List<WordGroup> wordGroups = wordGroupService.findWordGroups(lesson.getId());
        startCheckTime = System.currentTimeMillis();
        this.fileName = lessonName;
        testedWords = new ArrayList<>();
        Set<TestedWord> set = new HashSet<>();
        incorrectWords = new HashSet<>();
        totalAnswers = 0;
        totalCorrectAnswers = 0;
        try {

            for (WordGroup wordGroup : wordGroups) {
                Optional<Word> word = wordService.getWord(wordGroup.getWord2Id());
                Optional<Word> translation = wordService.getWord(wordGroup.getWord1Id());
                if (addNewWord(set, word.get(), translation.get())) {
                    totalWords++;
                }
            }
        } finally {
            testedWords.addAll(set);
        }
    }

    private boolean addNewWord(Set<TestedWord> set, Word word, Word translation) {

        for (TestedWord testedWord : set) {
            if (testedWord.getWord2().getText().equals(translation.getText().trim())) {
                if (testedWord.getWord1().getText().indexOf(word.getText().trim()) < 0) {
                    testedWord.getWord1().setText(testedWord.getWord1().getText() + ", " + word.getText());
                }
                return false;
            }
        }
        set.add(new TestedWord(word, translation));
        return true;

    }

    /**
     * Removes the learned word.
     *
     * @param testedWord the tested word
     */
    public void removeLearnedWord(TestedWord testedWord) {
        testedWords.remove(testedWord);
    }

    /**
     * Adds the answer.
     *
     * @param correctAnswer the correct answer
     */
    public void addAnswer(boolean correctAnswer) {
        ++totalAnswers;
        Word word = getCurrentTestWord().getWord2();
        Date date = new Date();
        if (correctAnswer) {
            ++totalCorrectAnswers;
            wordService.updateWordCount(word.getId(), date, true);
            word.addCorrectCount();
            word.setCorrectTime(date);
        } else {
            wordService.updateWordCount(word.getId(), date, false);
            word.addIncorrectCount();
            word.setIncorrectTime(date);
        }
    }

    /**
     * Gets the total answers.
     *
     * @return the total answers
     */
    public int getTotalAnswers() {
        return totalAnswers;
    }

    /**
     * Gets the total correct answers.
     *
     * @return the total correct answers
     */
    public int getTotalCorrectAnswers() {
        return totalCorrectAnswers;
    }

    /**
     * Gets the rest count.
     *
     * @return the rest count
     */
    public int getRestCount() {
        return totalWords - testedWords.size();
    }

    /**
     * Gets the percentage.
     *
     * @return the percentage
     */
    public String getPercentage() {
        if (totalCorrectAnswers == 0 || totalAnswers == 0) {
            return "0";
        }
        return new DecimalFormat("###").format((totalCorrectAnswers) / (((double) totalAnswers) / 100));
    }

    /**
     * Adds the incorrect word.
     *
     * @param testedWord the tested word
     */
    public void addIncorrectWord(TestedWord testedWord) {
        incorrectWords.add(testedWord);

    }

    /**
     * Gets the total words.
     *
     * @return the total words
     */
    public int getTotalWords() {
        return totalWords;
    }

    /**
     * Creates the incorrect word file.
     */
    public void createIncorrectLesson() {
        if (fileName.endsWith("_CHYBY") || incorrectWords == null || incorrectWords.isEmpty()) {
            return;
        }
        Lesson lessonNew = new Lesson();
        lessonNew.setId(GuiGenerator.generateId());
        lessonNew.setName(this.lesson.getName() + "_CHYBY");
        lessonNew.setTotalCount(incorrectWords.size());
        lessonNew.setCorrectCount(0);
        lessonNew.setLessonGroupId(lesson.getLessonGroupId());
        Lesson lessonOld = lessonService.findLesson(lessonNew.getName());
        if (lessonOld != null) {
            lessonService.deleteLesson(lessonOld);
        }

        lessonService.persist(lessonNew);

        if (!incorrectWords.isEmpty()) {
            for (TestedWord word : incorrectWords) {
                Long word1Id = word.getWord2().getId();
                // czech many words
                String[] words = word.getWord1().getText().split(",");
                for (int i = 0; i < words.length; i++) {
                    Word w = wordService.findWord(words[i].trim(), "cs");
                    if (w != null) {
                        Long czechWordId = w.getId();
                        WordGroup wordGroup = new WordGroup();
                        wordGroup.setId(GuiGenerator.generateId());
                        wordGroup.setLessonId(lessonNew.getId());
                        wordGroup.setWord1Id(word1Id);
                        wordGroup.setWord2Id(czechWordId);
                        wordGroupService.persist(wordGroup);
                    }
                }
            }
        }
    }

    /**
     * Gets the lesson.
     *
     * @return the lesson
     */
    public Lesson getLesson() {
        return lesson;
    }

    /**
     * Creates the result file.
     */
    public void updateLessonStats() {
        lesson.setCheckTime(new Date());
        lesson.setTotalCount(getTotalWords());
        lesson.setCorrectCount(getTotalWords() - incorrectWords.size());
        lessonService.persist(lesson);
        LessonCheck lessonCheck = new LessonCheck();
        lessonCheck.setId(GuiGenerator.generateId());
        lessonCheck.setLessonId(getLesson().getId());
        lessonCheck.setTime(new Date());
        lessonCheck.setTotalCount(getTotalWords());
        lessonCheck.setCorrectCount(getTotalWords() - incorrectWords.size());
        lessonCheck.setDuration(System.currentTimeMillis() - startCheckTime);
        lessonCheckService.persist(lessonCheck);

    }

    /**
     * Gets the file name.
     *
     * @return the file name
     */
    public String getFileName() {
        return fileName;
    }

}
