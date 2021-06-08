package me.albert.amazingbot;

import io.izzel.taboolib.module.config.TConfig;
import io.izzel.taboolib.module.inject.TInject;
import me.albert.amazingbot.sqlite.SQLer;
import io.izzel.taboolib.loader.Plugin;
import me.albert.amazingbot.bot.Bot;
import me.albert.amazingbot.database.MySQL;
import me.albert.amazingbot.listeners.NewPlayer;
import me.albert.amazingbot.listeners.OnBind;
import me.albert.amazingbot.listeners.OnCommand;
import me.albert.amazingbot.utils.CustomConfig;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class AmazingBot extends Plugin {

    @TInject(value = {"sqlite.yml"})
    public static TConfig sqlite;

    private static JavaPlugin instance;
    private static CustomConfig data;
    private static CustomConfig mysqlSettings;

    public static JavaPlugin getinstance() {
        return instance;
    }

    public static CustomConfig getData() {
        return data;
    }

    public static CustomConfig getMysqlSettings() {
        return mysqlSettings;
    }


    @Override
    public void onEnable() {
        instance = getPlugin();
        getPlugin().saveDefaultConfig();
        Bot.start();
        registerEvent(new OnCommand());
        registerEvent(new NewPlayer());
        registerEvent(new OnBind());
        data = new CustomConfig("data.yml", instance);
        mysqlSettings = new CustomConfig("mysql.yml", instance);
        if (mysqlSettings.getConfig().getBoolean("enable")) {
            MySQL.setUP();
        }
        if (SQLer.isEnable()){
            getPlugin().getLogger().info("已启用sqlite储存数据");
        }
        getPlugin().getLogger().info("Loaded");
    }

    private void registerEvent(Listener listener) {
        Bukkit.getServer().getPluginManager().registerEvents(listener, instance);
    }

    @Override
    public void onDisable() {
        if (MySQL.ENABLED) {
            MySQL.close();
            return;
        }
        data.save();
    }

}
