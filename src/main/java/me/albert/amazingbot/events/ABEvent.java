package me.albert.amazingbot.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.concurrent.ConcurrentHashMap;

public class ABEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final net.mamoe.mirai.event.Event event;

    private final ConcurrentHashMap<String, Object> metas = new ConcurrentHashMap<>();

    public ABEvent(net.mamoe.mirai.event.Event event) {
        super(true);
        this.event = event;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public ConcurrentHashMap<String, Object> getMetas() {
        return metas;
    }

    public void addMeta(String key, Object object) {
        metas.put(key, object);
    }

    public Object getMeta(String key) {
        if (!hasMeta(key)) return null;
        return metas.get(key);
    }

    public boolean hasMeta(String key) {
        return metas.containsKey(key);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }


    public net.mamoe.mirai.event.Event getEvent() {
        return event;
    }
}
