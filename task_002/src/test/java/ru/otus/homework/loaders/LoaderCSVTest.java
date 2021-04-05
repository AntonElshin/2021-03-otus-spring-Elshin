package ru.otus.homework.loaders;

import org.junit.Assert;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.homework.config.HomeworkApplicationConfig;
import org.junit.jupiter.api.Test;

import java.util.List;

@ExtendWith(SpringExtension.class)
@Import(HomeworkApplicationConfig.class)
public class LoaderCSVTest {

    @Autowired
    private Loader loader;

    @Value("${resource.name}")
    private String resourceName;

    @Test
    public void loadQuestionsTest() throws Exception {

        List<String> questions = loader.loadQuestions(resourceName);

        Assert.assertEquals(5, questions.size());

        for(String question : questions) {
            Assert.assertNotNull(question);
        }

    }

}
