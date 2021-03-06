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
        logger.info("Reading properties");
        loadSetting();

        logger.info("QQ ID: {}", qid);
        logger.info("Password: {}", password);
        logger.info("Protocol: {}", protocol);
        logger.info("Admin QID: {}", Arrays.toString(adminQid.toArray()));

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
            logger.error(e.getLocalizedMessage());
        }
    }

    static void subscribe(){
        bot.getEventChannel().subscribeAlways(MessageEvent.class, event -> {
            String[] key=StringUtil.separateString(event.getMessage().get(1).toString()," ");
            synchronized (ModuleManager.MODULES) {
                for (Module module : ModuleManager.MODULES) {
                    if (module.trigger.equals(key[0])) {
                        if (!module.resolve(event))
                            logger.warn("{} went wrong", module.getClass().getSimpleName());
                    }
                }
            }
        });
        bot.getEventChannel().subscribeAlways(BotUnmuteEvent.class,event -> {
            event.getGroup().sendMessage("??????"+event.getOperator().getNick()+"??????\n????????????????????????:)");
        });
        bot.getEventChannel().subscribeAlways(MemberJoinEvent.class, event -> {
            MessageChainBuilder mcb=new MessageChainBuilder();
            mcb.append("??????").append(new At(event.getMember().getId())).append("????????????\n");
            mcb.append("?????? !/help ????????????");
            event.getGroup().sendMessage(mcb.build());
        });
        bot.getEventChannel().subscribeAlways(MemberLeaveEvent.class, event -> {
            MessageChainBuilder mcb=new MessageChainBuilder();
            mcb.append(event.getMember().getNick()).append(" ????????????");
            event.getGroup().sendMessage(mcb.build());
        });
        bot.getEventChannel().subscribeAlways(BotGroupPermissionChangeEvent.class, event -> {
            String permissionOrigin=null;
            switch (event.getOrigin().getLevel()){
                case 0:permissionOrigin="??????";break;
                case 1:permissionOrigin="?????????";break;
                case 2:permissionOrigin="??????";break;
            }
            String permissionNew=null;
            switch (event.getNew().getLevel()){
                case 0:permissionNew="??????";break;
                case 1:permissionNew="?????????";break;
                case 2:permissionNew="??????";break;
            }
            MessageChainBuilder mcb=new MessageChainBuilder();
            mcb.append("???????????????").append(permissionOrigin).append("->").append(permissionNew);
            event.getGroup().sendMessage(mcb.build());
        });
        bot.getEventChannel().subscribeAlways(MemberPermissionChangeEvent.class, event -> {
            String permissionOrigin=null;
            switch (event.getOrigin().getLevel()){
                case 0:permissionOrigin="??????";break;
                case 1:permissionOrigin="?????????";break;
                case 2:permissionOrigin="??????";break;
            }
            String permissionNew=null;
            switch (event.getNew().getLevel()){
                case 0:permissionNew="??????";break;
                case 1:permissionNew="?????????";break;
                case 2:permissionNew="??????";break;
            }
            MessageChainBuilder mcb=new MessageChainBuilder();
            mcb.append(new At(event.getMember().getId())).append("???????????????").append(permissionOrigin).append("->").append(permissionNew);
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
