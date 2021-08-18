package com.redgrapefruit.itemnbt.itemnbt.mixin;

import com.google.common.collect.ImmutableList;
import com.redgrapefruit.itemnbt.itemnbt.ItemNBT;
import com.redgrapefruit.itemnbt.itemnbt.internal.ItemNBTEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

@Mixin(ItemStack.class)
public final class ItemStackMixin {
    @Inject(method = "<init>(Lnet/minecraft/nbt/NbtCompound;)V", at = @At("TAIL"))
    private void itemnbt$constructor(NbtCompound nbt, CallbackInfo ci) {
        forEachEntry((entry) -> entry.getDeserializer().deserialize(nbt, (ItemStack) (Object) this));
    }

    @Inject(method = "writeNbt", at = @At("TAIL"))
    private void itemnbt$writeNbt(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        forEachEntry((entry) -> entry.getSerializer().serialize(nbt, (ItemStack) (Object) this));
    }

    private void forEachEntry(Consumer<ItemNBTEntry> action) {
        ImmutableList<ItemNBTEntry> entries = ItemNBT.search((ItemStack) (Object) this);

        entries.forEach(action);
    }
}
