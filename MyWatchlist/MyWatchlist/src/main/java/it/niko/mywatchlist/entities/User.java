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
@Entity(name = "User")
@Table(name = "user", schema = "myserieslist")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "nickname", nullable = false, length = 50)
    private String nickname;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @OneToMany(targetEntity = SeriesInList.class, mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private Set<SeriesInList> watchlist = new HashSet<>();
}
