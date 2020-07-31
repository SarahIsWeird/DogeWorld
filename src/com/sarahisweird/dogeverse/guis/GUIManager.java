package com.sarahisweird.dogeverse.guis;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GUIManager implements Listener {
    private static final Map<Player, Inventory> playerInventory = new HashMap<>();
    private static final Map<Player, InventoryTypes> playerInventoryType = new HashMap<>();
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

    public static void openInventory(final HumanEntity humanEntity, InventoryTypes type) {
        Player player = (Player) humanEntity;

        switch (type) {
            case MAIN:
                playerInventoryType.put(player, InventoryTypes.MAIN);
                playerInventory.put(player, Bukkit.createInventory(null, 5 * 9, "Town Management"));
                playerInventory.get(player).setContents(MainInventory.getContents(player));
                break;

            case TREASURY:
                Inventory inv = Bukkit.createInventory(humanEntity, 5 * 9, "Town Treasury");
                inv.setContents(TreasuryInventory.getContents());

                playerInventoryType.put(player, InventoryTypes.TREASURY);
                playerInventory.put(player, inv);

                TreasuryInventory.updateInventory(player);
                break;

            case DEPOSIT:
                playerInventoryType.put(player, InventoryTypes.DEPOSIT);
                playerInventory.put(player, Bukkit.createInventory(null, 5 * 9, "Deposit into town treasury"));
                playerInventory.get(player).setContents(DepositInventory.getContents());
                break;

            case WITHDRAW:
                playerInventoryType.put(player, InventoryTypes.WITHDRAW);
                playerInventory.put(player, Bukkit.createInventory(null, 5 * 9, "Withdraw from town treasury"));
                playerInventory.get(player).setContents(WithdrawInventory.getContents());
                break;
        }

        player.openInventory(playerInventory.get(player));
    }

    @EventHandler
    public void onInventoryLeave(InventoryCloseEvent e) {

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        if (!playerInventoryType.containsKey(player))
            return;

        if (e.getClickedInventory() == null || e.getClickedInventory().getItem(0) == null)
            return;

        switch (e.getClickedInventory().getItem(0).getItemMeta().getDisplayName()) {
            case "MAIN":
                playerInformation.put(player,
                        MainInventory.handleClick(e, playerInformation.get(player)));
                break;
            case "TREASURY":
                Integer info = TreasuryInventory.handleClick(e, null);
                playerInformation.put(player, info);
                break;
            case "DEPOSIT":
                playerInformation.put(player,
                        DepositInventory.handleClick(e, playerInformation.get(player)));
                break;
            case "WITHDRAW":
                playerInformation.put(player,
                        WithdrawInventory.handleClick(e, playerInformation.get(player)));
                break;
        }
    }

    static Inventory getInventory(Player player) {
        return playerInventory.get(player);
    }

    static Integer getInformation(Player player) {
        return playerInformation.get(player);
    }

    static void setInformation(Player player, Integer info) {
        playerInformation.put(player, info);
    }
}
