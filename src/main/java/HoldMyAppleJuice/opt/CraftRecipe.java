package HoldMyAppleJuice.opt;

import HoldMyAppleJuice.CustomCrafts;
import HoldMyAppleJuice.Util;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.*;

import javax.naming.NamingSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static HoldMyAppleJuice.Util.max;
import static HoldMyAppleJuice.Util.min;

public class CraftRecipe extends AbstractRecipe
{
    Fork fork = Fork.CRAFT;
    String[] shape=null;
    HashMap<Character, ItemStack> ingredients;
    ItemStack result;
    String name;

    public static Character[] chars = new Character[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'l'};
    // Stores data about recipe

    public CraftRecipe(HashMap<Character, ItemStack> ingredients, boolean shaped, ItemStack result, String loadedName)
    {

        if (loadedName == null)
        {
            System.out.println("CraftRecipe loaded name is null (recipe just created) calling getUID");
            name = new NamespacedKey(CustomCrafts.plugin, result.getType().toString() + "_" + CustomCrafts.recipeStorage.getUID()).toString().replace(":", "_");
        }
        else
        {
            name = loadedName;
        }

        this.result = result;
        this.ingredients = ingredients;

        if (shaped) {
            this.shape = getShape();
        }
    }

    public Recipe getRecipe()
    {
        NamespacedKey key = getKey();
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
                if (!ingredients.containsKey(c))continue;
                r.setIngredient(c, ingredients.get(c).getData());
            }
            return r;
        }


    }

    public NamespacedKey getKey()
    {
        return new NamespacedKey(CustomCrafts.plugin, name.replace("customcrafts_", ""));
    }





    void checkRecipeValidOnCraft(PrepareItemCraftEvent event) {

    }

    void checkRecipeValidOnFurnace() {

    }

    public List<String> getNames()
    {
        return null;
    }

    Fork getFork() {
        return Fork.CRAFT;
    }



    public HashMap<ItemStack, Integer> getIngredientsWithCount()
    {
        HashMap<ItemStack, Integer> count = new HashMap<ItemStack, Integer>();
        for (ItemStack i : this.ingredients.values())
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

    public String[] getShape()
    {
        ArrayList<Integer> xs = new ArrayList<Integer>();
        ArrayList<Integer>ys = new ArrayList<Integer>();

        for (int x = 0; x<3; x++)
        {
            for (int y = 0; y<3; y++)
            {
                if (this.ingredients.containsKey(chars[y*3+x]))
                {
                    xs.add(x);
                    ys.add(y);
                }
            }
        }
        //System.out.println("xs " + xs);
        //System.out.println("ys " + ys);
        int chars_per_string = max(xs) - min(xs) + 1;
        int list_members = max(ys) - min(ys) + 1;
        //System.out.println("chars per line: " + chars_per_string);
        //System.out.println("rows: " + list_members);

        String[] shape = new String[list_members];
        for (int i=0; i<list_members; i++)
        {
            String row = "";
            for (int c=0; c<chars_per_string; c++)
            {
                row = row.concat(String.valueOf(chars[(i+min(ys))*3+c+min(xs)]));
                //System.out.println("adding " + chars[i*3+c] + " to row " + i);
            }
            //System.out.println("line done" + row);
            shape[i] = row;
        }
        return shape;

    }
}
