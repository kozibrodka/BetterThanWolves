package net.kozibrodka.wolves.network;

public class ClientScreenData {
    public static int count;

    public static boolean isGrinding(){
        return count > 0;
    }

    public static boolean IsCooking()
    {
        return count > 0;
    }

    public static boolean isPowered(){
        return count > 0;
    }

    public static int getGrindProgressScaled(int iScale)
    {
        return (count * iScale) / 200;
    }

    public static int getCookProgressScaled(int iScale)
    {
        return (count * iScale) / 1950;
    }
}
