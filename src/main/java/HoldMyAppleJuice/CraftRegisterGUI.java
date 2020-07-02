package HoldMyAppleJuice;

import HoldMyAppleJuice.opt.CraftRecipeLoader;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CraftRegisterGUI implements Listener
{
    String[] chars = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "l"};

    Inventory inv;
    ItemStack barrier = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
    ItemStack confirm = new ItemStack(Material.EMERALD_BLOCK);
    ItemStack cancel = new ItemStack(Material.REDSTONE_BLOCK);
    ItemStack shaped_icon = new ItemStack(Material.GOLDEN_AXE);
    ItemStack shapeless_icon = new ItemStack(Material.BOOK);
    ItemStack craft_icon = new ItemStack(Material.CRAFTING_TABLE);
    ItemStack furnace_icon = new ItemStack(Material.FURNACE);
    ItemStack empty = new ItemStack(Material.AIR);
    ItemStack mode_icon = shaped_icon;
    ItemStack recipe_mode = craft_icon;
    Recipe recipe;
    boolean shaped = true;




    public CraftRegisterGUI()
    {
        ItemMeta m = barrier.getItemMeta();
        m.setDisplayName(ChatColor.RED + "X");
        barrier.setItemMeta(m);

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

        Bukkit.getServer().getPluginManager().registerEvents(this, CustomCrafts.plugin);
    }



    @EventHandler
    public void invClick(final InventoryClickEvent event)
    {

        //Bukkit.broadcastMessage(event.getClickedInventory()+ " " + event.getInventory() + " actual " + inv);
        if (event.getClickedInventory() != this.inv) return;

        ItemStack clicked = event.getCurrentItem();

        //Bukkit.broadcastMessage("clicked " + event.getSlot());


        ItemStack out = getItem(2, 7);

        if (clicked!=null && clicked.equals(barrier))
        {
            event.setCancelled(true);
        }

        if (clicked!=null && clicked.equals(cancel))
        {
            event.setCancelled(true);
        }

        if (clicked!=null && clicked.equals(mode_icon))
        {
            toggleMode();
            event.setCancelled(true);
        }

        if (clicked!=null && clicked.equals(recipe_mode))
        {
            toggleRecipeMode();
            event.setCancelled(true);
        }




        if (clicked!=null && clicked.equals(confirm) && !clicked.isSimilar(cancel))
        {
            event.setCancelled(true);
            boolean inv_has_item = false;

            for (int x = 0; x<3; x++)
            {
                for (int y = 0; y<3; y++)
                {
                    if (notEmpty(getCraftingGridSlot(y*3+x)))
                    {
                        inv_has_item = true;
                        break;
                    }
                }
            }


            if (!inv_has_item)
            {
                return;
            }

            if (!notEmpty(out))
            {
                for (int i = 0; i <= 5; i++)
                {
                    final int finalI = i;

                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(CustomCrafts.plugin, new Runnable() {
                        public void run() {
                            if (finalI %2!=0)
                            {
                                inv.setItem(25, new ItemStack(Material.AIR));
                                ((Player)event.getWhoClicked()).playNote(event.getWhoClicked().getLocation(), Instrument.BELL, Note.flat(1, Note.Tone.E));
                            }
                            else
                            {
                                inv.setItem(25, new ItemStack(cancel));
                                ((Player)event.getWhoClicked()).playNote(event.getWhoClicked().getLocation(), Instrument.BELL, Note.flat(1, Note.Tone.D));
                            }

                        }
                    }, i*5);
                }
                return;
            }

            if (recipe_mode == craft_icon)
            {
                String[]shape = getShape();

                NamespacedKey key = new NamespacedKey(CustomCrafts.plugin,  out.getType().toString()+ "_" + CustomCrafts.craftsStorage.getUID("crafts") );
                CustomCrafts.craftsStorage.updateUID("crafts");

                HashMap<Character, ItemStack> ingredients = new HashMap<Character, ItemStack>();

                ShapedRecipe shapedRecipe = null;
                ShapelessRecipe shapelessRecipe = null;

                if (shaped) {
                    recipe = new ShapedRecipe(key, out);
                    shapedRecipe = (ShapedRecipe) recipe;
                    shapedRecipe.shape(shape);
                }
                else
                {
                    recipe = new ShapelessRecipe(key, out);
                    shapelessRecipe = (ShapelessRecipe) recipe;
                }

                for (int y = 0; y<3; y++)
                {
                    for (int x = 0; x<3; x++)
                    {
                        ItemStack item = getCraftingGridSlot(y*3+x);

                        char c = chars[y*3+x].charAt(0);
                        //System.out.println("item at " + c + " is " + item);
                        if (ingredientIsAppearing(c, shape))
                        {
                            ingredients.put(c, item);
                            if (shaped)
                            {
                                shapedRecipe.setIngredient(c, item.getData());
                            }
                            else
                            {
                                shapelessRecipe.addIngredient(item.getData());
                            }
                        }

                    }
                }
                if (shaped)
                {
                    Bukkit.getServer().addRecipe(shapedRecipe);
                    CustomCrafts.craftsStorage.saveCraft(shapedRecipe.getKey().toString(), shapedRecipe.getShape(), ingredients, out);
                    event.getWhoClicked().sendMessage(Command.PREFIX + "Форменный крафт сохранен.");
                }
                else
                {
                    Bukkit.getServer().addRecipe(shapelessRecipe);
                    CustomCrafts.craftsStorage.saveCraft(shapelessRecipe.getKey().toString(), null, ingredients, out);
                    event.getWhoClicked().sendMessage(Command.PREFIX + "Бесформенный крафт сохранен.");
                }

                CraftRecipeLoader.loadRecipes();
                clearinputs();

            }

        }

    }


    public void showSlider()
    {

    }

    @EventHandler
    public void onSlide(InventoryClickEvent event)
    {

    }

    public Inventory getGUI(Player player)
    {
        Inventory inventory = Bukkit.getServer().createInventory(player, 45, "Создание рецепта");
        if (mode_icon == craft_icon)
        {
            // set all with placeholders
            for (int i=0; i<45; i++)
            {
                inventory.setItem(i, barrier);
            }

            for (int x=0; x<3; x++)
            {
                for (int y=0; y<3; y++)
                {
                    inventory.setItem((y+1)*9+(x+4), empty);
                }

            }
            //   0 1 2 3 4 5 6 7 8
            // 0 - - - - X - - - - 8
            // 1 - - - | | | - - - 17
            // 2 - M - | | | - | - 26
            // 3 - - - | | | - - - 35
            // 4 - - - - - - - - - 44

        }

        if (mode_icon == furnace_icon)
        {
            // set all with placeholders
            for (int i=0; i<45; i++)
            {
                inventory.setItem(i, barrier);
            }
            inventory.setItem(22, empty);

        }
        // output
        inventory.setItem(25, empty);

        inventory.setItem(4, mode_icon);
        inventory.setItem(19, recipe_mode);
        inventory.setItem(42, confirm);
        return inventory;
    }

    public void clearinputs()
    {
        if (mode_icon == craft_icon)
        {
            // craft grid
            for (int slot = 0; slot < 9; slot ++)
            {
                setCraftingGridSlot(slot, new ItemStack(Material.AIR));
            }
            // set result slot
            inv.setItem(25, new ItemStack(Material.AIR));
        }
        if (mode_icon == furnace_icon)
        {
            inv.setItem(25, new ItemStack(Material.AIR));
            inv.setItem(22, new ItemStack(Material.AIR));
        }
    }

    public void toggleRecipeMode()
    {
        ItemStack[] modes = new ItemStack[]{craft_icon, furnace_icon};
        for (int i = 0; i<modes.length; i++)
        {
            if (modes[i] == recipe_mode)
            {
                recipe_mode = modes[(i+1)%modes.length];
            }
        }
    }

    public boolean ingredientIsAppearing(char a, String[] shape)
    {
        for (String c : shape)
        {
            if (c.contains(String.valueOf(a)))
            {
                return true;
            }
        }
        return false;
    }

    public void toggleMode()
    {
        shaped = !shaped;
        if (shaped)
        {
            mode_icon = shaped_icon;
            inv.setItem(4, mode_icon);
        }
        else
        {
            mode_icon = shapeless_icon;
            inv.setItem(4, mode_icon);
        }

    }

    public String[] getShape()
    {
        ArrayList<Integer>xs = new ArrayList<Integer>();
        ArrayList<Integer>ys = new ArrayList<Integer>();
        for (int x = 0; x<3; x++)
        {
            for (int y = 0; y<3; y++)
            {
                if (notEmpty(getCraftingGridSlot(y*3+x)))
                {
                    //System.out.println(y*3+x + "( " + x + " " + y + ") " + "not empty. has " + getCraftingGridSlot(y*3+x));
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
                row = row.concat(chars[(i+min(ys))*3+c+min(xs)]);
                //System.out.println("adding " + chars[i*3+c] + " to row " + i);
            }
            //System.out.println("line done" + row);
            shape[i] = row;
        }
        //System.out.println(Arrays.toString(shape));
        return shape;

    }

    public int min(List<Integer> list)
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
    public int max(List<Integer> list)
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

    public int getSlot(int x, int y)
    {
        return y*9+x;
    }
    public void setCraftingGridSlot(int slot, ItemStack i)
    {
        int left = 3;
        int up = 1;

        int row = (int) Math.floor(slot/3d);
        int col = slot%3;
        //System.out.println("set slot " + slot + " " + ((up+col)*9 + left+row) );
        //   0 1 2 3 4 5 6 7 8
        // 0 - - - - - - - - -
        // 1 - - - | | | - - -
        // 2 - - - | | | - | -
        // 3 - - - | | | - - -
        // 4 - - - - - - - - -
        inv.setItem((up+row)*9 + left+col, i);

    }

    public ItemStack getCraftingGridSlot(int slot)
    {
        int left = 3;
        int up = 1;

        int row = (int) Math.floor(slot/3d);
        int col = slot%3;
        //   0 1 2 3 4 5 6 7 8
        // 0 - - - - - - - - -
        // 1 - - - | | | - - -
        // 2 - - - | | | - | -
        // 3 - - - | | | - - -
        // 4 - - - - - - - - -

        return getItem(up+row, left+col);
    }

    public boolean notEmpty(ItemStack i)
    {
        return (i != null && i.getType() != Material.AIR);
    }

    public ItemStack getItem(int x, int y)
    {
        //Bukkit.getServer().broadcastMessage("get slot "+ x*9 + y);
        if (inv.getItem(x*9 + y) == null)return new ItemStack(Material.AIR);
        return inv.getItem(x*9 + y);
    }
}
