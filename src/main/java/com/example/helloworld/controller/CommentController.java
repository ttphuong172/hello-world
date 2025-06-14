package com.example.helloworld.controller;

import com.example.helloworld.model.Comment;
import com.example.helloworld.model.Notification;
import com.example.helloworld.service.CommentService;
import com.example.helloworld.service.NotificationService;
import com.example.helloworld.service.TknService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/comment")
@CrossOrigin
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TknService tknService;


    @PostMapping("")
    public ResponseEntity<Comment> save(@RequestBody Comment comment) {
        try {
            // Set the current date and time for the comment
            comment.setCommentDate(LocalDateTime.now());

            // Save the comment
            commentService.save(comment);

            // Create a new notification and associate it with the comment
            Notification notification = new Notification();
            notification.setComment(comment);

            // Save the notification
            notificationService.save(notification);

            // Return the saved comment
            return new ResponseEntity<>(comment, HttpStatus.CREATED);
        } catch (Exception e) {
            // Handle any errors and return a bad request response
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
