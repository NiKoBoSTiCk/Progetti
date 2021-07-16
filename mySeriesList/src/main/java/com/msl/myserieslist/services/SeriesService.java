package com.msl.myserieslist.services;

import com.msl.myserieslist.entities.Series;
import com.msl.myserieslist.repositories.SeriesRepository;
import com.msl.myserieslist.support.exceptions.SeriesAlreadyExistException;
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
}
