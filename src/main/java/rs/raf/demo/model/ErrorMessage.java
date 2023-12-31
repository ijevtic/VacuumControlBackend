package rs.raf.demo.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class ErrorMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long errorMessageId;

    private String message;

    private Long dateCreated;

    //eager fetch
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vacuum_id")
    private Vacuum vacuum;
}
