package me.ikevoodoo.awakensmp.listeners;

import me.ikevoodoo.awakensmp.AwakenSMP;
import me.ikevoodoo.awakensmp.utils.InventoryUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ShapedRecipe;

public class CraftItemListener implements Listener {

    @EventHandler
    public void on(CraftItemEvent event) {
        if (!(event.getRecipe() instanceof ShapedRecipe)) {
            return;
        }

        ShapedRecipe recipe = (ShapedRecipe) event.getRecipe();

        if(!recipe.getKey().equals(AwakenSMP.getInstance().getRecipeKey()))
            return;

        event.setCurrentItem(InventoryUtils.getHeadItem());
    }

}
