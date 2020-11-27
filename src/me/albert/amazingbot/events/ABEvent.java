package me.albert.amazingbot.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ABEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private net.mamoe.mirai.event.Event event;

    public ABEvent(net.mamoe.mirai.event.Event event) {
        super(true);
        this.event = event;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public net.mamoe.mirai.event.Event getEvent() {
        return event;
    }
}
