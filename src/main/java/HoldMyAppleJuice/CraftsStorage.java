package HoldMyAppleJuice;

import HoldMyAppleJuice.opt.CraftRecipe;
import HoldMyAppleJuice.opt.CraftRecipeLoader;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

public class CraftsStorage
{
    File file;
    FileConfiguration conf;

    public CraftsStorage(File file, FileConfiguration conf)
    {
        this.file = file;
        this.conf = conf;
    }


    public void addToNames(String fork, String name)
    {
        List<String> l = (List<String>) conf.getList(fork+".names");
        if (null == l)
        {
            l = new ArrayList<String>();
        }
        l.add(name);
        conf.set(fork+".names", l);
        try
        {
            conf.save(file);
        }catch (Exception e){}
    }


    public void saveFurnaceRecipe(ItemStack input, ItemStack output, float exp, int time)
    {
        String recipe_name = "customcrafts_" + output.getType().toString() + "_" + getUID("furnace");
        NamespacedKey key = new NamespacedKey(CustomCrafts.plugin, output.getType().toString() + "_" + getUID("furnace"));
        FurnaceRecipe recipe = new FurnaceRecipe(key, output, input.getType(), exp, time);
        Bukkit.addRecipe(recipe);

        // save data
        conf.set("furnace.input", input.getType().toString());
        conf.set("furnace.output", output.getType().toString());
        conf.set("furnace.xp", exp);
        conf.set("furnace.time", time);
        addToNames("furnace", recipe_name);

        // save nbt
        new NBTWrapper(input).saveNBT(recipe_name, 'i');
        new NBTWrapper(output).saveNBT(recipe_name, 'o');



    }

    public void saveCraft(String craftName, String[] shape, Map<Character, ItemStack> ingredients, ItemStack output) //
    {
        String escapedName = craftName.replace(":", "_");
        try
        {
            addToNames("crafts", escapedName);

            if (shape !=null)
            {
                conf.set("crafts." + escapedName + ".shape", shape);
            }
            else
            {
                conf.set("crafts." + escapedName + ".shape", new ArrayList<String>());
            }

            for (char i : new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'l'})
            {
                if (!ingredients.containsKey(i))continue;
                ItemStack item = ingredients.get(i);
                new NBTWrapper(item).saveNBT(escapedName, i);
                conf.set("crafts." + escapedName + ".ingredient." + i +".mat", ingredients.get(i).getType().toString());
            }

            conf.set("crafts." + escapedName + ".result.material", output.getType().toString());
            new NBTWrapper(output).saveNBT(escapedName, '-');
            conf.save(file);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

//        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(CustomCrafts.plugin, new Runnable() {
//            public void run() {
//                RecipeLoader.loadRecipes();
//                System.out.println("recipes updated");
//            }
//        }, 1000);
    }

    public void save(String path, Object data)
    {
        conf.set(path, data);
        try
        {
            conf.save(file);
        }catch (Exception e){}
    }

    public void deleteCraft(String id)
    {
        if (load("crafts." + id)!=null)
        {
            conf.set("crafts." + id, null);
            List<String> l = (List<String>) loadList("crafts.names");
            if (l.contains(id))
            {
                l.remove(id);
            }
            conf.set("crafts.names", l);
            //RecipeLoader.loadRecipes();
            try {
                conf.save(file);
            }
            catch (Exception e)
            {

            }
            CraftRecipeLoader.loadRecipes();
        }
//        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(CustomCrafts.plugin, new Runnable() {
//            public void run() {
//                RecipeLoader.loadRecipes();
//                System.out.println("recipes updated");
//            }
//        }, 1000);

    }

    public void updateUID(String fork)
    {
        conf.set(fork + ".uid", String.valueOf(Integer.parseInt(getUID(fork))+1 ));
    }

    public String getUID(String fork)
    {
        if (load(fork+".uid") == null)
        {
            conf.set(fork+".uid", "0");
            return "0";
        }
        return (String) load(fork+".uid");
    }

    public String getCrafts(String fork)
    {
        String out = "";
        if ((List<String>)loadList(fork+".names") == null) return "Нет ни одного крафта";

        for (String craft : (List<String>)loadList(fork+".names"))
        {
            out = out.concat("- " + craft + "\n");
        }
        return out;
    }
    public List<String> getCraftsList(String fork)
    {
        return  ((List<String>)loadList(fork+".names"));
    }

    public Object load(String path)
    {
        return conf.get(path);
    }

    public float loadFloat(String path)
    {
        return conf.getLong(path);
    }

    public int loadInt(String path)
    {
        return conf.getInt(path);
    }


    public Object loadList(String path)
    {
        return conf.getList(path);
    }

    public void get(Player p)
    {
        List<String> names = getCraftsList("crafts");
        String out = "";
        for (String name : names)
        {
            CraftRecipe recipe = CraftRecipeLoader.loadRecipe("crafts");
            out = name + ":\n";

            if (recipe.getShape() != null)
            {
                out+= "shaped recipe:\n" + Arrays.toString(recipe.getShape()) + "\n";
            }

            HashMap<ItemStack, Integer> count = new HashMap<ItemStack, Integer>();
            count = recipe.getIngredientsWithCount();
            for (ItemStack i : count.keySet())
            {
                out += count.get(i) + " of " + i.getType() + "\n";
                out += i.getItemFlags();
            }

        }
        p.sendMessage(out);
    }
}
