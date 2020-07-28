package com.sarahisweird.dogeverse.inventories;

import com.sarahisweird.dogeverse.guis.InventoryTypes;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public interface DogeInventory {
    InventoryTypes getType();

    Inventory getInventory();

    void handleClick(final InventoryClickEvent e);
}
