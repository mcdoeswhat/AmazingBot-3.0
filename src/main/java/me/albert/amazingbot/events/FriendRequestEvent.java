package me.albert.amazingbot.events;

import net.mamoe.mirai.event.events.NewFriendRequestEvent;

public class FriendRequestEvent extends ABEvent {

    private final NewFriendRequestEvent event;

    public FriendRequestEvent(NewFriendRequestEvent event) {
        super(event);
        this.event = event;
    }


    public NewFriendRequestEvent getEvent() {
        return event;
    }
}
