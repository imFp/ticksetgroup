package dev.fp.ticksetgroup.plugin;

import dev.fp.ticksetgroup.Main;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

public class Config {

    private static final Map<String, Config> cache = new HashMap<>();
    private final JavaPlugin plugin;
    private final File file;
    private YamlConfiguration config;

    private Config(JavaPlugin plugin, String path, String name) {
        this.plugin = plugin;
        this.file = new File(path + "/" + name + ".yml");
        if (!file.exists()) {
            this.file.getParentFile().mkdirs();
            InputStream in = plugin.getResource(name + ".yml");
            if (in != null) {
                Main.getInstance().getFileUtils().copyFile(in, file);
            } else {
                try {
                    this.file.createNewFile();
                } catch (IOException ex) {
                    plugin.getLogger().log(Level.SEVERE, "Um erro inesperado ocorreu ao criar o arquivo \"" + file.getName() + "\": ", ex);
                }
            }
        }

        try {
            this.config = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Um erro inesperado ocorreu ao criar a config \"" + file.getName() + "\": ", ex);
        }
    }

    public static Config getConfig(JavaPlugin plugin, String path, String name) {
        if (!cache.containsKey(path + "/" + name)) {
            cache.put(path + "/" + name, new Config(plugin, path, name));
        }

        return cache.get(path + "/" + name);
    }

    public boolean createSection(String path) {
        this.config.createSection(path);
        return save();
    }

    public boolean set(String path, Object obj) {
        this.config.set(path, obj);
        return save();
    }

    public boolean contains(String path) {
        return this.config.contains(path);
    }

    public Object get(String path) {
        return this.config.get(path);
    }

    public int getInt(String path) {
        return this.config.getInt(path);
    }

    public int getInt(String path, int def) {
        return this.config.getInt(path, def);
    }

    public double getDouble(String path) {
        return this.config.getDouble(path);
    }

    public double getDouble(String path, double def) {
        return this.config.getDouble(path, def);
    }

    public String getString(String path) {
        return this.config.getString(path);
    }

    public boolean getBoolean(String path) {
        return this.config.getBoolean(path);
    }

    public boolean getBoolean(String path, boolean def) {
        return this.config.getBoolean(path, def);
    }

    public List<String> getStringList(String path) {
        return this.config.getStringList(path);
    }

    public List<Integer> getIntegerList(String path) {
        return this.config.getIntegerList(path);
    }

    public Set<String> getKeys(boolean flag) {
        return this.config.getKeys(flag);
    }

    public ConfigurationSection getSection(String path) {
        return this.config.getConfigurationSection(path);
    }

    public void reload() {
        try {
            this.config = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        } catch (IOException ex) {
            Main.getInstance().getLogger().log(Level.SEVERE, "Um erro inesperado ocorreu ao recarregar a config \"" + file.getName() + "\": ", ex);
        }
    }

    public boolean save() {
        try {
            this.config.save(this.file);
            return true;
        } catch (IOException ex) {
            Main.getInstance().getLogger().log(Level.SEVERE, "Um erro inesperado ocorreu ao salvar a config \"" + file.getName() + "\": ", ex);
            return false;
        }
    }

    public File getFile() {
        return this.file;
    }

    public YamlConfiguration getRawConfig() {
        return this.config;
    }
}