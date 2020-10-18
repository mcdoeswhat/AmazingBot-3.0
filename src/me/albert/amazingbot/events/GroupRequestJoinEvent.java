package me.albert.amazingbot.events;

import net.mamoe.mirai.event.events.MemberJoinRequestEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GroupRequestJoinEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private MemberJoinRequestEvent event;

    public GroupRequestJoinEvent(MemberJoinRequestEvent event) {
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


    public MemberJoinRequestEvent getEvent() {
        return event;
    }
}
