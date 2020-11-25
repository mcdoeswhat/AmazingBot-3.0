package me.albert.amazingbot.bot;

import me.albert.amazingbot.AmazingBot;
import me.albert.amazingbot.database.MySQL;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;

public class BotAPI {

    private net.mamoe.mirai.Bot bot;

    public BotAPI(net.mamoe.mirai.Bot bot) {
        this.bot = bot;
    }

    public void sendGroupMsg(String groupID, String msg) {
        Bukkit.getScheduler().runTaskAsynchronously(AmazingBot.getInstance(), () -> {
            try {
                bot.getGroup(Long.parseLong(groupID)).sendMessage(msg);
            } catch (Exception ignored) {
            }
        });
    }

    public void sendPrivateMsg(String userID, String msg) {
        Bukkit.getScheduler().runTaskAsynchronously(AmazingBot.getInstance(), () -> {
            try {
                bot.getFriend(Long.parseLong(userID)).sendMessage(msg);
            } catch (Exception ignored) {
            }
        });
    }

    public void sendRawMsg(String msg) {
        Bukkit.getLogger().info("§e[AmazingBot] 有插件在使用过时方法发送Raw信息,此方法已被删除,可能会影响到插件功能...");
    }

    public void changeTitle(Long groupID, Long userID, String title) {
        Bukkit.getScheduler().runTaskAsynchronously(AmazingBot.getInstance(), () -> {
            try {
                bot.getGroup(groupID).get(userID).setNameCard(title);
            } catch (Exception ignored) {
            }
        });
    }

    public Group getGroup(Long groupID) {
        try {
            return bot.getGroup(groupID);
        } catch (Exception ignored) {
        }
        return null;
    }

    public Friend getFriend(Long userID) {
        try {
            return bot.getFriend(userID);
        } catch (Exception ignored) {
        }
        return null;
    }

    public net.mamoe.mirai.Bot getBot() {
        return bot;
    }

    public void setBind(Long userID, UUID uuid) {
        if (MySQL.ENABLED) {
            MySQL.savePlayer(userID, uuid.toString());
            return;
        }
        FileConfiguration data = AmazingBot.getData().getConfig();
        data.set(String.valueOf(userID), uuid.toString());
        AmazingBot.getData().save();
    }

    public UUID getPlayer(Long userID) {
        if (MySQL.ENABLED) {
            return MySQL.getPlayer(userID);
        }
        UUID uuid = null;
        FileConfiguration data = AmazingBot.getData().getConfig();
        if (data.getString(String.valueOf(userID)) != null) {
            uuid = UUID.fromString(data.getString(String.valueOf(userID)));
        }
        return uuid;
    }

    public Long getUser(UUID playerID) {
        if (MySQL.ENABLED) {
            return MySQL.getQQ(playerID.toString());
        }
        Long userID = null;
        FileConfiguration data = AmazingBot.getData().getConfig();
        for (String key : data.getConfigurationSection("").getKeys(false)) {
            if (data.getString(key).equalsIgnoreCase(playerID.toString())) {
                return
                        Long.parseLong(key);
            }
        }
        return userID;
    }


}
