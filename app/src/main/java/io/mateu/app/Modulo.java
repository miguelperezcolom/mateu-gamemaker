package io.mateu.app;

import com.google.common.collect.Lists;
import io.mateu.ui.core.client.app.AbstractAction;
import io.mateu.ui.core.client.app.AbstractModule;
import io.mateu.ui.core.client.app.MateuUI;
import io.mateu.ui.core.client.app.MenuEntry;

import java.util.List;

public class Modulo extends AbstractModule {
    @Override
    public String getName() {
        return "Módulo";
    }

    @Override
    public List<MenuEntry> getMenu() {
        return Lists.newArrayList(new AbstractAction("Test action") {
            @Override
            public void run() {
                MateuUI.alert("Hola mundo!!!");
            }
        });
    }
}
