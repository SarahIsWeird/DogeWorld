package com.sarahisweird.dogeverse.guis;

import com.sarahisweird.dogeverse.towns.Town;
import com.sarahisweird.dogeverse.towns.TownManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import static com.sarahisweird.dogeverse.guis.GUIManager.createGuiItem;

public class MainInventory {
    public static ItemStack[] getContents(Player player) {
        ItemStack[] contents = new ItemStack[9 * 5];

        contents[0] = createGuiItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE, "MAIN", "This is a secret c;");

        Town town = TownManager.getTown(player);

        if (town == null) { // Player is not in a town
            contents[9 + 4] = createGuiItem(Material.BELL, "§aCreate a town", "This will cost you 500 Doge!");

        } else { // Player is in a town
            contents[9 + 3] = createGuiItem(Material.KNOWLEDGE_BOOK, "§9Members",
                    "Show, add and", "remove members.");

            ItemStack itemStack = new ItemStack(Material.GRASS_BLOCK);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("§dTeleport to " + town.name);
            itemMeta.addEnchant(Enchantment.DURABILITY, 1, false);
            itemStack.setItemMeta(itemMeta);

            contents[9 + 4] = itemStack;

            contents[9 + 5] = createGuiItem(Material.GOLD_NUGGET, "§6Town Treasury",
                    "Lets you view and", "modify the town's", "treasury.");

            contents[9 * 2 + 4] = createGuiItem(Material.GRASS_BLOCK, "§dTeleport to another town");
        }

        return contents;
    }

    @Nullable
    public static Integer handleClick(InventoryClickEvent e, @Nullable Integer playerData) {
        e.setCancelled(true);

        if (e.getCurrentItem() == null)
            return playerData;

        Player player = (Player) e.getWhoClicked();

        switch (e.getCurrentItem().getType()) {
            case GOLD_NUGGET:
                GUIManager.openInventory((Player) e.getWhoClicked(), InventoryTypes.TREASURY);
                TreasuryInventory.updateInventory((Player) e.getWhoClicked());
                break;
            case BELL:
                if (!TownManager.initTownSetup(player)) {
                    player.sendMessage("§cYou are already creating a town!");
                }

                player.closeInventory();

                return playerData;
        }

        return playerData;
    }
}
