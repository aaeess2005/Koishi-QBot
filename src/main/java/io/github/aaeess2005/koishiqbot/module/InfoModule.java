package io.github.aaeess2005.koishiqbot.module;

import io.github.aaeess2005.koishiqbot.ModuleManager;
import io.github.aaeess2005.koishiqbot.SharedConstant;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class InfoModule extends Module{
    private Logger logger= LoggerFactory.getLogger(InfoModule.class);

    public InfoModule(){
        super("!/info");
    }
    @Override
    public boolean resolve(MessageEvent context, String key) {
        Image profilePicture = null;
        try {
            profilePicture= context.getSubject().uploadImage(ExternalResource.create(new URL(context.getBot().getAvatarUrl()).openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        MessageChainBuilder mcb=new MessageChainBuilder();
        mcb.append("Koishi's QBot by aaeess2005");
        mcb.append(profilePicture);
        mcb.append("版本：").append(SharedConstant.VERSION_NAME).append("\n");
        mcb.append("操作系统：").append(System.getProperty("os.name")+" ").append(System.getProperty("os.arch")+" ").append(System.getProperty("os.version")).append("\n");
        mcb.append("内存状况：").append((Runtime.getRuntime().freeMemory()/1024/1024)+"M/").append((Runtime.getRuntime().maxMemory()/1024/1024)+"M").append("\n");
        mcb.append("项目已在GitHub上开源：https://github.com/aaeess2005/Koishi-QBot");

        context.getSubject().sendMessage(mcb.build());
        return true;
    }

    @Override
    public String getDescription() {
        return "显示系统信息";
    }
}
