package com.sarahisweird.dogeverse.tasks;

import com.sarahisweird.dogeverse.Dogeverse;
import com.sarahisweird.dogeverse.towns.TownManager;

public class AutoSaveDatabase implements Runnable {
    Dogeverse plugin;

    public AutoSaveDatabase(Dogeverse plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        this.plugin.getServer().broadcastMessage("§9[§6Dogeverse§9] §eAuto-saving, expect some lag...");

        TownManager.save();

        this.plugin.getServer().broadcastMessage("§9[§6Dogeverse§9] §aDone.");
    }
}
