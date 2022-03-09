package me.ikevoodoo.awakensmp.utils;

import me.ikevoodoo.awakensmp.AwakenSMP;

import java.io.File;
import java.util.HashMap;

public class FileManager {
    private static boolean init;

    private final HashMap<String, File> files = new HashMap<>();

    private final File dataFolder;

    public FileManager() {
        if(init) {
            throw new IllegalStateException("AwakenSMP FileManager already initialized!");
        }

        init = true;
        dataFolder = new File(AwakenSMP.getInstance().getDataFolder(), "data");

        if(!dataFolder.exists()) {
            dataFolder.mkdirs();
            return;
        }

        if(!dataFolder.isDirectory()) {
            dataFolder.delete();
            dataFolder.mkdirs();
            return;
        }

        if(dataFolder.exists()) {
            for(File file : dataFolder.listFiles()) {
                files.put(file.getName(), file);
            }
        }
    }

    public File
    getFile(String name) {
        if(!files.containsKey(name)) {
            return addFile(name);
        }
        return files.get(name);
    }

    public File
    addFile(String name) {
        File file = new File(dataFolder, name);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        files.put(name, file);
        return file;
    }

    public void
    removeFile(String name) {
        File file = files.remove(name);
        if (file != null) {
            file.delete();
        }
    }
}
