package io.mateu.app.modelo.authentication;

import io.mateu.app.modelo.common.Fichero;
import io.mateu.ui.mdd.server.annotations.Ignored;
import io.mateu.ui.mdd.server.annotations.ListColumn;
import io.mateu.ui.mdd.server.annotations.Output;
import io.mateu.ui.mdd.server.interfaces.WithTriggers;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * holder for users of our erp. It can be an internal user or a user created for a customer or a supplier
 *
 *
 * Created by miguel on 13/9/16.
 */
@Entity
@Table(name = "_USER")
@Getter@Setter
public class User implements WithTriggers {

    @Embedded
    @Output
    private Audit audit = new Audit();

    /**
     * login must always be uppercase. It is the primary key.
     */
    @Id
    @ListColumn("Login")
    @NotNull
    private String login;

    @ListColumn("Name")
    @NotNull
    private String name;

    @ListColumn("Email")
    @NotNull
    private String email;

    @Ignored
    private String password;

    @ListColumn("Status")
    @NotNull
    private USER_STATUS status;

    @OneToMany
    @Ignored
    private List<Permission> permissions = new ArrayList<Permission>();


    @Ignored
    @ManyToOne
    private Fichero photo;


    @ManyToMany
    private List<Account> accounts = new ArrayList<>();


    @Override
    public void beforeSet(EntityManager em, boolean isNew) throws Exception {

    }

    @Override
    public void afterSet(EntityManager em, boolean isNew) throws Exception {
        setPassword("1");
    }

    @Override
    public void beforeDelete(EntityManager em) throws Exception {

    }

    @Override
    public void afterDelete(EntityManager em) throws Exception {

    }

}
