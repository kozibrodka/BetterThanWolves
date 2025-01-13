package net.kozibrodka.wolves.items;

import net.kozibrodka.wolves.events.BlockListener;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;

public class StumpRemoverItem extends LazyItemTemplate {
    public StumpRemoverItem(Identifier identifier) {
        super(identifier);
    }

    @Override
    public boolean useOnBlock(ItemStack stack, PlayerEntity user, World world, int x, int y, int z, int side) {
        int blockId = world.getBlockId(x, y, z);
        if (blockId != BlockListener.oakStump.id && blockId != BlockListener.birchStump.id && blockId != BlockListener.spruceStump.id) {
            return false;
        }
        world.setBlock(x, y, z, 0);
        world.playSound(user, "random.explode", 0.25F, 0.5F);
        for(int var3 = 10; var3 >= 0; --var3) {
            double particleX = (float) x + world.random.nextFloat();
            double particleY = (float) y + world.random.nextFloat();
            double particleZ = (float) z + world.random.nextFloat();
            double motionX = particleX - x;
            double motionY = particleY - y;
            double motionZ = particleZ - z;
            double var21 = MathHelper.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
            motionX /= var21;
            motionY /= var21;
            motionZ /= var21;
            double var23 = 0.5 / (var21);
            var23 *= world.random.nextFloat() * world.random.nextFloat() + 0.3F;
            motionX *= var23;
            motionY *= var23;
            motionZ *= var23;
            if (world.random.nextBoolean()) {
                motionX *= -1;
            }
            if (world.random.nextBoolean()) {
                motionZ *= -1;
            }
            world.addParticle("explode", (particleX + x * 1.0) / 2.0, (particleY + y * 1.0) / 2.0, (particleZ + z * 1.0) / 2.0, motionX, motionY, motionZ);
            world.addParticle("smoke", particleX, particleY, particleZ, motionX, motionY, motionZ);
        }
        stack.count--;
        return true;
    }
}
