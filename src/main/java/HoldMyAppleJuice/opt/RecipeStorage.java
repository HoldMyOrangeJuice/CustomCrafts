package HoldMyAppleJuice.opt;

import HoldMyAppleJuice.NBTWrapper;
import HoldMyAppleJuice.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class RecipeStorage
{
    File file;
    FileConfiguration configuration;
    public static Character[] chars = new Character[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'l'};

    public RecipeStorage(File file, FileConfiguration configuration)
    {
        this.file = file;
        this.configuration = configuration;
    }

    public void saveCraftRecipe(CraftRecipe recipe)
    {
        // add craft recipe to validator's registry on save
        CraftRecipeValidator.addRecipeToRegistry(recipe, recipe.ingredients);

        // add craft to bukkit's registry
        Bukkit.addRecipe(recipe.getRecipe());

        String path = recipe.getFork().toString().toLowerCase()+ "." + recipe.name;

        if (recipe.getShape() != null)
        {
            for (Character slot : recipe.ingredients.keySet())
            {
                configuration.set(path + ".ingredient." + slot, recipe.ingredients.get(slot).getType().toString());
                configuration.set(path + ".shape", recipe.getShape());
                configuration.set(path + ".result", recipe.result.getType().toString());

                new NBTWrapper(recipe.ingredients.get(slot)).saveNBT(path, slot);
                save();
            }
        }
        if (recipe.getShape() == null)
        {
            HashMap<ItemStack, Integer> count = recipe.getIngredientsWithCount();
            int slot = 0;
            for (ItemStack item : count.keySet())
            {
                configuration.set(path + ".ingredient." + chars[slot] + ".mat", item.getType().toString());
                configuration.set(path + ".ingredient." + chars[slot] + ".count", count.get(item));
                configuration.set(path + ".shape", new ArrayList<String>());
                configuration.set(path + ".result", recipe.result.getType().toString());

                new NBTWrapper(recipe.ingredients.get(item)).saveNBT(path, chars[slot]);
                save();
            }
        }
    }

    public CraftRecipe loadCraftRecipe(String name)
    {
        String ingredient_path = "craft." + name + ".ingredient";
        ConfigurationSection section = configuration.getConfigurationSection(ingredient_path);
        ShapeMode shapeMode=null;
        System.out.println("looking at shape " + "craft." + name + ".shape");
        String[] shape = Util.getPrimitiveShape(configuration.getStringList("craft." + name + ".shape"));

        HashMap<Character, ItemStack> ingredients = new HashMap<Character, ItemStack>();

        int last_char_at = -1;
        for (String slot : section.getKeys(false))
        {
            Character c = slot.charAt(0);
            if (shape == null)
            {
                shapeMode = ShapeMode.SHAPELESS;
                String mat_name =configuration.getString(ingredient_path + "." + c);
                System.out.println("count of " + name + " " + configuration.get(ingredient_path + "." + c + ".count"));
                Integer count = configuration.getInt(ingredient_path + "." + c + ".count");
                ItemStack loaded_item = new NBTWrapper(new ItemStack(Material.valueOf(mat_name))).readNBT(name, c);
                for (int i = 0; i<count; i++)
                {
                    last_char_at++;
                    ingredients.put(chars[last_char_at], loaded_item);
                }
            }
            else
            {
                shapeMode = ShapeMode.SHAPED;
                String mat_name = (String) configuration.get(ingredient_path + "." + c);
                System.out.println("material name at " + ingredient_path + "." + c +" is " + mat_name);
                ItemStack loaded_item = new NBTWrapper( new ItemStack(Material.valueOf(mat_name)) ).readNBT(name, c);
                ingredients.put(c, loaded_item);
            }
        }
        ItemStack result = new NBTWrapper(new ItemStack( Material.valueOf ( (String) configuration.get("craft." + name + ".result")))).readNBT(name, '-');
        CraftRecipe recipe = new CraftRecipe(ingredients, shapeMode.isShaped(), result, name);

        // add craft recipe to validator's registry on load
        CraftRecipeValidator.addRecipeToRegistry(recipe, ingredients);

        // add craft to bukkit's registry
        Bukkit.addRecipe(recipe.getRecipe());
        return recipe;
    }

    public void saveFurnaceRecipe(CustomFurnaceRecipe recipe)
    {
        String path = recipe.getFork().toString().toLowerCase() + ".customcrafts_" + recipe.output.getType().toString() + "_" + getUID();
        configuration.set(path + ".input" , recipe.input.getType().toString());
        configuration.set(path + ".output" , recipe.output.getType().toString());
        configuration.set(path + ".xp" , recipe.xp);
        configuration.set(path + ".time" , recipe.time);

        new NBTWrapper(recipe.input).saveNBT(path, 'i');
        new NBTWrapper(recipe.output).saveNBT(path, 'o');


        Bukkit.addRecipe(recipe.getRecipe());

        save();
    }

    public String getUID()
    {
        Integer uid = (Integer) configuration.get("UID");
        if (uid == null)
        {
            uid = 0;
        }
        configuration.set("UID", uid+1);
        return String.valueOf(uid);
    }

    public void save()
    {
        try{
            configuration.save(file);
        }catch (Exception e)
        {

        }
    }

}
