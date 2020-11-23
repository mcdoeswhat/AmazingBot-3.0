package me.albert.amazingbot.events;

import me.albert.amazingbot.bot.Bot;
import net.mamoe.mirai.message.FriendMessageEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PrivateMessageEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private String msg;
    private Long userID;
    private String rawMessage;
    private FriendMessageEvent event;

    public PrivateMessageEvent(String msg, Long userID, String rawMessage, FriendMessageEvent event) {
        super(true);
        this.userID = userID;
        this.msg = msg;
        this.rawMessage = rawMessage;
        this.event = event;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public void response(String msg) {
        Bot.getApi().sendPrivateMsg(String.valueOf(userID), msg);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
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

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean b) {

    }

    public FriendMessageEvent getEvent() {
        return event;
    }
}
