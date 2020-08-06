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

public class BKAssembler
{
    String asm_head;
    String asm_tail;
    String asm_data;

    public BKAssembler()
    {
        this.asm_head = "";
        this.asm_tail = "";
        this.asm_data = "";
        this.pre_assembly();
    }

    public void assemble(java.util.List<HitboxElement> hblist, String filename)
    {
        String asm = "";
        asm += this.asm_head;

        this.asm_data = "";

        int data_size = 2; // starting with 2 to account for data-terminator
        System.out.println(data_size);
        for (HitboxElement hb : hblist)
        {
            this.asm_data += hb.toString();
            data_size += (int)hb.data_size.value;
        }

        // insert data-terminator
        this.asm_data += ".halfword 0x6666 //# data_end\n";

        // Padding routine for Data-Alignment
        while(data_size % 8 != 0)
        {
            this.asm_data += ".byte 0xFF\n";
            data_size++;
        }

        asm += this.asm_data;
        asm += this.asm_tail;

        // actual assembly file
        try
        {
            FileWriter writer = new FileWriter(filename);
            writer.write(asm);
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

                hb.level_id = new Hex(byteRead, 2);
                hb.data_size = new Hex(inputStream.read(), 2);

                byteRead = inputStream.read();
                hb.x1 = Hex.concat(new Hex(byteRead, 2), new Hex(inputStream.read(), 2));
                byteRead = inputStream.read();
                hb.x2 = Hex.concat(new Hex(byteRead, 2), new Hex(inputStream.read(), 2));
                
                byteRead = inputStream.read();
                hb.y1 = Hex.concat(new Hex(byteRead, 2), new Hex(inputStream.read(), 2));
                byteRead = inputStream.read();
                hb.y2 = Hex.concat(new Hex(byteRead, 2), new Hex(inputStream.read(), 2));

                byteRead = inputStream.read();
                hb.z1 = Hex.concat(new Hex(byteRead, 2), new Hex(inputStream.read(), 2));
                byteRead = inputStream.read();
                hb.z2 = Hex.concat(new Hex(byteRead, 2), new Hex(inputStream.read(), 2));

                hb.type_value = new Hex(inputStream.read(), 2);
                hb.special_size = new Hex(inputStream.read(), 2);

                switch((int)hb.type_value.value)
                {
                    case(01): // Wind
                    {
                        hb.type = "Wind";
                        byteRead = inputStream.read();
                        hb.v1 = Hex.concat(new Hex(byteRead, 2), new Hex(inputStream.read(), 2));
                        byteRead = inputStream.read();
                        hb.v2 = Hex.concat(new Hex(byteRead, 2), new Hex(inputStream.read(), 2));
                        byteRead = inputStream.read();
                        hb.v3 = Hex.concat(new Hex(byteRead, 2), new Hex(inputStream.read(), 2));
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

    public void pre_assembly()
    {
        this.asm_head += ".org 0x03F09478 //# Hijack Basement Door JAL by SpaceOmega5000\n";
        this.asm_head += ".word 0x804429F0\n";
        this.asm_head += "\n";
        this.asm_head += ".org 0x03F429F0 //# Custom Multi-Effect Code by ThatCowGuy\n";
        this.asm_head += "MAIN:\n";
        this.asm_head += "ADDIU SP, SP, 0xFFE8\n";
        this.asm_head += "SW RA, 0x14(SP)\n";
        this.asm_head += "BEQ R0, R0, START\n";
        this.asm_head += "NOP\n";
        this.asm_head += "\n";
    
        this.asm_tail += "\n";
        this.asm_tail += "START:\n";
        this.asm_tail += "LUI T1, @DATA\n";
        this.asm_tail += "ADDI T1, T1, @DATA\n";
        this.asm_tail += "\n";
        this.asm_tail += "LUI T3, @ADDR_MAP\n";
        this.asm_tail += "LB T3, @ADDR_MAP(T3)\n";
        this.asm_tail += "\n";
        this.asm_tail += "BOUNDARY_LOOP:\n";
        this.asm_tail += "//=================================\n";
        this.asm_tail += "    LH T2, 0x00(T1)  \n";
        this.asm_tail += "    ADDI T8, R0, 0x6666\n";
        this.asm_tail += "    BEQ T2, T8, BOUNDARY_LOOP_BREAK\n";
        this.asm_tail += "    NOP\n";
        this.asm_tail += "    \n";
        this.asm_tail += "    LB T2, 0x00(T1)\n";
        this.asm_tail += "    BNE T2, T3, WRONG_MAP_ID\n";
        this.asm_tail += "    NOP\n";
        this.asm_tail += "    \n";
        this.asm_tail += "    ADDI T1, T1, 0x02\n";
        this.asm_tail += "    \n";
        this.asm_tail += "    LUI T0, @ADDR_XZY_POS\n";
        this.asm_tail += "    ADDI T0, T0, @ADDR_XZY_POS\n";
        this.asm_tail += "    ADDI T7, R0, 0x01 //# boolean: inside wind\n";
        this.asm_tail += "    \n";
        this.asm_tail += "    ADDI T9, R0, 0x00 //# i=0\n";
        this.asm_tail += "    ADDI T8, R0, 0x03 //# max=3\n";
        this.asm_tail += "    XYZ_LOOP:\n";
        this.asm_tail += "        LWC1 F0, 0x00(T0)\n";
        this.asm_tail += "        LH T2, 0x00(T1)\n";
        this.asm_tail += "        MTC1 T2, F1\n";
        this.asm_tail += "        CVT.S.W F1, F1\n";
        this.asm_tail += "        SUB.S F1, F0, F1\n";
        this.asm_tail += "        MFC1 T2, F1\n";
        this.asm_tail += "        BLTZ T2, CLEAR_FLAG\n";
        this.asm_tail += "        NOP\n";
        this.asm_tail += "        \n";
        this.asm_tail += "        LH T2, 0x02(T1)\n";
        this.asm_tail += "        MTC1 T2, F1\n";
        this.asm_tail += "        CVT.S.W F1, F1\n";
        this.asm_tail += "        SUB.S F1, F0, F1\n";
        this.asm_tail += "        MFC1 T2, F1\n";
        this.asm_tail += "        BGTZ T2, CLEAR_FLAG\n";
        this.asm_tail += "        NOP\n";
        this.asm_tail += "        \n";
        this.asm_tail += "        BEQ R0, R0, DONT_CLEAR_FLAG\n";
        this.asm_tail += "        NOP\n";
        this.asm_tail += "        \n";
        this.asm_tail += "        CLEAR_FLAG:\n";
        this.asm_tail += "        ADDI T7, R0, 0x00\n";
        this.asm_tail += "        \n";
        this.asm_tail += "        DONT_CLEAR_FLAG:\n";
        this.asm_tail += "        ADDI T0, T0, 0x04\n";
        this.asm_tail += "        ADDI T1, T1, 0x04\n";
        this.asm_tail += "        \n";
        this.asm_tail += "        ADDI T9, T9, 0x01 //# i++\n";
        this.asm_tail += "        BEQ T9, T8, XYZ_LOOP_BREAK\n";
        this.asm_tail += "        NOP\n";
        this.asm_tail += "        BEQ R0, R0, XYZ_LOOP\n";
        this.asm_tail += "        NOP\n";
        this.asm_tail += "    XYZ_LOOP_BREAK:\n";
        this.asm_tail += "    \n";
        this.asm_tail += "    BNE T7, R0, GET_EFFECT\n";
        this.asm_tail += "    NOP\n";
        this.asm_tail += "    \n";
        this.asm_tail += "    LB T2, 0x01(T1)\n";
        this.asm_tail += "    ADD T1, T1, T2 //# Skip special entries\n";
        this.asm_tail += "    ADDI T1, T1, 0x02\n";
        this.asm_tail += "    \n";
        this.asm_tail += "    BEQ R0, R0, BOUNDARY_LOOP\n";
        this.asm_tail += "    NOP\n";
        this.asm_tail += "    \n";
        this.asm_tail += "    WRONG_MAP_ID:\n";
        this.asm_tail += "    LB T2, 0x01(T1)\n";
        this.asm_tail += "    ADD T1, T1, T2 //# Skip entire Data Block\n";
        this.asm_tail += "    BEQ R0, R0, BOUNDARY_LOOP\n";
        this.asm_tail += "    NOP\n";
        this.asm_tail += "//=================================\n";
        this.asm_tail += "BOUNDARY_LOOP_BREAK:\n";
        this.asm_tail += "\n";
        this.asm_tail += "BEQ R0, R0, RETURN\n";
        this.asm_tail += "NOP\n";
        this.asm_tail += "\n";
        this.asm_tail += "GET_EFFECT:\n";
        this.asm_tail += "LB T2, 0x00(T1)\n";
        this.asm_tail += "\n";
        this.asm_tail += "ADDI T8, R0, 0x01 //# = Wind Hitbox\n";
        this.asm_tail += "ADDI T1, T1, 0x02\n";
        this.asm_tail += "BEQ T2, T8, APPLY_WIND\n";
        this.asm_tail += "NOP\n";
        this.asm_tail += "\n";
        this.asm_tail += "BEQ R0, R0, RETURN\n";
        this.asm_tail += "NOP\n";
        this.asm_tail += "\n";
        this.asm_tail += "APPLY_WIND:\n";
        this.asm_tail += "LUI T0, @ADDR_XZY_VEL\n";
        this.asm_tail += "ADDI T0, T0, @ADDR_XZY_VEL\n";
        this.asm_tail += "\n";
        this.asm_tail += "ADDI T2, R0, @WIND_SPEED_CAP\n";
        this.asm_tail += "MTC1 T2, F2\n";
        this.asm_tail += "CVT.S.W F2, F2\n";
        this.asm_tail += "\n";
        this.asm_tail += "ADDI T9, R0, 0x00 //# i=0\n";
        this.asm_tail += "ADDI T8, R0, 0x03 //# max=3\n";
        this.asm_tail += "WIND_LOOP:\n";
        this.asm_tail += "    LWC1 F0, 0x00(T0)\n";
        this.asm_tail += "    LH T2, 0x00(T1)\n";
        this.asm_tail += "    MTC1 T2, F1\n";
        this.asm_tail += "    CVT.S.W F1, F1\n";
        this.asm_tail += "    ADD.S F0, F0, F1\n";
        this.asm_tail += "    \n";
        this.asm_tail += "    SUB.S F1, F0, F2 //# F1 = spd - cap >>> has to be < 0\n";
        this.asm_tail += "    MFC1 T2, F1\n";
        this.asm_tail += "    BGTZ T2, POS_SPEED_CAP\n";
        this.asm_tail += "    NOP\n";
        this.asm_tail += "    ADD.S F1, F0, F2 //# F1 = spd + cap >>> has to be > 0\n";
        this.asm_tail += "    MFC1 T2, F1\n";
        this.asm_tail += "    BLTZ T2, NEG_SPEED_CAP\n";
        this.asm_tail += "    NOP\n";
        this.asm_tail += "    \n";
        this.asm_tail += "    MFC1 T2, F0\n";
        this.asm_tail += "    BEQ R0, R0, APPLY\n";
        this.asm_tail += "    NOP\n";
        this.asm_tail += "    \n";
        this.asm_tail += "    POS_SPEED_CAP:\n";
        this.asm_tail += "    MFC1 T2, F2\n";
        this.asm_tail += "    BEQ R0, R0, APPLY\n";
        this.asm_tail += "    NOP\n";
        this.asm_tail += "    \n";
        this.asm_tail += "    NEG_SPEED_CAP:\n";
        this.asm_tail += "    NEG.S F2, F2\n";
        this.asm_tail += "    MFC1 T2, F2\n";
        this.asm_tail += "    NEG.S F2, F2\n";
        this.asm_tail += "    BEQ R0, R0, APPLY\n";
        this.asm_tail += "    NOP\n";
        this.asm_tail += "    \n";
        this.asm_tail += "    APPLY:\n";
        this.asm_tail += "    SW T2, 0x00(T0)\n";
        this.asm_tail += "    \n";
        this.asm_tail += "    ADDI T0, T0, 0x04\n";
        this.asm_tail += "    ADDI T1, T1, 0x02\n";
        this.asm_tail += "    \n";
        this.asm_tail += "    ADDI T9, T9, 0x01 //# i++\n";
        this.asm_tail += "    BEQ T9, T8, BOUNDARY_LOOP\n";
        this.asm_tail += "    NOP\n";
        this.asm_tail += "    BEQ R0, R0, WIND_LOOP\n";
        this.asm_tail += "    NOP\n";
        this.asm_tail += "\n";
        this.asm_tail += "RETURN:\n";
        this.asm_tail += "LW RA, 0x14(SP)\n";
        this.asm_tail += "JR RA\n";
        this.asm_tail += "ADDIU SP, SP, 0x18\n";
        this.asm_tail += "\n";
        this.asm_tail += "[WIND_SPEED_CAP]: 0x0258 //# 600\n";
        this.asm_tail += "[ADDR_XZY_POS]: 0x8038C5A0 //# remember 0x10 offset\n";
        this.asm_tail += "[ADDR_XZY_VEL]: 0x8038C4B8\n";
        this.asm_tail += "[ADDR_MAP]: 0x8038E8F5 //# this is only 1 Byte\n";
        this.asm_tail += "[DATA]: 0x80442A00\n";
    }
}