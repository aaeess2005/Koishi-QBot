package io.github.aaeess2005.koishiqbot;

import io.github.aaeess2005.koishiqbot.util.FileUtil;
import net.mamoe.mirai.utils.BotConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.util.Properties;

public class Setting {
    private static final Logger logger = LoggerFactory.getLogger(Setting.class);
    public static long qid;
    public static String password;
    public static BotConfiguration.MiraiProtocol protocol;

    static void loadProperties() {
        Properties properties = new Properties();
        InputStream propertiesInputStream;
        try {
            propertiesInputStream = new FileInputStream(FileUtil.getFile(SharedConstant.WORKING_DIR + File.separator + "bot.properties",false));
        } catch (FileNotFoundException e) {
            logger.error("I wonder how did this error happen :/");
            throw new RuntimeException(e);
        }
        try {
            properties.load(propertiesInputStream);

            if (properties.getProperty("qid") == null || properties.getProperty("password") == null || properties.getProperty("protocol") == null) {
                logger.error("Properties error. Please check bot.properties");
                createProperties();
                throw new RuntimeException();
            }

            long qid = Long.parseLong(properties.getProperty("qid"));
            String password = properties.getProperty("password");
            String protocol = properties.getProperty("protocol");
            Setting.qid = qid;
            Setting.password = password;
            switch (protocol) {
                case "ANDROID_PHONE": {
                    Setting.protocol = BotConfiguration.MiraiProtocol.ANDROID_PHONE;
                    break;
                }
                case "ANDROID_PAD": {
                    Setting.protocol = BotConfiguration.MiraiProtocol.ANDROID_PAD;
                    break;
                }
                case "IPAD": {
                    Setting.protocol = BotConfiguration.MiraiProtocol.IPAD;
                    break;
                }
                case "MACOS": {
                    Setting.protocol = BotConfiguration.MiraiProtocol.MACOS;
                    break;
                }
                default: {
                    logger.error("Properties error. Please check bot.properties");
                    createProperties();
                    throw new RuntimeException();
                }
            }
            propertiesInputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NumberFormatException e) {
            logger.error("Properties error. Please check bot.properties");
            createProperties();
            throw new RuntimeException();
        }
    }

    static void createProperties() {
        try {
            Properties properties = new Properties();
            InputStream propertiesInputStream = new FileInputStream(FileUtil.getFile(SharedConstant.WORKING_DIR + File.separator + "bot.properties",false));
            properties.load(propertiesInputStream);

            properties.setProperty("qid", "123456");
            properties.setProperty("password", "example");
            properties.setProperty("protocol", "ANDROID_PHONE, ANDROID_PAD, IPAD, MACOS");

            propertiesInputStream.close();
            OutputStream out = new FileOutputStream(SharedConstant.WORKING_DIR + File.separator + "bot.properties");
            properties.store(out, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
