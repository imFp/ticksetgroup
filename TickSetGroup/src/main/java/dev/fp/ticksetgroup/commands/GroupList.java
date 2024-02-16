package dev.fp.ticksetgroup.commands;

import dev.fp.ticksetgroup.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GroupList implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            sender.sendMessage(Main.getInstance().getConfig("messages").getString("setgrouplist"));
            return true;
        }

        return false;
    }
}
