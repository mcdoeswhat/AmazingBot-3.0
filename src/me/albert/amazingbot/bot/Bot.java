package me.albert.amazingbot.bot;

import me.albert.amazingbot.AmazingBot;
import me.albert.amazingbot.events.*;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.utils.BotConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Event;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class Bot {
    private static final AtomicBoolean close = new AtomicBoolean(false);
    private static final AtomicBoolean starting = new AtomicBoolean(false);
    private static final AtomicLong startingTime = new AtomicLong();
    private static BotAPI api;
    private static Boolean connected = false;
    private static net.mamoe.mirai.Bot bot = null;

    private static void callEvent(Event event) {
        if (!AmazingBot.async.get()) {
            Bukkit.getScheduler().runTask(AmazingBot.getInstance(), () -> Bukkit.getPluginManager().callEvent(event));
            return;
        }
        Bukkit.getPluginManager().callEvent(event);
    }

    public static void start() {
        if (starting.get() && System.currentTimeMillis() - startingTime.get() < 1000 * 10) {
            AmazingBot.getInstance().getLogger().info("§c机器人正在启动中,请稍后再试");
            return;
        }
        starting.set(true);
        startingTime.set(System.currentTimeMillis());
        Bukkit.getScheduler().runTaskAsynchronously(AmazingBot.getInstance(), () -> {
            if (bot != null && close.get()) {
                close.set(false);
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
            File folder = AmazingBot.getInstance().getDataFolder();
            //删除缓存
            if (config.getBoolean("main.clear_cache")) {
                File accountSecrets = new File(folder, "cache" + File.separator + "account.secrets");
                if (accountSecrets.exists()) {
                    accountSecrets.delete();
                }
            }
            configuration.setWorkingDir(folder);
            configuration.setProtocol(BotConfiguration.MiraiProtocol.valueOf(config.getString("main.protocol")));
            if (!config.getBoolean("main.botlog")) {
                configuration.noBotLog();
                configuration.noNetworkLog();
            }
            if (config.getBoolean("main.filelog")) {
                configuration.redirectBotLogToDirectory(new File("botlog"));
                configuration.redirectNetworkLogToDirectory(new File("networklog"));
            }
            if (config.getBoolean("main.cache")) {
                configuration.enableContactCache();
            }
            bot = BotFactory.INSTANCE.newBot(qq, pasword, configuration);
            close.set(true);
            bot.login();
            api = new BotAPI(bot);
            bot.getEventChannel().subscribeAlways(net.mamoe.mirai.event.Event.class, event -> {
                if (event instanceof NewFriendRequestEvent) {
                    callEvent(new FriendRequestEvent((NewFriendRequestEvent) event));
                    return;
                }
                if (event instanceof MemberJoinEvent) {
                    callEvent(new GroupMemberJoinEvent((MemberJoinEvent) event));
                    return;
                }
                if (event instanceof MemberJoinRequestEvent) {
                    callEvent(new GroupRequestJoinEvent((MemberJoinRequestEvent) event));
                    return;
                }
                if (event instanceof MessageEvent) {
                    if (event instanceof GroupMessageEvent) {
                        GroupMessageEvent messageEvent = (GroupMessageEvent) event;
                        String content = messageEvent.getMessage().contentToString();
                        long sender = messageEvent.getSender().getId();
                        long group = messageEvent.getGroup().getId();
                        callEvent(new me.albert.amazingbot.events.GroupMessageEvent(content, sender, group, content, messageEvent));
                        return;
                    }

                    if (event instanceof FriendMessageEvent) {
                        FriendMessageEvent messageEvent = (FriendMessageEvent) event;
                        String content = messageEvent.getMessage().contentToString();
                        callEvent(new PrivateMessageEvent(content, messageEvent.getUser().getId(), content, messageEvent));
                        return;
                    }

                    if (event instanceof GroupTempMessageEvent) {
                        GroupTempMessageEvent messageEvent = (GroupTempMessageEvent) event;
                        String content = messageEvent.getMessage().contentToString();
                        callEvent(new me.albert.amazingbot.events.TempMessageEvent(content, messageEvent.getSubject().getId(), content, messageEvent));
                        return;
                    }
                    MessageEvent messageEvent = (MessageEvent) event;
                    String content = messageEvent.getMessage().contentToString();
                    MessageReceiveEvent messageReceiveEvent = new MessageReceiveEvent(content, messageEvent);
                    callEvent(messageReceiveEvent);
                    return;
                }
                callEvent(new ABEvent(event));
            });
            starting.set(false);
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