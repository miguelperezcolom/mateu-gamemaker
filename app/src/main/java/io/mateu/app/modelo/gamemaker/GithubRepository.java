package io.mateu.app.modelo.gamemaker;

import com.google.common.base.Strings;
import com.google.common.io.Files;
import io.mateu.app.modelo.common.Fichero;
import io.mateu.app.util.Helper;
import io.mateu.ui.mdd.server.annotations.Action;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

@Entity
@Getter@Setter
public class GithubRepository {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String url;

    @Column(name = "_user")
    private String user;

    private String password;

    @Action(name = "Get content")
    public void getContent(EntityManager em) throws IOException {

        File d = new File(System.getProperty("tmpdir", System.getProperty("java.io.tmpdir")) + "/github_" + getId());

        if (!d.exists()) {
            d.mkdirs();
            d.mkdir();
        }

        File s = new File(d.getAbsolutePath() + "/github.sh");
        if (true || !s.exists()) {

            String auth = "";
            if (!Strings.isNullOrEmpty(getUser())) {
                auth += getUser().replaceAll("@", "%26");
                if (!Strings.isNullOrEmpty(getPassword())) auth += ":" + getPassword();
                if (!Strings.isNullOrEmpty(auth)) auth += "@";
            }


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
                    "  git clone https://" + auth + "github.com/miguelperezcolom/game-test1.git data\n" +
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
        addAll(em, dd, new File(dd.getAbsolutePath() + "/img"));
        addAll(em, dd, new File(dd.getAbsolutePath() + "/sound"));
    }

    private void addAll(EntityManager em, File d, File ddi) throws IOException {
        if (ddi.exists() && ddi.isDirectory()) for (File f : ddi.listFiles()) {
            if (f.isDirectory()) addAll(em, d, f);
            else {

                String path = ddi.getAbsolutePath().replaceFirst(d.getAbsolutePath(), "");

                Fichero fich = null;
                try {
                    List<Fichero> l = em.createQuery("select x from " + Fichero.class.getName() + " x where x.name = :n and x.path = :p").setParameter("n", f.getName()).setParameter("p", path).getResultList();
                    if (l.size() > 0) fich = l.get(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (fich == null) {
                    fich = new Fichero();
                    em.persist(fich);
                }
                fich.setName(f.getName());
                fich.setPath(path);
                fich.setBytes(java.nio.file.Files.readAllBytes(f.toPath()));
            }
        }
    }
}
