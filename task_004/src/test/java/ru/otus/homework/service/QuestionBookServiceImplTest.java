package ru.otus.homework.service;

import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import ru.otus.homework.domain.*;
import ru.otus.homework.exceptions.BusinessException;
import ru.otus.homework.loaders.Loader;
import ru.otus.homework.parsers.Parser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

@DisplayName("Класс QuestionBookServiceImpl")
@ExtendWith(MockitoExtension.class)
public class QuestionBookServiceImplTest {

    @Mock
    private Loader loader;
    @Mock
    private Parser parser;
    @Mock
    private MessageSource messageSource;

    private InputStream in;
    private ByteArrayOutputStream outByte;
    private PrintStream out;

    @InjectMocks
    private QuestionBookServiceImpl questionBookService;

    private final InputStream consoleInputStream = System.in;
    private final PrintStream consoleOutputStream = System.out;

    @AfterEach
    void restoreDefault() {

        try {
            if(in != null) {
                in.close();
            }
            if(outByte != null) {
                outByte.close();
            }
            if(out != null) {
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.setIn(consoleInputStream);
        System.setOut(consoleOutputStream);
    }

    @DisplayName("метод prepareQuestion подготовит все вопросы для вывода")
    @Test
    void prepareQuestionTest() throws Exception {

        QuestionBook questionBook = getQuestionBook();

        Integer questionNumber = 1;

        for(Question question : questionBook.getQuestions()) {

            String str = questionBookService.prepareQuestion(questionNumber, question);

            assertAll(str,
                    () -> str.contains(question.getText()),
                    () -> str.contains(question.getType().getName()),
                    () -> str.contains(question.getType().getQuestionExample()),
                    () -> str.contains(question.getType().getQuestionHint()),
                    () -> str.contains("Our answer: ")
            );

            questionNumber++;

        }

    }

    @DisplayName("метод prepareQuestion не выведет ответ для вопроса с вводом ответа")
    @Test
    void prepareQuestionTest_TypingQuestionWithoutAnswer() throws Exception {

        QuestionBook questionBook = getQuestionBook();
        Question question = questionBook.getQuestions().get(0);
        List<Answer> answers = question.getAnswers();
        Answer answer = answers.get(0);
        String str = questionBookService.prepareQuestion(1, question);

        assertThat(str).doesNotContainIgnoringCase("1." + answer.getText());

    }

    @DisplayName("метод prepareQuestion выведет ответы для вопроса с единичным выбором")
    @Test
    void prepareQuestionTest_SingleOptionQuestionHasAnswers() throws Exception {

        QuestionBook questionBook = getQuestionBook();
        Question question = questionBook.getQuestions().get(1);
        getLocalizedMessageQuestionFull2();

        String str = questionBookService.prepareQuestion(1, question);

        Integer answerNumber = 1;
        for(Answer answer : question.getAnswers()) {
            assertThat(str).containsIgnoringCase(answerNumber + "." + questionBookService.getLocalizedMessage(answer.getText()));
            answerNumber++;
        }

    }

    @DisplayName("метод prepareQuestion выведет ответы для вопроса с множественным выбором")
    @Test
    void prepareQuestionTest_MultyOptionQuestionHasAnswers() throws Exception {

        QuestionBook questionBook = getQuestionBook();
        Question question = questionBook.getQuestions().get(2);
        getLocalizedMessageQuestionFull3();

        String str = questionBookService.prepareQuestion(1, question);

        Integer answerNumber = 1;
        for(Answer answer : question.getAnswers()) {
            assertThat(str).containsIgnoringCase(answerNumber + "." + questionBookService.getLocalizedMessage(answer.getText()));
            answerNumber++;
        }

    }

    @DisplayName("метод printQuestionBook выведет список вопросов")
    @Test
    public void printQuestionBookTest() throws Exception {

        List<String> questions = getQuestions();
        given(loader.loadQuestions(any())).willReturn(questions);
        given(parser.getQuestionBook(any())).willReturn(getQuestionBook());
        configurateEnvironment("\n");

        questionBookService.printQuestionBook(true);

        String str = outByte.toString();

        assertAll(str,
                () -> str.contains("Current capital of Russia?"),
                () -> str.contains("JavaScript is language for?"),
                () -> str.contains("Mark languages and frameworks for current course ..."),
                () -> str.contains("Print name of company for which course customized for?"),
                () -> str.contains("Select name of education company?")
        );

    }

    @DisplayName("метод printQuestionBook присвоит номер каждому вопросу")
    @Test
    public void printQuestionBookTest_addQuestionNumbers() throws Exception {

        List<String> questions = getQuestions();
        given(loader.loadQuestions(any())).willReturn(questions);
        given(parser.getQuestionBook(any())).willReturn(getQuestionBook());
        configurateEnvironment("\n");

        questionBookService.printQuestionBook(true);

        String str = outByte.toString();

        assertAll(str,
                () -> str.contains("1.Current capital of Russia?"),
                () -> str.contains("2.JavaScript is language for?"),
                () -> str.contains("3.Mark languages and frameworks for current course ..."),
                () -> str.contains("4.Print name of company for which course customized for?"),
                () -> str.contains("5.Select name of education company?")
        );

    }

    @DisplayName("метод validateQuestionBook свалидирует QuestionBook без ошибок")
    @Test
    void validateQuestionBook_QuestionBookValid() throws Exception {

        Boolean validationFlag = questionBookService.validateQuestionBook(getQuestionBook());
        Assert.assertTrue(validationFlag);
    }

    @DisplayName("метод validateQuestionBook не свалидирует QuestionBook с нулевым списком вопросов")
    @Test
    void validateQuestionBook_NullQuestionsUnvalid() throws Exception {

        QuestionBook questionBook = new QuestionBook();
        Boolean validationFlag = questionBookService.validateQuestionBook(questionBook);
        Assert.assertFalse(validationFlag);
    }

    @DisplayName("метод validateQuestionBook не свалидирует QuestionBook с пустым списком вопросов")
    @Test
    void validateQuestionBook_EmptyQuestionsUnvalid() throws Exception {

        QuestionBook questionBook = new QuestionBook();
        questionBook.setQuestions(new ArrayList<>());
        Boolean validationFlag = questionBookService.validateQuestionBook(questionBook);
        Assert.assertFalse(validationFlag);
    }

    @DisplayName("метод validateQuestionBook не свалидирует QuestionBook с нулевым вопросом")
    @Test
    void validateQuestionBook_NullQuestionTextUnvalid() throws Exception {

        ArrayList<Answer> answers = new ArrayList<>();
        answers.add(new Answer("Moscow", true));
        answers.add(new Answer("Yaroslavl", false));

        Question question = new Question();
        question.setText(null);
        question.setType(QuestionTypes.TYPING);
        question.setAnswers(answers);

        ArrayList<Question> questions = new ArrayList<>();
        questions.add(question);

        QuestionBook testQuestionBook = new QuestionBook();
        testQuestionBook.setQuestions(questions);

        Assert.assertFalse(questionBookService.validateQuestionBook(testQuestionBook));

    }

    @DisplayName("метод validateQuestionBook не свалидирует QuestionBook с пустым вопросом")
    @Test
    void validateQuestionBook_EmptyQuestionTextUnvalid() throws Exception {

        ArrayList<Answer> answers = new ArrayList<>();
        answers.add(new Answer("Moscow", true));
        answers.add(new Answer("Yaroslavl", false));

        Question question = new Question();
        question.setText("");
        question.setType(QuestionTypes.TYPING);
        question.setAnswers(answers);

        ArrayList<Question> questions = new ArrayList<>();
        questions.add(question);

        QuestionBook testQuestionBook = new QuestionBook();
        testQuestionBook.setQuestions(questions);

        Assert.assertFalse(questionBookService.validateQuestionBook(testQuestionBook));

    }

    @DisplayName("метод validateQuestionBook не свалидирует QuestionBook с пустым вопросом без учёта пробелов")
    @Test
    void validateQuestionBook_EmptyQuestionTextIgnoreSpacesUnvalid() throws Exception {

        ArrayList<Answer> answers = new ArrayList<>();
        answers.add(new Answer("Moscow", true));
        answers.add(new Answer("Yaroslavl", false));

        Question question = new Question();
        question.setText("  ");
        question.setType(QuestionTypes.TYPING);
        question.setAnswers(answers);

        ArrayList<Question> questions = new ArrayList<>();
        questions.add(question);

        QuestionBook testQuestionBook = new QuestionBook();
        testQuestionBook.setQuestions(questions);

        Assert.assertFalse(questionBookService.validateQuestionBook(testQuestionBook));

    }

    @DisplayName("метод validateQuestionBook не свалидирует QuestionBook с нулевым типом вопроса")
    @Test
    void validateQuestionBook_NullQuestionTypeUnvalid() throws Exception {

        ArrayList<Answer> answers = new ArrayList<>();
        answers.add(new Answer("Moscow", true));
        answers.add(new Answer("Yaroslavl", false));

        Question question = new Question();
        question.setText("Current capital of Russia?");
        question.setType(null);
        question.setAnswers(answers);

        ArrayList<Question> questions = new ArrayList<>();
        questions.add(question);

        QuestionBook testQuestionBook = new QuestionBook();
        testQuestionBook.setQuestions(questions);

        Assert.assertFalse(questionBookService.validateQuestionBook(testQuestionBook));

    }

    @DisplayName("метод validateQuestionBook не свалидирует QuestionBook с нулевым ответом")
    @Test
    void validateQuestionBook_NullAnswerQuestionUnvalid() throws Exception {

        ArrayList<Answer> answers = new ArrayList<>();
        answers.add(new Answer(null, true));

        Question question = new Question();
        question.setText("Current capital of Russia?");
        question.setType(QuestionTypes.TYPING);
        question.setAnswers(answers);

        ArrayList<Question> questions = new ArrayList<>();
        questions.add(question);

        QuestionBook testQuestionBook = new QuestionBook();
        testQuestionBook.setQuestions(questions);

        Assert.assertFalse(questionBookService.validateQuestionBook(testQuestionBook));

    }

    @DisplayName("метод validateQuestionBook не свалидирует QuestionBook с пустым ответом")
    @Test
    void validateQuestionBook_EmptyAnswerQuestionUnvalid() throws Exception {

        ArrayList<Answer> answers = new ArrayList<>();
        answers.add(new Answer("", true));

        Question question = new Question();
        question.setText("Current capital of Russia?");
        question.setType(QuestionTypes.TYPING);
        question.setAnswers(answers);

        ArrayList<Question> questions = new ArrayList<>();
        questions.add(question);

        QuestionBook testQuestionBook = new QuestionBook();
        testQuestionBook.setQuestions(questions);

        Assert.assertFalse(questionBookService.validateQuestionBook(testQuestionBook));

    }

    @DisplayName("метод validateQuestionBook не свалидирует QuestionBook с пустым ответом без учёта пробелов")
    @Test
    void validateQuestionBook_EmptyAnswerQuestionIgnoreSpacesUnvalid() throws Exception {

        ArrayList<Answer> answers = new ArrayList<>();
        answers.add(new Answer("   ", true));

        Question question = new Question();
        question.setText("Current capital of Russia?");
        question.setType(QuestionTypes.TYPING);
        question.setAnswers(answers);

        ArrayList<Question> questions = new ArrayList<>();
        questions.add(question);

        QuestionBook testQuestionBook = new QuestionBook();
        testQuestionBook.setQuestions(questions);

        Assert.assertFalse(questionBookService.validateQuestionBook(testQuestionBook));

    }

    @DisplayName("метод validateQuestionBook не свалидирует QuestionBook с нулевым признаком правильности ответа")
    @Test
    void validateQuestionBook_NullAnswerIsValidQuestionUnvalid() throws Exception {

        ArrayList<Answer> answers = new ArrayList<>();
        answers.add(new Answer("Moscow", null));

        Question question = new Question();
        question.setText("Current capital of Russia?");
        question.setType(QuestionTypes.TYPING);
        question.setAnswers(answers);

        ArrayList<Question> questions = new ArrayList<>();
        questions.add(question);

        QuestionBook testQuestionBook = new QuestionBook();
        testQuestionBook.setQuestions(questions);

        Assert.assertFalse(questionBookService.validateQuestionBook(testQuestionBook));

    }

    @DisplayName("метод validateQuestionBook не свалидирует QuestionBook c двумя правильными ответами в вопросе с вводом ответа")
    @Test
    void validateQuestionBook_TotalValidTypingAnswerCountUnvalid() throws Exception {

        ArrayList<Answer> answers = new ArrayList<>();
        answers.add(new Answer("Moscow", true));
        answers.add(new Answer("Yaroslavl", false));

        Question question = new Question();
        question.setText("Current capital of Russia?");
        question.setType(QuestionTypes.TYPING);
        question.setAnswers(answers);

        ArrayList<Question> questions = new ArrayList<>();
        questions.add(question);

        QuestionBook testQuestionBook = new QuestionBook();
        testQuestionBook.setQuestions(questions);

        Assert.assertFalse(questionBookService.validateQuestionBook(testQuestionBook));

    }

    @DisplayName("метод validateQuestionBook не свалидирует QuestionBook c двумя правильными ответами в вопросе с выбором единственной опции")
    @Test
    void validateQuestionBook_TotalValidSingleAnswerCountUnvalid() throws Exception {

        ArrayList<Answer> answers = new ArrayList<>();
        answers.add(new Answer("Only for frontend", true));
        answers.add(new Answer("Only for backend", false));
        answers.add(new Answer("Frontend and backend", true));

        Question question = new Question();
        question.setText("JavaScript is language for?");
        question.setType(QuestionTypes.SINGLE);
        question.setAnswers(answers);

        ArrayList<Question> questions = new ArrayList<>();
        questions.add(question);

        QuestionBook testQuestionBook = new QuestionBook();
        testQuestionBook.setQuestions(questions);

        Assert.assertFalse(questionBookService.validateQuestionBook(testQuestionBook));

    }

    @DisplayName("метод validateQuestionBook не свалидирует QuestionBook c без неправильного ответа в вопросе с выбором единственной опции")
    @Test
    void validateQuestionBook_TotalUnvalidSingleAnswerCountUnvalid() throws Exception {

        ArrayList<Answer> answers = new ArrayList<>();
        answers.add(new Answer("Only for frontend", true));
        answers.add(new Answer("Only for backend", true));
        answers.add(new Answer("Frontend and backend", true));

        Question question = new Question();
        question.setText("JavaScript is language for?");
        question.setType(QuestionTypes.SINGLE);
        question.setAnswers(answers);

        ArrayList<Question> questions = new ArrayList<>();
        questions.add(question);

        QuestionBook testQuestionBook = new QuestionBook();
        testQuestionBook.setQuestions(questions);

        Assert.assertFalse(questionBookService.validateQuestionBook(testQuestionBook));

    }

    @DisplayName("метод validateQuestionBook не свалидирует QuestionBook с одним правильным ответом в вопросе с выбором множества опции")
    @Test
    void validateQuestionBook_TotalValidMultyAnswerCountUnvalid() throws Exception {

        ArrayList<Answer> answers = new ArrayList<>();
        answers.add(new Answer("Java", true));
        answers.add(new Answer("Angular", false));
        answers.add(new Answer("Spring", false));
        answers.add(new Answer("TypeScript", false));

        Question question = new Question();
        question.setText("Mark languages and frameworks for current course ...");
        question.setType(QuestionTypes.MULTY);
        question.setAnswers(answers);

        ArrayList<Question> questions = new ArrayList<>();
        questions.add(question);

        QuestionBook testQuestionBook = new QuestionBook();
        testQuestionBook.setQuestions(questions);

        Assert.assertFalse(questionBookService.validateQuestionBook(testQuestionBook));

    }

    @DisplayName("метод validateQuestionBook не свалидирует QuestionBook без неправильного ответа в вопросе с выбором множества опции")
    @Test
    void validateQuestionBook_TotalUnvalidMultyAnswerCountUnvalid() throws Exception {

        ArrayList<Answer> answers = new ArrayList<>();
        answers.add(new Answer("Java", true));
        answers.add(new Answer("Angular", true));
        answers.add(new Answer("Spring", true));
        answers.add(new Answer("TypeScript", true));

        Question question = new Question();
        question.setText("Mark languages and frameworks for current course ...");
        question.setType(QuestionTypes.MULTY);
        question.setAnswers(answers);

        ArrayList<Question> questions = new ArrayList<>();
        questions.add(question);

        QuestionBook testQuestionBook = new QuestionBook();
        testQuestionBook.setQuestions(questions);

        Assert.assertFalse(questionBookService.validateQuestionBook(testQuestionBook));

    }

    @DisplayName("метод performTesting проведёт тест с правильными ответами на все вопросы")
    @Test
    public void performTestingTest_AllAnswersValid() throws Exception {

        List<String> questions = getQuestions();
        given(loader.loadQuestions(any())).willReturn(questions);
        given(parser.getQuestionBook(any())).willReturn(getQuestionBook());
        questionBookService.setValidAnswerMinCount(4);
        getLocalizedMessagePerformTest();
        getLocalizedMessagePrepareStudentInfo();
        getLocalizedMessagePreparePositiveResult();

        outByte = configurateEnvironment("\nAnton\nElshin\nMoscow\n3\n1 3\nDiasoft\n3");

        questionBookService.performTesting(true);

        String str = outByte.toString();

        assertAll(str,
                () -> str.contains("Test result: 5 valid answers of 5 questions"),
                () -> str.contains("Test passed successfully!")
        );

    }

    @DisplayName("метод performTesting при ответе на вопросы теста игнорирует пробелы справа и слева от значимого текста")
    @Test
    public void performTestingTest_AnswerWithSpaceProcessedCorrect() throws Exception {

        List<String> questions = getQuestions();
        given(loader.loadQuestions(any())).willReturn(questions);
        given(parser.getQuestionBook(any())).willReturn(getQuestionBook());
        questionBookService.setValidAnswerMinCount(4);
        getLocalizedMessagePerformTest();
        getLocalizedMessagePrepareStudentInfo();
        getLocalizedMessagePreparePositiveResult();

        outByte = configurateEnvironment("\nAnton\nElshin\n   Moscow   \n 3 \n  1  3  \n  Diasoft  \n  3  ");

        questionBookService.performTesting(true);

        String str = outByte.toString();

        assertAll(str,
                () -> str.contains("Test result: 5 valid answers of 5 questions"),
                () -> str.contains("Test passed successfully!")
        );

    }

    @DisplayName("метод performTesting корректно обрабатывает дубли ответов на вопрос, предполагающий несколько ответов")
    @Test
    public void performTestingTest_MultyAnswerWithSpaceAndDuplication() throws Exception {

        List<String> questions = getQuestions();
        given(loader.loadQuestions(any())).willReturn(questions);
        given(parser.getQuestionBook(any())).willReturn(getQuestionBook());
        questionBookService.setValidAnswerMinCount(4);
        getLocalizedMessagePerformTest();
        getLocalizedMessagePrepareStudentInfo();
        getLocalizedMessagePreparePositiveResult();

        String answers = "Anton\nElshin\nMoscow\n3\n1 3 3 1\nDiasoft\n3";

        outByte = configurateEnvironment("\nAnton\nElshin\nMoscow\n3\n1 3 3 1\nDiasoft\n3");

        questionBookService.performTesting(true);

        String str = outByte.toString();

        assertAll(str,
                () -> str.contains("Test result: 5 valid answers of 5 questions"),
                () -> str.contains("Test passed successfully!")
        );

    }

    @DisplayName("метод performTesting корректно обрабатывает произвольный порядок ответов на вопрос с несколькими ответами")
    @Test
    public void performTestingTest_MultyInverseAnswer() throws Exception {

        List<String> questions = getQuestions();
        given(loader.loadQuestions(any())).willReturn(questions);
        given(parser.getQuestionBook(any())).willReturn(getQuestionBook());
        questionBookService.setValidAnswerMinCount(4);
        getLocalizedMessagePerformTest();
        getLocalizedMessagePrepareStudentInfo();
        getLocalizedMessagePreparePositiveResult();

        outByte = configurateEnvironment("\nAnton\nElshin\nMoscow\n3\n3 1\nDiasoft\n3");

        questionBookService.performTesting(true);

        String str = outByte.toString();

        assertAll(str,
                () -> str.contains("Test result: 5 valid answers of 5 questions"),
                () -> str.contains("Test passed successfully!")
        );

    }

    @DisplayName("метод performTesting не засчитывает правильный ответ при указании ошибочного ответа вместе с правильными для множественного ответа")
    @Test
    public void performTestingTest_MultyAnswerIncorrectWithOneUnvalidOption() throws Exception {

        List<String> questions = getQuestions();
        given(loader.loadQuestions(any())).willReturn(questions);
        given(parser.getQuestionBook(any())).willReturn(getQuestionBook());
        questionBookService.setValidAnswerMinCount(4);
        getLocalizedMessagePerformTest();
        getLocalizedMessagePrepareStudentInfo();
        getLocalizedMessagePreparePositiveResult();

        outByte = configurateEnvironment("\nAnton\nElshin\nMoscow\n3\n1 3 4\nDiasoft\n3");

        questionBookService.performTesting(true);

        String str = outByte.toString();

        assertAll(str,
                () -> str.contains("Test result: 4 valid answers of 5 questions"),
                () -> str.contains("Test passed successfully!")
        );

    }

    @DisplayName("метод performTesting не засчитывает правильный ответ при указании ошибочного ответа вместе с правильными для единичного ответа")
    @Test
    public void performTestingTest_SingleTwoAnswerWithOneValidIncorrect() throws Exception {

        List<String> questions = getQuestions();
        given(loader.loadQuestions(any())).willReturn(questions);
        given(parser.getQuestionBook(any())).willReturn(getQuestionBook());
        questionBookService.setValidAnswerMinCount(4);
        getLocalizedMessagePerformTest();
        getLocalizedMessagePrepareStudentInfo();
        getLocalizedMessagePreparePositiveResult();

        outByte = configurateEnvironment("\nAnton\nElshin\nMoscow\n1 3\n1 3\nDiasoft\n3");

        questionBookService.performTesting(true);

        String str = outByte.toString();

        assertAll(str,
                () -> str.contains("Test result: 4 valid answers of 5 questions"),
                () -> str.contains("Test passed successfully!")
        );

    }

    @DisplayName("метод performTesting корректно обрабатывает ответы на вопросы с печатью правильного ответа без учёта регистра")
    @Test
    public void performTestingTest_ignoreCaseValid() throws Exception {

        List<String> questions = getQuestions();
        given(loader.loadQuestions(any())).willReturn(questions);
        given(parser.getQuestionBook(any())).willReturn(getQuestionBook());
        questionBookService.setValidAnswerMinCount(4);
        getLocalizedMessagePerformTest();
        getLocalizedMessagePrepareStudentInfo();
        getLocalizedMessagePreparePositiveResult();

        outByte = configurateEnvironment("\nAnton\nElshin\nmoscow\n3\n1 3\ndiasoft\n3");

        questionBookService.performTesting(true);

        String str = outByte.toString();

        assertAll(str,
                () -> str.contains("Test result: 5 valid answers of 5 questions"),
                () -> str.contains("Test passed successfully!")
        );

    }

    @DisplayName("метод performTesting не выведет имя и фамилию студента, если данные не указаны")
    @Test
    public void performTestingTest_Anonymous() throws Exception {

        List<String> questions = getQuestions();
        given(loader.loadQuestions(any())).willReturn(questions);
        given(parser.getQuestionBook(any())).willReturn(getQuestionBook());
        questionBookService.setValidAnswerMinCount(4);
        getLocalizedMessagePerformTest();
        getLocalizedMessagePreparePositiveResult();

        outByte = configurateEnvironment("\n\n\nMoscow\n3\n1 3\nDiasoft\n3");

        questionBookService.performTesting(true);

        String str = outByte.toString();

        Assert.assertFalse(str.contains("Dear, "));

    }

    @DisplayName("метод performTesting не засчитает тест при указании недостаточного количества правильных ответов")
    @Test
    public void performTestingTest_TestFailed() throws Exception {

        List<String> questions = getQuestions();
        given(loader.loadQuestions(any())).willReturn(questions);
        given(parser.getQuestionBook(any())).willReturn(getQuestionBook());
        questionBookService.setValidAnswerMinCount(4);
        getLocalizedMessagePerformTest();
        getLocalizedMessagePrepareStudentInfo();
        getLocalizedMessagePrepareNegativeResult();

        outByte = configurateEnvironment("\nAnton\nElshin\nParis\n1 2\n1 3\nDiasoft\n3");

        questionBookService.performTesting(true);

        String str = outByte.toString();

        assertAll(str,
                () -> str.contains("Test result: 3 valid answers of 5 questions"),
                () -> str.contains("Test failed! Please try again ... ")
        );

    }

    @DisplayName("метод askLanguage запросит и получит язык по умолчанию для теста")
    @Test
    public void askLanguageTest_Default() {

        getLocalizedMessageAskLanguage();

        outByte = configurateEnvironment("\n");

        questionBookService.askLanguage(true);

        String str = outByte.toString();

        assertThat(str).containsIgnoringCase("Available languages:");

        Integer languageNumber = 1;
        for(Locales locale : Locales.values()) {
            assertThat(str).containsIgnoringCase(languageNumber + "." + questionBookService.getLocalizedMessage(locale.getName()));
            languageNumber++;
        }

        assertThat(str).containsIgnoringCase("Please select language number: ");

    }

    @DisplayName("метод askLanguage запросит и получит английский язык для теста")
    @Test
    public void askLanguageTest_English() {

        getLocalizedMessageAskLanguage();

        outByte = configurateEnvironment("1");

        questionBookService.askLanguage(true);

        String str = outByte.toString();

        assertThat(str).containsIgnoringCase("Available languages:");

        Integer languageNumber = 1;
        for(Locales locale : Locales.values()) {
            assertThat(str).containsIgnoringCase(languageNumber + "." + questionBookService.getLocalizedMessage(locale.getName()));
            languageNumber++;
        }

        assertThat(str).containsIgnoringCase("Please select language number: 1");

    }

    @DisplayName("метод askLanguage запросит и получит русский язык для теста")
    @Test
    public void askLanguageTest_Russian() {

        getLocalizedMessageAskLanguageRussian();

        outByte = configurateEnvironment("2");
        questionBookService.setLanguageTag("ru-RU");

        questionBookService.askLanguage(true);

        String str = outByte.toString();

        assertThat(str).containsIgnoringCase("Доступные языки:");

        Integer languageNumber = 1;
        for(Locales locale : Locales.values()) {
            assertThat(str).containsIgnoringCase(languageNumber + "." + questionBookService.getLocalizedMessage(locale.getName()));
            languageNumber++;
        }

        assertThat(str).containsIgnoringCase("Пожалуйста, выберите язык: 2");

    }

    @DisplayName("метод askStudentInfo запросит и получит имя и фамилию студента")
    @Test
    public void askStudentInfoTest() throws Exception {

        outByte = configurateEnvironment("Anton\nElshin");

        questionBookService.askStudentInfo(true);

        String str = outByte.toString();

        assertAll(str,
                () -> str.contains("Please enter your name: Anton"),
                () -> str.contains("Please enter your surname: Elshin")
        );

    }

    @DisplayName("метод askLanguage запросит и получит английский язык для теста при выборе несуществующего номера языка")
    @Test
    public void askLanguageTest_NotExistIndex() {

        getLocalizedMessageAskLanguage();

        outByte = configurateEnvironment("1000");

        questionBookService.askLanguage(true);

        String str = outByte.toString();

        assertThat(str).containsIgnoringCase("Available languages:");

        Integer languageNumber = 1;
        for(Locales locale : Locales.values()) {
            assertThat(str).containsIgnoringCase(languageNumber + "." + questionBookService.getLocalizedMessage(locale.getName()));
            languageNumber++;
        }

        assertThat(str).containsIgnoringCase("Please select language number: 1000");

    }

    @Test
    public void askLanguageTest_NotDigitInput() {

        getLocalizedMessageAskLanguage();

        outByte = configurateEnvironment("ru");

        questionBookService.askLanguage(true);

        String str = outByte.toString();

        assertThat(str).containsIgnoringCase("Available languages:");

        Integer languageNumber = 1;
        for(Locales locale : Locales.values()) {
            assertThat(str).containsIgnoringCase(languageNumber + "." + questionBookService.getLocalizedMessage(locale.getName()));
            languageNumber++;
        }

        assertThat(str).containsIgnoringCase("Please select language number: ru");

    }

    @DisplayName("метод askStudentInfo позволит не вводить имя и фамилию студента")
    @Test
    public void askStudentInfoTest_WithoutName() throws Exception {

        outByte = configurateEnvironment("\n\n");

        questionBookService.askStudentInfo(true);

        String str = outByte.toString();

        assertAll(str,
                () -> str.contains("Please enter your name: \n"),
                () -> str.contains("Please enter your surname: \n")
        );

    }

    @DisplayName("метод askQuestions задаст вопросы и получит на них ответы")
    @Test
    public void askQuestionsTest() throws Exception {

        QuestionBook questionBook = getQuestionBook();

        outByte = configurateEnvironment("Moscow\n3\n1 3\nDiasoft\n3");

        questionBookService.askQuestions(questionBook, true);

        String str = outByte.toString();

        assertAll(str,
                () -> str.contains("Current capital of Russia? Moscow"),
                () -> str.contains("JavaScript is language for?: 3"),
                () -> str.contains("Mark languages and frameworks for current course ...: 1 3"),
                () -> str.contains("Print name of company for which course customized for?: Diasoft"),
                () -> str.contains("Select name of education company?: 3")
        );

    }

    @DisplayName("метод askQuestions позволит не вводить ответы на вопросы")
    @Test
    public void askQuestionsTest_skipAnswers() throws Exception {

        QuestionBook questionBook = getQuestionBook();

        outByte = configurateEnvironment("\n\n\n\n\n");

        questionBookService.askQuestions(questionBook, true);

        String str = outByte.toString();

        assertAll(str,
                () -> str.contains("Current capital of Russia? \n"),
                () -> str.contains("JavaScript is language for?: \n"),
                () -> str.contains("Mark languages and frameworks for current course ...: \n"),
                () -> str.contains("Print name of company for which course customized for?: \n"),
                () -> str.contains("Select name of education company?: \n")
        );

    }

    @DisplayName("метод prepareResult подготовит успешный результат теста")
    @Test
    void prepareResultTest() throws BusinessException {

        questionBookService.setValidAnswerMinCount(4);
        doReturn("Moscow").when(messageSource)
                .getMessage("answers.moscow", null, Locale.forLanguageTag(""));
        doReturn("Diasoft").when(messageSource)
                .getMessage("answers.diasoft", null, Locale.forLanguageTag(""));
        getLocalizedMessagePrepareStudentInfo();
        getLocalizedMessagePreparePositiveResult();

        Student student = new Student("Anton", "Elshin");
        QuestionBook questionBook = getQuestionBook();
        List<String> questionAnswers = new ArrayList<>();
        questionAnswers.add("Moscow");
        questionAnswers.add("3");
        questionAnswers.add("1 3");
        questionAnswers.add("Diasoft");
        questionAnswers.add("3");

        String str = questionBookService.prepareResult(student, questionBook, questionAnswers);

        assertAll(str,
                () -> str.contains("Dear, " + student.getName() + " " + student.getSurname()),
                () -> str.contains("Test result: 5 valid answers of 5 questions"),
                () -> str.contains("Test passed successfully!")
        );
    }

    @DisplayName("метод prepareResult подготовит успешный результат теста без учёта регистра")
    @Test
    void prepareResultTest_IgnoreSpaces() throws BusinessException {

        questionBookService.setValidAnswerMinCount(4);
        doReturn("Moscow").when(messageSource)
                .getMessage("answers.moscow", null, Locale.forLanguageTag(""));
        doReturn("Diasoft").when(messageSource)
                .getMessage("answers.diasoft", null, Locale.forLanguageTag(""));
        getLocalizedMessagePrepareStudentInfo();
        getLocalizedMessagePreparePositiveResult();

        Student student = new Student("Anton", "Elshin");
        QuestionBook questionBook = getQuestionBook();
        List<String> questionAnswers = new ArrayList<>();
        questionAnswers.add("  Moscow  ");
        questionAnswers.add(" 3 ");
        questionAnswers.add("  1   3  ");
        questionAnswers.add("  Diasoft  ");
        questionAnswers.add("  3  ");

        String str = questionBookService.prepareResult(student, questionBook, questionAnswers);

        assertAll(str,
                () -> str.contains("Dear, " + student.getName() + " " + student.getSurname()),
                () -> str.contains("Test result: 5 valid answers of 5 questions"),
                () -> str.contains("Test passed successfully!")
        );
    }

    @DisplayName("метод prepareResult подготовит успешный результат теста без учёта регистра")
    @Test
    void prepareResultTest_IgnoreCase() throws BusinessException {

        questionBookService.setValidAnswerMinCount(4);
        doReturn("Moscow").when(messageSource)
                .getMessage("answers.moscow", null, Locale.forLanguageTag(""));
        doReturn("Diasoft").when(messageSource)
                .getMessage("answers.diasoft", null, Locale.forLanguageTag(""));
        getLocalizedMessagePrepareStudentInfo();
        getLocalizedMessagePreparePositiveResult();

        Student student = new Student("Anton", "Elshin");
        QuestionBook questionBook = getQuestionBook();
        List<String> questionAnswers = new ArrayList<>();
        questionAnswers.add("moscow");
        questionAnswers.add("3");
        questionAnswers.add("1 3");
        questionAnswers.add("diasoft");
        questionAnswers.add("3");

        String str = questionBookService.prepareResult(student, questionBook, questionAnswers);

        assertAll(str,
                () -> str.contains("Dear, " + student.getName() + " " + student.getSurname()),
                () -> str.contains("Test result: 5 valid answers of 5 questions"),
                () -> str.contains("Test passed successfully!")
        );
    }

    @DisplayName("метод prepareResult подготовит успешный результат теста без учёта дублей для единичного и множественного ответа")
    @Test
    void prepareResultTest_SingleAndMultyIgnoreDuplication() throws BusinessException {

        questionBookService.setValidAnswerMinCount(4);
        doReturn("Moscow").when(messageSource)
                .getMessage("answers.moscow", null, Locale.forLanguageTag(""));
        doReturn("Diasoft").when(messageSource)
                .getMessage("answers.diasoft", null, Locale.forLanguageTag(""));
        getLocalizedMessagePrepareStudentInfo();
        getLocalizedMessagePreparePositiveResult();

        Student student = new Student("Anton", "Elshin");
        QuestionBook questionBook = getQuestionBook();
        List<String> questionAnswers = new ArrayList<>();
        questionAnswers.add("Moscow");
        questionAnswers.add("3 3");
        questionAnswers.add("1 3 1 3");
        questionAnswers.add("Diasoft");
        questionAnswers.add("3");

        String str = questionBookService.prepareResult(student, questionBook, questionAnswers);

        assertAll(str,
                () -> str.contains("Dear, " + student.getName() + " " + student.getSurname()),
                () -> str.contains("Test result: 5 valid answers of 5 questions"),
                () -> str.contains("Test passed successfully!")
        );
    }

    @DisplayName("метод prepareResult подготовит успешный результат теста без учёта порядка в вопросе множественного выбора")
    @Test
    void prepareResultTest_MultyIgnoreInverse() throws BusinessException {

        questionBookService.setValidAnswerMinCount(4);
        doReturn("Moscow").when(messageSource)
                .getMessage("answers.moscow", null, Locale.forLanguageTag(""));
        doReturn("Diasoft").when(messageSource)
                .getMessage("answers.diasoft", null, Locale.forLanguageTag(""));
        getLocalizedMessagePrepareStudentInfo();
        getLocalizedMessagePreparePositiveResult();

        Student student = new Student("Anton", "Elshin");
        QuestionBook questionBook = getQuestionBook();
        List<String> questionAnswers = new ArrayList<>();
        questionAnswers.add("Moscow");
        questionAnswers.add("3");
        questionAnswers.add("3 1");
        questionAnswers.add("Diasoft");
        questionAnswers.add("3");

        String str = questionBookService.prepareResult(student, questionBook, questionAnswers);

        assertAll(str,
                () -> str.contains("Dear, " + student.getName() + " " + student.getSurname()),
                () -> str.contains("Test result: 5 valid answers of 5 questions"),
                () -> str.contains("Test passed successfully!")
        );
    }

    @DisplayName("метод prepareResult подготовит провальный результат теста")
    @Test
    void prepareResultTest_TestFailed() throws BusinessException {

        questionBookService.setValidAnswerMinCount(4);
        doReturn("Moscow").when(messageSource)
                .getMessage("answers.moscow", null, Locale.forLanguageTag(""));
        doReturn("Diasoft").when(messageSource)
                .getMessage("answers.diasoft", null, Locale.forLanguageTag(""));
        getLocalizedMessagePrepareStudentInfo();
        getLocalizedMessagePrepareNegativeResult();

        Student student = new Student("Anton", "Elshin");
        QuestionBook questionBook = getQuestionBook();
        List<String> questionAnswers = new ArrayList<>();
        questionAnswers.add("Paris");
        questionAnswers.add("3");
        questionAnswers.add("1 3");
        questionAnswers.add("Sberbank");
        questionAnswers.add("3");

        String str = questionBookService.prepareResult(student, questionBook, questionAnswers);

        assertAll(str,
                () -> str.contains("Dear, " + student.getName() + " " + student.getSurname()),
                () -> str.contains("Test result: 3 valid answers of 5 questions"),
                () -> str.contains("Test failed! Please try again ... ")
        );
    }

    @DisplayName("метод prepareResult выведет неуспешный результат при пустых ответах")
    @Test
    void prepareResultTest_TestFailedAllEmptyAnswers() throws BusinessException {

        questionBookService.setValidAnswerMinCount(4);
        doReturn("Moscow").when(messageSource)
                .getMessage("answers.moscow", null, Locale.forLanguageTag(""));
        doReturn("Diasoft").when(messageSource)
                .getMessage("answers.diasoft", null, Locale.forLanguageTag(""));
        getLocalizedMessagePrepareStudentInfo();
        getLocalizedMessagePrepareNegativeResult();

        Student student = new Student("Anton", "Elshin");
        QuestionBook questionBook = getQuestionBook();
        List<String> questionAnswers = new ArrayList<>();
        questionAnswers.add("");
        questionAnswers.add("");
        questionAnswers.add("");
        questionAnswers.add("");
        questionAnswers.add("");

        String str = questionBookService.prepareResult(student, questionBook, questionAnswers);

        assertAll(str,
                () -> str.contains("Dear, " + student.getName() + " " + student.getSurname()),
                () -> str.contains("Test result: 0 valid answers of 5 questions"),
                () -> str.contains("Test failed! Please try again ... ")
        );
    }

    @DisplayName("метод prepareResult выведет неуспешный результат при пустых ответах без учёта пробелов")
    @Test
    void prepareResultTest_TestFailedAllEmptyAnswersIgnoreCases() throws BusinessException {

        questionBookService.setValidAnswerMinCount(4);
        doReturn("Moscow").when(messageSource)
                .getMessage("answers.moscow", null, Locale.forLanguageTag(""));
        doReturn("Diasoft").when(messageSource)
                .getMessage("answers.diasoft", null, Locale.forLanguageTag(""));
        getLocalizedMessagePrepareStudentInfo();
        getLocalizedMessagePrepareNegativeResult();

        Student student = new Student("Anton", "Elshin");
        QuestionBook questionBook = getQuestionBook();
        List<String> questionAnswers = new ArrayList<>();
        questionAnswers.add("  ");
        questionAnswers.add("  ");
        questionAnswers.add("  ");
        questionAnswers.add("  ");
        questionAnswers.add("  ");

        String str = questionBookService.prepareResult(student, questionBook, questionAnswers);

        assertAll(str,
                () -> str.contains("Dear, " + student.getName() + " " + student.getSurname()),
                () -> str.contains("Test result: 0 valid answers of 5 questions"),
                () -> str.contains("Test failed! Please try again ... ")
        );
    }

    @DisplayName("метод prepareResult не выведет данные студента с null в имени и фамилии")
    @Test
    void prepareResultTest_NameAndSurnameNull() throws BusinessException {

        questionBookService.setValidAnswerMinCount(4);
        doReturn("Moscow").when(messageSource)
                .getMessage("answers.moscow", null, Locale.forLanguageTag(""));
        doReturn("Diasoft").when(messageSource)
                .getMessage("answers.diasoft", null, Locale.forLanguageTag(""));
        getLocalizedMessagePreparePositiveResult();

        Student student = new Student();
        QuestionBook questionBook = getQuestionBook();
        List<String> questionAnswers = new ArrayList<>();
        questionAnswers.add("Moscow");
        questionAnswers.add("3");
        questionAnswers.add("1 3");
        questionAnswers.add("Diasoft");
        questionAnswers.add("3");

        String str = questionBookService.prepareResult(student, questionBook, questionAnswers);

        assertAll(str,
                () -> Assert.assertFalse(str.contains("Dear, "))
        );
    }

    @DisplayName("метод prepareResult не выведет данные студента с пустыми данными в имени и фамилии")
    @Test
    void prepareResultTest_NameAndSurnameEmpty() throws BusinessException {

        questionBookService.setValidAnswerMinCount(4);
        doReturn("Moscow").when(messageSource)
                .getMessage("answers.moscow", null, Locale.forLanguageTag(""));
        doReturn("Diasoft").when(messageSource)
                .getMessage("answers.diasoft", null, Locale.forLanguageTag(""));
        getLocalizedMessagePreparePositiveResult();

        Student student = new Student("", "");
        QuestionBook questionBook = getQuestionBook();
        List<String> questionAnswers = new ArrayList<>();
        questionAnswers.add("Moscow");
        questionAnswers.add("3");
        questionAnswers.add("1 3");
        questionAnswers.add("Diasoft");
        questionAnswers.add("3");

        String str = questionBookService.prepareResult(student, questionBook, questionAnswers);

        assertAll(str,
                () -> Assert.assertFalse(str.contains("Dear, "))
        );
    }

    @DisplayName("метод prepareResult не выведет данные студента с пустыми данными в имени и фамилии без учёта пробелов")
    @Test
    void prepareResultTest_NameAndSurnameEmptyIgnoreSpaces() throws BusinessException {

        questionBookService.setValidAnswerMinCount(4);
        doReturn("Moscow").when(messageSource)
                .getMessage("answers.moscow", null, Locale.forLanguageTag(""));
        doReturn("Diasoft").when(messageSource)
                .getMessage("answers.diasoft", null, Locale.forLanguageTag(""));
        getLocalizedMessagePreparePositiveResult();

        Student student = new Student("  ", "  ");
        QuestionBook questionBook = getQuestionBook();
        List<String> questionAnswers = new ArrayList<>();
        questionAnswers.add("Moscow");
        questionAnswers.add("3");
        questionAnswers.add("1 3");
        questionAnswers.add("Diasoft");
        questionAnswers.add("3");

        String str = questionBookService.prepareResult(student, questionBook, questionAnswers);

        assertAll(str,
                () -> Assert.assertFalse(str.contains("Dear, "))
        );
    }

    @DisplayName("метод prepareResult выдаст ошибку при разном количестве вопросов и ответов")
    @Test
    void prepareResultTest_QuestionAndAnswerQuantityNotEquals() throws BusinessException {

        questionBookService.setValidAnswerMinCount(4);

        Student student = new Student("Anton", "Elshin");
        QuestionBook questionBook = getQuestionBook();
        List<String> questionAnswers = new ArrayList<>();
        questionAnswers.add("Moscow");
        questionAnswers.add("3");
        questionAnswers.add("1 3");
        questionAnswers.add("Diasoft");

        Assertions.assertThrows(BusinessException.class, () -> {
            questionBookService.prepareResult(student, questionBook, questionAnswers);
        });
    }

    @DisplayName("метод prepareResult выдаст ошибку при разном количестве вопросов и ответов")
    @Test
    void prepareResultTest_AnswersListNullEquals() throws BusinessException {

        questionBookService.setValidAnswerMinCount(4);

        Student student = new Student("Anton", "Elshin");
        QuestionBook questionBook = getQuestionBook();

        Assertions.assertThrows(BusinessException.class, () -> {
            questionBookService.prepareResult(student, questionBook, null);
        });
    }

    @DisplayName("метод prepareResult выдаст ошибку при разном количестве вопросов и ответов")
    @Test
    void prepareResultTest_AnswersListEmptyEquals() throws BusinessException {

        questionBookService.setValidAnswerMinCount(4);

        Student student = new Student("Anton", "Elshin");
        QuestionBook questionBook = getQuestionBook();

        Assertions.assertThrows(BusinessException.class, () -> {
            questionBookService.prepareResult(student, questionBook, new ArrayList<>());
        });
    }

    @DisplayName("метод uniqueAnswersMultyTypeQuestion получит уникальные значения из ответа")
    @Test
    void uniqueAnswersMultyTypeQuestionTest() {

        List<String> questionAnswers = questionBookService.uniqueAnswersMultyTypeQuestion("1 3");

        assertThat(questionAnswers).hasSize(2);
        assertThat(questionAnswers.get(0)).containsIgnoringCase("1");
        assertThat(questionAnswers.get(1)).containsIgnoringCase("3");
    }

    @DisplayName("метод uniqueAnswersMultyTypeQuestion получит уникальные значения из ответа без учёта пробелов")
    @Test
    void uniqueAnswersMultyTypeQuestionTest_IgnoreSpaces() {

        List<String> questionAnswers = questionBookService.uniqueAnswersMultyTypeQuestion("  1   3  ");

        assertThat(questionAnswers).hasSize(2);
        assertThat(questionAnswers.get(0)).containsIgnoringCase("1");
        assertThat(questionAnswers.get(1)).containsIgnoringCase("3");
    }

    @DisplayName("метод uniqueAnswersMultyTypeQuestion получит уникальные значения из ответа без учёта дублей")
    @Test
    void uniqueAnswersMultyTypeQuestionTest_IgnoreDuplication() {

        List<String> questionAnswers = questionBookService.uniqueAnswersMultyTypeQuestion("1 3 3 1");

        assertThat(questionAnswers).hasSize(2);
        assertThat(questionAnswers.get(0)).containsIgnoringCase("1");
        assertThat(questionAnswers.get(1)).containsIgnoringCase("3");
    }

    @DisplayName("метод setValidAnswerMinCount установит минимально допустимое количество правильных ответов")
    @Test
    void setValidAnswerMinCountTest() {

        questionBookService.setValidAnswerMinCount(4);

        assertThat(questionBookService.getValidAnswerMinCount()).isEqualTo(4);
    }

    @DisplayName("метод getValidAnswerMinCount получит минимально допустимое количество правильных ответов")
    @Test
    void getValidAnswerMinCountTest() {

        questionBookService.setValidAnswerMinCount(4);

        assertThat(questionBookService.getValidAnswerMinCount()).isEqualTo(4);
    }

    @DisplayName("метод setLanguageTag установит русский язык")
    @Test
    void setLanguageTagTest() {

        questionBookService.setLanguageTag("ru-RU");

        assertThat(questionBookService.getLanguageTag()).isEqualTo("ru-RU");

    }

    @DisplayName("метод getLanguageTag получит установленный русский язык")
    @Test
    void getLanguageTagTest() {

        questionBookService.setLanguageTag("ru-RU");

        assertThat(questionBookService.getLanguageTag()).isEqualTo("ru-RU");

    }

    @DisplayName("метод initQuestionBook выполнит загрузку и парсинг файла csv")
    @Test
    void initQuestionBookTest() throws Exception {

        List<String> questions = getQuestions();
        QuestionBook questionBook = getQuestionBook();

        given(loader.loadQuestions(any())).willReturn(questions);
        given(parser.getQuestionBook(any())).willReturn(questionBook);

        questionBookService.initQuestionBook();

        Mockito.verify(loader, Mockito.times(1))
                .loadQuestions(any());
        Mockito.verify(parser, Mockito.times(1))
                .getQuestionBook(any());

    }

    @DisplayName("метод getLocalizedMessage запросит строку в английской локали")
    @Test
    void getLocalizedMessageTest_Default() {

        given(messageSource.getMessage("some key", null, Locale.forLanguageTag(""))).willReturn("some localized string");
        questionBookService.setLanguageTag("");

        questionBookService.getLocalizedMessage("some key");

        Mockito.verify(messageSource, Mockito.times(1))
                .getMessage("some key", null, Locale.forLanguageTag(""));
    }

    @DisplayName("метод getLocalizedMessage запросит строку в русской локали")
    @Test
    void getLocalizedMessageTest_Russian() {

        given(messageSource.getMessage("some key", null, Locale.forLanguageTag("ru-RU"))).willReturn("some localized string");
        questionBookService.setLanguageTag("ru-RU");

        questionBookService.getLocalizedMessage("some key");

        Mockito.verify(messageSource, Mockito.times(1))
                .getMessage("some key", null, Locale.forLanguageTag("ru-RU"));
    }

    private ByteArrayOutputStream configurateEnvironment(String answers) {

        in = new ByteArrayInputStream(answers.getBytes(StandardCharsets.UTF_8));
        outByte = new java.io.ByteArrayOutputStream();
        out = new java.io.PrintStream(outByte);

        System.setIn(in);
        System.setOut(out);

        return outByte;

    }

    private List<String> getQuestions() {

        List<String> questions = new ArrayList<>();

        questions.add("questions.current_capital_of_russia,typing,answers.moscow:true");
        questions.add("questions.javascript_is_language_for,single,answers.only_for_frontend:false,answers.only_for_backend:false,answers.frontend_and_backend:true");
        questions.add("questions.mark_languages_and_frameworks_for_current_course,multy,answers.java:true,answers.angular:false,answers.spring:true,answers.typescript:false");
        questions.add("questions.print_name_of_company_for_which_course_customized_for,typing,answers.diasoft:true");
        questions.add("questions.select_name_of_education_company,single,answers.university:false,answers.udemy:false,answers.otus:true");

        return questions;

    }

    private QuestionBook getQuestionBook() {

        QuestionBook questionBook = new QuestionBook();

        ArrayList<Answer> q1_answers = new ArrayList<>();
        q1_answers.add(new Answer("answers.moscow", true));

        ArrayList<Answer> q2_answers = new ArrayList<>();
        q2_answers.add(new Answer("answers.only_for_frontend", false));
        q2_answers.add(new Answer("answers.only_for_backend", false));
        q2_answers.add(new Answer("answers.frontend_and_backend", true));

        ArrayList<Answer> q3_answers = new ArrayList<>();
        q3_answers.add(new Answer("answers.java", true));
        q3_answers.add(new Answer("answers.angular", false));
        q3_answers.add(new Answer("answers.spring", true));
        q3_answers.add(new Answer("answers.typescript", false));

        ArrayList<Answer> q4_answers = new ArrayList<>();
        q4_answers.add(new Answer("answers.diasoft", true));

        ArrayList<Answer> q5_answers = new ArrayList<>();
        q5_answers.add(new Answer("answers.university", false));
        q5_answers.add(new Answer("answers.udemy", false));
        q5_answers.add(new Answer("answers.otus", true));

        Question question_1 = new Question();
        question_1.setText("questions.current_capital_of_russia");
        question_1.setType(QuestionTypes.TYPING);
        question_1.setAnswers(q1_answers);

        Question question_2 = new Question();
        question_2.setText("questions.javascript_is_language_for");
        question_2.setType(QuestionTypes.SINGLE);
        question_2.setAnswers(q2_answers);

        Question question_3 = new Question();
        question_3.setText("questions.mark_languages_and_frameworks_for_current_course");
        question_3.setType(QuestionTypes.MULTY);
        question_3.setAnswers(q3_answers);

        Question question_4 = new Question();
        question_4.setText("questions.print_name_of_company_for_which_course_customized_for");
        question_4.setType(QuestionTypes.TYPING);
        question_4.setAnswers(q4_answers);

        Question question_5 = new Question();
        question_5.setText("questions.select_name_of_education_company");
        question_5.setType(QuestionTypes.SINGLE);
        question_5.setAnswers(q5_answers);

        ArrayList<Question> questions = new ArrayList<>();
        questions.add(question_1);
        questions.add(question_2);
        questions.add(question_3);
        questions.add(question_4);
        questions.add(question_5);

        questionBook.setQuestions(questions);

        return questionBook;

    }

    private void getLocalizedMessageAskLanguage() {

        doReturn("Available languages").when(messageSource)
                .getMessage("messages.available_languages", null, Locale.forLanguageTag(""));

        doReturn("English").when(messageSource)
                .getMessage("locales.en", null, Locale.forLanguageTag(""));
        doReturn("Russian").when(messageSource)
                .getMessage("locales.ru", null, Locale.forLanguageTag(""));

        doReturn("Please select language number").when(messageSource)
                .getMessage("messages.please_select_language_number", null, Locale.forLanguageTag(""));
    }

    private void getLocalizedMessageAskLanguageRussian() {

        doReturn("Доступные языки").when(messageSource)
                .getMessage("messages.available_languages", null, Locale.forLanguageTag("ru-RU"));

        doReturn("Английский").when(messageSource)
                .getMessage("locales.en", null, Locale.forLanguageTag("ru-RU"));
        doReturn("Русский").when(messageSource)
                .getMessage("locales.ru", null, Locale.forLanguageTag("ru-RU"));

        doReturn("Пожалуйста, выберите язык").when(messageSource)
                .getMessage("messages.please_select_language_number", null, Locale.forLanguageTag("ru-RU"));

    }

    private void getLocalizedMessageAskStudentInfo() {

        doReturn("Please enter your name").when(messageSource)
                .getMessage("messages.please_enter_your_name", null, Locale.forLanguageTag(""));
        doReturn("Please enter your surname").when(messageSource)
                .getMessage("messages.please_enter_your_surname", null, Locale.forLanguageTag(""));

    }

    private void getLocalizedMessageQuestion1() {

        doReturn("Moscow").when(messageSource)
                .getMessage("answers.moscow", null, Locale.forLanguageTag(""));

        doReturn("Hint").when(messageSource)
                .getMessage("messages.hint", null, Locale.forLanguageTag(""));
        doReturn("Please type on keyboard correct answer (case insensitive)").when(messageSource)
                .getMessage("enums.typing.hint", null, Locale.forLanguageTag(""));
        doReturn("Question example").when(messageSource)
                .getMessage("messages.question_example", null, Locale.forLanguageTag(""));
        doReturn("Question").when(messageSource)
                .getMessage("enums.typing.question_example", null, Locale.forLanguageTag(""));
        doReturn("Answer example").when(messageSource)
                .getMessage("messages.answer_example", null, Locale.forLanguageTag(""));
        doReturn("Our answer: Answer").when(messageSource)
                .getMessage("enums.typing.answer_example", null, Locale.forLanguageTag(""));

        doReturn("Current capital of Russia?").when(messageSource)
                .getMessage("questions.current_capital_of_russia", null, Locale.forLanguageTag(""));

        doReturn("Our answer").when(messageSource)
                .getMessage("messages.our_answer", null, Locale.forLanguageTag(""));

    }

    private void getLocalizedMessageQuestion2() {

        doReturn("Only for frontend").when(messageSource)
                .getMessage("answers.only_for_frontend", null, Locale.forLanguageTag(""));
        doReturn("Only for backend").when(messageSource)
                .getMessage("answers.only_for_backend", null, Locale.forLanguageTag(""));
        doReturn("Frontend and backend").when(messageSource)
                .getMessage("answers.frontend_and_backend", null, Locale.forLanguageTag(""));

        doReturn("Please type on keyboard single correct option number").when(messageSource)
                .getMessage("enums.single.hint", null, Locale.forLanguageTag(""));
        doReturn("Question?   1.Option   2.Option   3.Option").when(messageSource)
                .getMessage("enums.single.question_example", null, Locale.forLanguageTag(""));
        doReturn("Our answer: 2").when(messageSource)
                .getMessage("enums.single.answer_example", null, Locale.forLanguageTag(""));

        doReturn("JavaScript is language for?").when(messageSource)
                .getMessage("questions.javascript_is_language_for", null, Locale.forLanguageTag(""));

    }

    private void getLocalizedMessageQuestionFull2() {

        doReturn("Only for frontend").when(messageSource)
                .getMessage("answers.only_for_frontend", null, Locale.forLanguageTag(""));
        doReturn("Only for backend").when(messageSource)
                .getMessage("answers.only_for_backend", null, Locale.forLanguageTag(""));
        doReturn("Frontend and backend").when(messageSource)
                .getMessage("answers.frontend_and_backend", null, Locale.forLanguageTag(""));

        doReturn("Hint").when(messageSource)
                .getMessage("messages.hint", null, Locale.forLanguageTag(""));
        doReturn("Please type on keyboard single correct option number").when(messageSource)
                .getMessage("enums.single.hint", null, Locale.forLanguageTag(""));
        doReturn("Question example").when(messageSource)
                .getMessage("messages.question_example", null, Locale.forLanguageTag(""));
        doReturn("Question?   1.Option   2.Option   3.Option").when(messageSource)
                .getMessage("enums.single.question_example", null, Locale.forLanguageTag(""));
        doReturn("Answer example").when(messageSource)
                .getMessage("messages.answer_example", null, Locale.forLanguageTag(""));
        doReturn("Our answer: 2").when(messageSource)
                .getMessage("enums.single.answer_example", null, Locale.forLanguageTag(""));

        doReturn("JavaScript is language for?").when(messageSource)
                .getMessage("questions.javascript_is_language_for", null, Locale.forLanguageTag(""));

    }

    private void getLocalizedMessageQuestion3() {

        doReturn("Java").when(messageSource)
                .getMessage("answers.java", null, Locale.forLanguageTag(""));
        doReturn("Angular").when(messageSource)
                .getMessage("answers.angular", null, Locale.forLanguageTag(""));
        doReturn("Spring").when(messageSource)
                .getMessage("answers.spring", null, Locale.forLanguageTag(""));
        doReturn("TypeScript").when(messageSource)
                .getMessage("answers.typescript", null, Locale.forLanguageTag(""));

        doReturn("Please type on keyboard correct option numbers through space").when(messageSource)
                .getMessage("enums.multy.hint", null, Locale.forLanguageTag(""));
        doReturn("Question?   1.Option   2.Option   3.Option").when(messageSource)
                .getMessage("enums.multy.question_example", null, Locale.forLanguageTag(""));
        doReturn("Our answer: 1 2").when(messageSource)
                .getMessage("enums.multy.answer_example", null, Locale.forLanguageTag(""));

        doReturn("Mark languages and frameworks for current course ...").when(messageSource)
                .getMessage("questions.mark_languages_and_frameworks_for_current_course", null, Locale.forLanguageTag(""));

    }

    private void getLocalizedMessageQuestionFull3() {

        doReturn("Java").when(messageSource)
                .getMessage("answers.java", null, Locale.forLanguageTag(""));
        doReturn("Angular").when(messageSource)
                .getMessage("answers.angular", null, Locale.forLanguageTag(""));
        doReturn("Spring").when(messageSource)
                .getMessage("answers.spring", null, Locale.forLanguageTag(""));
        doReturn("TypeScript").when(messageSource)
                .getMessage("answers.typescript", null, Locale.forLanguageTag(""));

        doReturn("Hint").when(messageSource)
                .getMessage("messages.hint", null, Locale.forLanguageTag(""));
        doReturn("Please type on keyboard correct option numbers through space").when(messageSource)
                .getMessage("enums.multy.hint", null, Locale.forLanguageTag(""));
        doReturn("Question example").when(messageSource)
                .getMessage("messages.question_example", null, Locale.forLanguageTag(""));
        doReturn("Question?   1.Option   2.Option   3.Option").when(messageSource)
                .getMessage("enums.multy.question_example", null, Locale.forLanguageTag(""));
        doReturn("Answer example").when(messageSource)
                .getMessage("messages.answer_example", null, Locale.forLanguageTag(""));
        doReturn("Our answer: 1 2").when(messageSource)
                .getMessage("enums.multy.answer_example", null, Locale.forLanguageTag(""));

        doReturn("Mark languages and frameworks for current course ...").when(messageSource)
                .getMessage("questions.mark_languages_and_frameworks_for_current_course", null, Locale.forLanguageTag(""));

    }

    private void getLocalizedMessageQuestion4() {

        doReturn("Diasoft").when(messageSource)
                .getMessage("answers.diasoft", null, Locale.forLanguageTag(""));

        doReturn("Print name of company for which course customized for?").when(messageSource)
                .getMessage("questions.print_name_of_company_for_which_course_customized_for", null, Locale.forLanguageTag(""));

    }

    private void getLocalizedMessageQuestion5() {

        doReturn("University").when(messageSource)
                .getMessage("answers.university", null, Locale.forLanguageTag(""));
        doReturn("Udemy").when(messageSource)
                .getMessage("answers.udemy", null, Locale.forLanguageTag(""));
        doReturn("Otus").when(messageSource)
                .getMessage("answers.otus", null, Locale.forLanguageTag(""));

        doReturn("Select name of education company?").when(messageSource)
                .getMessage("questions.select_name_of_education_company", null, Locale.forLanguageTag(""));

    }

    private void getLocalizedMessagePrepareStudentInfo() {

        doReturn("Dear").when(messageSource)
                .getMessage("messages.dear", null, Locale.forLanguageTag(""));

    }

    private void getLocalizedMessagePreparePositiveResult() {

        doReturn("Test result").when(messageSource)
                .getMessage("messages.test_result", null, Locale.forLanguageTag(""));
        doReturn("valid answers of").when(messageSource)
                .getMessage("messages.valid_answers_of", null, Locale.forLanguageTag(""));
        doReturn("questions").when(messageSource)
                .getMessage("messages.questions", null, Locale.forLanguageTag(""));
        doReturn("Test passed successfully").when(messageSource)
                .getMessage("messages.test_passed_successfully", null, Locale.forLanguageTag(""));

    }

    private void getLocalizedMessagePrepareNegativeResult() {

        doReturn("Test result").when(messageSource)
                .getMessage("messages.test_result", null, Locale.forLanguageTag(""));
        doReturn("valid answers of").when(messageSource)
                .getMessage("messages.valid_answers_of", null, Locale.forLanguageTag(""));
        doReturn("questions").when(messageSource)
                .getMessage("messages.questions", null, Locale.forLanguageTag(""));
        doReturn("Test failed! Please try again").when(messageSource)
                .getMessage("messages.test_failed_please_try_again", null, Locale.forLanguageTag(""));

    }

    private void getLocalizedMessagePerformTest() {

        getLocalizedMessageAskLanguage();
        getLocalizedMessageAskStudentInfo();
        getLocalizedMessageQuestion1();
        getLocalizedMessageQuestion2();
        getLocalizedMessageQuestion3();
        getLocalizedMessageQuestion4();
        getLocalizedMessageQuestion5();

    }

    private void getLocalizedMessageAnswers() {

        doReturn("Moscow").when(messageSource)
                .getMessage("answers.moscow", null, Locale.forLanguageTag(""));

        doReturn("Only for frontend").when(messageSource)
                .getMessage("answers.only_for_frontend", null, Locale.forLanguageTag(""));
        doReturn("Only for backend").when(messageSource)
                .getMessage("answers.only_for_backend", null, Locale.forLanguageTag(""));
        doReturn("Frontend and backend").when(messageSource)
                .getMessage("answers.frontend_and_backend", null, Locale.forLanguageTag(""));

        doReturn("Java").when(messageSource)
                .getMessage("answers.java", null, Locale.forLanguageTag(""));
        doReturn("Angular").when(messageSource)
                .getMessage("answers.angular", null, Locale.forLanguageTag(""));
        doReturn("Spring").when(messageSource)
                .getMessage("answers.spring", null, Locale.forLanguageTag(""));
        doReturn("TypeScript").when(messageSource)
                .getMessage("answers.typescript", null, Locale.forLanguageTag(""));

        doReturn("Diasoft").when(messageSource)
                .getMessage("answers.diasoft", null, Locale.forLanguageTag(""));

        doReturn("University").when(messageSource)
                .getMessage("answers.university", null, Locale.forLanguageTag(""));
        doReturn("Udemy").when(messageSource)
                .getMessage("answers.udemy", null, Locale.forLanguageTag(""));
        doReturn("Otus").when(messageSource)
                .getMessage("answers.otus", null, Locale.forLanguageTag(""));

    }

}
