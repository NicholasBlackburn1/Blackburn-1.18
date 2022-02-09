package net.optifine.shaders.config;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.optifine.Config;
import net.optifine.shaders.Shaders;

public class MacroProcessor
{
    public static InputStream process(InputStream in, String path, boolean useShaderOptions) throws IOException
    {
        String s = Config.readInputStream(in, "ASCII");
        String s1 = getMacroHeader(s, useShaderOptions);

        if (!s1.isEmpty())
        {
            s = s1 + s;

            if (Shaders.saveFinalShaders)
            {
                String s2 = path.replace(':', '/') + ".pre";
                Shaders.saveShader(s2, s);
            }

            s = process(s);
        }

        if (Shaders.saveFinalShaders)
        {
            String s3 = path.replace(':', '/');
            Shaders.saveShader(s3, s);
        }

        byte[] abyte = s.getBytes("ASCII");
        return new ByteArrayInputStream(abyte);
    }

    public static String process(String strIn) throws IOException
    {
        StringReader stringreader = new StringReader(strIn);
        BufferedReader bufferedreader = new BufferedReader(stringreader);
        MacroState macrostate = new MacroState();
        StringBuilder stringbuilder = new StringBuilder();

        while (true)
        {
            String s = bufferedreader.readLine();

            if (s == null)
            {
                s = stringbuilder.toString();
                return s;
            }

            if (macrostate.processLine(s) && !MacroState.isMacroLine(s))
            {
                stringbuilder.append(s);
                stringbuilder.append("\n");
            }
        }
    }

    private static String getMacroHeader(String str, boolean useShaderOptions) throws IOException
    {
        StringBuilder stringbuilder = new StringBuilder();
        List<ShaderOption> list = null;
        List<ShaderMacro> list1 = null;
        StringReader stringreader = new StringReader(str);
        BufferedReader bufferedreader = new BufferedReader(stringreader);

        while (true)
        {
            String s = bufferedreader.readLine();

            if (s == null)
            {
                return stringbuilder.toString();
            }

            if (MacroState.isMacroLine(s))
            {
                if (stringbuilder.length() == 0)
                {
                    stringbuilder.append(ShaderMacros.getFixedMacroLines());
                }

                if (useShaderOptions)
                {
                    if (list == null)
                    {
                        list = getMacroOptions();
                    }

                    Iterator iterator = list.iterator();

                    while (iterator.hasNext())
                    {
                        ShaderOption shaderoption = (ShaderOption)iterator.next();

                        if (s.contains(shaderoption.getName()))
                        {
                            stringbuilder.append(shaderoption.getSourceLine());
                            stringbuilder.append("\n");
                            iterator.remove();
                        }
                    }
                }

                if (list1 == null)
                {
                    list1 = new ArrayList<>(Arrays.asList(ShaderMacros.getExtensions()));
                }

                Iterator iterator1 = list1.iterator();

                while (iterator1.hasNext())
                {
                    ShaderMacro shadermacro = (ShaderMacro)iterator1.next();

                    if (s.contains(shadermacro.getName()))
                    {
                        stringbuilder.append(shadermacro.getSourceLine());
                        stringbuilder.append("\n");
                        iterator1.remove();
                    }
                }
            }
        }
    }

    private static List<ShaderOption> getMacroOptions()
    {
        List<ShaderOption> list = new ArrayList<>();
        ShaderOption[] ashaderoption = Shaders.getShaderPackOptions();

        for (int i = 0; i < ashaderoption.length; ++i)
        {
            ShaderOption shaderoption = ashaderoption[i];
            String s = shaderoption.getSourceLine();

            if (s != null && s.startsWith("#"))
            {
                list.add(shaderoption);
            }
        }

        return list;
    }
}
