package io.mateu.app.modelo.gamemaker;


import io.mateu.app.modelo.common.Fichero;
import io.mateu.ui.mdd.server.annotations.Owned;
import lombok.Getter;
import lombok.Setter;
import org.jdom2.Element;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Pawn extends Actor {

    private int strength;

    private int damage;

    private boolean friend;

    private int frequencyMilliseconds = 0;


    @OneToMany
    private List<Fichero> images = new ArrayList<>();

    public Pawn() {
        super();
    }

    public Pawn(EntityManager em, Element xml) {
        super();
        /*
        <jugador img="nave1x.png" ancho="64" alto="64" />1
        <malo img="marciano1x.png" ancho="64" alto="64" frecuencia="100000" sonidoDestruido="blast.wav" />
         */

        Fichero f = (Fichero) em.createQuery("select x from " + Fichero.class.getName() + " x where concat(x.path, '/', x.name) = :p").setParameter("p", xml.getAttributeValue("img")).getSingleResult();

        getImages().add(f);
    }

    @Override
    public Element toXml() {
        Element xml = super.toXml();
        Fichero i = getImages().get(0);
        xml.setAttribute("img", i.getPath() + "/" + i.getName()).setAttribute("ancho", "64").setAttribute("alto", "64");
        xml.setAttribute("origen", "random")
                .setAttribute("sonidoDestruido", "blast.wav");
        return xml;
    }
}
