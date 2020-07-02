package HoldMyAppleJuice.opt;

import HoldMyAppleJuice.CustomCrafts;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.HashMap;
import java.util.List;

public class CustomFurnaceRecipe extends AbstractRecipe
{
    ItemStack input;
    ItemStack output;
    float xp;
    int time;
    String name;

    public CustomFurnaceRecipe(ItemStack input, ItemStack output, float xp, int time, String loadedName)
    {
        this.input = input;
        this.output = output;
        this.xp = xp;
        this.time = time;
        if (loadedName == null)
        {
            String id = CustomCrafts.recipeStorage.getUID();
            System.out.println("[CustomCrafts] creating furnace recipe with id " + id);
            name = "customcrafts_" + output.getType().toString() + "_" + id;
        }
        else
        {
            name = loadedName;
        }
    }


    public HashMap<ItemStack, ItemStack> getItems()
    {
        HashMap<ItemStack, ItemStack> m = new HashMap<ItemStack, ItemStack>();
        m.put(input, output);
        return m;
    }

    Recipe getRecipe()
    {
        return new FurnaceRecipe(getKey(), output, input.getType(), xp, time);
    }

    public NamespacedKey getKey()
    {
        return new NamespacedKey(CustomCrafts.plugin, name.replace("customcrafts_", ""));
    }

    List<String> getNames() {
        return null;
    }

    Fork getFork() {
        return Fork.FURNACE;
    }
}
