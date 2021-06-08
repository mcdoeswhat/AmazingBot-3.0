package me.albert.amazingbot.events;

import org.bukkit.Bukkit;

public class GroupMessageEvent extends MessageReceiveEvent {
    private final String msg;
    private final Long userID;
    private final Long groupID;
    private final String rawMessage;
    private final net.mamoe.mirai.event.events.GroupMessageEvent event;

    public GroupMessageEvent(String msg, Long userID, Long groupID, String rawMessage, net.mamoe.mirai.event.events.GroupMessageEvent event) {
        super(msg, event);
        this.userID = userID;
        this.groupID = groupID;
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

    public Long getGroupID() {
        return groupID;
    }

    public String getRawMessage() {
        Bukkit.getLogger().info("§e[AmazingBot] 有插件在使用过时方法获取Raw信息,此方法已被删除,可能会影响到插件功能...");
        return rawMessage;
    }


    public net.mamoe.mirai.event.events.GroupMessageEvent getEvent() {
        return event;
    }
}
