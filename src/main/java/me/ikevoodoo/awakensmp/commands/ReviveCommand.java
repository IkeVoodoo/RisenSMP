package me.ikevoodoo.awakensmp.commands;

import me.ikevoodoo.awakensmp.utils.EliminationUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReviveCommand implements CommandExecutor {
    @Override
    public boolean
    onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /revive <player>");
            return true;
        }

        if ("all".equals(args[0])) {
            EliminationUtils.getEliminations().forEach(id ->
                    EliminationUtils.revivePlayer(id, Bukkit.getWorlds().get(0).getSpawnLocation()));
            sender.sendMessage(ChatColor.GREEN + "All players have been revived.");
            return true;
        }


        Player target = sender.getServer().getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return true;
        }

        if(!EliminationUtils.isEliminated(target.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "Player is not eliminated.");
            return true;
        }

        Location loc =
                !(sender instanceof Player) ? target.getWorld().getSpawnLocation()
                        : ((Player) sender).getLocation();
        EliminationUtils.revivePlayer(target.getUniqueId(), loc);
        sender.sendMessage(ChatColor.GREEN + target.getName() + " has been revived.");
        return true;
    }
}
