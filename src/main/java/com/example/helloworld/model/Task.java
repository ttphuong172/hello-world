package com.example.helloworld.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
//    For report
    private String name;
    private Boolean isP1;
//    private LocalDateTime startDateTime;
//    private LocalDateTime endDateTime;

    @ManyToOne
    @JoinColumn
    private Impact impact;

    private String configure;
    private String configureKr;

    private String cause;
    private String causeKr;

    @ElementCollection
    @CollectionTable(name = "action", joinColumns =@JoinColumn(name = "actionId"))
    private List<Action> actionList;

    @ManyToOne
    @JoinColumn
    private Company company;

    @ManyToOne
    @JoinColumn
    private Site site;

    @ManyToOne
    @JoinColumn
    private Position position;

    private Boolean isVisible = true;






}
