package fr.hiredream.chestrewardroyal;

import java.io.IOException;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Listener implements org.bukkit.event.Listener {
	
	Main main;
	public Listener(Main m) {
		this.main = m;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if ( e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK ) {
			if ( e.getPlayer().getInventory().getItemInHand().getType().equals(Material.CHEST) ) {
				String name = e.getPlayer().getInventory().getItemInHand().getItemMeta().getDisplayName();
				Chest chest = this.main.getUtil().getChestByName(name);

				if ( chest != null ) {
					e.getPlayer().sendMessage(this.main.getUtil().getPreventMessage());
					e.getPlayer().openInventory(chest.createInventory());
					main.chestListAll.put(e.getPlayer().getName(), chest);

					main.playerOpenCase.set(e.getPlayer().getName(), 0);
					try {
						main.playerOpenCase.save(main.playerOpenCaseFile);
					} catch (IOException e1) {
						e1.printStackTrace();
					}


					ItemStack hand = e.getPlayer().getInventory().getItemInMainHand();
					int amount = hand.getAmount();
					if (amount > 1) {
						hand.setAmount(amount - 1);
						e.getPlayer().getInventory().setItemInMainHand(hand);
					} else {
						e.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.AIR));
					}
					e.setCancelled(true);

					

				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerClick(InventoryClickEvent e) {
		Inventory inv = e.getInventory();

		if(inv instanceof AnvilInventory){
			AnvilInventory anvil = (AnvilInventory)inv;
			
			if ( anvil.contains(Material.CHEST)) {
				e.setCancelled(true);
			}
		}
		else {
			Chest chest = this.main.getUtil().getChestByName(inv.getName());
			if ( chest != null ) {
				if ( e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.STAINED_GLASS_PANE)) {
					int nbCaseOpen = main.playerOpenCase.getInt(e.getWhoClicked().getName())+1;
					main.playerOpenCase.set(e.getWhoClicked().getName(), nbCaseOpen);

					int numberCase = chest.getNumberCase();

					if ( nbCaseOpen <= numberCase ) {
						inv.setItem(e.getSlot(), main.chestListAll.get(e.getWhoClicked().getName()).itemRandom() );
					} else {
						e.getWhoClicked().sendMessage(this.main.getUtil().getPreventMessage());
					}

					e.setCancelled(true);
				}
			}
			
		}
	}
}
