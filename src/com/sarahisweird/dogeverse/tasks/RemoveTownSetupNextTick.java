package com.sarahisweird.dogeverse.tasks;

import com.sarahisweird.dogeverse.towns.TownSetup;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class RemoveTownSetupNextTick extends BukkitRunnable {
    private final List<TownSetup> list;
    private final TownSetup townSetup;

    public RemoveTownSetupNextTick(List<TownSetup> list, TownSetup townSetup) {
        this.list = list;
        this.townSetup = townSetup;
    }

    @Override
    public void run() {
        this.list.remove(this.townSetup);
    }
}
