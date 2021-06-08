package me.albert.amazingbot.listeners;

import me.albert.amazingbot.AmazingBot;
import me.albert.amazingbot.events.GroupMessageEvent;
import me.albert.amazingbot.utils.ConsoleSender;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public class OnCommand implements Listener {


    private static boolean isAdmin(Long userID) {
        return AmazingBot.getinstance().getConfig().getStringList("owners").contains(String.valueOf(userID));
    }

    private static boolean hasGroup(Long groupID) {
        ConfigurationSection section = AmazingBot.getinstance().getConfig().getConfigurationSection("groups");
        for (String s : section.getKeys(false)) {
            if (s.equalsIgnoreCase(String.valueOf(groupID))) {
                return true;
            }
        }
        return false;
    }

    private static String getLabel(Long groupID) {
        if (!hasGroup(groupID)) {
            return null;
        }
        return AmazingBot.getinstance().getConfig().getString("groups." + groupID + ".command") + " ";
    }

    @EventHandler
    public void onCommand(GroupMessageEvent e) {
        if (!isAdmin(e.getUserID())) {
            return;
        }
        String label = getLabel(e.getGroupID());
        if (label == null || !e.getMsg().startsWith(label)) {
            return;
        }
        e.response("命令已提交");
        Bukkit.getScheduler().runTaskAsynchronously(AmazingBot.getinstance(), () -> {
            String cmd = e.getMsg().substring(label.length());
            ConsoleSender sender = new ConsoleSender(Bukkit.getServer(), e);
            Bukkit.getScheduler().runTask(AmazingBot.getinstance(), () -> Bukkit.dispatchCommand(sender, cmd));
            String log = AmazingBot.getinstance().getConfig().getString("messages.log_command")
                    .replace("%user%", String.valueOf(e.getUserID())).replace("%cmd%", cmd)
                    .replace("&", "§");
            Bukkit.getLogger().info(log);
        });
    }

    @EventHandler
    public void onSwitch(GroupMessageEvent e) {
        if (!isAdmin(e.getUserID())) {
            return;
        }
        FileConfiguration config = AmazingBot.getinstance().getConfig();
        String serverName = config.getString("server_name");
        String label = config.getString("commands.toggle_on").replace("%server%", serverName);
        String off = config.getString("commands.toggle_off");
        off = off.replace("%server%", serverName);
        if (e.getMsg().equalsIgnoreCase(off) && hasGroup(e.getGroupID())) {
            config.set("groups." + e.getGroupID(), null);
            AmazingBot.getinstance().saveConfig();
            String toggle_off = config.getString("messages.toggle_off").replace("%server%", serverName);
            e.response(toggle_off);
            return;
        }
        if (e.getMsg().startsWith(label)) {
            if (!hasGroup(e.getGroupID())) {
                config.set("groups." + e.getGroupID() + ".command", e.getMsg().substring(label.length()));
                config.set("groups." + e.getGroupID() + ".enable_bind", false);
                AmazingBot.getinstance().saveConfig();
                String toggle_on = config.getString("messages.toggle_on").replace("%server%", serverName)
                        .replace("%label%", e.getMsg().substring(label.length()));
                e.response(toggle_on);
            }
        }
    }

    @EventHandler
    public void onAddRemove(GroupMessageEvent e) {
        if (!isAdmin(e.getUserID())) {
            return;
        }
        FileConfiguration config = AmazingBot.getinstance().getConfig();
        String serverName = config.getString("server_name");
        String add = config.getString("commands.add").replace("%server%", serverName);
        String remove = config.getString("commands.remove").replace("%server%", serverName);
        String userID;
        if (e.getMsg().startsWith(add)) {
            userID = e.getMsg().substring(add.length());
            List<String> admins = config.getStringList("owners");
            admins.add(userID);
            config.set("owners", admins);
            AmazingBot.getinstance().saveConfig();
            String msg = config.getString("messages.add")
                    .replace("%server%", serverName)
                    .replace("%user%", userID);
            e.response(msg);
            return;
        }
        if (e.getMsg().startsWith(remove)) {
            userID = e.getMsg().substring(add.length());
            List<String> admins = config.getStringList("owners");
            admins.remove(userID);
            config.set("owners", admins);
            AmazingBot.getinstance().saveConfig();
            String msg = config.getString("messages.remove")
                    .replace("%server%", serverName)
                    .replace("%user%", userID);
            e.response(msg);
        }


    }

}
