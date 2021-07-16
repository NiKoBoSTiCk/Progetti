package com.msl.myserieslist.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "watchlist", schema = "myserieslist")
public class Watchlist {
    public enum Status {WATCHING, COMPLETED, DROPPED}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @ManyToOne
    @JoinColumn(name = "idUser", nullable = false)
    private User user;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "idSeries", nullable = false)
    private Series series;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private Status status;

    @Basic
    @Column(name = "progress", nullable = false)
    private int progress;

    @Basic
    @Column(name = "score")
    private int score;

    @Basic
    @Column(name = "comment")
    private String comment;
}

