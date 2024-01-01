package rs.raf.demo.model;

import lombok.Getter;
import lombok.Setter;
import rs.raf.demo.utils.VacuumStatus;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Vacuum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long vacuumId;

    @Column(unique = true)
    private String name;

    @Enumerated
    private VacuumStatus status;

    private Boolean active;

    private Long dateCreated;

    private Boolean locked;

    @Version
    private Integer version = 0;

    private Integer lastDischarge = 0;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
