package com.mojang.realmsclient;

import java.util.Locale;

public enum Unit
{
    B,
    KB,
    MB,
    GB;

    private static final int BASE_UNIT = 1024;

    public static Unit getLargest(long pBytes)
    {
        if (pBytes < 1024L)
        {
            return B;
        }
        else
        {
            try
            {
                int i = (int)(Math.log((double)pBytes) / Math.log(1024.0D));
                String s = String.valueOf("KMGTPE".charAt(i - 1));
                return valueOf(s + "B");
            }
            catch (Exception exception)
            {
                return GB;
            }
        }
    }

    public static double convertTo(long pBytes, Unit p_86944_)
    {
        return p_86944_ == B ? (double)pBytes : (double)pBytes / Math.pow(1024.0D, (double)p_86944_.ordinal());
    }

    public static String humanReadable(long pBytes)
    {
        int i = 1024;

        if (pBytes < 1024L)
        {
            return pBytes + " B";
        }
        else
        {
            int j = (int)(Math.log((double)pBytes) / Math.log(1024.0D));
            String s = "" + "KMGTPE".charAt(j - 1);
            return String.format(Locale.ROOT, "%.1f %sB", (double)pBytes / Math.pow(1024.0D, (double)j), s);
        }
    }

    public static String humanReadable(long pBytes, Unit p_86949_)
    {
        return String.format("%." + (p_86949_ == GB ? "1" : "0") + "f %s", convertTo(pBytes, p_86949_), p_86949_.name());
    }
}
