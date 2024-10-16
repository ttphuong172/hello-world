package com.example.helloworld.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Action {
    @ManyToOne
    @JoinColumn
    private Type type;
    private LocalDateTime actionDate;

    @Column(length = 1337)
    private String actionContent;

    @Column(length = 1337)
    private String actionContentKr;

}
