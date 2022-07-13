package io.github.aaeess2005.koishiqbot.module;

import io.github.aaeess2005.koishiqbot.ModuleManager;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;

public class HelpModule extends Module{

    public HelpModule(){
        super("!/help");
    }
    @Override
    public boolean resolve(MessageEvent context, String key) {
        MessageChainBuilder mcb=new MessageChainBuilder();
        mcb.append("可用指令：");
        for (Module module: ModuleManager.MODULES){
            mcb.append("\n");
            mcb.append(module.trigger).append("  ").append(module.getDescription());
        }
        context.getSubject().sendMessage(mcb.build());
        return true;
    }

    @Override
    public String getDescription() {
        return "显示帮助";
    }
}
