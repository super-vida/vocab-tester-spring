package cz.prague.vida.vocab;

import cz.prague.vida.vocab.model.Lesson;
import cz.prague.vida.vocab.model.LessonCheck;
import cz.prague.vida.vocab.model.TestedWord;
import cz.prague.vida.vocab.repository.VocabHolderDB;
import cz.prague.vida.vocab.service.LessonCheckService;
import cz.prague.vida.vocab.service.LessonService;
import cz.prague.vida.vocab.service.WordService;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
@ManagedBean
@ViewScoped
public class LessonBean {
    private final LessonService lessonService;
    private final WordService wordService;
    private final LessonCheckService lessonCheckService;
    private Lesson selectedLesson;
    private final VocabHolderDB vocabHolder;
    private String testWord;
    private String translation;
    private String result;
    private List<Lesson> lessons = null;
    private List<LessonCheck> lessonChecks;
    private String translationStyle;
    private String wordTotalVocabularyCorrect;
    private String wordTotalVocabulary;
    private String wordTotalDistincVocabulary;
    private String lessonCount;
    private String wordTotalCorrect;
    private String wordTotalIncorrect;
    private String lastCorrectAnswer;
    private String lastIncorrectAnswer;
    private String lessonTotalCount;
    private String lessonTotalCorrect;
    private String wordLesson;
    private boolean translationEditable;

    @Autowired
    public LessonBean(LessonService lessonService, LessonCheckService lessonCheckService, WordService wordService, VocabHolderDB vocabHolder) {
        this.lessonService = lessonService;
        this.lessonCheckService = lessonCheckService;
        this.vocabHolder = vocabHolder;
        this.wordService = wordService;
    }

    @PostConstruct
    public void init() {

    }

    public String getLessonTotalCorrect() {
        return lessonTotalCorrect;
    }

    public void setLessonTotalCorrect(String lessonTotalCorrect) {
        this.lessonTotalCorrect = lessonTotalCorrect;
    }

    public String getLessonTotalCount() {
        return lessonTotalCount;
    }

    public void setLessonTotalCount(String lessonTotalCount) {
        this.lessonTotalCount = lessonTotalCount;
    }

    public void setTestWord(String testWord) {
        this.testWord = testWord;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getWordTotalCorrect() {
        return wordTotalCorrect;
    }

    public void setWordTotalCorrect(String wordTotalCorrect) {
        this.wordTotalCorrect = wordTotalCorrect;
    }

    public String getWordTotalIncorrect() {
        return wordTotalIncorrect;
    }

    public void setWordTotalIncorrect(String wordTotalIncorrect) {
        this.wordTotalIncorrect = wordTotalIncorrect;
    }

    public String getLastCorrectAnswer() {
        return lastCorrectAnswer;
    }

    public void setLastCorrectAnswer(String lastCorrectAnswer) {
        this.lastCorrectAnswer = lastCorrectAnswer;
    }

    public String getLastIncorrectAnswer() {
        return lastIncorrectAnswer;
    }

    public void setLastIncorrectAnswer(String lastIncorrectAnswer) {
        this.lastIncorrectAnswer = lastIncorrectAnswer;
    }

    public String getResult() {
        return result;
    }

    public List<Lesson> getLessons() {
        if (lessons == null) {
            initLessons();
            fillTotalVocabStats();
        }
        return lessons;

    }

    private void initLessons() {
        lessons = lessonService.getAllLessons();
    }

    public List<LessonCheck> getLessonChecks() {
        if (selectedLesson != null) {
            if (lessonChecks == null) {
                initLessonChecks();
            }
            return lessonChecks;
        }
        return Collections.emptyList();
    }

    private void initLessonChecks() {
        lessonChecks = lessonCheckService.findByLessonId(selectedLesson.getId());
    }

    public Lesson getSelectedLesson() {
        return selectedLesson;
    }

    public void setSelectedLesson(Lesson selectedLesson) {
        this.selectedLesson = selectedLesson;
    }

    public void onRowSelect(SelectEvent<Lesson> event) {
        this.selectedLesson = event.getObject();
        initLessonChecks();
        getStartLessonTest();
    }

    public void onRowUnselect(SelectEvent<Lesson> event) {
        this.selectedLesson = null;
    }

    public String getStartLessonTest() {
        System.out.println("Je to tady");
        cleanOldValues();
//        Lesson lesson = tableView.getSelectionModel().getSelectedItem();
        if (selectedLesson == null) {
            return null;
        }
        //      vocabHolder = new VocabHolderDB();
        vocabHolder.loadVocabularyFile(selectedLesson.getName(), selectedLesson);
        vocabHolder.shuffleWords();
        TestedWord testedWord = vocabHolder.getCurrentTestWord();
//        textFieldTranslation.requestFocus();
        testWord = testedWord.getWord1().getText();
        lessonTotalCount = String.valueOf(vocabHolder.getTotalWords());
        setWordTotalStatistic(testedWord);
        translationEditable = true;
//        textFieldTranslation.requestFocus();
        loadLessonHistory(selectedLesson);
        return null;
    }

    private void setWordTotalStatistic(TestedWord testedWord) {
        wordTotalCorrect = String.valueOf(testedWord.getWord2().getCorrectCount());
        wordTotalIncorrect = String.valueOf(testedWord.getWord2().getIncorrectCount());
        lastCorrectAnswer = formatDate(testedWord.getWord2().getCorrectTime());
        lastIncorrectAnswer = formatDate(testedWord.getWord2().getIncorrectTime());
    }

    private String formatDate(Date date) {
        if (date != null) {
            return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(date);
        }
        return "";
    }

    private void loadLessonHistory(Lesson lesson) {
        translation = "";
        result = "";
    }

    private void cleanOldValues() {

    }

    public String getTestWord() {
        return this.testWord;
    }

    public String getTranslation() {
        return this.translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public void handleTranslateEvent(AjaxBehaviorEvent event) {
        TestedWord testedWord = vocabHolder.getCurrentTestWord();
        if (testedWord.getWord2().getText().equalsIgnoreCase(translation)) {
            setTranslationStyle("color: green;text-align:center;");
            result = testedWord.getWord2().getText();
        } else if (testedWord.getWord2().getText().toLowerCase().startsWith(translation.toLowerCase()) && translation.length() <= testedWord.getWord2().getText().length()) {
            setTranslationStyle("color: black;text-align:center;");
        } else if (checkIfTranslationMatchWholeWord(testedWord)) {
            setTranslationStyle("color: green;text-align:center;");
            result = testedWord.getWord1().getText();
        } else if (checkIfTranslationStartsWithWord(testedWord)) {
            setTranslationStyle("color: black;text-align:center;");
        } else {
            setTranslationStyle("color: red;text-align:center;");

        }
    }

    private boolean checkIfTranslationMatchWholeWord(TestedWord testedWord) {
        String[] words = testedWord.getWord1().getText().split(",");
        for (int i = 0; i < words.length; i++) {
            if (words[i].equalsIgnoreCase(translation)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkIfTranslationStartsWithWord(TestedWord testedWord) {
        String[] words = testedWord.getWord1().getText().split(",");
        for (int i = 0; i < words.length; i++) {
            if (words[i].toLowerCase().startsWith(translation.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public boolean isTranslationEditable() {
        return translationEditable;
    }

    public void setTranslationEditable(boolean translationEditable) {
        this.translationEditable = translationEditable;
    }

    public String action() {

        if (!translationEditable || processCheck()) {
            translationEditable = true;
            result = "";
            translation = "";
            testWord = "";
            if (vocabHolder.prepareNextPair()) {
                TestedWord testedWord = vocabHolder.getCurrentTestWord();
                testWord = getTestText();
                lessonTotalCorrect = String.valueOf(vocabHolder.getTotalCorrectAnswers());
                wordLesson = testedWord.getPercentage() + "% " + testedWord.getAnswers() + "x (" + testedWord.getCorrectAnswers() + "/" + 1 + ")";
                setWordTotalStatistic(testedWord);
            } else {
                result = "KONEC";
                translationEditable = false;
                lessonTotalCorrect = vocabHolder.getPercentage() + "% (" + vocabHolder.getTotalCorrectAnswers() + "/" + vocabHolder.getTotalAnswers() + ")";
                vocabHolder.updateLessonStats();
                vocabHolder.createIncorrectLesson();
                initLessons();
                initLessonChecks();
                fillTotalVocabStats();

            }
        }

        return null;
    }

    public String getWordLesson() {
        return wordLesson;
    }

    public void setWordLesson(String wordLesson) {
        this.wordLesson = wordLesson;
    }

    private void fillTotalVocabStats() {
        wordTotalVocabularyCorrect = String.valueOf(wordService.getWordTotalCorrectCount());
        wordTotalVocabulary = String.valueOf(wordService.getWordTotalCount());
        wordTotalDistincVocabulary = String.valueOf(wordService.getDistinctCount());
        lessonCount = String.valueOf(lessonService.getLessonCount());
    }

    public String getWordTotalVocabularyCorrect() {
        return wordTotalVocabularyCorrect;
    }

    public void setWordTotalVocabularyCorrect(String wordTotalVocabularyCorrect) {
        this.wordTotalVocabularyCorrect = wordTotalVocabularyCorrect;
    }

    public String getWordTotalVocabulary() {
        return wordTotalVocabulary;
    }

    public void setWordTotalVocabulary(String wordTotalVocabulary) {
        this.wordTotalVocabulary = wordTotalVocabulary;
    }

    public String getWordTotalDistincVocabulary() {
        return wordTotalDistincVocabulary;
    }

    public void setWordTotalDistincVocabulary(String wordTotalDistincVocabulary) {
        this.wordTotalDistincVocabulary = wordTotalDistincVocabulary;
    }

    public String getLessonCount() {
        return lessonCount;
    }

    public void setLessonCount(String lessonCount) {
        this.lessonCount = lessonCount;
    }

    private String getTestText() {
        return vocabHolder.getCurrentTestWord().getWord1().getText();
    }


    private boolean processCheck() {
        TestedWord testedWord = vocabHolder.getCurrentTestWord();
        if (check(testedWord)) {
            testedWord.addAnswer(true);
            vocabHolder.addAnswer(true);
            if (testedWord.getCorrectAnswers() == 1) {
                vocabHolder.removeLearnedWord(testedWord);
            }
            return true;
        } else {
            setTranslationStyle("color: red;text-align:center;");
            translationEditable = false;
            result = testedWord.getWord2().getText();
            testedWord.addAnswer(false);
            vocabHolder.addAnswer(false);
            vocabHolder.addIncorrectWord(testedWord);
            return false;
        }

    }

    private boolean check(TestedWord testedWord) {
        if (testedWord.getWord2().getText().equalsIgnoreCase(translation)) {
            return true;
        } else {
            String[] words = testedWord.getWord1().getText().split(",");
            for (int i = 0; i < words.length; i++) {
                if (words[i].equalsIgnoreCase(translation)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getTranslationStyle() {
        return translationStyle;
    }

    public void setTranslationStyle(String translationStyle) {
        this.translationStyle = translationStyle;
    }
}