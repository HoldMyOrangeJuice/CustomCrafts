package HoldMyAppleJuice.opt;


import net.minecraft.server.v1_15_R1.IInventory;
import org.bukkit.block.data.type.Furnace;
import org.bukkit.craftbukkit.v1_15_R1.inventory.util.CraftTileInventoryConverter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Recipe;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRecipe implements Listener
{
    abstract Recipe getRecipe();
    abstract List<String> getNames();
    abstract Fork getFork();



    public List<Character> getChars(String[] shape)
    {
        //System.out.println("shape: " + Arrays.toString(shape));
        ArrayList<Character> l = new ArrayList<Character>();
        for (String row : shape)
        {
            for (int i = 0; i<row.length(); i++)
            {
                if (!l.contains(row.charAt(i))) {
                    l.add(row.charAt(i));
                }
            }
        }
        //System.out.println("chars: " + l);
        return l;
    }




}
