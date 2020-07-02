package HoldMyAppleJuice.opt;

import HoldMyAppleJuice.CustomCrafts;
import HoldMyAppleJuice.LoadedRecipe;
import HoldMyAppleJuice.opt.CraftRecipe;
import com.mojang.authlib.yggdrasil.response.MinecraftTexturesPayload;
import com.mysql.fabric.xmlrpc.base.Array;
import net.minecraft.server.v1_15_R1.BlockPortal;
import net.minecraft.server.v1_15_R1.ItemSaddle;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Item;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import sun.security.x509.FreshestCRLExtension;

import java.util.*;
import java.util.logging.Logger;

import static HoldMyAppleJuice.Util.matrixHasNeededItems;

public class CraftRecipeLoader implements Listener
{
    public static HashMap<LoadedRecipe, HashMap<Character, ItemStack>> loadedRecipesIngredients = null;
    public static HashMap<LoadedRecipe, HashMap<ItemStack, ItemStack>> loadedFurnaceRecipes = null;

    public static CraftRecipe loadRecipe(String name)
    {
        CraftRecipe recipe = CustomCrafts.recipeStorage.loadCraftRecipe(name);
        return recipe;
    }

    public static List<CraftRecipe> loadRecipes()
    {

        List<CraftRecipe> loaded = new ArrayList<CraftRecipe>();
        ConfigurationSection s = CustomCrafts.recipeStorage.configuration.getConfigurationSection("craft");

        if (s==null)return loaded;

        for (String name : s.getKeys(false) )
        {
            CraftRecipe recipe = CustomCrafts.recipeStorage.loadCraftRecipe(name);
            loaded.add(recipe);
        }
        return loaded;
    }
}

