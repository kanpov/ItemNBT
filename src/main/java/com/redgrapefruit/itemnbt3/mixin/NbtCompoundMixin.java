package com.redgrapefruit.itemnbt3.mixin;

import com.redgrapefruit.itemnbt3.util.NbtCompoundMixinAccess;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@ApiStatus.Internal
@Mixin(NbtCompound.class)
public class NbtCompoundMixin implements NbtCompoundMixinAccess {
    @Shadow @Final private Map<String, NbtElement> entries;

    @Override
    public void clearNbt() {
        entries.clear();
    }
}
