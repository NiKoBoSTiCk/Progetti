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

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "episodes")
    private int episodes;

    @Column(name = "rating", length = 4)
    private double rating;

    @Column(name = "views", length = 10)
    private int views;

    @Column(name = "plot", length = 100)
    private String plot;

    @Column(name = "members")
    private int members;

    @Column(name = "genre", length = 45)
    private String genre;

    @OneToMany(targetEntity = SeriesInList.class, mappedBy = "series", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    Set<SeriesInList> watchlist = new HashSet<>();

    private void increaseMembers(){ members++; }
    private void decreaseMembers() { members--; }
    public void upgradeViews() { views++; }

    public void updateRating(int newScore, boolean leave){
        double x = rating * members;
        if(leave){
            x -= newScore;
            decreaseMembers();
        }
        else {
            x += newScore;
            increaseMembers();
        }
        x /= members;
        rating = x;
    }
}
