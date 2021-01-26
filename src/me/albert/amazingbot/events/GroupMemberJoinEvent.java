package me.albert.amazingbot.events;

import net.mamoe.mirai.event.events.MemberJoinEvent;

public class GroupMemberJoinEvent extends ABEvent {

    private final MemberJoinEvent event;

    public GroupMemberJoinEvent(MemberJoinEvent event) {
        super(event);
        this.event = event;
    }

    public MemberJoinEvent getEvent() {
        return event;
    }
}
