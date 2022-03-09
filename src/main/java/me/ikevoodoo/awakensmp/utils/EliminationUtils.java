package me.ikevoodoo.awakensmp.utils;

import me.ikevoodoo.awakensmp.AwakenSMP;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class EliminationUtils {
    private EliminationUtils() {

    }

    private static final List<UUID> eliminations = new CopyOnWriteArrayList<>();
    private static final ConcurrentHashMap<UUID, ReviveEntry> toRevive = new ConcurrentHashMap<>();
    private static boolean init;

    static {
        AwakenSMP instance = AwakenSMP.getInstance();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, () -> Bukkit.getScheduler().runTaskAsynchronously(instance,
                EliminationUtils::save), 0, 100);
    }

    public static List<UUID> getEliminations() {
        return new ArrayList<>(eliminations);
    }

    private static void
    save() {
        AwakenSMP instance = AwakenSMP.getInstance();
        FileManager fileManager = instance.getFileManager();
        fileManager.removeFile("eliminations");
        File eliminationsFile = fileManager.getFile("eliminations");
        try(DataOutputStream os = new DataOutputStream(
                new GZIPOutputStream(new FileOutputStream(eliminationsFile)))) {
            os.writeInt(eliminations.size());
            for(UUID uuid : eliminations) {
                os.writeUTF(uuid.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        fileManager.removeFile("revive");
        File reviveFile = fileManager.getFile("revive");
        try(DataOutputStream os = new DataOutputStream(
                new GZIPOutputStream(new FileOutputStream(reviveFile)))) {
            os.writeInt(toRevive.size());
            toRevive.forEach((uuid, reviveEntry) -> {
                try {
                    os.writeUTF(uuid.toString());
                    reviveEntry.write(os);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void
    load() {
        if(init) throw new IllegalStateException("Already initialized!");
        init = true;

        FileManager fileManager = AwakenSMP.getInstance().getFileManager();
        File eliminationsFile = fileManager.getFile("eliminations");
        if(!eliminationsFile.exists()) save();
        try(DataInputStream is = new DataInputStream(
                new GZIPInputStream(new FileInputStream(eliminationsFile)))) {
            int size = is.readInt();
            for(int i = 0; i < size; i++) {
                eliminations.add(UUID.fromString(is.readUTF()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        File reviveFile = fileManager.getFile("revive");
        try(DataInputStream is = new DataInputStream(
                new GZIPInputStream(new FileInputStream(reviveFile)))) {
            int size = is.readInt();
            for(int i = 0; i < size; i++) {
                UUID uuid = UUID.fromString(is.readUTF());
                ReviveEntry reviveEntry = ReviveEntry.read(is);
                toRevive.put(uuid, reviveEntry);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void
    eliminate(Player player, Player killer) {
        player.setGameMode(GameMode.SPECTATOR);
        player.sendMessage(ChatColor.RED + "You have been eliminated!");

        InventoryUtils.giveHeadItem(killer, player);

        addElimination(player);
    }

    public static void
    revivePlayer(OfflinePlayer reviver, UUID uuid, Location loc) {
        eliminations.remove(uuid);
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        if(offlinePlayer.isOnline()) {
            Player player = offlinePlayer.getPlayer();
            player.teleport(loc);
            player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            player.setGameMode(GameMode.SURVIVAL);
            String name = reviver == null ? "Console" : reviver.getName();
            player.sendTitle(ChatColor.GREEN + "Revived!",
                    ChatColor.GOLD + "By " + ChatColor.DARK_AQUA + name, 0, 20, 0);
            return;
        }

        toRevive.put(uuid, new ReviveEntry(
                reviver == null ? null : reviver.getUniqueId(), loc));
    }

    public static void
    revivePlayer(UUID uuid, Location loc) {
        revivePlayer(null, uuid, loc);
    }

    public static void
    revivePlayer(Player player) {
        ReviveEntry entry = toRevive.get(player.getUniqueId());
        if(entry == null) {
            return;
        }
        UUID reviver = entry.getReviver();
        revivePlayer(reviver == null ? null : Bukkit.getOfflinePlayer(reviver), player.getUniqueId(), entry.getLocation());
        toRevive.remove(player.getUniqueId());
    }

    public static boolean
    shouldRevive(Player player) {
        return toRevive.containsKey(player.getUniqueId());
    }

    public static boolean
    isEliminated(UUID uuid) {
        return eliminations.contains(uuid);
    }

    private static void
    addElimination(Player player) {
        eliminations.add(player.getUniqueId());
    }


}
