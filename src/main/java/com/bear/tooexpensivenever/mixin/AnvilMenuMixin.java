package com.bear.tooexpensivenever.mixin;

import com.bear.tooexpensivenever.mixin.AnvilMenuAccessor;
import com.bear.tooexpensivenever.mixin.ItemCombinerMenuAccessor;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public class AnvilMenuMixin {

    @Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
    private void modifyAnvilCost(CallbackInfo ci) {
        // Access the private `cost` field using the Accessor
        DataSlot cost = ((AnvilMenuAccessor) this).getCost();

        // Access the protected `inputSlots` field using the Accessor
        CraftingContainer inputSlots = ((ItemCombinerMenuAccessor) this).getInputSlots();

        // Get the input items
        ItemStack left = inputSlots.getItem(0);
        ItemStack right = inputSlots.getItem(1);

        if (!left.isEmpty() && !right.isEmpty()) {
            // Compute a new cost
            int calculatedCost = computeAnvilCost(left, right);

            // Set the new cost, bypassing the cap
            cost.set(calculatedCost);

            // Prevent further processing of the original method
            ci.cancel();
        }
    }

    private int computeAnvilCost(ItemStack left, ItemStack right) {
        // Example: Dynamic cost scaling based on enchantments
        int leftEnchantments = EnchantmentHelper.getEnchantments(left).size();
        int rightEnchantments = EnchantmentHelper.getEnchantments(right).size();

        int baseCost = leftEnchantments + rightEnchantments;
        int enchantmentCost = EnchantmentHelper.getEnchantments(left).values().stream()
                .mapToInt(level -> level * 2).sum();

        return baseCost + enchantmentCost; // Example cost logic
    }
}
