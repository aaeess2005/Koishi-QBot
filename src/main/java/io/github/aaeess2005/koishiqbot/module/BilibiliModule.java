package io.github.aaeess2005.koishiqbot.module;

import net.mamoe.mirai.event.events.MessageEvent;

public class BilibiliModule extends Module{
    public BilibiliModule(){
        super("!/mikufans");
    }
    @Override
    public boolean resolve(MessageEvent event) {
        //TODO mikufans
        return false;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
