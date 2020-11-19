package com.reinke.webquiz.service;

import com.reinke.webquiz.exception.QuizNotCreatedByYouException;
import com.reinke.webquiz.exception.ResourceNotFoundException;
import com.reinke.webquiz.exception.UserExistsException;
import com.reinke.webquiz.model.CompletedQuiz;
import com.reinke.webquiz.model.Quiz;
import com.reinke.webquiz.model.User;
import com.reinke.webquiz.payload.AnswerResponse;
import com.reinke.webquiz.payload.QuizRequest;
import com.reinke.webquiz.payload.QuizResponse;
import com.reinke.webquiz.payload.UserRequest;
import com.reinke.webquiz.repository.CompletedQuizRepository;
import com.reinke.webquiz.repository.QuizRepository;
import com.reinke.webquiz.repository.UserRepository;
import com.reinke.webquiz.security.UserPrincipal;
import com.reinke.webquiz.util.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private CompletedQuizRepository completedQuizRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Quiz createQuiz(QuizRequest quizRequest, UserPrincipal userPrincipal) {
        Optional<User> userOptional = userRepository.findByEmail(userPrincipal.getEmail());

        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("User", "email", userPrincipal.getEmail());
        }

        User user = userOptional.get();

        Quiz quiz = new Quiz();
        quiz.setTitle(quizRequest.getTitle());
        quiz.setText(quizRequest.getText());
        quiz.setOptions(quizRequest.getOptions());
        quiz.setAnswer(quizRequest.getAnswer());
        quiz.setUser(user);

        return quizRepository.save(quiz);
    }

    public List<QuizResponse> getQuizzes(int page) {
        Pageable pageable = PageRequest.of(page, 10);

        Page<Quiz> quizPage = quizRepository.findAll(pageable);

        List<QuizResponse> quizResponseList = quizPage.stream().map(quiz -> ModelMapper.mapQuizToQuizResponse(quiz)).collect(Collectors.toList());

        return quizResponseList;

    }

    public AnswerResponse solveQuizById(Long id, int answer, UserPrincipal userPrincipal) {
        Quiz quiz = quizRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", id));

        if (quiz.getAnswer() == answer) {
            User user = userRepository.findByEmail(userPrincipal.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User", "email", userPrincipal.getEmail()));

            CompletedQuiz completedQuiz = new CompletedQuiz();
            completedQuiz.setQuiz(quiz);
            completedQuiz.setUser(user);
            completedQuiz.setCompletedAt(Instant.now());

            completedQuizRepository.save(completedQuiz);


            return new AnswerResponse(true, "Congratulations, you're right!");
        } else {
            return new AnswerResponse(false, "Wrong answer! Please, try again.");
        }

    }

    public QuizResponse getQuizById(Long id) {
        Quiz quiz = quizRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", id));

        return ModelMapper.mapQuizToQuizResponse(quiz);
    }

    public User registerNewUser(UserRequest userRequest) {
        String email = userRequest.getEmail();
        String password = passwordEncoder.encode(userRequest.getPassword());

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new UserExistsException(email);
        }


        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        return userRepository.save(user);
    }

    public void deleteQuizById(Long id, UserPrincipal userPrincipal) {
        Optional<Quiz> quizOptional = quizRepository.findById(id);

        if (quizOptional.isEmpty()) {
            throw new ResourceNotFoundException("Quiz", "id", id);
        }

        Quiz quiz = quizOptional.get();
        if (!userPrincipal.getEmail().equals(quiz.getUser().getEmail())) {
            throw new QuizNotCreatedByYouException("You can remove only quizzes created by yourself");
        }

        quizRepository.deleteById(id);

    }

    public void updateQuizById(Long id, QuizRequest quizRequest, UserPrincipal userPrincipal) {
        Optional<Quiz> quizOptional = quizRepository.findById(id);

        if (quizOptional.isEmpty()) {
            throw new ResourceNotFoundException("Quiz", "id", id);
        }

        Quiz quiz = quizOptional.get();
        if (!userPrincipal.getEmail().equals(quiz.getUser().getEmail())) {
            throw new QuizNotCreatedByYouException("You can update only quizzes created by yourself");
        }

        quiz.setTitle(quizRequest.getTitle());
        quiz.setText(quizRequest.getText());
        quiz.setOptions(quizRequest.getOptions());
        quiz.setAnswer(quizRequest.getAnswer());

        quizRepository.save(quiz);

    }

}
