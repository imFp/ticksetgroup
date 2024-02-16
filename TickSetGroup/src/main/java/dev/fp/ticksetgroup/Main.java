package dev.fp.ticksetgroup;

import dev.fp.ticksetgroup.commands.DarVip;
import dev.fp.ticksetgroup.commands.GroupList;
import dev.fp.ticksetgroup.commands.SetGroup;
import dev.fp.ticksetgroup.commands.Vips;
import dev.fp.ticksetgroup.event.PlayerEvents;
import dev.fp.ticksetgroup.plugin.Config;
import dev.fp.ticksetgroup.plugin.FileUtils;
import dev.fp.ticksetgroup.util.GroupManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener {

    private static Main instance;
    private FileUtils fileUtils;

    public static Main getInstance() {return instance;}
    @Override
    public void onEnable() {
        System.out.println("§aPlugin TickSetGroup foi inicializado com sucesso.");
        GroupManager groupManager = new GroupManager(this);

        instance = this;
        this.fileUtils = new FileUtils(this);

        Bukkit.getPluginManager().registerEvents(new PlayerEvents(groupManager), this);
        Bukkit.getPluginManager().registerEvents(new SetGroup(groupManager), this);


        this.getCommand("setgroup").setExecutor(new SetGroup(groupManager));
        this.getCommand("groups").setExecutor(new GroupList());
        this.getCommand("darvip").setExecutor(new DarVip(groupManager));
        this.getCommand("vips").setExecutor(new Vips());
    }

    public Config getConfig(String name) {
        return this.getConfig("", name);
    }
    public Config getConfig(String path, String name) {
        return Config.getConfig(this, "plugins/" + this.getName() + "/" + path, name);
    }

    public FileUtils getFileUtils() {
        return this.fileUtils;
    }

    @Override
    public void onDisable() {
        System.out.println("§cPlugin TicketSetGroup foi desligado com sucesso.");
    }
}
