package me.albert.amazingbot.bot;

import me.albert.amazingbot.AmazingBot;
import me.albert.amazingbot.events.*;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.TempMessageEvent;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.utils.BotConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Event;

public class Bot {
    private static BotAPI api;
    private static Boolean connected = false;
    private static net.mamoe.mirai.Bot bot = null;

    private static void callEvent(Event event) {
        if (!AmazingBot.getInstance().getConfig().getBoolean("async")) {
            Bukkit.getScheduler().runTask(AmazingBot.getInstance(), () -> Bukkit.getPluginManager().callEvent(event));
            return;
        }
        Bukkit.getPluginManager().callEvent(event);
    }

    public static void start() {
        if (bot != null) {
            bot.close(new Throwable());
        }

        FileConfiguration config = AmazingBot.getInstance().getConfig();
        long qq = config.getLong("main.qq");
        String pasword = config.getString("main.password");
        // 使用自定义的配置
        BotConfiguration configuration = new BotConfiguration() {
            {
                fileBasedDeviceInfo("deviceInfo.json");
            }
        };
        configuration.setProtocol(BotConfiguration.MiraiProtocol.valueOf(config.getString("main.protocol")));

        if (!config.getBoolean("main.botlog")) {
            configuration.noBotLog();
            configuration.noNetworkLog();
        }
        Bukkit.getScheduler().runTaskAsynchronously(AmazingBot.getInstance(), () -> {
            bot = BotFactory.INSTANCE.newBot(qq, pasword, configuration);
            bot.login();
            api = new BotAPI(bot);
            bot.getEventChannel().subscribeAlways(net.mamoe.mirai.event.Event.class, event -> {
                callEvent(new ABEvent(event));
            });
            bot.getEventChannel().subscribeAlways(NewFriendRequestEvent.class, event -> {
                callEvent(new FriendRequestEvent(event));
            });
            bot.getEventChannel().subscribeAlways(MemberJoinEvent.class, event -> {
                callEvent(new GroupMemberJoinEvent(event));
            });
            bot.getEventChannel().subscribeAlways(GroupMessageEvent.class, event -> {
                String content = event.getMessage().contentToString();
                me.albert.amazingbot.events.GroupMessageEvent groupMessageEvent =
                        new me.albert.amazingbot.events.GroupMessageEvent(content, event.getSender().getId(), event.getGroup().getId(),
                                content, event);
                callEvent(groupMessageEvent);
            });
            bot.getEventChannel().subscribeAlways(MemberJoinRequestEvent.class, event -> {
                callEvent(new GroupRequestJoinEvent(event));
            });
            bot.getEventChannel().subscribeAlways(MessageEvent.class, event -> {
                String content = event.getMessage().contentToString();
                MessageReceiveEvent messageReceiveEvent = new MessageReceiveEvent(content, event);
                callEvent(messageReceiveEvent);
            });
            bot.getEventChannel().subscribeAlways(FriendMessageEvent.class, event -> {
                String content = event.getMessage().contentToString();
                PrivateMessageEvent privateMessageEvent = new PrivateMessageEvent(content, event.getUser().getId(), content, event);
                callEvent(privateMessageEvent);
            });
            bot.getEventChannel().subscribeAlways(GroupTempMessageEvent.class, event -> {
                String content = event.getMessage().contentToString();
                me.albert.amazingbot.events.TempMessageEvent tempMessageEvent = new me.albert.amazingbot.events.TempMessageEvent(content,
                        event.getSubject().getId(), content, event);
                callEvent(tempMessageEvent);
            });
        });
    }


    public static BotAPI getApi() {
        return api;
    }

    public static void setApi(BotAPI api) {
        Bot.api = api;
    }


    public static Boolean getConnected() {
        return connected;
    }

    public static void setConnected(Boolean connected) {
        Bot.connected = connected;
    }
}