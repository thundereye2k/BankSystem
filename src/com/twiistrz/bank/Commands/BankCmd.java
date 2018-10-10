package com.twiistrz.bank.Commands;

import com.twiistrz.bank.Main;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Twiistrz
 */
public class BankCmd implements CommandExecutor {

    private final Main plugin;
    
    public BankCmd(Main pl) {
        plugin = pl;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String prefix = plugin.getConfig().getString("prefix");
        
        if (!(sender instanceof Player)) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("reload")) {
                    pluginReload();
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&aConfig reloaded! Author: Twiistrz"));
                    return true;
                } else if (args[0].equalsIgnoreCase("balance") || args[0].equalsIgnoreCase("withdraw") || args[0].equalsIgnoreCase("deposit")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + plugin.getConfig().getString("players-only")));
                    return true;
                }
            }
            
            List<String> helpMsgs = plugin.getConfig().getStringList("help");
            for(String helpMsg : helpMsgs) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', helpMsg));
            }
            return true;
        }

        Player p = (Player) sender;

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (p.hasPermission("mcpz.banksystem.reload") || p.hasPermission("mcpz.banksystem.admin")) {
                    pluginReload();
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&aConfig reloaded! Author: Twiistrz"));
                    return true;
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + plugin.getConfig().getString("no-permission")));
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("balance")) {
                Double bankBalance = 123.45;
                Double handBalance = 678.90;
                Double totalBalance = bankBalance + handBalance;                
                
                List<String> balanceMsgs = plugin.getConfig().getStringList("balance");
                for (String balanceMsg : balanceMsgs) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', balanceMsg
                        .replace("%bank%", bankBalance.toString())
                        .replace("%player%", handBalance.toString())
                        .replace("%total%", totalBalance.toString())
                    ));
                }
                return true;
            }
        }
        
        // 'help' message
        List<String> helpMsgs = plugin.getConfig().getStringList("help");
        for (String helpMsg : helpMsgs) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', helpMsg));
        }
        return true;
    }

    private void pluginReload() {
        plugin.reloadConfig();
        plugin.saveConfig();
    }
}
