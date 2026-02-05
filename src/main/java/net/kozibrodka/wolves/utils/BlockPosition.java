package net.kozibrodka.wolves.utils;


public class BlockPosition {

    public BlockPosition() {
        x = y = z = 0;
    }

    public BlockPosition(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BlockPosition(int facing) {
        x = y = z = 0;
        addFacingAsOffset(facing);
    }

    public void addFacingAsOffset(int facing) {
        switch (facing) {
            case 0 -> y--;
            case 1 -> y++;
            case 2 -> z--;
            case 3 -> z++;
            case 4 -> x--;
            default -> x++;
        }
    }

    public void invert() {
        x = -x;
        y = -y;
        z = -z;
    }

    public void addPos(BlockPosition pos) {
        x += pos.x;
        y += pos.y;
        z += pos.z;
    }

    public int x;
    public int y;
    public int z;
}
