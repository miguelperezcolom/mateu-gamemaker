package io.mateu.app;

import com.google.common.collect.Lists;
import io.mateu.app.modelo.gamemaker.Game;
import io.mateu.app.modelo.gamemaker.Level;
import io.mateu.app.modelo.gamemaker.Pawn;
import io.mateu.ui.core.client.app.AbstractAction;
import io.mateu.ui.core.client.app.AbstractModule;
import io.mateu.ui.core.client.app.MateuUI;
import io.mateu.ui.core.client.app.MenuEntry;
import io.mateu.ui.mdd.client.MDDAction;

import java.util.List;

public class Modulo extends AbstractModule {
    @Override
    public String getName() {
        return "Módulo";
    }

    @Override
    public List<MenuEntry> getMenu() {
        return Lists.newArrayList(

                new MDDAction("Games", Game.class),
                new MDDAction("Levels", Level.class),
                new MDDAction("Pawns", Pawn.class)

        );
    }
}
