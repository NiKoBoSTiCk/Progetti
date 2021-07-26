package it.niko.mywatchlist.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity(name = "SeriesInList")
@Table(name = "series_in_list", schema = "myserieslist")
public class SeriesInList {

    public enum Status {
        @JsonProperty("watching")
        WATCHING,
        @JsonProperty("completed")
        COMPLETED,
        @JsonProperty("dropped")
        DROPPED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_series", nullable = false)
    private Series series;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "progress")
    private int progress;

    @Column(name = "score")
    private int score;

    @Column(name = "comment")
    private String comment;
}

