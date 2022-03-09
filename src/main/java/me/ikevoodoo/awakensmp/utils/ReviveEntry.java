package me.ikevoodoo.awakensmp.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.UUID;

public class ReviveEntry {

    private final UUID reviver;
    private final Location location;

    public ReviveEntry(UUID reviver, Location location) {
        this.reviver = reviver;
        this.location = location;
    }

    public UUID // reviver
    getReviver() {
        return reviver;
    }

    public Location // location
    getLocation() {
        return location;
    }

    public void
    write(DataOutputStream out) throws Exception {
        out.writeUTF(String.valueOf(reviver));
        out.writeUTF(location.getWorld().getName());
        out.writeDouble(location.getX());
        out.writeDouble(location.getY());
        out.writeDouble(location.getZ());
    }

    public static ReviveEntry // Creates a new revive entry from a data input stream
    read(DataInputStream in) throws Exception {
        String uuid = in.readUTF();
        UUID id = uuid.equals("null") ? null : UUID.fromString(uuid);
        return new ReviveEntry(
                id,
                new Location(
                        Bukkit.getWorld(in.readUTF()),
                        in.readDouble(),
                        in.readDouble(),
                        in.readDouble()
                )
        );
    }

}
