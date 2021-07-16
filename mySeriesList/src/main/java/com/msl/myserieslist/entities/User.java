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
@Table(name = "user", schema = "myserieslist")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int idUser;

    @Basic
    @Column(name = "firstName", nullable = false, length = 50)
    private String firstName;

    @Basic
    @Column(name = "lastName", nullable = false, length = 50)
    private String lastName;

    @Basic
    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @OneToMany(targetEntity = Watchlist.class, mappedBy = "user", cascade = CascadeType.MERGE)
    @JsonIgnore
    private List<Watchlist> watchlist;
}
