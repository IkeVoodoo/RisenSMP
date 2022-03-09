package me.ikevoodoo.awakensmp.listeners;

import me.ikevoodoo.awakensmp.utils.InventoryUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;

public class MoveItemListener implements Listener {

    @EventHandler
    public void on(InventoryClickEvent event) {
        InventoryUtils.updateItem(event.getCurrentItem());
    }

    @EventHandler
    public void on(InventoryPickupItemEvent event) {
        InventoryUtils.updateItem(event.getItem().getItemStack());
    }

    @EventHandler
    public void on(PlayerDropItemEvent event) {
        InventoryUtils.updateItem(event.getItemDrop().getItemStack());
    }


}
