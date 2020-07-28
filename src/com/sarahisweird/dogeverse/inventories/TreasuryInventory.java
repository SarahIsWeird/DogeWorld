package com.sarahisweird.dogeverse.inventories;

import com.sarahisweird.dogeverse.guis.InventoryTypes;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static com.sarahisweird.dogeverse.inventories.GUIManager.createGuiItem;

public class TreasuryInventory implements DogeInventory {
    private final Inventory inventory;

    public TreasuryInventory() {
        inventory = Bukkit.createInventory(null, 9, "Town Treasury");

        inventory.setItem(2, createGuiItem(Material.GOLD_BLOCK));
        inventory.setItem(3, createGuiItem(Material.GOLD_NUGGET, "§6Deposit Doge", "§eLets you deposit money", "§einto the town treasury."));
        inventory.setItem(4, createGuiItem(Material.IRON_NUGGET, "§4Withdraw Doge", "§cLets you withdraw money", "§cfrom the town treasury."));
    }

    @Override
    public InventoryTypes getType() {
        return InventoryTypes.TREASURY;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void handleClick(InventoryClickEvent e) {
        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        if (clickedItem == null || clickedItem.getType() == Material.AIR)
            return;

        Player player = (Player) e.getWhoClicked();

        switch (clickedItem.getType()) {
            case GOLD_NUGGET:
                player.closeInventory();

                ((DepositInventory) GUIManager.openInventory(player, InventoryTypes.DEPOSIT)).updateInventory(player);
                break;
        }
    }
}
