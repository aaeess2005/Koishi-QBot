package io.github.aaeess2005.koishiqbot.module;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.*;

public class BakaModule extends Module{
    public BakaModule(){
        super("!/baka");
    }
    @Override
    public boolean resolve(MessageEvent event) {
        if (event.getSubject() instanceof Group) {
            ForwardMessageBuilder fmb = new ForwardMessageBuilder(event.getSubject());
            for (SingleMessage message : event.getMessage()) {
                if (message instanceof At) {
                    fmb.add(((At) message).getTarget(), ((At) message).getDisplay((Group) event.getSubject()).substring(1), new PlainText("全体目光向我看齐嗷"));
                    fmb.add(((At) message).getTarget(), ((At) message).getDisplay((Group) event.getSubject()).substring(1), new PlainText("我宣布个事"));
                    fmb.add(((At) message).getTarget(), ((At) message).getDisplay((Group) event.getSubject()).substring(1), new PlainText("我是个傻逼！"));
                }
            }
            event.getSubject().sendMessage(fmb.build());
            return true;
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "让群员宣布个事";
    }
}
