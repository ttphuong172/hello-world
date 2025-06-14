package com.example.helloworld.service.impl;

import com.example.helloworld.model.Ping;
import com.example.helloworld.model.Tkn;
import com.example.helloworld.repository.TknRepository;
import com.example.helloworld.service.TknService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TknServiceImpl implements TknService {
    @Autowired
    private TknRepository tknRepository;

    @Override
    public List<Tkn> findAll() {
        return tknRepository.findAllByIsVisibleIsTrueOrderByIdDesc();
    }

    @Override
    public void save(Tkn tkn) {
        tknRepository.save(tkn);
    }

    @Override
    public Tkn findById(int id) {
        return tknRepository.findById(id).orElse(null);
    }

    @Override
    public List<Tkn> searchByKeyword(String keyword)
    {
        return tknRepository.searchByKeyword(keyword);
    }
}
