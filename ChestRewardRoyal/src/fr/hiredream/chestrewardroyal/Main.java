package fr.hiredream.chestrewardroyal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.mysql.fabric.xmlrpc.base.Array;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	private CommandExecutor chestGiveCommand;
	public FileConfiguration playerOpenCase;
	public File playerOpenCaseFile;
	public Util util;
	public ArrayList<Chest> listChest = new ArrayList<Chest>();
    public HashMap<String, Chest> chestListAll = new HashMap<String, Chest>();


	
	@Override
    public void onEnable(){
	    this.util = new Util(this);
		saveDefaultConfig();
		registerConfig();
		chestGiveCommand = new ChestGiveCommand(this);
		getCommand("crr").setExecutor(chestGiveCommand);
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new Listener(this), this);
    }
	
	@Override
    public void onDisable(){
        
    }
	
	public void registerConfig() {

        ConfigurationSection section = this.getConfig().getConfigurationSection("chests");
		for(String chest : section.getKeys(false)) {
		    String id = chest;
			String name = ChatColor.translateAlternateColorCodes('&', section.getString(chest+".name"));
			int numberCase = section.getInt(chest+".maxOpenCase");

            File fileTmp = new File("plugins/ChestRewardRoyal/chest/"+id+".yml");
            FileConfiguration configTmp = null;
            if(!fileTmp.exists()) {
                configTmp = YamlConfiguration.loadConfiguration(getResource("configBase.yml"));
                try { configTmp.save(fileTmp); }
                catch (IOException e) {}
            }
            else {
                configTmp = YamlConfiguration.loadConfiguration(fileTmp);
            }

            Chest tmp = new Chest(this, configTmp, name, numberCase,id);
            this.listChest.add(tmp);
		}


		playerOpenCaseFile = new File("plugins/ChestRewardRoyal/playerOpenCase.yml");
	    if(!playerOpenCaseFile.exists()) {
	    	try { playerOpenCaseFile.createNewFile(); }
            catch (IOException e) {}
	    }
		playerOpenCase = YamlConfiguration.loadConfiguration(playerOpenCaseFile);
	}

	public ArrayList<Chest> getListChest() {
        return this.listChest;
    }
    public Util getUtil() {
        return this.util;
    }
}
