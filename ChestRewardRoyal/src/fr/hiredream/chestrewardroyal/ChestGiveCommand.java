package fr.hiredream.chestrewardroyal;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ChestGiveCommand implements CommandExecutor {
	
	public Main main;
	
	public ChestGiveCommand(Main m)
	{
		this.main = m;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if ( (sender instanceof Player && sender.isOp()) || !(sender instanceof Player) ) {
			if ( args.length == 0 || (args.length == 1 && !args[0].equalsIgnoreCase("reload") || (args.length == 2 && args[0].equalsIgnoreCase("give")))) {
				sender.sendMessage("§6/crr reload");
				sender.sendMessage("§6/crr give <player> <chestName>");
                sender.sendMessage("§6/crr list");
			}
			if ( args.length == 3) {
				Player p = Bukkit.getPlayer(args[1]);
				
				if ( p != null ) {
					if ( args[0].equals("give")) {
						ItemStack item1 = new ItemStack(Material.CHEST, 1);
						ItemMeta meta = (ItemMeta) item1.getItemMeta();

						Chest chest = this.main.getUtil().chestExist(args[2]);
						if ( chest != null ) {
							meta.setDisplayName(chest.getName());
							item1.setItemMeta(meta);
							p.getInventory().addItem(item1);
						}
					}
				}
			}
			else if ( args.length == 1 ) {
			    if (args[0].equals("reload")) {
                    main.reloadConfig();
                    if (sender instanceof Player)
                        sender.sendMessage("§aPlugin ChestRewardRoyal reloaded !");
                } else if ( args[0].equals("list") ) {
			        sender.sendMessage("§aList of chests :");
                    for ( Chest chest : this.main.getListChest()) {
                        sender.sendMessage("§a  - "+chest.getId());
                    }
                }
			}
		}
		else {
			sender.sendMessage("§cYou don't have permission !");
		}
		return true;
	}
}