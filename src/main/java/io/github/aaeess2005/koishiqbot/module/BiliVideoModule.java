package io.github.aaeess2005.koishiqbot.module;

import net.mamoe.mirai.event.events.MessageEvent;

import java.util.regex.Pattern;

public class BiliVideoModule extends Module{
    public static final Pattern B_AV_VIDEO_PATTERN = Pattern.compile("[aA][vV][1-9]\\d{0,9}");
    public static final Pattern B_BV_VIDEO_PATTERN = Pattern.compile("[bB][vV][fZodR9XQDSUm21yCkr6zBqiveYah8bt4xsWpHnJE7jL5VG3guMTKNPAwcF]{10}");
    public static final Pattern B_EP_EPISODE_PATTERN = Pattern.compile("[eE][pP][1-9]\\d{0,7}");
    public static final Pattern B_SS_EPISODE_PATTERN = Pattern.compile("[sS]{2}[1-9]\\d{0,7}");
    public static final Pattern B_SHORT_LINK_PATTERN = Pattern.compile("https://b23\\.tv/[0-9a-zA-Z]+");

    public static final String BILIBILI_VIDEO_API = "https://api.bilibili.com/x/web-interface/";

    public BiliVideoModule(){
        super(B_AV_VIDEO_PATTERN,B_BV_VIDEO_PATTERN,B_EP_EPISODE_PATTERN,B_SS_EPISODE_PATTERN,B_SHORT_LINK_PATTERN);
    }
    @Override
    public boolean resolve(MessageEvent event) {
        return false;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
