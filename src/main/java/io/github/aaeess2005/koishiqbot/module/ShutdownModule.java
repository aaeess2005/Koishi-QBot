package io.github.aaeess2005.koishiqbot.module;

import io.github.aaeess2005.koishiqbot.Setting;
import net.mamoe.mirai.event.events.MessageEvent;

public class ShutdownModule extends Module{
    public ShutdownModule(){
        super("!/shutdown");
    }

    @Override
    public boolean resolve(MessageEvent event) {
        for(Long qid : Setting.adminQid){
            if (qid==event.getSender().getId()){
                System.exit(0);
            }
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "关机";
    }
}
