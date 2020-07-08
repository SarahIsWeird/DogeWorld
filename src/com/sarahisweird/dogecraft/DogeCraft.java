package com.sarahisweird.dogecraft;

import com.sarahisweird.dogecraft.commands.CommandManager;
import com.sarahisweird.dogecraft.commands.TeleportCmds;
import com.sarahisweird.dogecraft.config.Config;
import com.sarahisweird.dogecraft.dbmanager.DBException;
import com.sarahisweird.dogecraft.dbmanager.DBManager;
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

    Config config = new Config(this);

    DBManager dbManager;

    // Alias for brevity
    private String fmt(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    @Override
    public void onEnable() {
        config.load();

        this.getServer().getPluginManager().registerEvents(this, this);

        dbManager = new DBManager();

        System.out.println("Successfully connected to the database.");

        System.out.print("Plugin enabled.");
    }

    @Override
    public void onDisable() {
        config.save();

        dbManager.disable();

        System.out.print("Plugin disabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return CommandManager.executeCommand(sender, command, label, args, dbManager);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        boolean flying = false;

        try {
            dbManager.loadPlayer(player);
            flying = dbManager.isPlayerFlying(player);
        } catch (DBException e) {
            e.printStackTrace();
        }

        player.setAllowFlight(flying);
        player.setFlying(flying);
    }

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent event) {
        event.setMessage(fmt(event.getMessage()));
    }
}
