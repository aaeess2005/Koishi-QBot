package io.github.aaeess2005.koishiqbot;

import io.github.aaeess2005.koishiqbot.module.HelloWorldModule;
import io.github.aaeess2005.koishiqbot.module.HelpModule;
import io.github.aaeess2005.koishiqbot.module.InfoModule;
import io.github.aaeess2005.koishiqbot.module.Module;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public static final List<Module> MODULES = new ArrayList<>();

    static {
        MODULES.add(new HelpModule());
        MODULES.add(new InfoModule());
        MODULES.add(new HelloWorldModule());
    }
}
