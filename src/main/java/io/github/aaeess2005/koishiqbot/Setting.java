package io.github.aaeess2005.koishiqbot;

import com.google.gson.*;
import io.github.aaeess2005.koishiqbot.util.FileUtil;
import io.github.aaeess2005.koishiqbot.util.ResourceUtil;
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
    private static final File SETTING_FILE=FileUtil.getFile(SharedConstant.WORKING_DIR+File.separator+"setting.json",false);
    private static final String DEFAULT_SETTING_FILE_PATH="default"+File.separator+"setting.json";
    public static long qid;
    public static String password;
    public static BotConfiguration.MiraiProtocol protocol;
    public static List<Long> adminQid=new ArrayList<>();

    static void loadSetting() {
        //Reading JSON
        JsonObject json=null;
        byte[] b=null;
        try(FileInputStream input=new FileInputStream(SETTING_FILE)){
            b=input.readAllBytes();
        }catch (IOException e) {
            e.printStackTrace();
        }

        //Resolve JSON
        try {
            json = JsonParser.parseString(new String(b,StandardCharsets.UTF_8)).getAsJsonObject();
            //Bot QID
            qid=json.getAsJsonObject("bot").get("qid").getAsLong();
            //Bot Password
            password=json.getAsJsonObject("bot").get("password").getAsString();
            //Bot Protocol
            switch (json.getAsJsonObject("bot").get("protocol").getAsString()){
                case "ANDROID_PAD":protocol= BotConfiguration.MiraiProtocol.ANDROID_PAD;
                break;
                case "ANDROID_PHONE":protocol= BotConfiguration.MiraiProtocol.ANDROID_PHONE;
                break;
                case "IPAD":protocol= BotConfiguration.MiraiProtocol.IPAD;
                break;
                default:throw new Exception();
            }
            for (JsonElement je : json.get("admins").getAsJsonArray()) {
                adminQid.add(je.getAsLong());
            }
        } catch (Exception e) {
            logger.error("Please check setting.json");
            createDefaultSetting();
            throw new RuntimeException(e);
        }
    }
    private static void createDefaultSetting(){
        try (OutputStream output = new FileOutputStream(SETTING_FILE)){
            output.write(ResourceUtil.getResourceAsBytes(DEFAULT_SETTING_FILE_PATH));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
