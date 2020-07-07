package com.sarahisweird.dogecraft.config;

import com.sarahisweird.dogecraft.DogeCraft;
import org.bukkit.entity.Player;

public class Config {
    private DogeCraft plugin;

    public Config(DogeCraft plugin) {
        this.plugin = plugin;

        this.load();
    }

    public void load() {
        this.plugin.saveDefaultConfig();
    }

    public void save() {
        this.plugin.saveConfig();
    }

    public boolean getFlying(Player player) {
        String path = "flying." + player.getUniqueId().toString();

        if (!this.plugin.getConfig().isBoolean(path)) {
            this.plugin.getConfig().set(path, false);
            return false;
        }

        return this.plugin.getConfig().getBoolean(path);
    }

    public void setFlying(Player player, boolean flyingEnabled) {
        String path = "flying." + player.getUniqueId().toString();

        this.plugin.getConfig().set(path, flyingEnabled);
    }
}
