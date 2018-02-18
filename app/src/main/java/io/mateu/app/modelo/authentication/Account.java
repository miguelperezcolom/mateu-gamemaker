package io.mateu.app.modelo.authentication;

import io.mateu.ui.mdd.server.annotations.Output;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter@Setter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Embedded
    @Output
    private Audit audit = new Audit();

    @ManyToOne
    private User owner;

    @ManyToMany(mappedBy = "accounts")
    private List<User> users = new ArrayList<>();
}
