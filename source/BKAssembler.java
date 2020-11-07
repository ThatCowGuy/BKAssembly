import java.io.*;
import javax.swing.*;
import javax.swing.event.*; 
import javax.swing.BorderFactory; 
import javax.swing.border.*; 
import javax.swing.table.*; 
import java.awt.*;  
import java.awt.event.*;
import java.awt.Color;  
import java.util.*;
import java.util.HashMap;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;



public class BKAssembler
{
    public BKAssembler()
    {
    }

    public void assemble(java.util.List<HitboxElement> hblist, String filename)
    {
        String template = "";
        try
        {
            template = Files.readString(Paths.get("source/multi_effect_template.asm"));
        }
        catch (Exception e)
        {
            System.out.println("In BKAssembler.java, assemble:" + e);
        }
        
        template += "\n";
        for (HitboxElement hb : hblist)
        {
            template += hb.toString();
            template += "//#==========================================\n";
        }

        // insert data-terminator
        template += ".halfword 0x6666 //# terminator\n";
        template += "//#%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%";

        // actually creating the new assembly file
        try
        {
            FileWriter writer = new FileWriter(filename);
            writer.write(template);
            writer.close();
            System.out.println("Successfully wrote to the File: " + filename);
        }
        catch (IOException e)
        {
            System.out.println("An error occurred while writing to File: " + filename);
            e.printStackTrace();
        }
        
        // the data snippet for reloading
        try
        {
            FileOutputStream fos = new FileOutputStream(filename.replace(".asm", ".data"));
            for (HitboxElement hb : hblist)
            {
                java.util.List<Hex> hexlist = hb.toData();
                for (Hex h : hexlist)
                {
                    fos.write(h.outputInBytes());
                }
            }
            fos.flush();
            fos.close();
        }
        catch (Exception e)
        {
            System.out.print("Oops: " + e);
        }
    }

    public int readFileBytewise(String path)
    {
        try
        {
            InputStream inputStream = new FileInputStream(path);
            if (inputStream == null)
            {
                System.out.println("Unable to locate File: " + path);
                return -1;
            }

            int byteRead;
            while ((byteRead = inputStream.read()) != -1)
            {

            }
        }
        catch (Exception e)
        {
            System.out.print("Oops: " + e);
            return -1;
        }

        return 1;
    }
    public java.util.List<HitboxElement> constructFromFile(String path)
    {
        java.util.List<HitboxElement> hblist = new ArrayList();

        try
        {
            InputStream inputStream = new FileInputStream(path);
            if (inputStream == null)
            {
                System.out.println("Unable to locate File: " + path);
                return hblist;
            }

            int byteRead;
            while ((byteRead = inputStream.read()) != -1)
            {
                HitboxElement hb = new HitboxElement();

                hb.room_id = new Hex(byteRead, 2);
                hb.struct_size = new Hex(inputStream.read(), 2);
                hb.effect_id = new Hex(inputStream.read(), 2);
                hb.special_size = new Hex(inputStream.read(), 2);

                byteRead = inputStream.read();
                Hex help = new Hex(byteRead, 2);
                help = Hex.concat(help, new Hex(inputStream.read(), 2));
                help = Hex.concat(help, new Hex(inputStream.read(), 2));
                hb.x1 = Hex.concat(help, new Hex(inputStream.read(), 2));
                byteRead = inputStream.read();
                help = new Hex(byteRead, 2);
                help = Hex.concat(help, new Hex(inputStream.read(), 2));
                help = Hex.concat(help, new Hex(inputStream.read(), 2));
                hb.y1 = Hex.concat(help, new Hex(inputStream.read(), 2));
                byteRead = inputStream.read();
                help = new Hex(byteRead, 2);
                help = Hex.concat(help, new Hex(inputStream.read(), 2));
                help = Hex.concat(help, new Hex(inputStream.read(), 2));
                hb.z1 = Hex.concat(help, new Hex(inputStream.read(), 2));

                byteRead = inputStream.read();
                help = new Hex(byteRead, 2);
                help = Hex.concat(help, new Hex(inputStream.read(), 2));
                help = Hex.concat(help, new Hex(inputStream.read(), 2));
                hb.x2 = Hex.concat(help, new Hex(inputStream.read(), 2));
                byteRead = inputStream.read();
                help = new Hex(byteRead, 2);
                help = Hex.concat(help, new Hex(inputStream.read(), 2));
                help = Hex.concat(help, new Hex(inputStream.read(), 2));
                hb.y2 = Hex.concat(help, new Hex(inputStream.read(), 2));
                byteRead = inputStream.read();
                help = new Hex(byteRead, 2);
                help = Hex.concat(help, new Hex(inputStream.read(), 2));
                help = Hex.concat(help, new Hex(inputStream.read(), 2));
                hb.z2 = Hex.concat(help, new Hex(inputStream.read(), 2));

                switch((int)hb.effect_id.value)
                {
                    case(01): // Wind
                    {
                        hb.type = "Wind";
                        byteRead = inputStream.read();
                        help = new Hex(byteRead, 2);
                        help = Hex.concat(help, new Hex(inputStream.read(), 2));
                        help = Hex.concat(help, new Hex(inputStream.read(), 2));
                        hb.v1 = Hex.concat(help, new Hex(inputStream.read(), 2));
                        byteRead = inputStream.read();
                        help = new Hex(byteRead, 2);
                        help = Hex.concat(help, new Hex(inputStream.read(), 2));
                        help = Hex.concat(help, new Hex(inputStream.read(), 2));
                        hb.v1 = Hex.concat(help, new Hex(inputStream.read(), 2));
                        byteRead = inputStream.read();
                        help = new Hex(byteRead, 2);
                        help = Hex.concat(help, new Hex(inputStream.read(), 2));
                        help = Hex.concat(help, new Hex(inputStream.read(), 2));
                        hb.v1 = Hex.concat(help, new Hex(inputStream.read(), 2));
                        break;
                    }
                    default:
                    {
                        System.out.print("Whoho, read type value appears to be garbo");
                    }
                }
                hb.value_init = true;
                hblist.add(hb);
            }
        }
        catch (Exception e)
        {
            System.out.print("Oops: " + e);
            return hblist;
        }

        return hblist;
    }
}