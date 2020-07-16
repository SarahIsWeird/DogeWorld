package com.sarahisweird.dogeverse;

import com.sarahisweird.dogeverse.commands.CommandManager;
import com.sarahisweird.dogeverse.commands.TownCmds;
import com.sarahisweird.dogeverse.config.Config;
import com.sarahisweird.dogeverse.dbmanager.DBException;
import com.sarahisweird.dogeverse.dbmanager.DBManager;
import com.sarahisweird.dogeverse.ranks.Rank;
import com.sarahisweird.dogeverse.ranks.RankManager;
import com.sarahisweird.dogeverse.tasks.UpdatePlayerCountNextTick;
import com.sarahisweird.dogeverse.towns.TownManager;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Dogeverse extends JavaPlugin implements Listener {

    // Alias for brevity
    private String fmt(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    @Override
    public void onEnable() {
        Config.load(this);

        this.getServer().getPluginManager().registerEvents(this, this);
        this.getServer().getPluginManager().registerEvents(TownCmds.townGui, this);
        System.out.println("Successfully registered event listeners.");

        DBManager.loadDatabase();
        System.out.println("Successfully connected to the database.");

        TownManager.load();
        RankManager.load();
        System.out.println("Successfully loaded all managers.");

        System.out.print("Plugin enabled.");
    }

    @Override
    public void onDisable() {
        Config.save();

        TownManager.save();

        DBManager.disable();

        System.out.print("Plugin disabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return CommandManager.executeCommand(sender, command, label, args);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        boolean newPlayer = false;
        boolean flying = false;

        try {
            newPlayer = DBManager.loadPlayer(player);
            flying = DBManager.isPlayerFlying(player);

        } catch (DBException e) {
            e.printStackTrace();
        }

        if (player.getGameMode() == GameMode.SURVIVAL) {
            player.setAllowFlight(flying);
            player.setFlying(flying);
        }

        Rank rank = RankManager.getRank(player);
        String order = rank.getOrder();

        this.getServer()
                .getScoreboardManager()
                .getMainScoreboard()
                .getTeam(order)
                .addEntry(player.getName());

        player.setPlayerListHeader(fmt("   &6&lDogeverse &e&l1.16  "));

        for (Player p : this.getServer().getOnlinePlayers())
            p.setPlayerListFooter(fmt("&6&lOnline players: &e&l" + player.getServer().getOnlinePlayers().size()));

        if (newPlayer) {
            event.setJoinMessage(fmt("&9Welcome, " + rank.getNameColor() + player.getDisplayName() + "&9!"));
        } else {
            event.setJoinMessage(fmt("&7[&2&l+&7] " + rank.getNameColor() + "&l" + player.getDisplayName()));
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Rank rank = RankManager.getRank(player);

        List<Player> onlinePlayers = new ArrayList<>(this.getServer().getOnlinePlayers());

        (new UpdatePlayerCountNextTick(onlinePlayers, player)).runTaskLater(this, 2L);

        event.setQuitMessage(fmt("&7[&4-&7] " + rank.getNameColor() + "&l" + player.getDisplayName()));
    }

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent event) {
        if (TownManager.isPlayerInTownCreation(event.getPlayer())) {
            event.setCancelled(true);

            TownManager.onInput(event.getPlayer(), event.getMessage(), this);
            return;
        }

        Rank playerRank = RankManager.getRank(event.getPlayer());

        String prefix = playerRank.getPrefix();
        String nameColor = playerRank.getNameColor();
        String messageColor = playerRank.getMessageColor();
        String townPrefix = TownManager.getTownPrefix(event.getPlayer());

        townPrefix = townPrefix.equals("") ? townPrefix : "&b&o" + townPrefix + "&r ";

        String playerName = event.getPlayer().getDisplayName();

        try {
            String nickName = DBManager.getPlayerNickname(event.getPlayer());

            if (!nickName.equals(""))
                playerName = nickName;
        } catch (DBException e) {
            e.printStackTrace();
        }

        this.getServer().broadcastMessage(fmt(prefix + "&r " + townPrefix
                + nameColor + playerName + "&r: " + messageColor)
                + RankManager.formatMessage(event.getPlayer(), event.getMessage()));

        event.setCancelled(true);
    }
}
