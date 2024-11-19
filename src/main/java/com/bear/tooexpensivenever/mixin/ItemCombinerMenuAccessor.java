package com.bear.tooexpensivenever.mixin;

import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemCombinerMenu.class)
public interface ItemCombinerMenuAccessor {
    @Accessor("inputSlots")
    CraftingContainer getInputSlots(); // Exposes the protected `inputSlots` field
}
