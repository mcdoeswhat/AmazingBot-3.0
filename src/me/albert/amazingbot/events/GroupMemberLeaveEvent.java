package me.albert.amazingbot.events;

import net.mamoe.mirai.event.events.MemberLeaveEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GroupMemberLeaveEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private MemberLeaveEvent event;

    public GroupMemberLeaveEvent(MemberLeaveEvent event) {
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


    public MemberLeaveEvent getEvent() {
        return event;
    }
}
