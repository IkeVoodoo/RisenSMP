package me.ikevoodoo.awakensmp;

import me.ikevoodoo.awakensmp.commands.ReloadCommand;
import me.ikevoodoo.awakensmp.commands.ReviveCommand;
import me.ikevoodoo.awakensmp.commands.completer.ReviveCompleter;
import me.ikevoodoo.awakensmp.listeners.*;
import me.ikevoodoo.awakensmp.utils.EliminationUtils;
import me.ikevoodoo.awakensmp.utils.FileManager;
import me.ikevoodoo.awakensmp.utils.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;

public final class AwakenSMP extends JavaPlugin {

    private static AwakenSMP instance;

    private NamespacedKey headKey, recipeKey;

    private FileManager fileManager;

    @Override
    public void onEnable() {
        instance = this;
        headKey = new NamespacedKey(this, "head");
        recipeKey = new NamespacedKey(this, "head_recipe");

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerDamageListener(), this);
        pm.registerEvents(new PlayerPlaceListener(), this);
        pm.registerEvents(new PlayerJoinListener(), this);
        pm.registerEvents(new CraftItemListener(), this);
        pm.registerEvents(new MoveItemListener(), this);

        getCommand("arevive").setExecutor(new ReviveCommand());
        getCommand("arevive").setTabCompleter(new ReviveCompleter());

        getCommand("areload").setExecutor(new ReloadCommand());

        new Metrics(this, 14577);

        fileManager = new FileManager();

        EliminationUtils.load();

        saveDefaultConfig();

        updateRecipe();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void
    reload() {
        reloadConfig();
        updateRecipe();
    }

    public void
    updateRecipe() {
        if(Bukkit.getRecipe(recipeKey) != null) {
            Bukkit.removeRecipe(recipeKey);
        }
        FileConfiguration config = getConfig();
        Material[] materials = new Material[9];
        for (int i = 0; i < 9; i++) {
            String item = config.getString("recipe." + (i + 1));
            if (item != null) {
                try {
                    materials[i] = Material.valueOf(item.toUpperCase(Locale.ROOT));
                } catch (Exception e) {
                    materials[i] = Material.AIR;
                }
                continue;
            }

            materials[i] = Material.AIR;
        }

        ShapedRecipe recipe = new ShapedRecipe(recipeKey, InventoryUtils.getHeadItem());

        recipe.shape("012", "345", "678");
        for (int i = 0; i < 9; i++) {
            recipe.setIngredient((i + "").charAt(0), materials[i]);
        }
        Bukkit.addRecipe(recipe);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if(!player.hasDiscoveredRecipe(recipeKey))
                player.discoverRecipe(recipeKey);
        }
    }

    public static AwakenSMP
    getInstance() {
        return instance;
    }

    public NamespacedKey
    getHeadKey() {
        return headKey;
    }

    public NamespacedKey
    getRecipeKey() {
        return recipeKey;
    }

    public FileManager
    getFileManager() {
        return fileManager;
    }
}
