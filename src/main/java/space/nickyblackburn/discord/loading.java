package space.nickyblackburn.discord;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.print.DocFlavor.URL;

import org.apache.commons.io.FileUtils;

import net.minecraft.client.Minecraft;
import space.nickyblackburn.utils.Consts;

public class loading {


    java.net.URL lib;
    File dest;

    public void getOS(){


        // windows os
        if(System.getProperty("os.name").startsWith("Windows")){

            Consts.log("Windows is detected loading RPC dll");

            this.lib = getClass().getResource("/assets/minecraft/blackburn/discord/win32-x86-64/discord-rpc.dll");
            this.dest= new File(Minecraft.getInstance().gameDirectory.getAbsolutePath()+"\\"+"discord-rpc.dll");
            
            Consts.log("Copying files... to "+Minecraft.getInstance().gameDirectory.getAbsolutePath()+"\\"+"discord-rpc.dll");
            try {

                Consts.log("Copyting dll");
                FileUtils.copyURLToFile(lib, dest);
                Consts.error("copyed successfully");

                Consts.rpcdll =(Minecraft.getInstance().gameDirectory.getAbsolutePath()+"\\"+"discord-rpc.dll");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        // linux oso
        if(System.getProperty("os.name").startsWith("Linux")){

            Consts.log("Linux is detected loading RPC so");

            this.lib = getClass().getResource("/assets/minecraft/blackburn/discord/linux-x86-64/libdiscord-rpc.so");
            this.dest= new File(Minecraft.getInstance().gameDirectory.getAbsolutePath()+"/"+"libdiscord-rpc.so");
            
            Consts.log("Copying files... to "+Minecraft.getInstance().gameDirectory.getAbsolutePath()+"/"+"libdiscord-rpc.so");
            try {
                FileUtils.copyURLToFile(lib, dest);
                Consts.error("copyed successfully");
                Consts.rpcdll =(Minecraft.getInstance().gameDirectory.getAbsolutePath()+"/"+"libdiscord-rpc.so");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        if(System.getProperty("os.name").startsWith("Darwin")){

            Consts.log("Linux is detected loading RPC so");

            this.lib = getClass().getResource("/assets/minecraft/blackburn/discord/darwin/libdiscord-rpc.dylib");
            this.dest= new File(Minecraft.getInstance().gameDirectory.getAbsolutePath()+"/"+"libdiscord-rpc.dylib");
            
            Consts.log("Copying files... to "+Minecraft.getInstance().gameDirectory.getAbsolutePath()+"/"+"libdiscord-rpc.dylib");
            try {
                FileUtils.copyURLToFile(lib, dest);
                Consts.error("copyed successfully");
                Consts.rpcdll =(Minecraft.getInstance().gameDirectory.getAbsolutePath()+"/"+"libdiscord-rpc.dylib");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

 
}
