package io.mateu.app;

import com.google.common.collect.Lists;
import io.mateu.ui.core.client.app.AbstractArea;
import io.mateu.ui.core.client.app.AbstractModule;

import java.util.List;

public class Area extends AbstractArea {


    public Area() {
        super("Area");
    }

    @Override
    public List<AbstractModule> getModules() {
        return Lists.newArrayList(new Modulo());
    }
}
