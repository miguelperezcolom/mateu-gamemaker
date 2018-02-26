package io.mateu.app.modelo.gamemaker;

import io.mateu.app.modelo.authentication.Audit;
import io.mateu.app.modelo.authentication.User;
import io.mateu.app.modelo.population.Populator;
import io.mateu.erp.model.util.Constants;
import io.mateu.erp.model.util.Helper;
import io.mateu.erp.model.util.JPATransaction;
import org.jdom2.input.SAXBuilder;

import javax.persistence.EntityManager;

public class Tester {

    public static void main(String[] args) throws Throwable {


        System.setProperty("appconf", "/home/miguel/IdeaProjects/mateu-gamemaker/app/src/main/resources/appconf.properties");


        //Populator.main(args);

        if (false) Helper.transact(new JPATransaction() {
            @Override
            public void run(EntityManager em) throws Throwable {

                Game g = new Game();
                em.persist(g);

                g.setAudit(new Audit(em.find(User.class, "admin")));

                g.setName("Test");

                GithubRepository r;
                g.getRepositories().add(r = new GithubRepository());
                em.persist(r);
                r.setUrl("https://github.com/miguelperezcolom/game-test1.git");

            }
        });

        if (false) Helper.transact(new JPATransaction() {
            @Override
            public void run(EntityManager em) throws Throwable {

                em.find(GithubRepository.class, 1l).getContent(em);

            }
        });


        if (false) Helper.transact(new JPATransaction() {
            @Override
            public void run(EntityManager em) throws Throwable {

                Game g = em.find(Game.class, 1l);

                g.fill(em, new SAXBuilder().build(getClass().getResourceAsStream("/samples/juego.xml")).getRootElement());

            }
        });


        if (true) Helper.transact(new JPATransaction() {
            @Override
            public void run(EntityManager em) throws Throwable {

                em.find(Game.class, 1l).publish(em);

            }
        });


    }

}
