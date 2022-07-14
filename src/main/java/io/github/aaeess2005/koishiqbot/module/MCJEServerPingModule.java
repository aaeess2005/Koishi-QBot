package io.github.aaeess2005.koishiqbot.module;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.aaeess2005.koishiqbot.util.StringUtil;
import io.github.aaeess2005.koishiqbot.util.minecraft.ping.ServerPing;
import net.mamoe.mirai.event.events.MessageEvent;

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
        try {
            json = new ServerPing(address).fetchData();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        StringBuilder sb = new StringBuilder();

        sb.append("服务器: ").append(address.getHostName()).append(":").append(port).append("\n");
        sb.append("延迟: ").append(json.get("ping").getAsLong()).append("ms\n");
        sb.append("版本: ").append(json.getAsJsonObject("version").get("name").getAsString());
        sb.append("(协议版本 ").append(json.getAsJsonObject("version").get("protocol").getAsString()).append(")\n");

        sb.append("玩家数量: ")
                .append(json.getAsJsonObject("players").get("online").getAsLong()).append("/")
                .append(json.getAsJsonObject("players").get("max").getAsLong()).append("\n");


        sb.append("目前玩家: ");
        JsonArray players=json.getAsJsonArray("sample");
        if (players!=null) {
            for (JsonElement obj : players) {
                sb.append(obj.getAsJsonObject().get("name").getAsString()).append("\n");
                sb.append(obj.getAsJsonObject().get("id").getAsString()).append("\n\n");
            }
        }

        if (json.getAsJsonArray("sample").size() < json.getAsJsonObject("players").get("online").getAsLong())
            sb.append("(仅显示一部分)").append("\n");


        sb.append((json.getAsJsonObject("description").get("text").getAsString()).replaceAll("§\\w", ""));
        event.getSubject().sendMessage(sb.toString().trim());
        return true;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
