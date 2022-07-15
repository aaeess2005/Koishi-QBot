package io.github.aaeess2005.koishiqbot.module;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.aaeess2005.koishiqbot.util.StringUtil;
import io.github.aaeess2005.koishiqbot.util.minecraft.ping.ServerPing;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.ForwardMessageBuilder;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class MCJEServerPingModule extends Module {
    public MCJEServerPingModule() {
        super("!/mcping");
    }

    @Override
    public boolean resolve(MessageEvent event) {
        List<String> tmp = new ArrayList<>();
        for (String s : StringUtil.separateString(event.getMessage().get(1).contentToString(), " ")) {
            if (!s.equals(""))
                tmp.add(s);
        }
        String[] key = tmp.toArray(new String[0]);

        String host = key[1];
        int port = Integer.parseInt(key[2]);
        InetSocketAddress address = new InetSocketAddress(host, port);
        JsonObject json = null;
        String imgSrc;
        try {
            json = new ServerPing(address).fetchData();
        } catch (IOException ex) {
            ForwardMessageBuilder fmb=new ForwardMessageBuilder(event.getSubject());
            fmb.add(event.getBot().getId(),"Reporter",new PlainText(ex.toString()));
            event.getSubject().sendMessage(fmb.build());
            throw new RuntimeException(ex);
        }

        StringBuilder sb = new StringBuilder();

        try {
            sb.append("服务器: ").append(address.getHostName()).append(":").append(port).append("\n");
            sb.append("延迟: ").append(json.get("ping").getAsLong()).append("ms\n");
            sb.append("版本: ").append(json.getAsJsonObject("version").get("name").getAsString());
            sb.append("(协议版本 ").append(json.getAsJsonObject("version").get("protocol").getAsString()).append(")\n");

            sb.append("玩家数量: ")
                    .append(json.getAsJsonObject("players").get("online").getAsLong()).append("/")
                    .append(json.getAsJsonObject("players").get("max").getAsLong()).append("\n");

            sb.append("目前玩家: ").append("\n");
            JsonArray players = json.getAsJsonObject("players").getAsJsonArray("sample");
            for (JsonElement obj : players) {
                sb.append(obj.getAsJsonObject().get("name").getAsString()).append("\n");
                sb.append(obj.getAsJsonObject().get("id").getAsString()).append("\n\n");
            }

            if (players.size() < json.getAsJsonObject("players").get("online").getAsLong()) {
                sb.append("(仅显示一部分)").append("\n\n");
            }

            sb.append((json.getAsJsonObject("description").get("text").getAsString()).replaceAll("§\\w", ""));
            imgSrc=json.get("favicon").getAsString().split(",", 2)[1];
        } catch (Exception e) {
            ForwardMessageBuilder fmb=new ForwardMessageBuilder(event.getSubject());
            fmb.add(event.getBot().getId(),"Reporter",new PlainText("非标准格式"));
            fmb.add(event.getBot().getId(),"Reporter",new PlainText(e.toString()));
            event.getSubject().sendMessage(fmb.build());
            throw new RuntimeException(e);
        }
        ByteArrayInputStream bais=new ByteArrayInputStream(Base64.decodeBase64(imgSrc));
        Image favicon= null;
        try {
            favicon = event.getSubject().uploadImage(ExternalResource.create(bais));
        } catch (IOException e) {
            ForwardMessageBuilder fmb=new ForwardMessageBuilder(event.getSubject());
            fmb.add(event.getBot().getId(),"Reporter",new PlainText("图片可能有错误"));
            fmb.add(event.getBot().getId(),"Reporter",new PlainText(e.toString()));
            event.getSubject().sendMessage(fmb.build());
        }

        MessageChainBuilder mcb=new MessageChainBuilder();

        mcb.append(sb.toString().trim()).append(favicon);
        event.getSubject().sendMessage(mcb.build());
        return true;
    }

    @Override
    public String getDescription() {
        return "获取MCJE服务器状态";
    }
}
