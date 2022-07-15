package io.github.aaeess2005.koishiqbot;

import io.github.aaeess2005.koishiqbot.module.*;
import io.github.aaeess2005.koishiqbot.module.Module;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public static final List<Module> MODULES = new ArrayList<>();

    static {
        MODULES.add(new HelpModule());
        MODULES.add(new InfoModule());
        MODULES.add(new HelloWorldModule());
        MODULES.add(new ShutdownModule());
        MODULES.add(new BakaModule());
        MODULES.add(new MCJEServerPingModule());
        MODULES.add(new MikufansModule());
    }
}
