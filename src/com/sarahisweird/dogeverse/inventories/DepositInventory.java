package com.sarahisweird.dogeverse.inventories;

import com.sarahisweird.dogeverse.dbmanager.DBException;
import com.sarahisweird.dogeverse.dbmanager.DBManager;
import com.sarahisweird.dogeverse.guis.InventoryTypes;
import com.sarahisweird.dogeverse.towns.TownManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static com.sarahisweird.dogeverse.inventories.GUIManager.createGuiItem;

public class DepositInventory implements DogeInventory {
    private final Inventory inventory;
    
    public DepositInventory() {
        inventory = Bukkit.createInventory(null, 45, "Deposit Doge");

        inventory.setItem(2, createGuiItem(Material.DIAMOND));

        ItemStack itemStackDiamond = new ItemStack(Material.DIAMOND, 1);
        ItemMeta meta = itemStackDiamond.getItemMeta();

        assert meta != null;

        meta.addEnchant(Enchantment.DURABILITY, 1, false);
        meta.setDisplayName("§4ERROR, please report.");
        itemStackDiamond.setItemMeta(meta);

        inventory.setItem(3, itemStackDiamond);

        inventory.setItem(5, createGuiItem(Material.ENCHANTED_GOLDEN_APPLE));
        inventory.setItem(6, createGuiItem(Material.GOLDEN_APPLE));

        inventory.setItem(10, createGuiItem(Material.YELLOW_WOOL, "§aAdd 100"));
        inventory.setItem(19, createGuiItem(Material.YELLOW_STAINED_GLASS, "§aAdd 10"));
        inventory.setItem(28, createGuiItem(Material.GOLD_INGOT, "§aAdd 1"));

        inventory.setItem(16, createGuiItem(Material.GRAY_WOOL, "§cRemove 100"));
        inventory.setItem(25, createGuiItem(Material.GRAY_STAINED_GLASS, "§cRemove 10"));
        inventory.setItem(34, createGuiItem(Material.IRON_INGOT, "§cRemove 1"));

        inventory.setItem(22, createGuiItem(Material.GOLD_ORE));

        inventory.setItem(39, createGuiItem(Material.LIME_CONCRETE, "§2Confirm"));
        inventory.setItem(41, createGuiItem(Material.RED_CONCRETE, "§4Cancel"));
    }
    
    @Override
    public InventoryTypes getType() {
        return null;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }

    @Override
    public void handleClick(InventoryClickEvent e) {
        e.setCancelled(true);
    }

    public void updateInventory(Player player) {
        Inventory inv = this.inventory;

        float playerBalance;
        float townBalance;
        float amount = (float) 0;

        try {
            playerBalance = DBManager.getPlayerBalance(player);
            townBalance = TownManager.getTown(player).getBalance();
        } catch (DBException e) {
            e.printStackTrace();

            player.closeInventory();

            GUIManager.removePlayerInformation(player);

            player.sendMessage("§cSorry, there was an error. Please contact a staff member for further assistance.");

            return;
        }

        ItemMeta meta = inv.getItem(2).getItemMeta();
        meta.setDisplayName("§6Your old balance: " + playerBalance);
        inv.getItem(2).setItemMeta(meta);

        meta = inv.getItem(3).getItemMeta();
        meta.setDisplayName("§6Your new balance: " + (playerBalance - amount));
        inv.getItem(3).setItemMeta(meta);

        meta = inv.getItem(5).getItemMeta();
        meta.setDisplayName("§6Town's new balance: " + (townBalance + amount));
        inv.getItem(5).setItemMeta(meta);

        meta = inv.getItem(6).getItemMeta();
        meta.setDisplayName("§6Town's old balance: " + townBalance);
        inv.getItem(6).setItemMeta(meta);

        meta = inv.getItem(22).getItemMeta();
        meta.setDisplayName("§a4You're adding: " + amount);
        inv.getItem(22).setItemMeta(meta);
    }
}
