package io.mateu.app.modelo.gamemaker;

import com.google.common.base.Strings;
import com.google.common.io.Files;
import io.mateu.app.modelo.authentication.Account;
import io.mateu.app.modelo.authentication.Audit;
import io.mateu.app.modelo.authentication.User;
import io.mateu.app.modelo.common.Fichero;
import io.mateu.app.modelo.config.AppConfig;
import io.mateu.app.util.Helper;
import io.mateu.erp.model.util.JPATransaction;
import io.mateu.ui.core.shared.FileLocator;
import io.mateu.ui.mdd.server.annotations.Action;
import io.mateu.ui.mdd.server.annotations.Output;
import lombok.Getter;
import lombok.Setter;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
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
    @Output
    private Account account;

    private String name;

    @OneToMany(mappedBy = "game")
    @Output
    private List<Level> levels = new ArrayList<>();


    @OneToMany
    private List<GithubRepository> repositories = new ArrayList<>();

    public Element toXml() {

        /*
        <juego nombre="Prueba 1">

    <menu ancho="800" alto="600"/>

    <nivel nombre="Nivel 1" ancho="800" alto="600">

        <jugador img="ship1.png" ancho="64" alto="64"/>

        <malo img="alien1.png" ancho="64" alto="64" origen="random" sonidoDestruido="blast.wav"/>


        <musicax src="masai.mp3"/>

    </nivel>
</juego>
         */

        Element xml = new Element("juego").setAttribute("nombre", getName());

        xml.addContent(new Element("menu").setAttribute("ancho", "800").setAttribute("alto", "600"));

        for (Level l : getLevels()) xml.addContent(l.toXml());

        return xml;
    }

    @Action(name = "Publish")
    public void publish(EntityManager em) throws Exception {

        File d = new File(System.getProperty("tmpdir", System.getProperty("java.io.tmpdir")) + "/game_" + getId());

        System.out.println("publicando juego en " + d.getAbsolutePath());


        if (!d.exists()) {
            d.mkdirs();
            d.mkdir();
        }

        File s = new File(d.getAbsolutePath() + "/download.sh");
        if (true || !s.exists()) {

            String auth = "";
            /*
            if (!Strings.isNullOrEmpty(getUser())) {
                auth += getUser().replaceAll("@", "%26");
                if (!Strings.isNullOrEmpty(getPassword())) auth += ":" + getPassword();
                if (!Strings.isNullOrEmpty(auth)) auth += "@";
            }
            */


            Files.write("#!/bin/sh\n" +
                    "\n" +
                    "\n" +
                    "echo \"hola\"\n" +
                    "cd " + d.getAbsolutePath() + "\n" +
                    "\n" +
                    "\n" +
                    "if [ ! -d data ]; then\n" +
                    "# Control will enter here if $DIRECTORY exists.\n" +
                    "\n" +
                    "  echo \"no existe\"\n" +
                    "\n" +
                    "  git clone https://" + auth + "github.com/miguelperezcolom/game-plantilla1.git data\n" +
                    "\n" +
                    "else\n" +
                    "\n" +
                    "  echo \"existe\"\n" +
                    "\n" +
                    "  cd data\n" +
                    "  git pull\n" +
                    "\n" +
                    "fi", s, Charset.defaultCharset());

            Helper.run("chmod u+x " + s.getAbsolutePath());
        }

        Helper.run(s.getAbsolutePath());


        File dd = new File(d.getAbsolutePath() + "/data");


        for (Level l : getLevels()) {
            for (Pawn p : l.getPawns()) {
                for (Fichero f : p.getImages()) {
                    FileLocator fl = f.toFileLocator();
                    File dest = new File(d.getAbsolutePath() + "/data/android/assets/" + f.getPath() + "/" + f.getName());
                    dest.getParentFile().mkdirs();
                    Files.copy(new File(fl.getTmpPath()), dest);
                }
            }
        }

        Files.write(new XMLOutputter(Format.getPrettyFormat()).outputString(toXml()), new File(d.getAbsolutePath() + "/data/android/assets/juego.xml"), Charset.defaultCharset());



        File dh = new File(d.getAbsolutePath() + "/createweb.sh");
        if (!dh.exists()) {
            s = new File(d.getAbsolutePath() + "/createweb.sh");
            Files.write("#!/bin/sh\n" +
                    "\n" +
                    "\n" +
                    "echo \"hola\"\n" +
                    "cd " + d.getAbsolutePath() + "/data\n" +
                    "\n" +
                    "./gradlew html:dist\n" +
                    "", s, Charset.defaultCharset());

            Helper.run("chmod u+x " + s.getAbsolutePath());
            Helper.run(s.getAbsolutePath());
        }



        s = new File(d.getAbsolutePath() + "/dist.sh");
        Files.write("#!/bin/sh\n" +
                "\n" +
                "\n" +
                "echo \"hola\"\n" +
                "cd " + d.getAbsolutePath() + "/data\n" +
                "\n" +
                "cp -rf android/assets html/build/dist\n" +
                "./gradlew android:assembleRelease\n" +
                "", s, Charset.defaultCharset());

        Helper.run("chmod u+x " + s.getAbsolutePath());
        Helper.run(s.getAbsolutePath());


        s = new File(AppConfig.get(em).getNginxConfigDirectory() + "/game_"  + getId() + ".conf");
        Files.write("#\n" +
                "# A virtual host using mix of IP-, name-, and port-based configuration\n" +
                "#\n" +
                "\n" +
                "server {\n" +
                "    #listen       80;\n" +
                "    #listen       somename:8080;\n" +
                "    #server_name  somename  alias  another.alias;\n" +
                "    server_name " + getId() + ".gamemaker.mateu.io" + ";\n" +
                "\n" +
                "    location / {\n" +
                "            root " + d.getAbsolutePath() + "/data/html/build/dist" + ";\n" +
                "    }\n" +
                "\n" +
                "}\n", s, Charset.defaultCharset());

        Helper.run(AppConfig.get(em).getNginxReloadCommand());

    }


    public Game() {

    }

    public Game(EntityManager em, Element xml) {
        fill(em, xml);
    }


    public static void main(String[] args) throws Throwable {

        System.setProperty("appconf", "/Users/miguel/Documents/mateu/mateu-gamemaker/appconf.properties");

        io.mateu.erp.model.util.Helper.transact(new JPATransaction() {
            @Override
            public void run(EntityManager em) throws Throwable {

                em.find(Game.class, 1l).publish(em);

            }
        });
    }

    public void fill(EntityManager em, Element xml) {

        for (Level o : getLevels()) {
            em.remove(o);
            em.remove(o.getPlayer());
            o.getPawns().forEach((p) -> em.remove(p));
        }
        getLevels().clear();


        /*
        <juego nombre="ssss">
    <menu ancho="800" alto="600" />
    <nivel nombre="l1" ancho="800" alto="600">
        <jugador img="nave1x.png" ancho="64" alto="64" />1
        <malo img="marciano1x.png" ancho="64" alto="64" frecuencia="100000" sonidoDestruido="blast.wav" />
        <malo img="marciano2x.png" ancho="64" alto="64" frecuencia="500000" sonidoDestruido="blast.wav" />
        <malo img="marciano3x.png" ancho="64" alto="64" frecuencia="500000" sonidoDestruido="blast.wav" />
        <musica src="masai.mp3" />
    </nivel>
</juego>
         */
        for (Element en : xml.getChildren("nivel")) {
            Level l;
            getLevels().add(l = new Level(em, en));
            em.persist(l);
        }
    }
}
