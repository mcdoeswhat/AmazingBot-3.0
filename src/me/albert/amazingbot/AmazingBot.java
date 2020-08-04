package me.albert.amazingbot;

import me.albert.amazingbot.bot.Bot;
import me.albert.amazingbot.listeners.NewPlayer;
import me.albert.amazingbot.listeners.OnBind;
import me.albert.amazingbot.listeners.OnCommand;
import me.albert.amazingbot.utils.CustomConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class AmazingBot extends JavaPlugin {
    private static AmazingBot instance;
    private static CustomConfig data;

    public static AmazingBot getInstance() {
        return instance;
    }

    public static CustomConfig getData() {
        return data;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        Bot.start();
        registerEvent(new OnCommand());
        registerEvent(new NewPlayer());
        registerEvent(new OnBind());
        data = new CustomConfig("data.yml", this);
        getLogger().info("Loaded");
    }

    private void registerEvent(Listener listener) {
        Bukkit.getServer().getPluginManager().registerEvents(listener, this);
    }

    @Override
    public void onDisable() {
        data.save();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        reloadConfig();
        data.reload();
        Bot.start();
        sender.sendMessage("§a所有配置文件已经重新载入!");
        return true;
    }
}
