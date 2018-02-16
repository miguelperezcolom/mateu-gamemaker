package io.mateu.app;

import com.google.common.collect.Lists;
import io.mateu.ui.core.client.app.AbstractApplication;
import io.mateu.ui.core.client.app.AbstractArea;

import java.util.List;

/**
 * Hello world!
 *
 */
public class App extends AbstractApplication
{

    @Override
    public String getName() {
        return "App";
    }

    @Override
    public List<AbstractArea> getAreas() {
        return Lists.newArrayList(new Area());
    }
}
