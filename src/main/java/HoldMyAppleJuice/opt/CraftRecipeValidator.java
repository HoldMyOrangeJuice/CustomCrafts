package HoldMyAppleJuice.opt;

import HoldMyAppleJuice.LoadedRecipe;
import HoldMyAppleJuice.NBTWrapper;
import HoldMyAppleJuice.Util;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CraftRecipeValidator implements Listener
{

    public static HashMap<CraftRecipe, HashMap<Character, ItemStack>> loadedRecipesIngredients = new HashMap<CraftRecipe, HashMap<Character, ItemStack>>();


    public static void addRecipeToRegistry(CraftRecipe recipe, HashMap<Character, ItemStack>ingredients)
    {
        loadedRecipesIngredients.put(recipe, ingredients);
    }


    @EventHandler
    public void craft(PrepareItemCraftEvent e)
    {

        CraftingInventory inv = e.getInventory();
        ItemStack[] matrix = inv.getMatrix();
        //System.out.println("loaded " + loadedRecipesIngredients);
        for (CraftRecipe loadedRecipe : loadedRecipesIngredients.keySet())
        {
            Recipe recipe = loadedRecipe.getRecipe();
            //Bukkit.broadcastMessage("recipe from event " + e.getRecipe() + " recipe from registry " + loadedRecipe + recipe.equals(e.getRecipe()));
            Recipe event_recipe = e.getRecipe();
            if (event_recipe == null)return;
            //Bukkit.broadcastMessage("" + rec + " " + recipe);
            ItemStack result_actual = event_recipe.getResult();
            ItemStack result_recipe = recipe.getResult();

            Bukkit.broadcastMessage("\nresult from event\n" + result_actual + " \nresult from registry\n " + result_recipe + (result_recipe.isSimilar(result_actual)) + "\n");
            Bukkit.broadcastMessage("\nkey from event:\n" + ((Keyed)event_recipe).getKey().toString() + " \nkey from registry\n " + loadedRecipe.getKey().toString() + (loadedRecipe.getKey().toString().equals(((Keyed) event_recipe).getKey().toString())) + "\n");

//            if (event_recipe instanceof ShapedRecipe)
//            {
//                result_recipe = new NBTWrapper( (event_recipe).getResult()).readNBT( ((ShapedRecipe)event_recipe).getKey().toString().replace(":", "_"), '-');
//            }
//            else
//            {
//                result_recipe = new NBTWrapper( (event_recipe).getResult()).readNBT( ((ShapelessRecipe)event_recipe).getKey().toString().replace(":", "_"), '-');
//            }
//            if (recipe instanceof ShapedRecipe)
//            {
//                result_actual = new NBTWrapper( (recipe).getResult()).readNBT( ((ShapedRecipe)recipe).getKey().toString().replace(":", "_"), '-');
//            }
//            else
//            {
//                result_actual = new NBTWrapper( (recipe).getResult()).readNBT( ((ShapelessRecipe)recipe).getKey().toString().replace(":", "_"), '-');
//            }


            if (result_recipe.isSimilar(result_actual) && loadedRecipe.getKey().toString().equals(((Keyed) event_recipe).getKey().toString()))
            {
                Bukkit.broadcastMessage("CUSTOM RECIPE FOUND");
                HashMap<Character, ItemStack> ingredients = loadedRecipesIngredients.get(loadedRecipe);
                //Bukkit.broadcastMessage("ingredients " + ingredients);
                ArrayList<Character> letters =  new ArrayList<Character>(Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'l'));

                if (!Util.matrixHasNeededItems(matrix, loadedRecipe))
                {
                    //System.out.println("some ingredients are missing");
                    e.getInventory().setResult(new ItemStack(Material.AIR));
                }
                break;
            }

        }
    }

}
