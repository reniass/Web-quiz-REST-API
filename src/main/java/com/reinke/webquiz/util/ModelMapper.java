package com.reinke.webquiz.util;

import com.reinke.webquiz.model.Quiz;
import com.reinke.webquiz.payload.QuizResponse;
import com.reinke.webquiz.payload.UserResponse;

public class ModelMapper {

    public static QuizResponse mapQuizToQuizResponse(Quiz quiz) {
        QuizResponse quizResponse = new QuizResponse();
        quizResponse.setId(quiz.getId());
        quizResponse.setTitle(quiz.getTitle());
        quizResponse.setText(quiz.getText());
        quizResponse.setOptions(quiz.getOptions());

        UserResponse userResponse = new UserResponse();
        userResponse.setId(quiz.getUser().getId());
        userResponse.setEmail(quiz.getUser().getEmail());

        quizResponse.setUserResponse(userResponse);

        return quizResponse;
    }
}
