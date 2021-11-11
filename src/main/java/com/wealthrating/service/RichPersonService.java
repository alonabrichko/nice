package com.wealthrating.service;

import com.wealthrating.entity.RichPerson;
import com.wealthrating.repository.RichPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RichPersonService {

    @Autowired
    RichPersonRepository richPersonRepository;

    public List<RichPerson> getAllRichPeople() {
        List<RichPerson> richPeople = new ArrayList<>();
        richPersonRepository.findAll().forEach(richPeople::add);
        return richPeople;
    }

    public RichPerson getRichPersonById(int id) {
        Optional<RichPerson> richPerson = richPersonRepository.findById(id);
        if (richPerson.isPresent())
            return richPersonRepository.findById(id).get();
        return null;
    }

    public void saveOrUpdate(RichPerson richPerson) {
        richPersonRepository.save(richPerson);
    }
}
