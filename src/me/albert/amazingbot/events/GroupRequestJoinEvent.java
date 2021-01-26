package me.albert.amazingbot.events;

import net.mamoe.mirai.event.events.MemberJoinRequestEvent;

public class GroupRequestJoinEvent extends ABEvent {

    private final MemberJoinRequestEvent event;

    public GroupRequestJoinEvent(MemberJoinRequestEvent event) {
        super(event);
        this.event = event;
    }

    public MemberJoinRequestEvent getEvent() {
        return event;
    }
}
