package com.twiistrz.banksystem;

import com.twiistrz.banksystem.commands.BankCommand;
import java.util.logging.Level;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Twiistrz
 */
public class BankSystem extends JavaPlugin {
    
    private static BankSystem plugin;
    public static Economy econ = null;
    
    public static BankSystem getInstance() {
        return plugin;
    }
    
    @Override
    public void onEnable() {
        plugin = this;
        
        // Check vault dependency
        if (!setupEconomy()) {
            getLogger().log(Level.SEVERE, "Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        // Version checking of config file
        if (!getDescription().getVersion().equals(getConfig().getString("version"))) {
            getLogger().log(Level.WARNING, "Using outdated version of config file!");
        }
        
        saveDefaultConfig();
        getLogger().log(Level.INFO, "Enabled {0} v{1}!", 
            new Object[]{
                getDescription().getName(),
                getDescription().getVersion()
            });
        
        // Main command
        getCommand("bank").setExecutor(new BankCommand(this));
    }
    
    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "Disabled {0} v{1}!", 
            new Object[]{
                getDescription().getName(),
                getDescription().getVersion()
            });
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) { return false; }
        
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) { return false; }
        
        econ = rsp.getProvider();
        return econ != null;
    }
}