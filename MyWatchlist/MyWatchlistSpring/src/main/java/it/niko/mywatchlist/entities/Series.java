package it.niko.mywatchlist.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity(name = "Series")
@Table(name = "series", schema = "mywatchlist", uniqueConstraints = @UniqueConstraint(columnNames = "title"))
public class Series {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "title")
    @NotBlank
    @Size(max = 50)
    private String title;

    @Column(name = "episodes")
    private int episodes;

    @Column(name = "rating")
    private double rating;

    @Column(name = "views")
    private int views;

    @Column(name = "plot")
    @NotBlank
    @Size(max = 100)
    private String plot;

    @Column(name = "members")
    private int members;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "series_genre",
            joinColumns = @JoinColumn(name = "id_series"),
            inverseJoinColumns = @JoinColumn(name = "id_genre"))
    @ToString.Exclude
    private Set<Genre> genres = new HashSet<>();

    public Series() {}

    public Series(String title) {
        this.title = title;
    }

    public void increaseViews() { views++; }

    public void updateRating(int newScore, boolean leave){
        double x = rating * members;
        if(leave){ x -= newScore; members--;}
        else { x += newScore; members++;}
        x /= members;
        rating = x;
    }
}
