package com.twiistrz.banksystem;

import com.twiistrz.banksystem.commands.BankCommand;
import java.util.logging.Level;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Twiistrz
 */
public class BankSystem extends JavaPlugin {
    
    private static BankSystem instance;
    
    @Override
    public void onEnable() {
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
}