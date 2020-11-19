package com.reinke.webquiz.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuizRequest {

    private String title;
    private String text;
    private String[] options;
    private int answer;
}
