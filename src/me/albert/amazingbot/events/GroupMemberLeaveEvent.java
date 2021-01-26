package me.albert.amazingbot.events;

import net.mamoe.mirai.event.events.MemberLeaveEvent;

public class GroupMemberLeaveEvent extends ABEvent {

    private final MemberLeaveEvent event;

    public GroupMemberLeaveEvent(MemberLeaveEvent event) {
        super(event);
        this.event = event;
    }


    public MemberLeaveEvent getEvent() {
        return event;
    }
}
