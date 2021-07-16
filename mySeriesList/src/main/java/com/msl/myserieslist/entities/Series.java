package com.msl.myserieslist.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "series", schema = "myserieslist")
public class Series {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int idSeries;

    @Basic
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Basic
    @Column(name = "episodes", nullable = false, length = 4)
    private int episodes;

    @Basic
    @Column(name = "rating")
    private double rating;

    @Basic
    @Column(name = "views", length = 10)
    private int views;

    @OneToMany(targetEntity = Watchlist.class, mappedBy = "series", cascade = CascadeType.MERGE)
    @ToString.Exclude
    @JsonIgnore
    List<Watchlist> watchlist;

    @ManyToMany
    @JoinTable(
            name = "cast",
            joinColumns = @JoinColumn(name = "idSeries"),
            inverseJoinColumns = @JoinColumn(name = "idActor"))
    @JsonIgnore
    private List<Actor> cast;
}
