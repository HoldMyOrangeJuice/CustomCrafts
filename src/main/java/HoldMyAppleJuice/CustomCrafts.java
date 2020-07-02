package HoldMyAppleJuice;

import HoldMyAppleJuice.opt.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

public class CustomCrafts extends JavaPlugin
{
    public static CustomCrafts plugin;
    public static CraftsStorage craftsStorage;
    public static RecipeStorage recipeStorage;
    public static CraftRecipeLoader recipeLoader;
    public static File file;

    public CustomCrafts()
    {
        plugin = this;
    }

    @Override
    public void onEnable()
    {
        file = new File(getDataFolder(), "recipes.yml");
        FileConfiguration conf = YamlConfiguration.loadConfiguration(file);

        if (!file.exists())
        {
            saveResource("recipes.yml", false);
        }

        craftsStorage = new CraftsStorage(file, conf);
        recipeStorage = new RecipeStorage(file, conf);

        getCommand("customcrafts").setExecutor(new Command());
        getCommand("customcrafts").setTabCompleter(new TabComplete());


        recipeLoader = new CraftRecipeLoader();
        Bukkit.getServer().getPluginManager().registerEvents(recipeLoader, this);
        Bukkit.getServer().getPluginManager().registerEvents(new CraftRecipeValidator(), this);
        List<CraftRecipe> loaded = CraftRecipeLoader.loadRecipes();
        System.out.println("[CustomCrafts] loaded " + loaded.size() + " recipes");
        new FurnaceRecipeValidator();
    }

    @Override
    public void onLoad()
    {

    }

    @Override
    public void onDisable()
    {

    }


}
