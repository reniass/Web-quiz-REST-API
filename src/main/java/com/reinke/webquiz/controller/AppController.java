package com.reinke.webquiz.controller;

import com.reinke.webquiz.model.Quiz;
import com.reinke.webquiz.payload.AnswerResponse;
import com.reinke.webquiz.payload.QuizRequest;
import com.reinke.webquiz.payload.QuizResponse;
import com.reinke.webquiz.payload.UserRequest;
import com.reinke.webquiz.security.UserPrincipal;
import com.reinke.webquiz.service.AppService;
import com.reinke.webquiz.util.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AppController {

    @Autowired
    private AppService appService;

    // /api/guizzes GET
    @GetMapping("/quizzes")
    public List<QuizResponse> getQuizzes(@RequestParam("page") int page) {
        return appService.getQuizzes(page);
    }

    // /api/quizzes POST
    @PostMapping("/quizzes")
    public ResponseEntity<QuizResponse> createQuiz(@RequestBody QuizRequest quizRequest, UserPrincipal userPrincipal) {
        Quiz quiz = appService.createQuiz(quizRequest, userPrincipal);

        return ResponseEntity.ok(ModelMapper.mapQuizToQuizResponse(quiz));
    }

    // /api/quizzes/{id}/solve
    @PostMapping("/quizzes/{id}/solve")
    public ResponseEntity<AnswerResponse> solveQuizById(@PathVariable("id") Long id,
                                                        @RequestBody int answer,
                                                        UserPrincipal userPrincipal) {
        AnswerResponse answerResponse = appService.solveQuizById(id, answer, userPrincipal);

        return ResponseEntity.ok(answerResponse);
    }

    // /api/quizzes/{id} GET
    @GetMapping("/quizzes/{id}")
    public ResponseEntity<QuizResponse> getQuizById(@PathVariable("id") Long id) {
        QuizResponse quizResponse = appService.getQuizById(id);

        return ResponseEntity.ok(quizResponse);
    }

    // /api/register
    @PostMapping("/register")
    public ResponseEntity registerNewUser(@RequestBody UserRequest userRequest) {
        appService.registerNewUser(userRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // /api/quizzes/{id} DELETE
    @DeleteMapping("/quizzes/{id}")
    public ResponseEntity deleteQuizById(@PathVariable("id") Long id, UserPrincipal userPrincipal) {
        appService.deleteQuizById(id, userPrincipal);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // /api/quizzes/{id} PUT
    @PutMapping("/quizzes/{id}")
    public ResponseEntity updateQuizById(@PathVariable("id") Long id,
                                         @RequestBody QuizRequest quizRequest,
                                         UserPrincipal userPrincipal) {
        appService.updateQuizById(id, quizRequest, userPrincipal);

        return ResponseEntity.ok().build();
    }

    // /api/quizzes/completed

}
