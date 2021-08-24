package com.redgrapefruit.itemnbt.itemnbt.mixin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.redgrapefruit.itemnbt.itemnbt.Classifier;
import com.redgrapefruit.itemnbt.itemnbt.ItemData;
import com.redgrapefruit.itemnbt.itemnbt.ItemDataManager;
import com.redgrapefruit.itemnbt.itemnbt.access.ItemStackMixinAccess;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(ItemStack.class)
public class ItemStackMixin implements ItemStackMixinAccess {
    @Shadow @Final @Deprecated private Item item;

    @Unique
    private final @NotNull Map<Classifier, ItemData> itemnbt$associates = new HashMap<>();

    @Inject(method = "<init>(Lnet/minecraft/nbt/NbtCompound;)V", at = @At("TAIL"))
    private void itemnbt$readNbt(NbtCompound nbt, CallbackInfo ci) {
        final ItemStack self = (ItemStack) (Object) this;

        ItemDataManager.forAllMatching((classifier, factory) -> {
            itemnbt$associates.putIfAbsent(classifier, factory.get());
            itemnbt$associates.get(classifier).readNbt(item, self, nbt);
        }, self);
    }

    @Inject(method = "writeNbt", at = @At("TAIL"))
    private void itemnbt$writeNbt(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        final ItemStack self = (ItemStack) (Object) this;

        ItemDataManager.forAllMatching((classifier, factory) -> {
            itemnbt$associates.putIfAbsent(classifier, factory.get());
            itemnbt$associates.get(classifier).writeNbt(item, self, nbt);
        }, self);
    }

    @Override
    public @NotNull ImmutableList<ItemData> itemnbt$getAll() {
        final ItemStack self = (ItemStack) (Object) this;
        final ImmutableList.Builder<ItemData> builder = ImmutableList.builder();

        ItemDataManager.forAllMatching((classifier, factory) -> {
            itemnbt$associates.putIfAbsent(classifier, factory.get());
            builder.add(itemnbt$associates.get(classifier));
        }, self);

        return builder.build();
    }

    @Override
    public @NotNull ImmutableMap<Classifier, ItemData> itemnbt$getAllWithContext() {
        final ItemStack self = (ItemStack) (Object) this;
        final ImmutableMap.Builder<Classifier, ItemData> builder = ImmutableMap.builder();

        ItemDataManager.forAllMatching((classifier, factory) -> {
            itemnbt$associates.putIfAbsent(classifier, factory.get());
            builder.put(classifier, itemnbt$associates.get(classifier));
        }, self);

        return builder.build();
    }
}
