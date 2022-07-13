package io.github.aaeess2005.koishiqbot.module;

import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.MessageEvent;

public abstract class Module {
    public abstract boolean resolve(MessageEvent context, String key);
}
