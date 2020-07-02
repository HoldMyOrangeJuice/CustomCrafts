package HoldMyAppleJuice.opt;


import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum ShapeMode
{
    SHAPELESS(Material.BOOK),
    SHAPED(Material.GOLDEN_AXE);

    Material icon;

    ShapeMode(Material m)
    {
        this.icon = m;
    }

    public static ShapeMode determine(ItemStack i)
    {

        for (ShapeMode f : ShapeMode.values())
        {
            //System.out.println(i.getType() +" " + f.icon);
            if (i.getType() == f.icon)
            {
                return f;
            }
        }
        return null;
    }

    public boolean isShaped()
    {
        return (icon == Material.GOLDEN_AXE);
    }
}
