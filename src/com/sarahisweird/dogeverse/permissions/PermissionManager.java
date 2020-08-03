package com.sarahisweird.dogeverse.permissions;

import com.sarahisweird.dogeverse.Dogeverse;
import com.sarahisweird.dogeverse.ranks.Rank;
import com.sarahisweird.dogeverse.ranks.RankManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PermissionManager {
    private static FileConfiguration permissionConfig;

    private static HashMap<UUID, PermissionAttachment> permissions;

    private static List<String> allPermissionNodes;
    private static HashMap<Rank, List<String>> rankPermissionNodes;

    private static List<String> getAllFromNode(String wildcardedNode) {
        List<String> nodes = new ArrayList<>();

        for (String node : allPermissionNodes) {
            if (node.startsWith(wildcardedNode))
                nodes.add(node);
        }

        return nodes;
    }

    public static void load() {
        permissions = new HashMap<>();
        rankPermissionNodes = new HashMap<>();

        permissionConfig = Dogeverse.plugin.getPermissionsConfig();

        allPermissionNodes = permissionConfig.getStringList("all");

        for (Rank rank : RankManager.getRanks()) {
            String rankName = rank.getName();

            List<String> nodes = permissionConfig.getStringList(rankName);

            List<String> permissionNodes = new ArrayList<>();

            for (String node : nodes) {
                if (!node.endsWith(".*")) {
                    permissionNodes.add(node);
                } else {
                    permissionNodes.addAll(getAllFromNode(node.substring(0, node.length() - 2)));
                }
            }

            rankPermissionNodes.put(rank, permissionNodes);
        }
    }

    public static void initPlayer(Player player) {
        permissions.put(player.getUniqueId(), player.addAttachment(Dogeverse.plugin));

        Rank rank = RankManager.getRank(player);
        PermissionAttachment permissionAttachment = permissions.get(player.getUniqueId());

        for (String node : rankPermissionNodes.get(rank)) {
            Dogeverse.logger.info(node);
            permissionAttachment.setPermission(node, true);
        }
    }

    public static void reloadPlayer(Player player) {
        player.removeAttachment(permissions.get(player.getUniqueId()));
        permissions.remove(player.getUniqueId());
        permissions.put(player.getUniqueId(), player.addAttachment(Dogeverse.plugin));

        Rank rank = RankManager.getRank(player);
        PermissionAttachment permissionAttachment = permissions.get(player.getUniqueId());

        for (String node : rankPermissionNodes.get(rank)) {
            permissionAttachment.setPermission(node, true);
        }
    }

    public static boolean hasPermission(Player player, String permissionNode) {
        PermissionAttachment perms = permissions.get(player.getUniqueId());

        if (perms == null) {
            return false;
        }

        return perms.getPermissible().hasPermission(permissionNode);
    }

    public static boolean hasAnySubPermission(Player player, String parentNode) {
        PermissionAttachment perms = permissions.get(player.getUniqueId());

        if (perms == null) {
            return false;
        }

        for (PermissionAttachmentInfo info : perms.getPermissible().getEffectivePermissions()) {
            if (info.getPermission().startsWith(parentNode))
                return true;
        }

        return false;
    }

    public static List<String> getPermissions(Rank rank) {
        return rankPermissionNodes.get(rank);
    }

    public static List<String> getPermissions(Player player) {
        return new ArrayList<>(permissions.get(player.getUniqueId()).getPermissions().keySet());
    }
}
