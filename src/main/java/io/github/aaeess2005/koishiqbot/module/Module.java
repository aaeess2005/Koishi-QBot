package io.github.aaeess2005.koishiqbot.module;

import net.mamoe.mirai.event.events.MessageEvent;

import java.util.regex.Pattern;

public abstract class Module {
    public Pattern[] pattern;
    public String trigger;
    public Module(String trigger){
        this.trigger=trigger;
    }
    public Module(Pattern ...pattern){
        this.pattern=pattern;
    }
    public abstract boolean resolve(MessageEvent event);
    public abstract String getDescription();
}