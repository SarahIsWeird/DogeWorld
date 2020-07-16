package com.sarahisweird.dogeverse.inventories;

import com.sun.istack.internal.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class TownGui implements Listener {
    private final Inventory inv;

    public TownGui() {
        inv = Bukkit.createInventory(null, 9, "Town Management");

        initializeItems();
    }

    public void initializeItems() {
        inv.addItem(createGuiItem(Material.BELL, "Create a town", "This will cost you 500 Doge!"));
        inv.addItem(createGuiItem(Material.GOLD_NUGGET, "Town Treasury", "Let's you view and modify the town's treasury."));
    }

    protected ItemStack createGuiItem(final Material material, final String name, @Nullable final String lore) {
        final ItemStack itemStack = new ItemStack(material, 1);
        final ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName("§r" + name);

        if (lore != null)
            meta.setLore(Arrays.asList("§r" + lore));

        itemStack.setItemMeta(meta);

        return itemStack;
    }

    public void openInventory(final HumanEntity entity) {
        entity.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (e.getInventory() != inv)
            return;

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        if (clickedItem == null || clickedItem.getType() == Material.AIR)
            return;

        Player player = (Player) e.getWhoClicked();

        player.sendMessage("You clicked a " + clickedItem.getItemMeta().getDisplayName() + "!");
    }
}
