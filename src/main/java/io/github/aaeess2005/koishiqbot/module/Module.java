package io.github.aaeess2005.koishiqbot.module;

import net.mamoe.mirai.event.events.MessageEvent;

public abstract class Module {
    public String trigger;
    public Module(String trigger){
        this.trigger=trigger;
    }
    public abstract boolean resolve(MessageEvent event);
    public abstract String getDescription();
}