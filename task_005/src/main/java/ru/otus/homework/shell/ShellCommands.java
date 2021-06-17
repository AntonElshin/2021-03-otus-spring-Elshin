package ru.otus.homework.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.homework.service.MessageService;
import ru.otus.homework.service.QuestionBookService;

@ShellComponent
@RequiredArgsConstructor
public class ShellCommands {

    private final QuestionBookService questionBookService;
    private final MessageService messageService;

    @ShellMethod(value = "Select language", key = {"select_language"})
    public void selectLanguage() {
        questionBookService.askLanguage(false);
    }

    @ShellMethod(value = "Set language", key = {"set_language"})
    public void setLanguage(
            @ShellOption(value = {"-tag"}, defaultValue = "") String tag
    ) {
        messageService.setLanguageTag(tag);
    }

    @ShellMethod(value = "Print questions", key = {"print_questions"})
    public void printQuestionBook() {
        questionBookService.printQuestionBook(false);
    }

    @ShellMethod(value = "Set valid min answer count", key = {"set_valid_min_answer_count"})
    public void setValidMinAnswerCount(
            @ShellOption(value = {"-count"}, defaultValue = "") String count
    ) {
        questionBookService.setValidAnswerMinCount(Integer.valueOf(count));
    }

    @ShellMethod(value = "Perform testing", key = {"perform_testing"})
    public void performTesting() {
        questionBookService.performTesting(false);
    }



}
