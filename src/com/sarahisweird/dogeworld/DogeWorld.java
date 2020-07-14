package com.sarahisweird.dogeworld;

import com.sarahisweird.dogeworld.commands.CommandManager;
import com.sarahisweird.dogeworld.commands.TownCmds;
import com.sarahisweird.dogeworld.config.Config;
import com.sarahisweird.dogeworld.dbmanager.DBException;
import com.sarahisweird.dogeworld.dbmanager.DBManager;
import com.sarahisweird.dogeworld.ranks.DeveloperRank;
import com.sarahisweird.dogeworld.ranks.Rank;
import com.sarahisweird.dogeworld.ranks.RankManager;
import com.sarahisweird.dogeworld.towns.TownManager;
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

public class DogeWorld extends JavaPlugin implements Listener {

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

        player.setPlayerListHeader(fmt("   &6&lDogeverse &e&l1.16  "));

        player.setPlayerListName(fmt(rank.getRankAbbreviation() + " "
                + rank.getNameColor() + player.getDisplayName()));

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
