package it.niko.mywatchlist.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "status", schema = "mywatchlist")
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EStatus name;

    public Status() {}

    public Status(EStatus name) {
        this.name = name;
    }
}

