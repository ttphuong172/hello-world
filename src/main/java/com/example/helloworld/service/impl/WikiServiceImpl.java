package com.example.helloworld.service.impl;

import com.example.helloworld.model.Wiki;
import com.example.helloworld.repository.WikiRepository;
import com.example.helloworld.service.WikiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class WikiServiceImpl implements WikiService {
    @Autowired
    private WikiRepository wikiRepository;
    @Override
    public List<Wiki> findAll() {
        return wikiRepository.findAll();
    }

    @Override
    public Wiki findById(int id) {
        return wikiRepository.findById(id).orElse(null);
    }

    @Override
    public List<Wiki> searchByKeyword(String keyword) {
        return wikiRepository.searchByKeyword(keyword);
    }
}
