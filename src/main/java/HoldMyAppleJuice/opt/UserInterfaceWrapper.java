package HoldMyAppleJuice.opt;

import HoldMyAppleJuice.CustomCrafts;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class UserInterfaceWrapper
{
    public static Character[] chars = new Character[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'l'};

    Inventory inv;
    int grid_y = 1;
    int grid_x = 3;
    int output_slot = 25;
    int mode_slot = 19;
    int shape_slot = 4;
    int confirm_slot = 40;

    public static ItemStack placeholder = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
    public static ItemStack confirm = new ItemStack(Material.EMERALD_BLOCK);
    public static ItemStack cancel = new ItemStack(Material.REDSTONE_BLOCK);
    public static ItemStack shaped_icon = new ItemStack(Material.GOLDEN_AXE);
    public static ItemStack shapeless_icon = new ItemStack(Material.BOOK);

    public static ItemStack craft_icon = new ItemStack(Material.CRAFTING_TABLE);
    public static ItemStack furnace_icon = new ItemStack(Material.FURNACE);

    ItemStack empty = new ItemStack(Material.AIR);
    ItemStack shape_mode_icon = shaped_icon;
    ItemStack recipe_mode_icon = craft_icon;

    ItemStack[] recipe_modes = new ItemStack[]{craft_icon, furnace_icon};


    public UserInterfaceWrapper()
    {
        this.inv = Bukkit.createInventory(null, 45, "CC");

        ItemMeta m = placeholder.getItemMeta();
        m.setDisplayName(ChatColor.RED + "X");
        placeholder.setItemMeta(m);

        ItemMeta c = confirm.getItemMeta();
        c.setDisplayName(ChatColor.DARK_GREEN + "Сохранить");
        confirm.setItemMeta(c);

        ItemMeta t = craft_icon.getItemMeta();
        t.setDisplayName(ChatColor.DARK_GREEN + "Crafting Table");
        craft_icon.setItemMeta(t);

        ItemMeta f = furnace_icon.getItemMeta();
        f.setDisplayName(ChatColor.DARK_GREEN + "Furnace");
        furnace_icon.setItemMeta(f);

        ItemMeta cl = cancel.getItemMeta();
        cl.setDisplayName(ChatColor.DARK_RED + "Ошибка");
        cancel.setItemMeta(cl);

        ItemMeta shaped = shaped_icon.getItemMeta();
        shaped.setDisplayName("Shaped crafting");
        shaped_icon.setItemMeta(shaped);

        ItemMeta shapeless = shapeless_icon.getItemMeta();
        shapeless.setDisplayName("Shapeless crafting");
        shapeless_icon.setItemMeta(shapeless);

    }

    public void setItem(int x, int y, ItemStack i)
    {

    }

    public void clearRect(int x, int y, int rad)
    {

    }

    public void addSlider(int x, int y, int def)
    {

    }

    public void setResult(ItemStack i)
    {

    }

    public ItemStack getResult()
    {
        return inv.getItem(output_slot);
    }

    public Fork getRecipeMode()
    {
        return Fork.determine(inv.getItem(mode_slot));
    }

    public ShapeMode getShapeMode()
    {
        return ShapeMode.determine(inv.getItem(shape_slot));
    }

    public ItemStack getCraftingGridSlot(int x, int y)
    {
        return inv.getItem((y+grid_y)*9 + x + grid_x);
    }

    public ItemStack getCraftingGridSlot(int c)
    {
        return getCraftingGridSlot(c%3, (int) Math.floor(c/3d));
    }



    public HashMap<Character, ItemStack> getIngredients()
    {
        HashMap<Character, ItemStack> ingredients = new HashMap<Character, ItemStack>();

        for (int slot = 0; slot<9; slot++)
        {
            ItemStack item = getCraftingGridSlot(slot);

            if (item == null)continue;

            if (item.getType() != Material.AIR)
            {
                ingredients.put(chars[slot], item);
            }
        }

        return ingredients;
    }

    public Inventory getGUI()
    {

        if (recipe_mode_icon == craft_icon)
        {
            // set all with placeholders
            for (int i=0; i<45; i++)
            {
                inv.setItem(i, placeholder);
            }

            for (int x=0; x<3; x++)
            {
                for (int y=0; y<3; y++)
                {
                    inv.setItem((y+grid_y)*9+(x+grid_x), empty);
                }

            }
            inv.setItem(shape_slot, shape_mode_icon);
            //   0 1 2 3 4 5 6 7 8
            // 0 - - - - X - - - - 8
            // 1 - - - | | | - - - 17
            // 2 - M - | | | - | - 26
            // 3 - - - | | | - - - 35
            // 4 - - - - - - - - - 44

        }

        if (getRecipeMode() == Fork.FURNACE)
        {
            // set all with placeholders
            for (int i=0; i<45; i++)
            {
                inv.setItem(i, placeholder);
            }
            inv.setItem(22, empty);

        }
        // output
        inv.setItem(output_slot, empty);
        inv.setItem(mode_slot, recipe_mode_icon);
        inv.setItem(confirm_slot, confirm);
        return inv;
    }

    public void showGUI(Player player)
    {
        inv = getGUI();
        player.openInventory(inv);
    }

    public void cycleRecipeMode()
    {

        for (int i = 0; i<recipe_modes.length; i++)
        {
            if (recipe_mode_icon == recipe_modes[i])
            {
                recipe_mode_icon = recipe_modes[(i+1)%recipe_modes.length];
                break;
            }
        }
        inv.setItem(mode_slot, recipe_mode_icon);
    }

    public void cycleShapeMode()
    {
        if (getShapeMode().isShaped())
            shape_mode_icon = shapeless_icon;
        else
            shape_mode_icon = shaped_icon;

        inv.setItem(shape_slot, shape_mode_icon);

    }

    public void updateGUI(Player player)
    {
        System.out.println("updating gui to furn");
        Inventory i = getGUI();
        player.openInventory(i);

    }


}
