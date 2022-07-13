package io.github.aaeess2005.koishiqbot.module;

import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.MessageEvent;

public class HelloWorldModule extends Module{
    @Override
    public boolean resolve(MessageEvent context, String key) {
        return false;
    }
}
