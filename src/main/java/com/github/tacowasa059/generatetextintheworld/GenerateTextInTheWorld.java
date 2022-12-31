package com.github.tacowasa059.generatetextintheworld;

import com.github.tacowasa059.generatetextintheworld.commands.GenerateCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class GenerateTextInTheWorld extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        getCommand("GenerateText").setExecutor(new GenerateCommand(this));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
