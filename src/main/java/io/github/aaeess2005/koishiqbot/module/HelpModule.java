package io.github.aaeess2005.koishiqbot.module;

import io.github.aaeess2005.koishiqbot.ModuleManager;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;

public class HelpModule extends Module{
    public HelpModule(){
        super("!/help");
    }
    @Override
    public boolean resolve(MessageEvent event){
        MessageChainBuilder mcb=new MessageChainBuilder();
        mcb.append("可用的Module：");
        for (Module module: ModuleManager.MODULES){
            mcb.append("\n");
            mcb.append(module.trigger).append("  ").append(module.getDescription());
        }
        event.getSubject().sendMessage(mcb.build());
        return true;
    }
    @Override
    public String getDescription() {
        return "显示帮助";
    }

}
