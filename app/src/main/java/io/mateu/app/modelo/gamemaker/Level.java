package io.mateu.app.modelo.gamemaker;


import io.mateu.app.modelo.common.Fichero;
import io.mateu.ui.mdd.server.annotations.Output;
import lombok.Getter;
import lombok.Setter;
import org.jdom2.Element;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Level {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @ManyToOne
    @NotNull
    private Game game;

    @ManyToOne
    private Pawn player;

    @OneToMany
    @Output
    private List<Pawn> pawns = new ArrayList<>();

    @ManyToOne
    private Fichero music;

    public Level() {

    }

    public Level(EntityManager em, Element xml) {

        /*
        <nivel nombre="l1" ancho="800" alto="600">
        <jugador img="nave1x.png" ancho="64" alto="64" />1
        <malo img="marciano1x.png" ancho="64" alto="64" frecuencia="100000" sonidoDestruido="blast.wav" />
        <malo img="marciano2x.png" ancho="64" alto="64" frecuencia="500000" sonidoDestruido="blast.wav" />
        <malo img="marciano3x.png" ancho="64" alto="64" frecuencia="500000" sonidoDestruido="blast.wav" />
        <musica src="masai.mp3" />
    </nivel>
         */

        setName(xml.getAttributeValue("nombre"));

        Pawn p;
        setPlayer(p = new Pawn(em, xml.getChild("jugador")));
        em.persist(p);

        for (Element e : xml.getChildren("malo")) {
            getPawns().add(p = new Pawn(em, e));
            em.persist(p);
        }

    }

    public Element toXml() {
        /*
        <nivel nombre="Nivel 1" ancho="800" alto="600">

        <jugador img="ship1.png" ancho="64" alto="64"/>

        <malo img="alien1.png" ancho="64" alto="64" origen="random" sonidoDestruido="blast.wav"/>


        <musicax src="masai.mp3"/>

    </nivel>
         */
        Element xml = new Element("nivel").setAttribute("nombre", getName()).setAttribute("ancho", "800").setAttribute("alto", "600");

        xml.addContent(getPlayer().toXml().setName("jugador"));

        for (Pawn p : getPawns()) {
            xml.addContent(p.toXml().setName("malo"));
        }

        xml.addContent(new Element("musica").setAttribute("src", "masai.mp3"));

        return xml;
    }
}
