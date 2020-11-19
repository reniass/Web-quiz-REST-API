package com.reinke.webquiz.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuizResponse {

    private Long id;
    private String title;
    private String text;
    private String[] options;
    private UserResponse userResponse;
}
