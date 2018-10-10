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
    
    private static BankSystem instance;
    public static Economy econ = null;
    
    @Override
    public void onEnable() {
        if (!setupEconomy()) {
            getLogger().log(Level.SEVERE, "Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        instance = this;
        saveDefaultConfig();
        getLogger().log(Level.INFO, "Enabled {0} v{1}!", 
            new Object[]{
                getDescription().getName(),
                getDescription().getVersion()
            });
        if (!(getDescription().getVersion().equals(getConfig().getString("version")))) {
            getLogger().log(Level.WARNING, "Using outdated version of config file!");
        }
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
    
    public static BankSystem getInstance() {
        return instance;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}