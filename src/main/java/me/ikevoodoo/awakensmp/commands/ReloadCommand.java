package me.ikevoodoo.awakensmp.commands;

import me.ikevoodoo.awakensmp.AwakenSMP;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {
    @Override
    public boolean
    onCommand(CommandSender sender, Command command, String label, String[] args) {
        AwakenSMP.getInstance().reload();
        sender.sendMessage(ChatColor.GOLD + "Reloaded!");
        return true;
    }
}
