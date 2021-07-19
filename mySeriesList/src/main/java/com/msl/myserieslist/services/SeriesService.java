package com.msl.myserieslist.services;

import com.msl.myserieslist.entities.Actor;
import com.msl.myserieslist.entities.Series;
import com.msl.myserieslist.repositories.SeriesRepository;
import com.msl.myserieslist.support.exceptions.SeriesAlreadyExistException;
import com.msl.myserieslist.support.exceptions.SeriesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class SeriesService {
    @Autowired
    private SeriesRepository seriesRepository;

    @Transactional(readOnly = false)
    public void addSeries(Series series) throws SeriesAlreadyExistException {
        if(series.getName() != null && seriesRepository.existsByName(series.getName()))
            throw new SeriesAlreadyExistException();
        seriesRepository.save(series);
    }

    @Transactional(readOnly = false)
    public Series removeSeries(Series series) throws SeriesNotExistException {
        if(!seriesRepository.existsByName(series.getName()))
            throw new SeriesNotExistException();
        seriesRepository.delete(series);
        return series;
    }

    @Transactional(readOnly = true)
    public List<Series> showAllSeries(){
        return seriesRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Series> showAllSeries(int pageNumber, int pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Series> pagedResult = seriesRepository.findAll(paging);
        if(pagedResult.hasContent())
            return pagedResult.getContent();
        else
            return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<Series> showSeriesByName(String name){
        return seriesRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public List<Series> showSeriesByKeyword(String keyword){
        return seriesRepository.findByNameContaining(keyword);
    }

    @Transactional(readOnly = true)
    public List<Series> showSeriesByCast(Actor actor){
        return seriesRepository.findByCastContaining(actor);
    }
}
