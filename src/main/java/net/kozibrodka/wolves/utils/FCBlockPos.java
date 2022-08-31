package net.kozibrodka.wolves.utils;


public class FCBlockPos
{

    public FCBlockPos()
    {
        i = j = k = 0;
    }

    public FCBlockPos(int i1, int j1, int k1)
    {
        i = i1;
        j = j1;
        k = k1;
    }

    public FCBlockPos(int iFacing)
    {
        i = j = k = 0;
        AddFacingAsOffset(iFacing);
    }

    public void AddFacingAsOffset(int iFacing)
    {
        switch (iFacing) {
            case 0  -> j--;
            case 1  -> j++;
            case 2  -> k--;
            case 3  -> k++;
            case 4  -> i--;
            default -> i++;
        }
    }

    public void Invert()
    {
        i = -i;
        j = -j;
        k = -k;
    }

    public void AddPos(FCBlockPos pos)
    {
        i += pos.i;
        j += pos.j;
        k += pos.k;
    }

    public int i;
    public int j;
    public int k;
}
