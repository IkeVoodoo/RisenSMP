package me.ikevoodoo.awakensmp.listeners;

import me.ikevoodoo.awakensmp.AwakenSMP;
import me.ikevoodoo.awakensmp.utils.EliminationUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void on(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if(EliminationUtils.shouldRevive(p)) {
            EliminationUtils.revivePlayer(p);
        }

        if(!p.hasDiscoveredRecipe(AwakenSMP.getInstance().getRecipeKey()))
            p.discoverRecipe(AwakenSMP.getInstance().getRecipeKey());
    }

}
