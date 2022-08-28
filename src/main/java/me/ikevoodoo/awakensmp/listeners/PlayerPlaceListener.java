package me.ikevoodoo.awakensmp.listeners;

import me.ikevoodoo.awakensmp.AwakenSMP;
import me.ikevoodoo.awakensmp.utils.EliminationUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PlayerPlaceListener implements Listener {

    @EventHandler
    public void on(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        ItemStack stack = event.getItemInHand();

        event.setCancelled(run(player, stack, event.getHand(), event.getBlockPlaced().getLocation().clone().add(0.5, 0, 0.5)));
    }

    private boolean
    run(Player reviver, ItemStack stack, EquipmentSlot slot, Location loc) {
        ItemMeta meta = stack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if(!container.has(AwakenSMP.getInstance().getHeadKey(), PersistentDataType.INTEGER)) return false;
        String playerName = meta.getDisplayName();
        OfflinePlayer offlinePlayer;
        try {
            offlinePlayer = Bukkit.getOfflinePlayer(playerName);
        } catch (Exception e) {
            reviver.sendMessage(ChatColor.RED + "Player not found.");
            return true;
        }

        if (!offlinePlayer.isOnline()) {
            if (!offlinePlayer.hasPlayedBefore()) {
                reviver.sendMessage(ChatColor.RED + offlinePlayer.getName() + " not found!");
                return true;
            }
        }

        if(!EliminationUtils.isEliminated(offlinePlayer.getUniqueId())) {
            reviver.sendMessage(ChatColor.RED + offlinePlayer.getName() + " is not eliminated!");
            return true;
        }

        EliminationUtils.revivePlayer(reviver, offlinePlayer.getUniqueId(), loc);
        reviver.sendTitle(ChatColor.GOLD + "Revived",
                ChatColor.DARK_AQUA + offlinePlayer.getName(), 0, 20, 0);
        stack.setAmount(stack.getAmount() - 1);
        reviver.getInventory().setItem(slot, stack);
        return true;
    }

}
