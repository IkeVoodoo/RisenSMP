package me.ikevoodoo.awakensmp.listeners;

import me.ikevoodoo.awakensmp.utils.EliminationUtils;
import me.ikevoodoo.awakensmp.utils.InventoryUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerDamageListener implements Listener {
    
    @EventHandler
    private void on(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        if(!(event.getDamager() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        if(player.getHealth() - event.getFinalDamage() > 0) return;

        if(InventoryUtils.hasTotem(player)) return;

        event.setCancelled(true);

        InventoryUtils.dropItems(player);

        Player killer = (Player) event.getDamager();
        EliminationUtils.eliminate(player, killer);
    }
    
}
