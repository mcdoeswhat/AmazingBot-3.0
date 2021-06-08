package me.albert.amazingbot.events;


import me.albert.amazingbot.bot.Bot;
import net.mamoe.mirai.Mirai;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.Message;

public class MessageReceiveEvent extends ABEvent {
    private final String msg;
    private final MessageEvent event;

    public MessageReceiveEvent(String msg, MessageEvent event) {
        super(event);
        this.event = event;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void response(String message) {
        event.getSubject().sendMessage(message);
    }

    public void response(Message message) {
        event.getSubject().sendMessage(message);
    }

    public MessageEvent getEvent() {
        return event;
    }

    public void recall() {
        Mirai.getInstance().recallMessage(Bot.getApi().getBot(), getEvent().getSource());
    }


}
