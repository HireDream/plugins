package fr.hiredream.chestrewardroyal;

import org.bukkit.ChatColor;

public class Util {

    private Main main;

    public Util(Main m) {
        this.main = m;
    }

	public Chest chestExist(String id) {
		for ( Chest chest : this.main.getListChest()) {
            if ( chest.getId().equalsIgnoreCase(id)) {
                return chest;
            }
        }

        return null;
	}

	public Chest getChestByName(String name) {
        for ( Chest chest : this.main.getListChest()) {
            if ( chest.getName().equalsIgnoreCase(name)) {
                return chest;
            }
        }

        return null;
    }
	
	public String getPreventMessage() {
		return ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("preventMessage"));
	}

	public String getMaxOpenCaseMessage() {
        return ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("preventMessage"));
    }

}
