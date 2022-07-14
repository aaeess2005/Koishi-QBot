package io.github.aaeess2005.koishiqbot;

import com.google.gson.*;
import io.github.aaeess2005.koishiqbot.util.FileUtil;
import net.mamoe.mirai.utils.BotConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Setting {
    private static final Logger logger = LoggerFactory.getLogger(Setting.class);
    public static long qid;
    public static String password;
    public static BotConfiguration.MiraiProtocol protocol;
    public static List<Long> adminQid=new ArrayList<>();

    static void loadProperties() {
        loadBotProperties();
        loadAdminProperties();
    }
    private static void loadAdminProperties() {
        JsonObject json=null;
        byte[] b=null;
        try(FileInputStream input=new FileInputStream(FileUtil.getFile(SharedConstant.WORKING_DIR+File.separator+"admin.json",false))){
            b=input.readAllBytes();
        }catch (IOException e) {
            e.printStackTrace();
        }
        try {
            json = JsonParser.parseString(new String(b,StandardCharsets.UTF_8)).getAsJsonObject();
            for (JsonElement je : json.get("admin").getAsJsonArray()) {
                adminQid.add(je.getAsLong());
            }
        } catch (Exception e) {
            logger.error("Please check admin.json");
            createAdminProperties();
            throw new RuntimeException(e);
        }
    }
    private static void createAdminProperties(){
        JsonObject json=new JsonObject();
        JsonArray admins=new JsonArray();
        admins.add(114514);
        admins.add(314159);
        json.add("admin",admins);
        try(FileOutputStream fos=new FileOutputStream(FileUtil.getFile(SharedConstant.WORKING_DIR+File.separator+"admin.json",false))){
            fos.write(new Gson().toJson(json).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void loadBotProperties(){
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
                createBotProperties();
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
                    createBotProperties();
                    throw new RuntimeException();
                }
            }
            propertiesInputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NumberFormatException e) {
            logger.error("Properties error. Please check bot.properties");
            createBotProperties();
            throw new RuntimeException();
        }
    }
    private static void createBotProperties(){
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
