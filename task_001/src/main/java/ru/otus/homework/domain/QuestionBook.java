package ru.otus.homework.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class QuestionBook {

    List<Question> questions;

}
