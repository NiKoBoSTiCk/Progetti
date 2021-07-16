package com.msl.myserieslist.services;

import com.msl.myserieslist.entities.Actor;
import com.msl.myserieslist.entities.Series;
import com.msl.myserieslist.repositories.ActorRepository;
import com.msl.myserieslist.support.exceptions.ActorAlreadyExistException;
import com.msl.myserieslist.support.exceptions.SeriesAlreadyExistException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ActorService {
    ActorRepository actorRepository;

    @Transactional(readOnly = false)
    public void addActor(Actor actor) throws ActorAlreadyExistException {
        if(actorRepository.existsById(actor.getIdActor()))
            throw new ActorAlreadyExistException();
        actorRepository.save(actor);
    }

    @Transactional(readOnly = false)
    public List<Actor> showAllActor(){
        return actorRepository.findAll();
    }

    @Transactional(readOnly = false)
    public List<Actor> showAllActor(int pageNumber, int pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        Page<Actor> pagedResult = actorRepository.findAll(paging);
        if(pagedResult.hasContent())
            return pagedResult.getContent();
        else
            return new ArrayList<>();
    }

    @Transactional(readOnly = false)
    public List<Actor> showActorByFirstName(String firstName){
        return actorRepository.findByFirstName(firstName);
    }

    @Transactional(readOnly = false)
    public List<Actor> showActorByLastName(String lastName){
        return actorRepository.findByLastName(lastName);
    }
}
