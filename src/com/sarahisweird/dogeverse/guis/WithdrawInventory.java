package com.sarahisweird.dogeverse.guis;

import com.sarahisweird.dogeverse.dbmanager.DBException;
import com.sarahisweird.dogeverse.dbmanager.DBManager;
import com.sarahisweird.dogeverse.towns.TownManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import static com.sarahisweird.dogeverse.guis.GUIManager.createGuiItem;

public class WithdrawInventory {
    public static ItemStack[] getContents() {
        ItemStack[] contents = new ItemStack[5 * 9];

        contents[0] = createGuiItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE, "WITHDRAW", "This is a secret c;");
        contents[2] = createGuiItem(Material.DIAMOND, "§4ERROR, please report.");

        contents[3] = new ItemStack(Material.DIAMOND, 1);
        ItemMeta meta = contents[3].getItemMeta();

        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.setDisplayName("§4ERROR, please report.");
        contents[3].setItemMeta(meta);

        contents[5] = createGuiItem(Material.ENCHANTED_GOLDEN_APPLE, "§4ERROR, please report.");
        contents[6] = createGuiItem(Material.GOLDEN_APPLE, "§4ERROR, please report.");

        contents[10] = createGuiItem(Material.GRAY_WOOL, "§cRemove §l-100");
        contents[19] = createGuiItem(Material.GRAY_STAINED_GLASS, "§cRemove §l-10");
        contents[28] = createGuiItem(Material.IRON_INGOT, "§cRemove §l-1");

        contents[16] = createGuiItem(Material.YELLOW_WOOL, "§aAdd §l+100");
        contents[25] = createGuiItem(Material.YELLOW_STAINED_GLASS, "§aAdd §l+10");
        contents[34] = createGuiItem(Material.GOLD_INGOT, "§aAdd §l+1");

        contents[22] = createGuiItem(Material.IRON_ORE, "§4ERROR, please report.");

        contents[39] = createGuiItem(Material.LIME_CONCRETE, "§2Confirm");
        contents[41] = createGuiItem(Material.RED_CONCRETE, "§4Cancel");

        return contents;
    }


    @Nullable
    public static Integer handleClick(InventoryClickEvent e, Integer playerData) {
        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        if (clickedItem == null || clickedItem.getType() == Material.AIR)
            return playerData;

        Player player = (Player) e.getWhoClicked();

        float balance;
        int townBalance = TownManager.getTown(player).getBalance();

        try {
            balance = DBManager.getPlayerBalance(player);
        } catch (DBException dbException) {
            dbException.printStackTrace();

            player.closeInventory();

            player.sendMessage("§cSorry, there was an error. Please contact a staff member for further assistance.");

            return 0;
        }

        switch (e.getSlot()) {
            case 9 + 1: // Add +100
                if (playerData + 100 > townBalance) {
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, .5f, 1.f);
                    return playerData;
                }

                playerData += 100;
                updateInventory(player, playerData);
                return playerData;
            case 9 * 2 + 1:
                if (playerData + 10 > townBalance) {
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, .5f, 1.f);
                    return playerData;
                }

                playerData += 10;
                updateInventory(player, playerData);
                return playerData;
            case 9 * 3 + 1:
                if (playerData + 1 > townBalance) {
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, .5f, 1.f);
                    return playerData;
                }

                playerData += 1;
                updateInventory(player, playerData);
                return playerData;


            case 9 + 7:
                if (playerData == 0) {
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, .5f, 1.f);
                    return playerData;
                }

                playerData = playerData < 100 ? 0 : playerData - 100;
                updateInventory(player, playerData);
                return playerData;
            case 9 * 2 + 7:
                if (playerData == 0) {
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, .5f, 1.f);
                    return playerData;
                }

                playerData = playerData < 100 ? 0 : playerData - 10;
                updateInventory(player, playerData);
                return playerData;
            case 9 * 3 + 7:
                if (playerData == 0) {
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, .5f, 1.f);
                    return playerData;
                }

                playerData = playerData < 100 ? 0 : playerData - 1;
                updateInventory(player, playerData);
                return playerData;
            case 9 * 4 + 3:
                if (playerData == 0) {
                    player.closeInventory();
                    return 0;
                }

                try {
                    DBManager.addBalance(player, (float) playerData);
                    TownManager.getTown(player).removeBalance(playerData);
                } catch (DBException dbException) {
                    dbException.printStackTrace();

                    player.closeInventory();

                    player.sendMessage("§cSorry, there was an error. Please contact a staff member for further assistance.");

                    return 0;
                }
            case 9 * 4 + 5:
                player.closeInventory();

                return 0;
        }

        return playerData;
    }

    public static void updateInventory(Player player) {
        updateInventory(player, com.sarahisweird.dogeverse.guis.GUIManager.getInformation(player));
    }

    public static void updateInventory(Player player, Integer playerData) {
        Inventory inv = com.sarahisweird.dogeverse.guis.GUIManager.getInventory(player);

        float playerBalance;
        float townBalance;
        float amount = (float) playerData;

        try {
            playerBalance = DBManager.getPlayerBalance(player);
            townBalance = TownManager.getTown(player).getBalance();
        } catch (DBException e) {
            e.printStackTrace();

            player.closeInventory();

            GUIManager.setInformation(player, null);

            player.sendMessage("§cSorry, there was an error. Please contact a staff member for further assistance.");

            return;
        }

        ItemMeta meta = inv.getItem(2).getItemMeta();
        meta.setDisplayName("§6Your old balance: " + playerBalance);
        inv.getItem(2).setItemMeta(meta);

        meta = inv.getItem(3).getItemMeta();
        meta.setDisplayName("§6Your new balance: " + (playerBalance + amount));
        inv.getItem(3).setItemMeta(meta);

        meta = inv.getItem(5).getItemMeta();
        meta.setDisplayName("§6Town's new balance: " + (townBalance - amount));
        inv.getItem(5).setItemMeta(meta);

        meta = inv.getItem(6).getItemMeta();
        meta.setDisplayName("§6Town's old balance: " + townBalance);
        inv.getItem(6).setItemMeta(meta);

        meta = inv.getItem(22).getItemMeta();
        meta.setDisplayName("§eYou're withdrawing: " + amount);
        inv.getItem(22).setItemMeta(meta);
    }
}
