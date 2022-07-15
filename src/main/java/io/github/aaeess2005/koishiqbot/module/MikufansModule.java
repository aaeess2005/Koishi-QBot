package io.github.aaeess2005.koishiqbot.module;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.aaeess2005.koishiqbot.util.StringUtil;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.ExternalResource;
import netscape.javascript.JSObject;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class MikufansModule extends Module{
    private static final Pattern UID=Pattern.compile("uid[1-9]\\d*");
    private static final String USER_CARD_API="http://api.bilibili.com/x/web-interface/card?photo=true&mid=";
    public MikufansModule(){
        super("!/mikufans");
    }
    @Override
    public boolean resolve(MessageEvent event) {
        String[] args= StringUtil.separateString(event.getMessage().get(1).toString()," ");
        if(UID.matcher(args[1]).find()){
            CloseableHttpClient httpClient = HttpClients.createDefault();
            RequestConfig requestConfig = RequestConfig.custom()
                    // 设置连接超时时间
                    .setConnectTimeout(14_514)
                    // 设置从连接池获取链接的超时时间
                    .setConnectionRequestTimeout(14_514)
                    .build();
            HttpGet httpGet=new HttpGet(USER_CARD_API+args[1].replace("uid",""));
            httpGet.setConfig(requestConfig);
            CloseableHttpResponse response= null;
            JsonObject json;
            try {
                response = httpClient.execute(httpGet);
                json= JsonParser.parseString(new String(response.getEntity().getContent().readAllBytes())).getAsJsonObject();
            } catch (IOException e) {
                event.getSubject().sendMessage(e.toString());
                throw new RuntimeException(e);
            }
            StringBuffer sb=new StringBuffer();
            Image profilePicture=null;
            Image spacePicture=null;
            try{
                profilePicture=event.getSubject().uploadImage(ExternalResource.create(new URL(json.getAsJsonObject("data").getAsJsonObject("card").get("face").getAsString()).openStream()));
                spacePicture=event.getSubject().uploadImage(ExternalResource.create(new URL(json.getAsJsonObject("data").getAsJsonObject("space").get("l_img").getAsString()).openStream()));
                sb.append(json.getAsJsonObject("data").getAsJsonObject("card").get("name").getAsString()).append("\n");
                sb.append("等级：").append(json.getAsJsonObject("data").getAsJsonObject("card").getAsJsonObject("level_info").get("current_level").getAsInt()).append("\n");
                sb.append("粉丝：").append(json.getAsJsonObject("data").getAsJsonObject("card").get("fans").getAsLong()).append("\n");
                sb.append("关注：").append(json.getAsJsonObject("data").getAsJsonObject("card").get("friend").getAsLong()).append("\n");
                sb.append("会员：");
                switch (json.getAsJsonObject("data").getAsJsonObject("card").getAsJsonObject("vip").get("vipType").getAsInt()){
                    case 0: sb.append("无");
                        break;
                    case 1: sb.append("月度大会员");
                        break;
                    case 2: sb.append("年度及以上大会员");
                        break;
                    default: {
                        event.getSubject().sendMessage("发生了神仙也不知道的错误");
                        throw new RuntimeException();
                    }
                }
                event.getSubject().sendMessage(new MessageChainBuilder().append(profilePicture).append(sb.toString()).append(spacePicture).build());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return true;
        }
        event.getSubject().sendMessage("No match");
        return false;
    }
    @Override
    public String getDescription() {
        return "天下肥宅是一家";
    }
}
