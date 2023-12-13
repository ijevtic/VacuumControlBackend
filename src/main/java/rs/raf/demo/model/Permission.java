package rs.raf.demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Permission {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long permissionId;

    @Column
    private String name;

    @Column
    private String description;

    @ManyToMany(mappedBy = "permissions")
    private Set<User> users = new HashSet<>();
}