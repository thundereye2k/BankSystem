package com.twiistrz.banksystem.commands;

import com.twiistrz.banksystem.BankSystem;
import java.util.List;
import java.util.UUID;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Twiistrz
 */
public class BankCommand implements CommandExecutor {

    private final BankSystem plugin;
    
    public BankCommand(BankSystem pl) {
        plugin = pl;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String prefix = plugin.getConfig().getString("prefix");
        List<String> helpMessages = plugin.getConfig().getStringList("help");
        List<String> balanceMessages = plugin.getConfig().getStringList("balance");
        
        if (!(sender instanceof Player)) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("reload")) {
                    plugin.reloadConfig();
                    plugin.saveDefaultConfig();
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&aReloaded BankSystem's configuration file."));
                    return true;
                } else if (args[0].equalsIgnoreCase("balance") || args[0].equalsIgnoreCase("withdraw") || args[0].equalsIgnoreCase("deposit")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + plugin.getConfig().getString("players-only")));
                    return true;
                }
            }
            
            for (String helpMessage : helpMessages) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', helpMessage));
            }
            return true;
        }
        
        Player p = (Player) sender;
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("reload")) {
                // Reload plugin config file
                // Check for permission
                if (p.hasPermission("banksystem.reload") || p.hasPermission("banksystem.admin")) {
                    plugin.reloadConfig();
                    plugin.saveDefaultConfig();
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&aReloaded BankSystem's configuration file."));
                    return true;
                }
                
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + plugin.getConfig().getString("no-permission")));
                return true;
            } else if (args[0].equalsIgnoreCase("balance")) {
                // Show balance of player
                // Check for permission
                if (p.hasPermission("banksystem.balance") || p.hasPermission("banksystem.admin")) {
                    Double bankBalance = (double) Math.round(0.0 * 100) / 100; //Sample data
                    Double playerBalance = (double) Math.round(BankSystem.econ.getBalance(p) * 100) / 100; //Sample data
                    Double totalBalance = (double) Math.round((bankBalance + playerBalance) * 100) / 100; //Round to 2 decimal place
                    for (String balanceMessage : balanceMessages) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', balanceMessage
                            .replace("%bank%", bankBalance.toString())
                            .replace("%player%", playerBalance.toString())
                            .replace("%total%", totalBalance.toString())
                        ));
                    }
                    
                    UUID id = p.getUniqueId();
                    String uid = id.toString();
                    p.sendMessage(uid);
                    return true;
                }
                
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + plugin.getConfig().getString("no-permission")));
                return true;
            } else if (args[0].equalsIgnoreCase("withdraw")) {
                // Bank withdrawal
                // Check permission
                if (p.hasPermission("banksystem.withdraw")) {
                    if (args.length > 1 && args.length < 3) {
                        if (isInteger(args[1])) {
                            int money = Integer.parseInt(args[1]);
                            if (money < 1) {
                                // Money is less than or equal to zero
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + plugin.getConfig().getString("withdraw-zero")));
                                return true;
                            }

                            // Withdraw money
                            EconomyResponse r = BankSystem.econ.depositPlayer(p, money);
                            if (r.transactionSuccess()) {
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + plugin.getConfig().getString("withdraw").replace("%money%", args[1])));
                                return true;
                            } else {
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + plugin.getConfig().getString("not-enough-money")));
                                return true;
                            }
                        }

                        // Not whole number
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + plugin.getConfig().getString("withdraw-invalid")));
                        return true;
                    }

                    // Withdraw usage
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + plugin.getConfig().getString("withdraw-usage")));
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("deposit")) {
                // Bank deposits
                // Check permission
                if (p.hasPermission("banksystem.deposit")) {
                    if (args.length > 1 && args.length < 3) {
                        if (isInteger(args[1])) {
                            int money = Integer.parseInt(args[1]);
                            if (money < 1) {
                                // Money is less than or equal to zero
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + plugin.getConfig().getString("deposit-zero")));
                                return true;
                            }
                            
                            // Deposit money
                            if (money <= BankSystem.econ.getBalance(p)) {
                                EconomyResponse r = BankSystem.econ.withdrawPlayer(p, money);
                                if (r.transactionSuccess()) {
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + plugin.getConfig().getString("deposit").replace("%money%", args[1])));
                                    return true;
                                } else {
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + plugin.getConfig().getString("not-enough-money")));
                                    return true;
                                }
                            } else {
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + plugin.getConfig().getString("not-enough-money")));
                                return true;
                            }
                        }

                        // Not whole number
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + plugin.getConfig().getString("deposit-invalid")));
                        return true;
                    }

                    // Deposit usage
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + plugin.getConfig().getString("deposit-usage")));
                    return true;
                }
            }
        }

        // === Fallback message for bank === //
        for (String helpMessage : helpMessages) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', helpMessage));
        }
        return true;
    }

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }
}
