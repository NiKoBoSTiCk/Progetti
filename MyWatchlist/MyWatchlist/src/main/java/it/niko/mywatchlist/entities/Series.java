package it.niko.mywatchlist.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity(name = "Series")
@Table(name = "series", schema = "myserieslist")
public class Series {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "episodes", nullable = false, length = 4)
    private int episodes;

    @Column(name = "rating")
    private double rating;

    @Column(name = "views", length = 10)
    private int views;

    @OneToMany(targetEntity = SeriesInList.class, mappedBy = "series", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    Set<SeriesInList> watchlist = new HashSet<>();
}
