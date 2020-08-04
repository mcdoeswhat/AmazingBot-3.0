package me.albert.amazingbot.utils;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class CustomConfig {

    private String filename;
    private FileConfiguration config;
    private File configFile;
    private Plugin plugin;

    public CustomConfig(String name, Plugin plugin) {
        this.plugin = plugin;
        this.filename = name;
        this.config = create(name);
    }

    public String getFilename() {
        return filename;
    }

    public void save() {
        try {
            config.save(configFile);
        } catch (IOException ignored) {

        }
        this.config = create(filename);
    }

    public void reload() {
        this.config = create(filename);
    }

    private FileConfiguration create(String file) {
        File ConfigFile = new File(plugin.getDataFolder(), file);
        if (!ConfigFile.exists()) {
            ConfigFile.getParentFile().mkdirs();
            plugin.saveResource(file, false);
        }
        YamlConfiguration Config = new YamlConfiguration();
        try {
            Config.load(ConfigFile);
            this.configFile = ConfigFile;
        } catch (IOException | InvalidConfigurationException ignored) {

        }
        return Config;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public File getConfigFile() {
        return configFile;
    }

}
