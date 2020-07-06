package com.sarahisweird.dogecraft.config;

import com.sarahisweird.dogecraft.DogeCraft;
import org.bukkit.entity.Player;

public class Config {
    private DogeCraft plugin;

    public Config(DogeCraft plugin) {
        this.plugin = plugin;
        this.plugin.saveDefaultConfig();
    }

    public void save() {
        this.plugin.saveConfig();
    }

    public boolean getFlying(Player player) {
        String path = "flying." + player.getUniqueId().toString();

        if (!this.plugin.getConfig().isString(path))
            return false;


    }
}
