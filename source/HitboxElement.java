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

public class HitboxElement
{
    public int x;
    public int y;

    public String       type;
    public JLabel       type_label;
    public JComboBox    level_id_box;
    public Hex          level_id;
    public Hex          data_size;
    public Hex          type_value;
    public Hex          special_size;

    public Hex x1;
    public Hex x2;
    public Hex y1;
    public Hex y2;
    public Hex z1;
    public Hex z2;
    public JLabel       x1_label;
    public JTextField   x1_textf;
    public JLabel       x2_label;
    public JTextField   x2_textf;
    public JLabel       y1_label;
    public JTextField   y1_textf;
    public JLabel       y2_label;
    public JTextField   y2_textf;
    public JLabel       z1_label;
    public JTextField   z1_textf;
    public JLabel       z2_label;
    public JTextField   z2_textf;

    public Hex v1;
    public Hex v2;
    public Hex v3;
    public Hex v4;
    public JLabel       v1_label;
    public JTextField   v1_textf;
    public JLabel       v2_label;
    public JTextField   v2_textf;
    public JLabel       v3_label;
    public JTextField   v3_textf;
    public JLabel       v4_label;
    public JTextField   v4_textf;

    public JButton remove_b;

    public boolean value_init = false; 
    public boolean component_init = false; 

    public int index;

    // constructors
    public HitboxElement(int id, int x1, int x2, int y1, int y2, int z1, int z2, int v1, int v2, int v3, int v4)
    {
        this.level_id = new Hex(id, 2);
        this.x1 = new Hex(x1, 4);
        this.x2 = new Hex(x2, 4);
        this.y1 = new Hex(y1, 4);
        this.y2 = new Hex(y2, 4);
        this.z1 = new Hex(z1, 4);
        this.z2 = new Hex(z2, 4);
        this.v1 = new Hex(v1, 4);
        this.v2 = new Hex(v2, 4);
        this.v3 = new Hex(v3, 4);
        this.v4 = new Hex(v4, 4);
        this.value_init = true;
    }
    public HitboxElement(String id, String x1, String x2, String y1, String y2, String z1, String z2, String v1, String v2, String v3, String v4)
    {
        this.level_id = new Hex(id);
        this.x1 = new Hex(x1);
        this.x2 = new Hex(x2);
        this.y1 = new Hex(y1);
        this.y2 = new Hex(y2);
        this.z1 = new Hex(z1);
        this.z2 = new Hex(z2);
        this.v1 = new Hex(v1);
        this.v2 = new Hex(v2);
        this.v3 = new Hex(v3);
        this.v4 = new Hex(v4);
        this.value_init = true;
    }
    public HitboxElement(java.util.List<Hex> hexlist)
    {
        this.level_id = hexlist.get(0);
        this.x1 = hexlist.get(0);
        this.x2 = hexlist.get(1);
        this.y1 = hexlist.get(2);
        this.y2 = hexlist.get(3);
        this.z1 = hexlist.get(4);
        this.z2 = hexlist.get(5);
        this.v1 = hexlist.get(6);
        this.v2 = hexlist.get(7);
        this.v3 = hexlist.get(8);
        this.v4 = hexlist.get(9);
        this.value_init = true;
    }
    public HitboxElement()
    {
        this.level_id = new Hex("01");
        this.x1 = new Hex("0000");
        this.x2 = new Hex("0000");
        this.y1 = new Hex("0000");
        this.y2 = new Hex("0000");
        this.z1 = new Hex("0000");
        this.z2 = new Hex("0000");
        this.v1 = new Hex("0000");
        this.v2 = new Hex("0000");
        this.v3 = new Hex("0000");
        this.v4 = new Hex("0000");
        this.value_init = true;
    }

    public void update(int index)
    {
        if (this.component_init == false)
            return;
            
        this.type_label.setText(this.type + " Hitbox");

        this.level_id = new Hex(this.level_id_box.getSelectedIndex()+1, 2);

        this.x1 = new Hex(Integer.parseInt(this.x1_textf.getText()), 4);
        this.x1_textf.setText(""+this.x1.complement);
        this.x2 = new Hex(Integer.parseInt(this.x2_textf.getText()), 4);
        this.x2_textf.setText(""+this.x2.complement);

        this.y1 = new Hex(Integer.parseInt(this.y1_textf.getText()), 4);
        this.y1_textf.setText(""+this.y1.complement);
        this.y2 = new Hex(Integer.parseInt(this.y2_textf.getText()), 4);
        this.y2_textf.setText(""+this.y2.complement);

        this.z1 = new Hex(Integer.parseInt(this.z1_textf.getText()), 4);
        this.z1_textf.setText(""+this.z1.complement);
        this.z2 = new Hex(Integer.parseInt(this.z2_textf.getText()), 4);
        this.z2_textf.setText(""+this.z2.complement);
        
        this.v1 = new Hex(Integer.parseInt(this.v1_textf.getText()), 4);
        this.v1_textf.setText(""+this.v1.complement);
        this.v2 = new Hex(Integer.parseInt(this.v2_textf.getText()), 4);
        this.v2_textf.setText(""+this.v2.complement);
        this.v3 = new Hex(Integer.parseInt(this.v3_textf.getText()), 4);
        this.v3_textf.setText(""+this.v3.complement);
        this.v4 = new Hex(Integer.parseInt(this.v4_textf.getText()), 4);
        this.v4_textf.setText(""+this.v4.complement);

        this.place(this.x, this.y, index);
    }

    public void initGUI(String type, String[] level_id_list)
    {
        if (this.value_init == false)
            return;

        this.type = type;
        this.type_label = new JLabel(this.type + " Hitbox");
        this.level_id_box = new JComboBox(level_id_list);
        this.level_id_box.setSelectedIndex((int)this.level_id.value-1);

        this.x1_label = new JLabel("x_lo :");
        this.x2_label = new JLabel("x_hi :");

        this.y1_label = new JLabel("y_lo :");
        this.y2_label = new JLabel("y_hi :");

        this.z1_label = new JLabel("z_lo :");
        this.z2_label = new JLabel("z_hi :");

        this.x1_textf = new JTextField("" + this.x1.complement);
        this.x2_textf = new JTextField("" + this.x2.complement);
        
        this.y1_textf = new JTextField("" + this.y1.complement);
        this.y2_textf = new JTextField("" + this.y2.complement);
        
        this.z1_textf = new JTextField("" + this.z1.complement);
        this.z2_textf = new JTextField("" + this.z2.complement);
        switch(this.type)
        {
            case("Wind"):
                this.data_size = new Hex(22, 2);
                this.type_value = new Hex(1, 2);
                this.special_size = new Hex(6, 2);
                this.v1_label = new JLabel("Wind x :");
                this.v2_label = new JLabel("Wind y :");
                this.v3_label = new JLabel("Wind z :");
                this.v4_label = new JLabel("- - -");
                break;
            
            default:
                this.data_size = new Hex(255, 2);
                this.type_value = new Hex(255, 2);
                this.special_size = new Hex(255, 2);
                this.v1_label = new JLabel("Inv.Type");
                this.v2_label = new JLabel("Inv.Type");
                this.v3_label = new JLabel("Inv.Type");
                this.v4_label = new JLabel("Inv.Type");
                break;
        }
        this.v1_textf = new JTextField("" + this.v1.complement);
        this.v2_textf = new JTextField("" + this.v2.complement);
        this.v3_textf = new JTextField("" + this.v3.complement);
        this.v4_textf = new JTextField("" + this.v4.complement);
        
        this.remove_b = new JButton("X");

        this.component_init = true;
    }
    public void place(int x, int y, int index)
    {
        if (this.value_init == false)
            return;

        this.index = index;
        this.x = x;
        this.y = y;

        int element_h = 180;

        int dy = 3;
        int dx = 5;

        int h = 20;
        int label_w = 45;
        int textf_w = 60;
        int button_w = 20;

        int x_off = 224;

        this.type_label.setBounds(x, y+index*element_h+0*(dy+h), label_w*2, h);

        this.level_id_box.setBounds(x-8, y+index*element_h+1*(dy+h)-2, label_w*5, h);
        this.remove_b.setBounds(x+x_off, y+index*element_h+1*(dy+h)-2, button_w, h);
        this.remove_b.setMargin(new Insets(1, 2, 3, 2)); // TLBR

        this.x1_label.setBounds(x,                      y+index*element_h+2*(dy+h),     label_w, h);
        this.x1_textf.setBounds(x+label_w+dx,           y+index*element_h+1+2*(dy+h),   textf_w, h);
        this.x2_label.setBounds(x+label_w*3,            y+index*element_h+2*(dy+h),     label_w, h);
        this.x2_textf.setBounds(x+label_w*3+label_w+dx, y+index*element_h+1+2*(dy+h),   textf_w, h);

        this.y1_label.setBounds(x,                      y+index*element_h+3*(dy+h),     label_w, h);
        this.y1_textf.setBounds(x+label_w+dx,           y+index*element_h+1+3*(dy+h),   textf_w, h);
        this.y2_label.setBounds(x+label_w*3,            y+index*element_h+3*(dy+h),     label_w, h);
        this.y2_textf.setBounds(x+label_w*3+label_w+dx, y+index*element_h+1+3*(dy+h),   textf_w, h);

        this.z1_label.setBounds(x,                      y+index*element_h+4*(dy+h),     label_w, h);
        this.z1_textf.setBounds(x+label_w+dx,           y+index*element_h+1+4*(dy+h),   textf_w, h);
        this.z2_label.setBounds(x+label_w*3,            y+index*element_h+4*(dy+h),     label_w, h);
        this.z2_textf.setBounds(x+label_w*3+label_w+dx, y+index*element_h+1+4*(dy+h),   textf_w, h);

        this.v1_label.setBounds(x,                      y+dy*2+index*element_h+5*(dy+h),  label_w, h);
        this.v1_textf.setBounds(x+label_w+dx,           y+dy*2+index*element_h+1+5*(dy+h),textf_w, h);
        this.v2_label.setBounds(x+label_w*3,            y+dy*2+index*element_h+5*(dy+h),  label_w, h);
        this.v2_textf.setBounds(x+label_w*3+label_w+dx, y+dy*2+index*element_h+1+5*(dy+h),textf_w, h);

        this.v3_label.setBounds(x,                      y+dy*2+index*element_h+6*(dy+h),  label_w, h);
        this.v3_textf.setBounds(x+label_w+dx,           y+dy*2+index*element_h+1+6*(dy+h),textf_w, h);
        this.v4_label.setBounds(x+label_w*3,            y+dy*2+index*element_h+6*(dy+h),  label_w, h);
        this.v4_textf.setBounds(x+label_w*3+label_w+dx, y+dy*2+index*element_h+1+6*(dy+h),textf_w, h);

        this.component_init = true;
    }

    public void connect2(GUI mainframe)
    {
        if (this.component_init == false)
            return;

        mainframe.add(this.type_label);

        this.level_id_box.addActionListener(mainframe);
        mainframe.add(this.level_id_box);

        mainframe.add(this.x1_label);
        mainframe.add(this.x2_label);
        mainframe.add(this.y1_label);
        mainframe.add(this.y2_label);
        mainframe.add(this.z1_label);
        mainframe.add(this.z2_label);
        mainframe.add(this.v1_label);
        mainframe.add(this.v2_label);
        mainframe.add(this.v3_label);
        mainframe.add(this.v4_label);

        this.x1_textf.addActionListener(mainframe);
        mainframe.add(this.x1_textf);
        this.x2_textf.addActionListener(mainframe);
        mainframe.add(this.x2_textf);
        this.y1_textf.addActionListener(mainframe);
        mainframe.add(this.y1_textf);
        this.y2_textf.addActionListener(mainframe);
        mainframe.add(this.y2_textf);
        this.z1_textf.addActionListener(mainframe);
        mainframe.add(this.z1_textf);
        this.z2_textf.addActionListener(mainframe);
        mainframe.add(this.z2_textf);
        this.v1_textf.addActionListener(mainframe);
        mainframe.add(this.v1_textf);
        this.v2_textf.addActionListener(mainframe);
        mainframe.add(this.v2_textf);
        this.v3_textf.addActionListener(mainframe);
        mainframe.add(this.v3_textf);
        this.v4_textf.addActionListener(mainframe);
        mainframe.add(this.v4_textf);

        this.remove_b.addActionListener(mainframe);
        mainframe.add(this.remove_b);
    }

    public void disconnectFrom(GUI mainframe)
    {
        mainframe.remove(this.type_label);

        mainframe.remove(this.level_id_box);

        mainframe.remove(this.x1_label);
        mainframe.remove(this.x2_label);
        mainframe.remove(this.y1_label);
        mainframe.remove(this.y2_label);
        mainframe.remove(this.z1_label);
        mainframe.remove(this.z2_label);
        mainframe.remove(this.v1_label);
        mainframe.remove(this.v2_label);
        mainframe.remove(this.v3_label);
        mainframe.remove(this.v4_label);

        mainframe.remove(this.x1_textf);
        mainframe.remove(this.x2_textf);
        mainframe.remove(this.y1_textf);
        mainframe.remove(this.y2_textf);
        mainframe.remove(this.z1_textf);
        mainframe.remove(this.z2_textf);
        mainframe.remove(this.v1_textf);
        mainframe.remove(this.v2_textf);
        mainframe.remove(this.v3_textf);
        mainframe.remove(this.v4_textf);

        mainframe.remove(this.remove_b);

        mainframe.revalidate();
        mainframe.repaint();
    }

    public String toString()
    {
        if (this.value_init == false)
            return "VALUES UNITIALIZED";

        String result = "";
        result += ".byte 0x" + this.level_id + "\n";
        result += ".byte 0x" + this.data_size + "\n";
        result += ".halfword 0x" + this.x1 + "\n";
        result += ".halfword 0x" + this.x2 + "\n";
        result += ".halfword 0x" + this.y1 + "\n";
        result += ".halfword 0x" + this.y2 + "\n";
        result += ".halfword 0x" + this.z1 + "\n";
        result += ".halfword 0x" + this.z2 + "\n";
        result += ".byte 0x" + this.type_value + "\n";
        result += ".byte 0x" + this.special_size + "\n";
        switch(this.type)
        {
            case("Wind"):
                    result += ".halfword 0x" + this.v1 + "\n";
                    result += ".halfword 0x" + this.v2 + "\n";
                    result += ".halfword 0x" + this.v3 + "\n";
                break;
            
            default:
                break;
        }

        return result;
    }
    public java.util.List<Hex> toData()
    {
        if (this.value_init == false)
            return null;

        java.util.List<Hex> hexlist = new ArrayList<Hex>();

        hexlist.add(this.level_id);
        hexlist.add(this.data_size);
        
        hexlist.add(this.x1);
        hexlist.add(this.x2);
        hexlist.add(this.y1);
        hexlist.add(this.y2);
        hexlist.add(this.z1);
        hexlist.add(this.z2);
        
        hexlist.add(this.type_value);
        hexlist.add(this.special_size);

        switch(this.type)
        {
            case("Wind"):
                    hexlist.add(this.v1);
                    hexlist.add(this.v2);
                    hexlist.add(this.v3);
                break;
            
            default:
                break;
        }

        return hexlist;
    }
}