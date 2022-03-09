package me.ikevoodoo.awakensmp.commands.completer;

import me.ikevoodoo.awakensmp.utils.EliminationUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class ReviveCompleter implements TabCompleter {
    @Override
    public List<String>
    onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> arr = new ArrayList<>();
        arr.add("all");
        if(args.length > 1) {
            return null;
        }

        EliminationUtils.getEliminations().forEach(id ->
                arr.add(Bukkit.getOfflinePlayer(id).getName()));

        return arr;
    }
}
