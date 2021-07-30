package it.niko.mywatchlist.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "role", schema = "mywatchlist")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

    public Role() {}

    public Role(ERole name) {
        this.name = name;
    }
}