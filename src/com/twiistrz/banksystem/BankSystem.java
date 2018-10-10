package com.twiistrz.banksystem;

import com.twiistrz.banksystem.commands.BankCommand;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Twiistrz
 */
public class BankSystem extends JavaPlugin {

    @Override
    public void onEnable() {
        PluginDescriptionFile plFile = getDescription();
        Logger logger = getLogger();
        logger.log(Level.INFO, "Enabled {1} v{2}!", new Object[]{plFile.getName(), plFile.getVersion()});

        registerConfig();
        registerCommand();
    }

    @Override
    public void onDisable() {
        PluginDescriptionFile plFile = getDescription();
        Logger logger = getLogger();
        
        logger.log(Level.INFO, "Disabled {1} v{2}!", new Object[]{plFile.getName(), plFile.getVersion()});
    }

    private void registerConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    private void registerCommand() {
        this.getCommand("bank").setExecutor(new BankCommand(this));
    }
}
