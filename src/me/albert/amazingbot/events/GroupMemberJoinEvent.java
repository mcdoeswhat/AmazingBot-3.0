package me.albert.amazingbot.events;

import net.mamoe.mirai.event.events.MemberJoinEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GroupMemberJoinEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private MemberJoinEvent event;

    public GroupMemberJoinEvent(MemberJoinEvent event) {
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


    public MemberJoinEvent getEvent() {
        return event;
    }
}
