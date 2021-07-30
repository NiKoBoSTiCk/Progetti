package it.niko.mywatchlist.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity(name = "User")
@Table(name = "user", schema = "mywatchlist",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "username", nullable = false)
    @NotBlank
    @Size(min = 8, max = 20)
    private String username;

    @Column(name = "email")
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @Column(name = "password")
    @NotBlank
    @Size(min=8, max = 120)
    private String password;

    @OneToMany(targetEntity = Watchlist.class, mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private Set<Watchlist> watchlist = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "idUser"),
            inverseJoinColumns = @JoinColumn(name = "idRole")
    )
    private Set<Role> roles = new HashSet<>();

    public User() {}

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
