package com.example.helloworld.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Account {
    @Id
    private String username;
    private String fullname;
    private String email;
    private String password;
    private boolean enable;
    @Enumerated(EnumType.STRING)
    private ERole role;

    @OneToMany(mappedBy = "account")
    @JsonIgnore
    private List<Reading> readingList;

    @OneToMany(mappedBy = "account")
    @JsonIgnore
    private List<Comment> commentList;

}