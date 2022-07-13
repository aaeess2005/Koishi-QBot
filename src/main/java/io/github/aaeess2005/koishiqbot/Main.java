package io.github.aaeess2005.koishiqbot;

import io.github.aaeess2005.koishiqbot.module.Module;
import io.github.aaeess2005.koishiqbot.util.FileUtil;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.utils.BotConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
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

        bot.getEventChannel().subscribeAlways(MessageEvent.class, event -> {
            int index=event.getMessage().get(1).contentToString().indexOf(" ");
            String key;
            if (index!=-1){
                key=event.getMessage().get(1).contentToString().substring(0,index);
            }else {
                key=event.getMessage().get(1).contentToString();
            }

            synchronized (ModuleManager.MODULES) {
                for (Module module : ModuleManager.MODULES) {
                    if (module.trigger.equals(key)) {
                        if (!module.resolve(event, key))
                            logger.warn(module.getClass().getSimpleName() + "went wrong");
                    }
                }
            }
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
