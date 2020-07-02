package HoldMyAppleJuice.opt;

import HoldMyAppleJuice.CustomCrafts;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class EventManager implements Listener
{

    UserInterfaceWrapper wrapper;
    public EventManager(UserInterfaceWrapper wrapper)
    {
        Bukkit.getServer().getPluginManager().registerEvents(this, CustomCrafts.plugin);
        this.wrapper = wrapper;
    }

    @EventHandler
    public void buttonClicked(InventoryClickEvent e)
    {
        if (e.getClickedInventory() != wrapper.inv)return;

        if (!(e.getWhoClicked() instanceof Player))return;

        Player player = (Player) e.getWhoClicked();
        ItemStack clicked = e.getCurrentItem();

        if (clicked == null)return;


        if (clicked.isSimilar(UserInterfaceWrapper.confirm))
        {
            e.setCancelled(true);
            if (wrapper.getRecipeMode() == Fork.CRAFT)
            {
                HashMap<Character, ItemStack> ingredients = wrapper.getIngredients();

                if (ingredients.keySet().size() == 0) return;

                ItemStack output = wrapper.getResult();

                CraftRecipe recipe = new CraftRecipe(ingredients, wrapper.getShapeMode().isShaped(), output, null);
                /*
                saveCraftRecipe method already adds craft to bukkit and
                adds Validator
                 */
                CustomCrafts.recipeStorage.saveCraftRecipe(recipe);
            }
            if (wrapper.getRecipeMode() == Fork.FURNACE)
            {
                /*
                * TODO: XP and time interface
                */
                CustomFurnaceRecipe recipe = new CustomFurnaceRecipe(wrapper.getCraftingGridSlot(4), wrapper.getResult(), 1, 1, null);
                CustomCrafts.recipeStorage.saveFurnaceRecipe(recipe);
                FurnaceRecipeValidator.addToFurnaceRegistry(recipe);
            }

        }

        if (clicked.isSimilar(wrapper.recipe_mode_icon))
        {
            wrapper.cycleRecipeMode();
            wrapper.updateGUI(player);

            e.setCancelled(true);
        }

        if (clicked.isSimilar(UserInterfaceWrapper.placeholder))
        {
            e.setCancelled(true);
        }

        if (clicked.isSimilar(wrapper.shape_mode_icon))
        {
            wrapper.cycleShapeMode();
            e.setCancelled(true);
        }
    }
}
