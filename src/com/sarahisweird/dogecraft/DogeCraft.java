package com.sarahisweird.dogecraft;

import com.sarahisweird.dogecraft.commands.CommandManager;
import com.sarahisweird.dogecraft.commands.TeleportCmds;
import com.sarahisweird.dogecraft.config.Config;
import com.sarahisweird.dogecraft.dbmanager.DBException;
import com.sarahisweird.dogecraft.dbmanager.DBManager;
import com.sarahisweird.dogecraft.ranks.Rank;
import com.sarahisweird.dogecraft.ranks.RankManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class DogeCraft extends JavaPlugin implements Listener {

    // Alias for brevity
    private String fmt(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    @Override
    public void onEnable() {
        Config.load(this);

        this.getServer().getPluginManager().registerEvents(this, this);

        DBManager.loadDatabase();

        System.out.println("Successfully connected to the database.");

        System.out.print("Plugin enabled.");
    }

    @Override
    public void onDisable() {
        Config.save();

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
        boolean flying = false;

        try {
            DBManager.loadPlayer(player);
            flying = DBManager.isPlayerFlying(player);
        } catch (DBException e) {
            e.printStackTrace();
        }

        player.setAllowFlight(flying);
        player.setFlying(flying);
    }

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent event) {
        Rank playerRank = RankManager.getRank(event.getPlayer());

        String prefix = playerRank.getPrefix();
        String nameColor = playerRank.getNameColor();
        String messageColor = playerRank.getMessageColor();

        String playerName = event.getPlayer().getDisplayName();

        try {
            String nickName = DBManager.getPlayerNickname(event.getPlayer());

            if (!nickName.equals(""))
                playerName = nickName;
        } catch (DBException e) {
            e.printStackTrace();
        }

        this.getServer().broadcastMessage(fmt(prefix + "&r " + nameColor + playerName + "&r: " + messageColor)
                + RankManager.formatMessage(event.getPlayer(), event.getMessage()));

        event.setCancelled(true);
    }
}
