package me.albert.amazingbot.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MessageReceiveEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private String msg;

    public MessageReceiveEvent(String msg) {
        super(true);
        this.msg = msg;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean b) {

    }
}
