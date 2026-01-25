package net.kozibrodka.wolves.block;

import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;

public class ConveyorExtenderBlock extends LazyBlockTemplate {
    private static final double ACCELERATION = 1.1;
    private static final double MAXIMUM_VELOCITY = 1.5;
    private static final double DECELERATION = 0.75;
    private static final double MINIMUM = 0.1;


    public ConveyorExtenderBlock(Identifier identifier, Material material, float hardness, BlockSoundGroup blockSounds) {
        super(identifier, material, hardness, blockSounds);
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public int getTexture(int side, int meta) {
        return switch (meta) {
            case 0, 2 -> switch (side) {
                case 0 -> TextureListener.conveyor_bottom;
                case 1 -> TextureListener.conveyor_up;
                case 2 -> TextureListener.conveyor_front;
                case 3 -> TextureListener.conveyor_back;
                default -> TextureListener.conveyor_side;
            };
            case 3 -> switch (side) {
                case 0 -> TextureListener.conveyor_bottom;
                case 1 -> TextureListener.conveyor_down;
                case 2 -> TextureListener.conveyor_back;
                case 3 -> TextureListener.conveyor_front;
                default -> TextureListener.conveyor_side;
            };
            case 4 -> switch (side) {
                case 0 -> TextureListener.conveyor_bottom;
                case 1 -> TextureListener.conveyor_left;
                case 4 -> TextureListener.conveyor_front;
                case 5 -> TextureListener.conveyor_back;
                default -> TextureListener.conveyor_side;
            };
            case 5 -> switch (side) {
                case 0 -> TextureListener.conveyor_bottom;
                case 1 -> TextureListener.conveyor_right;
                case 4 -> TextureListener.conveyor_back;
                case 5 -> TextureListener.conveyor_front;
                default -> TextureListener.conveyor_side;
            };
            case 8 -> switch (side) {
                case 0 -> TextureListener.conveyor_bottom;
                case 1 -> TextureListener.conveyor_up_on;
                case 2 -> TextureListener.conveyor_front_on;
                case 3 -> TextureListener.conveyor_back_on;
                default -> TextureListener.conveyor_side;
            };
            case 9 -> switch (side) {
                case 0 -> TextureListener.conveyor_bottom;
                case 1 -> TextureListener.conveyor_down_on;
                case 2 -> TextureListener.conveyor_back_on;
                case 3 -> TextureListener.conveyor_front_on;
                default -> TextureListener.conveyor_side;
            };
            case 10 -> switch (side) {
                case 0 -> TextureListener.conveyor_bottom;
                case 1 -> TextureListener.conveyor_left_on;
                case 4 -> TextureListener.conveyor_front_on;
                case 5 -> TextureListener.conveyor_back_on;
                default -> TextureListener.conveyor_side;
            };
            case 11 -> switch (side) {
                case 0 -> TextureListener.conveyor_bottom;
                case 1 -> TextureListener.conveyor_right_on;
                case 4 -> TextureListener.conveyor_back_on;
                case 5 -> TextureListener.conveyor_front_on;
                default -> TextureListener.conveyor_side;
            };
            default -> 0;
        };
    }

    @Override
    public Box getCollisionShape(World world, int x, int y, int z) {
        return Box.createCached(x, y, z, x + 1, y + 0.9, z + 1);
    }

    public void onPlaced(World world, int x, int y, int z, LivingEntity placer) {
        int facing = MathHelper.floor((double)(placer.yaw * 4.0F / 360.0F) + (double)0.5F) & 3;
        if (facing == 0) {
            world.setBlockMeta(x, y, z, 2);
        }

        if (facing == 1) {
            world.setBlockMeta(x, y, z, 5);
        }

        if (facing == 2) {
            world.setBlockMeta(x, y, z, 3);
        }

        if (facing == 3) {
            world.setBlockMeta(x, y, z, 4);
        }
    }

    @Override
    public void onEntityCollision(World world, int x, int y, int z, Entity entity) {
        int blockMeta = world.getBlockMeta(x, y, z);
        if (blockMeta < 6) {
            return;
        }
        double xMinimum = 0;
        double zMinimum = 0;
        double xChange = 0;
        double zChange = switch (blockMeta) {
            case 8 -> {
                xChange = DECELERATION;
                zMinimum = MINIMUM;
                yield -ACCELERATION;
            }
            case 9 -> {
                xChange = DECELERATION;
                zMinimum = MINIMUM;
                yield ACCELERATION;
            }
            case 10 -> {
                xChange = -ACCELERATION;
                xMinimum = MINIMUM;
                yield DECELERATION;
            }
            case 11 -> {
                xChange = ACCELERATION;
                xMinimum = MINIMUM;
                yield DECELERATION;
            }
            default -> 0;
        };
        double xVelocity = Math.abs(entity.velocityX);
        double zVelocity = Math.abs(entity.velocityZ);
        if (xVelocity < MAXIMUM_VELOCITY) {
            xVelocity *= Math.abs(xChange);
        }
        if (zVelocity < MAXIMUM_VELOCITY) {
            zVelocity *= Math.abs(zChange);
        }
        if (xVelocity < xMinimum) {
            xVelocity = MINIMUM;
        }
        if (zVelocity < zMinimum) {
            zVelocity = MINIMUM;
        }
        if (Math.signum(xVelocity) != Math.signum(xChange)) {
            xVelocity *= -1;
        }
        if (Math.signum(zVelocity) != Math.signum(zChange)) {
            zVelocity *= -1;
        }
        entity.velocityX = xVelocity;
        entity.velocityZ = zVelocity;
    }

    public void updateConveyor(World world, int x, int y, int z, boolean enable, int range) {
        int meta = world.getBlockMeta(x, y, z);
        if (enable && meta < 6) {
            world.setBlockMeta(x, y, z, meta + 6);
        } else if (!enable && meta >= 6) {
            world.setBlockMeta(x, y, z, meta - 6);
        }
        if (meta >= 6) {
            meta -= 6;
        }
        int xChange = 0;
        int zChange = switch (meta) {
            case 2 -> -1;
            case 3 -> 1;
            case 4 -> {
                xChange = -1;
                yield 0;
            }
            case 5 -> {
                xChange = 1;
                yield 0;
            }
            default -> 0;
        };
        if (range > 0) {
            if (world.getBlockId(x + xChange, y, z + zChange) == BlockListener.conveyorExtender.id) {
                BlockListener.conveyorExtender.updateConveyor(world, x + xChange, y, z + zChange, enable, range - 1);
            }
        }
    }
}
