package space.nickyblackburn.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.Block;

public class Consts {
    
    public static List background = new ArrayList<Integer>();
    public static List keys = new ArrayList<Integer>();

    public static List commands = new LinkedList<String>();
    public static List commanddesc = new LinkedList<String>();


    public static List blocklistseen = new LinkedList<Block>();
    public static List blocklistseenpos = new LinkedList<double>();

    public static Minecraft minecraft  = Minecraft.getInstance();

    
    public static boolean devMode = false;
    public static boolean showStart = true;

    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();
   
    // this is the logo dimention on the loading screen
    public static int px =  -50;
    public static int py = -100;
    public static int  pTextureWidth = 120;
    public static int pTextureHeight= 120;

    public static int pWidth =  120;
    public static int pHight = 120;

    public static int pUOffset = 0 ;
    public static float pVOffset = 0;

    // Ufull
    public static int pxfull =  190;
    public static int pyfull = 80;
    public static int  pTextureWidthfull = 120;
    public static int pTextureHeightfull = 120;

    public static int pWidthfull =  120;
    public static int pHightfull = 120;

    public static int pUOffsetfull = 0 ;
    public static float pVOffsetfull = 0;

    public static String RELEASE = "DEBUG";
    public static String VERSION = "§c1§6.§e1§a8§9.§b1§5-§cB§6l§ea§ac§9k§bb§5u§cr§6n§r";
    public static String Date = null;


    public static String ReleaseName = "§cB§6l§ea§ac§9k§bb§5u§cr§6n§e'§as§r";


    // CopyRight Text
    public static String copyright = "§cC§6o§ep§ay§9R§bi§5g§ch§6t§ee§ad §bB§5y §6N§ei§ac§9k§by §cB§6l§ea§ac§9k§bb§5u§cr§6n";


    public static boolean enablePosInfo = false;
    public static boolean enableLightInfo = false;


    public boolean getEnablePos(){
        return enablePosInfo;
    }


    // commands stuff

    public static String commandprefix = "command.blackburn.";


    public static void dbg(String s)
    {
        LOGGER.info("[Blackburn] " + s);
    }

    public static void warn(String s)
    {
        LOGGER.warn("[Blackburn] " + s);
    }

    public static void warn(String s, Throwable t)
    {
        LOGGER.warn("[Blackburn] " + s, t);
    }

    public static void error(String s)
    {
        LOGGER.error("[Blackburn] " + s);
    }

    public static void error(String s, Throwable t)
    {
        LOGGER.error("[Blackburn] " + s, t);
    }

    public static void log(String s)
    {
        dbg(s);
    }


    public static void info(String s)
    {
        dbg(s);
    }

    public static void debug(String s)
    {
        dbg(s);
    }

 

}
