package com.sarahisweird.dogeverse.inventories;

import com.sarahisweird.dogeverse.guis.InventoryTypes;
import com.sarahisweird.dogeverse.towns.TownManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static com.sarahisweird.dogeverse.inventories.GUIManager.createGuiItem;

public final class MainInventory implements DogeInventory {
    private final Inventory inventory;

    public MainInventory() {
        this.inventory = Bukkit.createInventory(null, 9, "Town Management");

        this.inventory.setItem(3, createGuiItem(Material.BELL, "§aCreate a town", "This will cost you 500 Doge!"));
        this.inventory.setItem(5, createGuiItem(Material.GOLD_NUGGET, "§6Town Treasury", "Lets you view and", "modify the town's treasury."));
    }

    @Override
    public InventoryTypes getType() {
        return InventoryTypes.MAIN;
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    @Override
    public void handleClick(final InventoryClickEvent e) {
        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        if (clickedItem == null || clickedItem.getType() == Material.AIR)
            return;

        Player player = (Player) e.getWhoClicked();

        switch (clickedItem.getType()) {
            case BELL:
                if (TownManager.initTownSetup(player))
                    player.sendMessage("§cYou're already creating a town!");

                break;
            case GOLD_NUGGET:
                player.closeInventory();

                Inventory inv = GUIManager.openInventory(player, InventoryTypes.TREASURY).getInventory();

                ItemMeta meta = inv.getItem(2).getItemMeta();
                meta.setDisplayName("§9Balance: §3" + TownManager.getTown(player).getBalance());
                inv.getItem(2).setItemMeta(meta);

                break;
        }
    }
}
