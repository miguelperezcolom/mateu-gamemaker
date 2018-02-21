package io.mateu.app.modelo.gamemaker;


import io.mateu.app.modelo.common.Fichero;
import io.mateu.ui.mdd.server.annotations.Owned;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Pawn extends Actor {

    @OneToMany
    private List<Fichero> images = new ArrayList<>();

}
