package it.niko.mywatchlist.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "genre", schema = "mywatchlist")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EGenre name;

    public Genre() {}

    public Genre(EGenre name) {
        this.name = name;
    }
}
