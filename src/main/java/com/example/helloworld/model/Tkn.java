package com.example.helloworld.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tkn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;



    @Column(columnDefinition = "TEXT")
    private String summaryContent;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String creator;
    private LocalDateTime creatingDate;

    @OneToMany(mappedBy = "tkn")
    @JsonIgnore
    private List<Reading> readingList;

    @OneToMany(mappedBy = "tkn")
    @JsonIgnore
    private List<Comment> commentList;

    private Boolean isVisible = true;

    @ManyToOne
    @JoinColumn
    private TknType tknType;

}
