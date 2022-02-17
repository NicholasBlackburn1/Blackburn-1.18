package space.nickyblackburn.discord;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import space.nickyblackburn.utils.Consts;

public class loading {


    public void getOS(){
        // windows os 
        if(System.getProperty("os.name").startsWith("Windows")){

            Consts.log("Windows is detected loading RPC dll");

            try {
                Consts.log(ExportResource("/resources/assets/minecraft/blackburn/discord/win32-x86-64/discord-rpc.dll"));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        if(System.getProperty("os.name").startsWith("Linux")){

            Consts.log("Linux is detected loading RPC so");

            try {
                Consts.log(ExportResource("/resources/assets/minecraft/blackburn/discord/linux-x86-64/libdiscord-rpc.so"));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    public String ExportResource(String resourceName) throws Exception {
        InputStream stream = null;
        OutputStream resStreamOut = null;
        String jarFolder;
        try {
            stream = loading.class.getResourceAsStream(resourceName);//note that each / is a directory down in the "jar tree" been the jar the root of the tree
            if(stream == null) {
                throw new Exception("Cannot get resource \"" + resourceName + "\" from Jar file.");
            }

            int readBytes;
            byte[] buffer = new byte[4096];
            jarFolder = new File(loading.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/');
            resStreamOut = new FileOutputStream(jarFolder + resourceName);
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            
        }

        return jarFolder + resourceName;
    }
}
