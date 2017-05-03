package fr.hiredream.chestrewardroyal;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class Chest {

    private Main main;
    private FileConfiguration fileConfiguration;
    private int numberCase;
    private String name;
    private String id;
    private ArrayList<ItemStack> listItem = new ArrayList<ItemStack>();

    public Chest(Main m, FileConfiguration fileConfiguration, String name, int numberCase, String id) {
        this.main = m;
        this.fileConfiguration = fileConfiguration;
        this.name = name;
        this.numberCase = numberCase;
        this.id = id;

        this.initDrops();
    }

    public ItemStack itemRandom() {
        if ( !listItem.isEmpty() ) {
            Random r2 = new Random();
            int numR = 0 + r2.nextInt(listItem.size());

            ItemStack itemRandom = listItem.get(numR);

            for (Iterator<ItemStack> it = listItem.iterator(); it.hasNext(); ) {
                ItemStack is = it.next();
                if (is.equals(itemRandom)) {
                    it.remove();
                }
            }
            return itemRandom;
        }
        return null;
    }

    public Inventory createInventory() {
        Inventory inventory = Bukkit.createInventory(null, 9*4, this.name);

        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.LIGHT_BLUE.getDyeData());
        ItemMeta meta1 = (ItemMeta) glass.getItemMeta();
        meta1.setDisplayName("/");
        glass.setItemMeta(meta1);

        ItemStack glass2= new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.MAGENTA.getDyeData());
        ItemMeta meta2 = (ItemMeta) glass2.getItemMeta();
        meta2.setDisplayName("/");
        glass2.setItemMeta(meta2);

        int j = 0;
        while ( j < inventory.getSize()) {
            inventory.setItem(j, glass);
            inventory.setItem(j+1, glass2);
            j += 2;
        }

        return inventory;
    }

    private void initDrops(){
        HashMap<ItemStack, Integer> drops = new HashMap<ItemStack, Integer>();

        for(String drop : this.fileConfiguration.getConfigurationSection("drops").getKeys(false)) {
            Material mat;
            try {
                mat = Material.getMaterial(this.fileConfiguration.getConfigurationSection("drops").getInt(drop+".material"));
            } catch(NumberFormatException e) {
                mat = Material.getMaterial(this.fileConfiguration.getConfigurationSection("drops").getString(drop+".material"));
            }

            if(mat==null)
                return;

            ItemStack is = new ItemStack(mat);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.fileConfiguration.getConfigurationSection("drops").getString(drop+".name", im.getDisplayName())));
            is.setItemMeta(im);
            is.setAmount(this.fileConfiguration.getConfigurationSection("drops").getInt(drop+".amount", 1));
            HashMap<Character, Enchantment> enchantments = new HashMap<Character, Enchantment>();
            enchantments.put('P', Enchantment.PROTECTION_ENVIRONMENTAL);
            enchantments.put('U', Enchantment.DURABILITY);
            enchantments.put('T', Enchantment.DAMAGE_ALL);
            if(this.fileConfiguration.getConfigurationSection("drops").contains(drop+".enchantment")) {
                String[] enchants = this.fileConfiguration.getConfigurationSection("drops").getString(drop+".enchantment").split(" ");
                for(int i = 0;i < enchants.length;i++)
                    if(enchants[i].length()==2)
                        is.addEnchantment(enchantments.get(enchants[i].charAt(0)), Integer.parseInt(""+enchants[i].charAt(1)));

            }
            drops.put(is, this.fileConfiguration.getConfigurationSection("drops").getInt(drop+".rate"));
        }

        Iterator iterator = drops.entrySet().iterator();
        while (iterator.hasNext())
        {
            Map.Entry entry = (Map.Entry) iterator.next();
            ItemStack key = (ItemStack) entry.getKey();
            int value = (int) entry.getValue();

            for ( int i = 0; i < value; i++)
                this.listItem.add(key);

        }
    }

    public int getNumberCase() {
        return this.numberCase;
    }
    public String getName() {
        return this.name;
    }
    public String getId() {
        return this.id;
    }
}
