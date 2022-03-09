package me.ikevoodoo.awakensmp.utils;

import me.ikevoodoo.awakensmp.AwakenSMP;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.stream.Collectors;

public class InventoryUtils {
    private InventoryUtils() {

    }

    public static boolean
    hasTotem(Player player) {
        return player.getInventory().getItemInMainHand().getType().equals(Material.TOTEM_OF_UNDYING)
                || player.getInventory().getItemInOffHand().getType().equals(Material.TOTEM_OF_UNDYING);
    }

    public static void
    giveHeadItem(Player player, Player victim) {
        ItemStack stack = getHeadItem();
        SkullMeta meta = (SkullMeta) stack.getItemMeta();
        meta.setDisplayName(victim.getName());
        meta.setOwningPlayer(victim);
        stack.setItemMeta(meta);
        player.getInventory().addItem(stack).forEach((amount, item) ->
                player.getWorld().dropItem(victim.getLocation(), item));
    }

    public static ItemStack
    getHeadItem() {
        ItemStack stack = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = stack.getItemMeta();
        List<String> lore = AwakenSMP.getInstance()
                .getConfig().getStringList("skull-lore")
                .stream().map(s -> ChatColor.translateAlternateColorCodes('&', s))
                .collect(Collectors.toList());
        meta.setLore(lore);
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(AwakenSMP.getInstance().getHeadKey(), PersistentDataType.INTEGER, 0);
        stack.setItemMeta(meta);
        return stack;
    }


    public static void
    dropItems(Player player) {
        if(Boolean.TRUE.equals(player.getWorld().getGameRuleValue(GameRule.KEEP_INVENTORY))) return;
        for (ItemStack item : player.getInventory().getStorageContents()) {
            if (item != null) {
                player.getWorld().dropItem(player.getLocation(), item);
            }
        }
        player.getInventory().clear();
    }

    public static void
    updateItem(ItemStack item) {
        if(item == null || item.getType() != Material.PLAYER_HEAD) return;
        ItemMeta meta = item.getItemMeta();
        if(meta == null || !meta.hasDisplayName()) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if(!container.has(AwakenSMP.getInstance().getHeadKey(), PersistentDataType.INTEGER)) return;
        SkullMeta skullMeta = (SkullMeta) meta;
        OfflinePlayer owner = skullMeta.getOwningPlayer();
        OfflinePlayer player = Bukkit.getOfflinePlayer(skullMeta.getDisplayName());
        if(owner != null && owner.getUniqueId().equals(player.getUniqueId())) return;
        skullMeta.setOwningPlayer(player);
        item.setItemMeta(skullMeta);
    }

}
