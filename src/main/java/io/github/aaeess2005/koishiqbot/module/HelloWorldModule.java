package io.github.aaeess2005.koishiqbot.module;

import net.mamoe.mirai.event.events.MessageEvent;

public class HelloWorldModule extends Module {

    public HelloWorldModule() {
        super("!/helloworld");
    }

    @Override
    public boolean resolve(MessageEvent context, String key) {
        context.getSubject().sendMessage("hello, world");
        return true;
    }

    @Override
    public String getDescription() {
        return "输出hello, world";
    }
}
