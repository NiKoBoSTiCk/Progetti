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
@Table(name = "actor", schema = "myserieslist")
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int idActor;

    @Basic
    @Column(name = "firstName", nullable = false, length = 50)
    private String firstName;

    @Basic
    @Column(name = "lastName", nullable = false, length = 50)
    private String lastName;

    @ManyToMany(mappedBy = "cast")
    @ToString.Exclude
    @JsonIgnore
    private List<Series> career;
}
