package com.sarahisweird.dogeverse.inventories;

import com.sarahisweird.dogeverse.guis.InventoryTypes;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GUIManager implements Listener {
    private static final Map<Player, DogeInventory> playerInventory = new HashMap<>();
    private static final Map<Player, Integer> playerInformation = new HashMap<>();

    public static ItemStack createGuiItem(final Material material) {
        final ItemStack itemStack = new ItemStack(material, 1);
        final ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName("§4ERROR, please report.");
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public static ItemStack createGuiItem(final Material material, final String name) {
        final ItemStack itemStack = new ItemStack(material, 1);
        final ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName("§r" + name);

        itemStack.setItemMeta(meta);

        return itemStack;
    }

    public static ItemStack createGuiItem(final Material material, final String name, @Nullable final String... lore) {
        final ItemStack itemStack = new ItemStack(material, 1);
        final ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName("§r" + name);

        if (lore.length > 0) {
            List<String> lores = new ArrayList<>();

            for (String str : lore) {
                lores.add("§f" + str);
            }

            meta.setLore(lores);
        }

        itemStack.setItemMeta(meta);

        return itemStack;
    }

    public static DogeInventory openInventory(final HumanEntity entity, InventoryTypes inventoryType) {
        playerInventory.put((Player) entity, new MainInventory());

        entity.openInventory(playerInventory.get(entity).getInventory());

        return playerInventory.get(entity);
    }

    public static void setPlayerInformation(Player player, int information) {
        playerInformation.put(player, information);
    }

    public static void removePlayerInformation(Player player) {
        playerInformation.remove(player);
    }

    @EventHandler
    public static void onInventoryClick(final InventoryClickEvent e) {
        playerInventory.get(e.getWhoClicked()).handleClick(e);
    }

    @EventHandler
    public static void onInventoryLeave(final InventoryCloseEvent e) {
        removePlayerInformation((Player) e.getPlayer());
        playerInventory.remove(e.getPlayer());
    }
}
