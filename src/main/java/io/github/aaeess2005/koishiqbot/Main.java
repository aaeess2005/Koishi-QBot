package io.github.aaeess2005.koishiqbot;

import io.github.aaeess2005.koishiqbot.module.Module;
import io.github.aaeess2005.koishiqbot.util.FileUtil;
import io.github.aaeess2005.koishiqbot.util.StringUtil;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.BotConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

import static io.github.aaeess2005.koishiqbot.Setting.*;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    static Bot bot;

    public static void main(String[] args) {
        loadProperties();

        logger.info("QQ ID: " + qid);
        logger.info("Password: " + password);
        logger.info("Protocol: " + protocol);

        openConsole();

        bot = BotFactory.INSTANCE.newBot(qid, password, new BotConfiguration() {{
            setProtocol(protocol);
            setHeartbeatStrategy(HeartbeatStrategy.STAT_HB);
            setWorkingDir(FileUtil.getFile(
                    SharedConstant.WORKING_DIR + File.separator + "qq",
                    true
            ));
            fileBasedDeviceInfo();
        }});
        bot.login();

        try {
            subscribe();
        } catch (Exception e) {

        }
    }

    static void subscribe(){
        bot.getEventChannel().subscribeAlways(MessageEvent.class, event -> {
            String[] key=StringUtil.separateString(event.getMessage().get(1).contentToString()," ");
            synchronized (ModuleManager.MODULES) {
                for (Module module : ModuleManager.MODULES) {
                    if (module.trigger.equals(key[0])) {
                        if (!module.resolve(event))
                            logger.warn(module.getClass().getSimpleName() + " went wrong");
                    }
                }
            }
        });
        bot.getEventChannel().subscribeAlways(BotUnmuteEvent.class,event -> {
            event.getGroup().sendMessage("已被"+event.getOperator().getNick()+"解禁\n自由的感觉就是好:)");
        });
        bot.getEventChannel().subscribeAlways(MemberJoinEvent.class, event -> {
            MessageChainBuilder mcb=new MessageChainBuilder();
            mcb.append("欢迎").append(new At(event.getMember().getId())).append("加入群聊\n");
            mcb.append("输入 !/help 获得帮助");
            event.getGroup().sendMessage(mcb.build());
        });
        bot.getEventChannel().subscribeAlways(MemberLeaveEvent.class, event -> {
            MessageChainBuilder mcb=new MessageChainBuilder();
            mcb.append(event.getMember().getNick()).append(" 离开群聊");
            event.getGroup().sendMessage(mcb.build());
        });
        bot.getEventChannel().subscribeAlways(BotGroupPermissionChangeEvent.class, event -> {
            String permissionOrigin=null;
            switch (event.getOrigin().getLevel()){
                case 0:permissionOrigin="成员";break;
                case 1:permissionOrigin="管理员";break;
                case 2:permissionOrigin="群主";break;
            }
            String permissionNew=null;
            switch (event.getNew().getLevel()){
                case 0:permissionNew="成员";break;
                case 1:permissionNew="管理员";break;
                case 2:permissionNew="群主";break;
            }
            MessageChainBuilder mcb=new MessageChainBuilder();
            mcb.append("权限改变：").append(permissionOrigin).append("->").append(permissionNew);
            event.getGroup().sendMessage(mcb.build());
        });
        bot.getEventChannel().subscribeAlways(MemberPermissionChangeEvent.class, event -> {
            String permissionOrigin=null;
            switch (event.getOrigin().getLevel()){
                case 0:permissionOrigin="成员";break;
                case 1:permissionOrigin="管理员";break;
                case 2:permissionOrigin="群主";break;
            }
            String permissionNew=null;
            switch (event.getNew().getLevel()){
                case 0:permissionNew="成员";break;
                case 1:permissionNew="管理员";break;
                case 2:permissionNew="群主";break;
            }
            MessageChainBuilder mcb=new MessageChainBuilder();
            mcb.append(new At(event.getMember().getId())).append("权限改变：").append(permissionOrigin).append("->").append(permissionNew);
            event.getGroup().sendMessage(mcb.build());
        });
    }

    static void openConsole(){
        Scanner scanner=new Scanner(System.in);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    switch (scanner.nextLine()){
                        case "stop":{
                            System.exit(0);
                            break;
                        }
                        case "gc":{
                            System.gc();
                            break;
                        }
                        default:{
                            System.out.println("Unknown command");
                            break;
                        }
                    }
                }
            }
        }).start();
    }
}
