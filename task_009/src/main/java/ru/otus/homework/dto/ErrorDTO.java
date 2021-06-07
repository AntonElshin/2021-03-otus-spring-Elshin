package ru.otus.homework.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorDTO {

  private Long code;

  private String message;

}

