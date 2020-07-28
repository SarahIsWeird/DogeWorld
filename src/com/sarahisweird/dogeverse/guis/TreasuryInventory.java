package com.sarahisweird.dogeverse.guis;

import com.sarahisweird.dogeverse.towns.TownManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.sarahisweird.dogeverse.inventories.GUIManager.createGuiItem;

public class TreasuryInventory {
    public static ItemStack[] getContents() {
        ItemStack[] contents = new ItemStack[5 * 9];

        contents[0] = createGuiItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE, "TREASURY", "This is a secret c;");
        contents[2 * 9 + 3] = createGuiItem(Material.GOLD_BLOCK);
        contents[2 * 9 + 4] = createGuiItem(Material.GOLD_NUGGET, "§6Deposit Doge", "§eLets you deposit money", "§einto the town treasury.");
        contents[2 * 9 + 5] = createGuiItem(Material.IRON_NUGGET, "§4Withdraw Doge", "§cLets you withdraw money", "§cfrom the town treasury.");

        return contents;
    }

    public static void updateInventory(Player player) {

        player.getOpenInventory().getTopInventory().setItem(2 * 9 + 3, createGuiItem(Material.GOLD_BLOCK, "§6Town balance: "
                + (TownManager.getTown(player) == null ? 0 : TownManager.getTown(player).getBalance())));
    }

    @Nullable
    public static Integer handleClick(InventoryClickEvent e, @Nullable Integer playerData) {
        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        if (clickedItem == null || clickedItem.getType() == Material.AIR)
            return playerData;

        Player player = (Player) e.getWhoClicked();

        switch (e.getSlot()) {
            case 9 * 2 + 4:
                GUIManager.openInventory(player, InventoryTypes.DEPOSIT);
                GUIManager.setInformation(player, 0);
                DepositInventory.updateInventory(player);
                return 0;

            case 9 * 2 + 5:
                GUIManager.openInventory(player, InventoryTypes.WITHDRAW);
                GUIManager.setInformation(player, 0);
                WithdrawInventory.updateInventory(player);
                return 0;
        }

        return null;
    }
}
