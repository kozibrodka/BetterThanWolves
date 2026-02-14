package net.kozibrodka.wolves.mixin;

import net.kozibrodka.wolves.block.HopperBlock;
import net.kozibrodka.wolves.block.entity.CementBlockEntity;
import net.kozibrodka.wolves.block.entity.HopperBlockEntity;
import net.kozibrodka.wolves.entity.WindMillEntity;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.Block;
import net.minecraft.block.RailBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecartEntity.class)
public class MinecartEntityMixin extends Entity {

    @Shadow private ItemStack[] inventory;

    public MinecartEntityMixin(World world) {
        super(world);
    }

    @Override
    public void initDataTracker() {

    }

    @Override
    public void readNbt(NbtCompound nbt) {

    }

    @Override
    public void writeNbt(NbtCompound nbt) {

    }

    @Inject(method = "tick", at = @At(value = "HEAD", ordinal = 0))
    private void injected(CallbackInfo ci) {
        int var1 = MathHelper.floor(this.x);
        int var2 = MathHelper.floor(this.y);
        int var3 = MathHelper.floor(this.z);
        if(world.getBlockId(var1, var2 - 1, var3) == BlockListener.hopper.id && !world.isRemote){
            HopperBlockEntity tileEntity = (HopperBlockEntity) world.getBlockEntity(var1, var2 - 1, var3);
            tileEntity.detectedByMinecart(this.inventory);
        }
    }


}
