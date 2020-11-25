package me.albert.amazingbot.bot;

import kotlin.coroutines.CoroutineContext;
import me.albert.amazingbot.AmazingBot;
import me.albert.amazingbot.events.*;
import net.mamoe.mirai.BotFactoryJvm;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.Events;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MemberJoinEvent;
import net.mamoe.mirai.event.events.MemberJoinRequestEvent;
import net.mamoe.mirai.event.events.MemberLeaveEvent;
import net.mamoe.mirai.event.events.NewFriendRequestEvent;
import net.mamoe.mirai.message.FriendMessageEvent;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.utils.BotConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public class Bot {
    private static BotAPI api;
    private static Boolean connected = false;
    private static net.mamoe.mirai.Bot bot = null;

    private static void callEvent(Event event) {
        if (!AmazingBot.getInstance().getConfig().getBoolean("async")) {
            Bukkit.getScheduler().runTask(AmazingBot.getInstance(), () -> Bukkit.getPluginManager().callEvent(event));
        }
        Bukkit.getScheduler().runTaskAsynchronously(AmazingBot.getInstance(), () -> Bukkit.getPluginManager().callEvent(event));
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
        configuration.noNetworkLog();
        if (!config.getBoolean("main.botlog")) {
            configuration.noBotLog();
        }
        bot = BotFactoryJvm.newBot(qq, pasword, configuration);
        bot.loginAsync();
        api = new BotAPI(bot);
        Bukkit.getScheduler().runTaskAsynchronously(AmazingBot.getInstance(), () -> {
            Events.registerEvents(bot, new SimpleListenerHost() {
                @EventHandler
                public ListeningStatus onGroupMessage(GroupMessageEvent event) {
                    me.albert.amazingbot.events.GroupMessageEvent groupMessageEvent = new
                            me.albert.amazingbot.events.GroupMessageEvent(event.getMessage().contentToString(),
                            event.getSender().getId(), event.getGroup().getId(), "", event);
                    callEvent(groupMessageEvent);
                    return ListeningStatus.LISTENING;
                }

                @EventHandler
                public ListeningStatus onPrivate(FriendMessageEvent event) {
                    PrivateMessageEvent privateMessageEvent = new PrivateMessageEvent(event.getMessage().contentToString(),
                            event.getSender().getId(), "", event);
                    callEvent(privateMessageEvent);
                    return ListeningStatus.LISTENING;
                }

                @EventHandler
                public ListeningStatus onMemberJoinRequest(MemberJoinRequestEvent event) {
                    GroupRequestJoinEvent groupRequestJoinEvent = new GroupRequestJoinEvent(event);
                    callEvent(groupRequestJoinEvent);
                    return ListeningStatus.LISTENING;
                }

                @EventHandler
                public ListeningStatus onMemberJoin(MemberJoinEvent event) {
                    GroupMemberJoinEvent groupMemberJoinEvent = new GroupMemberJoinEvent(event);
                    callEvent(groupMemberJoinEvent);
                    return ListeningStatus.LISTENING;
                }

                @EventHandler
                public ListeningStatus onMemberLeave(MemberLeaveEvent event) {
                    GroupMemberLeaveEvent groupMemberLeaveEvent = new GroupMemberLeaveEvent(event);
                    callEvent(groupMemberLeaveEvent);
                    return ListeningStatus.LISTENING;
                }

                @EventHandler
                public ListeningStatus onFriendAdd(NewFriendRequestEvent event) {
                    FriendRequestEvent groupMemberLeaveEvent = new FriendRequestEvent(event);
                    callEvent(groupMemberLeaveEvent);
                    return ListeningStatus.LISTENING;
                }


                @Override
                public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
                    throw new RuntimeException("在事件处理中发生异常", exception);
                }
            });
            bot.join();
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
