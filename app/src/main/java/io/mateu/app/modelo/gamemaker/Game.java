package io.mateu.app.modelo.gamemaker;

import io.mateu.app.modelo.authentication.Account;
import io.mateu.app.modelo.authentication.Audit;
import io.mateu.app.modelo.authentication.User;
import io.mateu.ui.mdd.server.annotations.Output;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter@Setter
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Embedded
    @Output
    private Audit audit = new Audit();

    @ManyToOne
    private Account account;

    private String name;

    @OneToMany(mappedBy = "game")
    private List<Level> levels = new ArrayList<>();

}
