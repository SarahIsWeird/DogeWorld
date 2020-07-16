package com.sarahisweird.dogeverse.tasks;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UpdatePlayerCountNextTick extends BukkitRunnable {
    private final List<Player> players;
    private final Player leavingPlayer;

    public UpdatePlayerCountNextTick(Collection<? extends Player> players, Player leavingPlayer) {
        this.players = new ArrayList<>(players);
        this.leavingPlayer = leavingPlayer;
    }

    @Override
    public void run() {
        int playerCount = this.players.size() - 1;

        for (Player player : players) {
            if (!player.equals(leavingPlayer))
                player.setPlayerListFooter("§6§lOnline players: §e§l" + Integer.toString(playerCount));
        }
    }
}
