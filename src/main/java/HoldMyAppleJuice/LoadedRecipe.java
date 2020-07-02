package HoldMyAppleJuice;

import com.mysql.fabric.xmlrpc.base.Array;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class LoadedRecipe implements Recipe, Keyed {

    String[] shape;
    ItemStack result;
    HashMap<Character, ItemStack> ingredients;
    NamespacedKey key;
    String mode;
    ItemStack input;
    float exp;
    int time;

    public LoadedRecipe(HashMap<Character, ItemStack> ingredients, String[] shape, ItemStack result, String name)
    {
        mode = "craft";
        this.ingredients = ingredients;
        this.shape = shape;
        this.result = new NBTWrapper(result).readNBT(result.getType().toString(), '-');
        key = new NamespacedKey(CustomCrafts.plugin, name.replace("customcrafts_", ""));
    }

    public LoadedRecipe(String name, Material input, Material output, float exp, int time)
    {
        mode = "furnace";
        this.result = new NBTWrapper(new ItemStack(output)).readNBT(name, 'o');
        this.input = new NBTWrapper(new ItemStack(input)).readNBT(name, 'i');
        key = new NamespacedKey(CustomCrafts.plugin, name.replace("customcrafts_", ""));
        this.exp = exp;
        this.time = time;
    }

    public Recipe getRecipe()
    {
        if (mode == "craft") {
            if (shape == null || shape.length == 0) {
                HashMap<ItemStack, Integer> count = getIngredientsWithCount();
                //System.out.println("count: " + count);
                ShapelessRecipe r = new ShapelessRecipe(key, result);
                for (ItemStack i : count.keySet()) {
                    r.addIngredient(count.get(i), i.getData());
                }
                return r;
            } else {
                ShapedRecipe r = new ShapedRecipe(key, result);
                r.shape(shape);
                for (Character c : getChars(shape)) {
                    r.setIngredient(c, ingredients.get(c).getData());
                }
                return r;
            }
        }
        if (mode == "furnace")
        {
            HashMap<ItemStack, ItemStack> i = new HashMap<ItemStack, ItemStack>();
            i.put(this.input, this.result);
            Recipe r =new FurnaceRecipe(this.key, this.result, this.input.getType(), this.exp, this.time);
            return r;
        }
        return null;
    }


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

    public HashMap<ItemStack, Integer> getIngredientsWithCount()
    {
        HashMap<ItemStack, Integer> count = new HashMap<ItemStack, Integer>();
        for (ItemStack i : ingredients.values())
        {
            if (count.containsKey(i))
            {
                count.put(i, count.get(i) + 1);
            }
            else
            {
                count.put(i, 1);
            }
        }
        return count;
    }

    public ItemStack getResult() {
        return null;
    }

    public NamespacedKey getKey() {
        return this.key;
    }

    public static String keyToYMLname(NamespacedKey key, String UID)
    {
        return key.getKey().replace(":", "_") + "_" + UID;
    }

    public static String keyToNBTname(NamespacedKey key, String UID, Character slot)
    {
        return key.getKey().replace(":", "_") + "_" + UID + "_" + slot;
    }

   // public static NamespacedKey nameToKey()
}
