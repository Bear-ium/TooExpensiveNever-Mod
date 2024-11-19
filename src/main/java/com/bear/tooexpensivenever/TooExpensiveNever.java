package com.bear.tooexpensivenever;

import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@Mod("tooexpensivenever")
public class TooExpensiveNever {

    public TooExpensiveNever() {
        // Register the event handler
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();

        // Check if both input slots are used
        if (!left.isEmpty() && !right.isEmpty()) {
            // Copy the left item as the base
            ItemStack output = left.copy();

            // Get enchantments from both items
            Map<Enchantment, Integer> leftEnchantments = EnchantmentHelper.getEnchantments(left);
            Map<Enchantment, Integer> rightEnchantments = EnchantmentHelper.getEnchantments(right);

            // Combine enchantments
            for (Map.Entry<Enchantment, Integer> entry : rightEnchantments.entrySet()) {
                Enchantment enchantment = entry.getKey();
                int rightLevel = entry.getValue();
                int leftLevel = leftEnchantments.getOrDefault(enchantment, 0);

                int newLevel;

                // Check if the enchantment is at or below vanilla max level
                if (leftLevel == rightLevel && leftLevel < enchantment.getMaxLevel()) {
                    // Normal behavior: combine and increment by 1
                    newLevel = leftLevel + 1;
                } else {
                    // Custom behavior: bypass vanilla limits, sum levels
                    newLevel = Math.max(leftLevel, rightLevel) + 1;
                }

                // Update the enchantment level
                leftEnchantments.put(enchantment, newLevel);
            }

            // Apply the combined enchantments to the output item
            EnchantmentHelper.setEnchantments(leftEnchantments, output);

            // Set the output item
            event.setOutput(output);

            // Calculate cost (linear scaling)
            int cost = 0;
            for (int level : leftEnchantments.values()) {
                cost += level * 2; // Scales linearly with enchantment levels
            }
            event.setCost(cost); // Set the anvil level cost

            // Disable the "Too Expensive!" limit
            event.setMaterialCost(1);
        }
    }
}
