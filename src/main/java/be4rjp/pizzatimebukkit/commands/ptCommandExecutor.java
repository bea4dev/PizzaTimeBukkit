package be4rjp.pizzatimebukkit.commands;

import be4rjp.pizzatimebukkit.Config;
import be4rjp.pizzatimebukkit.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ptCommandExecutor implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if(args == null) return false;
        if(args.length == 0) return false;
        
        //------------------------Check sender type-----------------------------
        CommanderType type = CommanderType.CONSOLE;
        if(sender instanceof Player){
            if(((Player)sender).hasPermission("pt.admin"))
                type = CommanderType.ADMIN;
            else
                type = CommanderType.MEMBER;
        }
        //----------------------------------------------------------------------
        
        //--------------------------------/pt--------------------------------------
        if(args[0].equalsIgnoreCase("reload")){
            if(type == CommanderType.MEMBER){
                sender.sendMessage(ChatColor.RED + "You don't have permission!");
                return true;
            }
            Main.getPlugin().getLogger().info("Loading config files...");
            Main.config.reloadConfig();
            sender.sendMessage(ChatColor.GREEN + "The configuration was successfully reloaded!");
            return true;
        }
        //-------------------------------------------------------------------------
        return false;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        
        //------------------------Check sender type-----------------------------
        CommanderType type = CommanderType.CONSOLE;
        if(sender instanceof Player){
            if(((Player)sender).hasPermission("pt.admin"))
                type = CommanderType.ADMIN;
            else
                type = CommanderType.MEMBER;
        }
        //----------------------------------------------------------------------
        
        //-----------------------------Tab complete-----------------------------
        if (args.length == 1) {
            List<String> list = new ArrayList<>();
    
            list.add("help");
    
            if (type != CommanderType.MEMBER) {
                list.add("reload");
            }
    
            return list;
    
        }
        return null;
        //----------------------------------------------------------------------
    }
}
