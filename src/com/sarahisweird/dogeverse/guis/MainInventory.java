package com.sarahisweird.dogeverse.guis;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import static com.sarahisweird.dogeverse.inventories.GUIManager.createGuiItem;

public class MainInventory {
    public static ItemStack[] getContents() {
        ItemStack[] contents = new ItemStack[9 * 5];

        contents[0] = createGuiItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE, "MAIN", "This is a secret c;");
        contents[3] = createGuiItem(Material.BELL, "§aCreate a town", "This will cost you 500 Doge!");
        contents[5] = createGuiItem(Material.GOLD_NUGGET, "§6Town Treasury", "Lets you view and", "modify the town's treasury.");

        return contents;
    }

    @Nullable
    public static Integer handleClick(InventoryClickEvent e, @Nullable Integer playerData) {
        e.setCancelled(true);

        if (e.getCurrentItem() == null)
            return playerData;

        if (e.getCurrentItem().getType() == Material.GOLD_NUGGET) {
            GUIManager.openInventory((Player) e.getWhoClicked(), InventoryTypes.TREASURY);
            TreasuryInventory.updateInventory((Player) e.getWhoClicked());
        }

        return playerData;
    }
}
