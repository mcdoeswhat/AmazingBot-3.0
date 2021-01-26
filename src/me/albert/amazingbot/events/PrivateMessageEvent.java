package me.albert.amazingbot.events;

import net.mamoe.mirai.event.events.FriendMessageEvent;
import org.bukkit.Bukkit;

public class PrivateMessageEvent extends MessageReceiveEvent {

    private final String msg;
    private final Long userID;
    private final String rawMessage;
    private final FriendMessageEvent event;

    public PrivateMessageEvent(String msg, Long userID, String rawMessage, FriendMessageEvent event) {
        super(msg, event);
        this.userID = userID;
        this.msg = msg;
        this.rawMessage = rawMessage;
        this.event = event;
    }

    public String getMsg() {
        return msg;
    }

    public Long getUserID() {
        return userID;
    }


    public String getRawMessage() {
        Bukkit.getLogger().info("§e[AmazingBot] 有插件在使用过时方法获取Raw信息,此方法已被删除,可能会影响到插件功能...");
        return rawMessage;
    }


    public FriendMessageEvent getEvent() {
        return event;
    }
}
