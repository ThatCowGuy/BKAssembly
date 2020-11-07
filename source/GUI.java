import javax.swing.*;
import javax.swing.event.*; 
import javax.swing.BorderFactory; 
import javax.swing.border.*; 
import javax.swing.table.*; 
import java.awt.*;  
import java.awt.event.*;
import java.awt.Color;  
import java.util.*;
import java.io.*;
import java.io.File;
import java.awt.dnd.*;

public class GUI extends JFrame implements ActionListener, ChangeListener, AdjustmentListener, TableModelListener
{
    public BKAssembler bkasm;
    public SaveScreen saveScreen;
    public LoadScreen loadScreen;

    public java.util.List<HitboxElement> hblist = new ArrayList<HitboxElement>();
    public String level_id_list[] = new String[152];
    
    public JComboBox type_dropdown;
    public JButton add_b;
    public JButton asm_b;
    public JButton load_b;

    public GUI()
    {
        super(" BKAssembly 1.1");
        this.bkasm = new BKAssembler();
        init_level_id_list();
        int x_off = 0;
        int y_off = 0;
        int dy = 25*6;

        // create the input fields
        //========================
        x_off = 10;
        y_off = 10+dy*0;
        String[] placeholder = {"Wind"};
        this.type_dropdown = new JComboBox(placeholder);
        //this.updateComboBox();
        this.type_dropdown.setBounds(x_off, y_off, 120, 20);
        this.type_dropdown.setSelectedIndex(0);
        this.type_dropdown.addActionListener(this);
        this.add(this.type_dropdown);
        //========================
        this.add_b = new JButton("Add");
        this.add_b.setBounds(x_off+125, y_off, 60, 20);
        this.add_b.setMargin(new Insets(1, 5, 3, 5));
        this.add_b.addActionListener(this);
        this.add(this.add_b);
        //========================
        this.asm_b = new JButton("Assemble");
        this.asm_b.setBounds(x_off+190, y_off, 70, 20);
        this.asm_b.setMargin(new Insets(1, 1, 3, 1));
        this.asm_b.addActionListener(this);
        this.add(this.asm_b);
        //========================
        this.load_b = new JButton("Load Existing Data");
        this.load_b.setBounds(x_off, y_off+25, 120, 20);
        this.load_b.setMargin(new Insets(1, 0, 3, 1));
        this.load_b.addActionListener(this);
        this.add(this.load_b);

        this.setSize(300,110);
        this.setLayout(null);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public void actionPerformed(ActionEvent event)
    {
        try
        {
            int index = 0;
            for (HitboxElement hb : this.hblist)
            {
                hb.update(index);
                System.out.println("updated hb #" + index + ":");
                System.out.println(hb);
                index++;
            }
            System.out.println("Pre-Update done");

            if (event.getSource() == this.add_b)
            {
                this.add_hb();
                this.relocateDropDown();
            }
            else if (event.getSource() == this.asm_b)
            {
                //this.bkasm.assemble(this.hblist);
                this.saveScreen = new SaveScreen("", this);
                // clean the results to free up Memory again
                this.saveScreen.dealloc();
                this.saveScreen.dispose();
                System.gc();
            }
            else if (event.getSource() == this.load_b)
            {
                //this.bkasm.assemble(this.hblist);
                this.loadScreen = new LoadScreen(this);
                // clean the results to free up Memory again
                this.loadScreen.dealloc();
                this.loadScreen.dispose();
                System.gc();
                this.relocateDropDown();
            }
            
            for (HitboxElement hb : this.hblist)
            {
                if (event.getSource() == hb.remove_b)
                {
                    hb.disconnectFrom(this);
                    this.hblist.remove(hb);
                    this.relocateDropDown();
                    break;
                }
            }
            
            index = 0;
            for (HitboxElement hb : this.hblist)
            {
                hb.update(index);
                System.out.println("updated hb #" + index + ":");
                System.out.println(hb);
                index++;
            }
            System.out.println("Post-Update done");
            this.repaint();
        }
        catch(Exception e)
        {
            System.out.println("Oops (actionPerformed): " + e);
        }
    }
    public void stateChanged(ChangeEvent event)
    {
        try
        {
            
        }
        catch(Exception e)
        {
            System.out.println("Oops: " + e);
        }
    }
    public void adjustmentValueChanged(AdjustmentEvent event)
    {
        try
        {
            
        }
        catch(Exception e)
        {
            System.out.println("Oops: " + e);
        }
    }
    public void tableChanged(TableModelEvent event)
    {
        try
        {
            
        }
        catch(Exception e)
        {
            System.out.println("Oops: " + e);
        }
    }


    public void updateComboBox(JComboBox dropdown)
    {
        DefaultComboBoxModel model = (DefaultComboBoxModel) dropdown.getModel();
        model.removeAllElements();

        String[] list = {"Boop"};

        for (String s : list)
        {
            if (s.contains(".bin") == true)
            {
                model.addElement(s);
            }
        }

        dropdown.setModel(model);
        this.repaint();
    }

    public void add_hb()
    {
        HitboxElement hb = new HitboxElement();
        hb.initGUI(this.type_dropdown.getSelectedItem().toString(), this.level_id_list);
        //System.out.println(this.type_dropdown.getSelectedItem().toString());
        hb.place(15, 5, this.hblist.size());
        hb.update(this.hblist.size());
        hb.connect2(this);

        this.hblist.add(hb);
    }
    public void add_hb(HitboxElement hb)
    {
        hb.initGUI(this.type_dropdown.getSelectedItem().toString(), this.level_id_list);
        hb.place(15, 5, this.hblist.size());
        hb.update(this.hblist.size());
        hb.connect2(this);

        this.hblist.add(hb);
    }

    public void clearList()
    {
        for (HitboxElement hb : this.hblist)
        {
            hb.disconnectFrom(this);
        }

        this.hblist.clear();
        this.relocateDropDown();
    }

    public void relocateDropDown()
    {
        int x_dropdown = (int)this.type_dropdown.getLocation().getX();
        int w_dropdown = (int)this.type_dropdown.getWidth();
        int x_add_b = (int)this.add_b.getLocation().getX();
        int w_add_b = (int)this.add_b.getWidth();
        int x_asm_b = (int)this.asm_b.getLocation().getX();
        int w_asm_b = (int)this.asm_b.getWidth();
        //int y = (int)this.add_b.getLocation().getY();
        int h = (int)this.add_b.getHeight();

        int y = 15;
        int element_h = 180;
        this.type_dropdown.setBounds(x_dropdown, y+(element_h*this.hblist.size()), w_dropdown, h);
        this.add_b.setBounds(x_add_b, y+(element_h*this.hblist.size()), w_add_b, h);
        this.asm_b.setBounds(x_asm_b, y+(element_h*this.hblist.size()), w_asm_b, h);
        this.load_b.setBounds(x_dropdown, y+25+(element_h*this.hblist.size()), w_dropdown, h);

        this.setSize(300,110+element_h*this.hblist.size());
    }

    public void init_level_id_list()
    {
            level_id_list[0] = " SM - Spiral Mountain";
            level_id_list[1] = " MM - Mumbo's Mountain";
            level_id_list[2] = " !Unknown 0x03";
            level_id_list[3] = " !Unknown 0x04";
            level_id_list[4] = " TTC - Blubber's Ship";
            level_id_list[5] = " TTC - Nipper's Shell";
            level_id_list[6] = " TTC - Treasure Trove Cove";
            level_id_list[7] = " !Unknown 0x08";
            level_id_list[8] = " !Unknown 0x09";
            level_id_list[9] = " TTC - Sandcastle";
            level_id_list[10] = " CC - Clanker's Cavern";
            level_id_list[11] = " MM - Ticker's Tower";
            level_id_list[12] = " BGS - Bubblegloop Swamp";
            level_id_list[13] = " Mumbo's Skull (MM)";
            level_id_list[14] = " !Unknown 0x0F";
            level_id_list[15] = " BGS - Mr. Vile";
            level_id_list[16] = " BGS - Tiptup";
            level_id_list[17] = " GV - Gobi's Valley";
            level_id_list[18] = " GV - Matching Game";
            level_id_list[19] = " GV - Maze";
            level_id_list[20] = " GV - Water";
            level_id_list[21] = " GV - Rubee's Chamber";
            level_id_list[22] = " !Unknown 0x17";
            level_id_list[23] = " !Unknown 0x18";
            level_id_list[24] = " !Unknown 0x19";
            level_id_list[25] = " GV - Sphinx";
            level_id_list[26] = " MMM - Mad Monster Mansion";
            level_id_list[27] = " MMM - Church";
            level_id_list[28] = " MMM - Cellar";
            level_id_list[29] = " Start - Nintendo";
            level_id_list[30] = " Start - Rareware";
            level_id_list[31] = " End Scene 2: Not 100";
            level_id_list[32] = " CC - Witch Switch Room";
            level_id_list[33] = " CC - Inside Clanker";
            level_id_list[34] = " CC - Gold Feather Room";
            level_id_list[35] = " MMM - Tumblar's Shed";
            level_id_list[36] = " MMM - Well";
            level_id_list[37] = " MMM - Dining Room (Napper)";
            level_id_list[38] = " FP - Freezeezy Peak";
            level_id_list[39] = " MMM - Room 1";
            level_id_list[40] = " MMM - Room 2";
            level_id_list[41] = " MMM - Room 3: Fireplace";
            level_id_list[42] = " MMM - Church (Secret Room)";
            level_id_list[43] = " MMM - Room 4: Bathroom";
            level_id_list[44] = " MMM - Room 5: Bedroom";
            level_id_list[45] = " MMM - Room 6: Floorboards";
            level_id_list[46] = " MMM - Barrel";
            level_id_list[47] = " Mumbo's Skull (MMM)";
            level_id_list[48] = " RBB - Rusty Bucket Bay";
            level_id_list[49] = " !Unknown 0x32";
            level_id_list[50] = " !Unknown 0x33";
            level_id_list[51] = " RBB - Engine Room";
            level_id_list[52] = " RBB - Warehouse 1";
            level_id_list[53] = " RBB - Warehouse 2";
            level_id_list[54] = " RBB - Container 1";
            level_id_list[55] = " RBB - Container 3";
            level_id_list[56] = " RBB - Crew Cabin";
            level_id_list[57] = " RBB - Boss Boom Box";
            level_id_list[58] = " RBB - Store Room";
            level_id_list[59] = " RBB - Kitchen";
            level_id_list[60] = " RBB - Navigation Room";
            level_id_list[61] = " RBB - Container 2";
            level_id_list[62] = " RBB - Captain's Cabin";
            level_id_list[63] = " CCW - Start";
            level_id_list[64] = " FP - Boggy's Igloo";
            level_id_list[65] = " !Unknown 0x42";
            level_id_list[66] = " CCW - Spring";
            level_id_list[67] = " CCW - Summer";
            level_id_list[68] = " CCW - Autumn";
            level_id_list[69] = " CCW - Winter";
            level_id_list[70] = " Mumbo's Skull (BGS)";
            level_id_list[71] = " Mumbo's Skull (FP)";
            level_id_list[72] = " !Unknown 0x49";
            level_id_list[73] = " Mumbo's Skull (CCW Spring)";
            level_id_list[74] = " Mumbo's Skull (CCW Summer)";
            level_id_list[75] = " Mumbo's Skull (CCW Autumn)";
            level_id_list[76] = " Mumbo's Skull (CCW Winter)";
            level_id_list[77] = " !Unknown 0x4E";
            level_id_list[78] = " !Unknown 0x4F";
            level_id_list[79] = " !Unknown 0x50";
            level_id_list[80] = " !Unknown 0x51";
            level_id_list[81] = " !Unknown 0x52";
            level_id_list[82] = " FP - Inside Xmas Tree";
            level_id_list[83] = " !Unknown 0x54";
            level_id_list[84] = " !Unknown 0x55";
            level_id_list[85] = " !Unknown 0x56";
            level_id_list[86] = " !Unknown 0x57";
            level_id_list[87] = " !Unknown 0x58";
            level_id_list[88] = " !Unknown 0x59";
            level_id_list[89] = " CCW - Zubba's Hive (Summer)";
            level_id_list[90] = " CCW - Zubba's Hive (Spring)";
            level_id_list[91] = " CCW - Zubba's Hive (Autumn)";
            level_id_list[92] = " !Unknown 0x5D";
            level_id_list[93] = " CCW - Nabnut's House (Spring)";
            level_id_list[94] = " CCW - Nabnut's House (Summer)";
            level_id_list[95] = " CCW - Nabnut's House (Autumn)";
            level_id_list[96] = " CCW - Nabnut's House (Winter)";
            level_id_list[97] = " CCW - Nabnut's Room 1 (Winter)";
            level_id_list[98] = " CCW - Nabnut's Room 2 (Autumn)";
            level_id_list[99] = " CCW - Nabnut's Room 2 (Winter)";
            level_id_list[100] = " CCW - Top (Spring)";
            level_id_list[101] = " CCW - Top (Summer)";
            level_id_list[102] = " CCW - Top (Autumn)";
            level_id_list[103] = " CCW - Top (Winter)";
            level_id_list[104] = " Lair - MM Lobby";
            level_id_list[105] = " Lair - TTC/CC Puzzle";
            level_id_list[106] = " Lair - CCW Puzzle & 180 Note Door";
            level_id_list[107] = " Lair - Red Cauldron Room";
            level_id_list[108] = " LairLair - GV Lobby - TTC Lobby";
            level_id_list[109] = " Lair - FP Lobby";
            level_id_list[110] = " Lair - CC Lobby";
            level_id_list[111] = " Lair - Statue";
            level_id_list[112] = " Lair - BGS Lobby";
            level_id_list[113] = " !Unknown 0x73";
            level_id_list[114] = " Lair - GV Puzzle";
            level_id_list[115] = " Lair - MMM Lobby";
            level_id_list[116] = " Lair - 640 Note Door Room";
            level_id_list[117] = " Lair - RBB Lobby";
            level_id_list[118] = " Lair - RBB Puzzle";
            level_id_list[119] = " Lair - CCW Lobby";
            level_id_list[120] = " Lair - Floor 2 Area 5a: Crypt inside";
            level_id_list[121] = " Intro - Lair 1 - Scene 1";
            level_id_list[122] = " Intro - Banjo's House 1 - Scenes 37";
            level_id_list[123] = " Intro - Spiral 'A' - Scenes 24";
            level_id_list[124] = " Intro - Spiral 'B' - Scenes 56";
            level_id_list[125] = " FP - Wozza's Cave";
            level_id_list[126] = " Lair - Floor 3 Area 4a";
            level_id_list[127] = " Intro - Lair 2";
            level_id_list[128] = " Intro - Lair 3 - Machine 1";
            level_id_list[129] = " Intro - Lair 4 - Game Over";
            level_id_list[130] = " Intro - Lair 5";
            level_id_list[131] = " Intro - Spiral 'C'";
            level_id_list[132] = " Intro - Spiral 'D'";
            level_id_list[133] = " Intro - Spiral 'E'";
            level_id_list[134] = " Intro - Spiral 'F'";
            level_id_list[135] = " Intro - Banjo's House 2";
            level_id_list[136] = " Intro - Banjo's House 3";
            level_id_list[137] = " RBB - Anchor room";
            level_id_list[138] = " SM - Banjo's House";
            level_id_list[139] = " MMM - Inside Loggo";
            level_id_list[140] = " Lair - Furnace Fun";
            level_id_list[141] = " TTC - Sharkfood Island";
            level_id_list[142] = " Lair - Battlements";
            level_id_list[143] = " File Select Screen";
            level_id_list[144] = " GV - Secret Chamber";
            level_id_list[145] = " Lair - Dingpot";
            level_id_list[146] = " Intro - Spiral 'G'";
            level_id_list[147] = " End Scene 3: All 100";
            level_id_list[148] = " End Scene";
            level_id_list[149] = " End Scene 4";
            level_id_list[150] = " Intro - Grunty Threat 1";
            level_id_list[151] = " Intro - Grunty Threat 2";
    }
    
    public static void main(String[] args)
	{
        GUI gui = new GUI();
        return;
    }
}


	