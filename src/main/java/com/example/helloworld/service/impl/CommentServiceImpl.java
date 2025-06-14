package com.example.helloworld.service.impl;

import com.example.helloworld.model.Comment;
import com.example.helloworld.repository.CommentRepository;
import com.example.helloworld.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Override
    public void save(Comment comment) {
        commentRepository.save(comment);
    }
}
