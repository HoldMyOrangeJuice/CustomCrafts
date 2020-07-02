package HoldMyAppleJuice;

import HoldMyAppleJuice.opt.CraftRecipe;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Util
{
    public static boolean notEmpty(ItemStack i)
    {
        return (i != null && i.getType() != Material.AIR);
    }

    public static int min(List<Integer> list)
    {
        int res = Integer.MAX_VALUE;
        for (Integer i : list)
        {
            if (res > i)
            {
                res = i;
            }
        }
        return res;
    }
    public static int max(List<Integer> list)
    {
        int res = 0;
        for (Integer i : list)
        {
            if (res < i)
            {
                res = i;
            }
        }
        return res;
    }
    public static String[] getPrimitiveShape(List<String> shape) {

        System.out.println("getting primitive shape of " + shape);
        String[] shape_prim = null;

        // SHAPE
        if (shape != null && shape.size() > 0) {
            shape_prim = new String[shape.size()];
            for (int i = 0; i < shape.size(); i++) {
                shape_prim[i] = shape.get(i);
            }
        }
        System.out.println("shape is " + Arrays.toString(shape_prim));
        return shape_prim;
    }

    public static boolean matrixHasNeededItems(ItemStack[] matrix, CraftRecipe loadedRecipe)
    {
        HashMap<ItemStack, Integer> ingredient_count = loadedRecipe.getIngredientsWithCount();
        HashMap<ItemStack, Integer> matrix_count = new HashMap<ItemStack, Integer>();
        for (int slot=0; slot<9; slot++ )
        {
            ItemStack item = matrix[slot];

            if(item==null)continue;

            if (matrix_count.containsKey(item))
            {
                matrix_count.put(item, matrix_count.get(item)+1);
            }
            else
            {
                matrix_count.put(item, 1);
            }
        }

        for (ItemStack ingredient : ingredient_count.keySet())
        {
            if (ingredient.getType() == Material.AIR)continue;

            if (!containskey(matrix_count, ingredient))
            {
                //System.out.println("matrix does not have any " + ingredient);
                return false;
            }

            if (!get(matrix_count, ingredient).equals(ingredient_count.get(ingredient)))
            {
                //System.out.println("matrix does not have enough ( " + ingredient_count.get(ingredient) + " ) " + ingredient + ": " + get(matrix_count, ingredient));
                return false;
            }
        }
        return true;
    }
    public static boolean containskey(HashMap<ItemStack, Integer>h, ItemStack i)
    {
        //System.out.println("called with " + h + " " + i);
        for (ItemStack it : h.keySet())
        {
            if (it.isSimilar(i))
            {
                System.out.println("check isSimilar: " + it + " " + i + " > " + true);
                return true;
            }
        }
        System.out.println("check failed no such item");
        return false;
    }
    public static Integer get(HashMap<ItemStack, Integer>h, ItemStack i)
    {
        for (ItemStack it : h.keySet())
        {
            if (it.isSimilar(i)){return h.get(it);}
        }
        return null;
    }

}
