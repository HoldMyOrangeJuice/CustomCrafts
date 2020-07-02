package HoldMyAppleJuice.opt;

import HoldMyAppleJuice.CustomCrafts;
import HoldMyAppleJuice.LoadedRecipe;
import org.bukkit.Bukkit;
import org.bukkit.block.Furnace;
import org.bukkit.craftbukkit.v1_15_R1.inventory.util.CraftTileInventoryConverter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class FurnaceRecipeValidator implements Listener
{

    public static HashMap<CustomFurnaceRecipe, HashMap<ItemStack, ItemStack>>loadedFurnaceRecipeWithIngredients = new HashMap<CustomFurnaceRecipe, HashMap<ItemStack, ItemStack>>();


    public FurnaceRecipeValidator()
    {
        Bukkit.getServer().getPluginManager().registerEvents(this, CustomCrafts.plugin);
    }

    public static void addToFurnaceRegistry(CustomFurnaceRecipe recipe)
    {
        loadedFurnaceRecipeWithIngredients.put(recipe, recipe.getItems());
    }

    @EventHandler
    public void f1(FurnaceBurnEvent e)
    {

    }

    @EventHandler
    public void f2(FurnaceSmeltEvent e)
    {

    }

    @EventHandler
    public void f3(InventoryClickEvent e)
    {


        if (!(e.getClickedInventory() instanceof FurnaceInventory))return;

        FurnaceInventory inventory = (FurnaceInventory)e.getClickedInventory();
        ItemStack result = inventory.getResult();
        ItemStack item = inventory.getSmelting();
        ItemStack fuel = inventory.getFuel();

        for (CustomFurnaceRecipe recipe : loadedFurnaceRecipeWithIngredients.keySet())
        {
            if (recipe.input.isSimilar(item) && fuel.getType().isBurnable())
            {
               new Smelt()
            }
        }
    }
}
