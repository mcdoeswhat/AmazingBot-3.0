package me.albert.amazingbot.events;

import net.mamoe.mirai.event.events.NewFriendRequestEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FriendRequestEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private NewFriendRequestEvent event;

    public FriendRequestEvent(NewFriendRequestEvent event) {
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


    public NewFriendRequestEvent getEvent() {
        return event;
    }
}
