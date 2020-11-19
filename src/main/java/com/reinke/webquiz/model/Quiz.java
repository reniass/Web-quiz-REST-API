package com.reinke.webquiz.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String text;

    @ElementCollection
    private String[] options;

    private int answer;

    @ManyToOne
    private User user;

}
