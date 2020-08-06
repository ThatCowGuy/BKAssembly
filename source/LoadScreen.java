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

public class LoadScreen extends JDialog implements ActionListener
{
    public JLabel prompt;
    public JComboBox filename_dropdown;
    public JButton confirm;
    public JButton cancel;

    public GUI owner;

    public LoadScreen(GUI owner)
    {
        super(owner, true);
        String title = String.format(Locale.ROOT, " Loading File");
        this.setTitle(title);
        this.owner = owner;

        int x_off;
        int y_off;

        // create the input fields
        x_off = 15;
        y_off = 10;
        //========================
        this.prompt = new JLabel("Load :");
        this.prompt.setBounds(x_off, y_off, 35, 20);
        this.add(this.prompt);
        //========================
        String[] placeholder = {"Placeholder"};
        this.filename_dropdown = new JComboBox(placeholder);
        this.updateComboBox();
        this.filename_dropdown.setBounds(x_off+40, y_off, 200, 20);
        this.filename_dropdown.setSelectedIndex(0);
        this.filename_dropdown.addActionListener(this);
        this.add(this.filename_dropdown);
        //========================
        
        x_off = 15;
        y_off = 40;
        //========================
        this.confirm = new JButton("Confirm");
        this.confirm.setBounds(x_off, y_off, 80, 20);
        this.confirm.addActionListener(this);
        this.add(this.confirm);
        //========================
        this.cancel = new JButton("Cancel");
        this.cancel.setBounds(x_off+90, y_off, 80, 20);
        this.cancel.addActionListener(this);
        this.add(this.cancel);
        //========================

        this.setSize(320,120);
        this.setLayout(null);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent event)
    {
        try
        {
            if ( event.getSource() == this.confirm )
            {
                String filename = "" + (String)this.filename_dropdown.getSelectedItem() + ".data";
                this.owner.clearList();
                for (HitboxElement hb : this.owner.bkasm.constructFromFile(filename))
                {
                    this.owner.add_hb(hb);
                }
                this.dispose();
            }
            else if ( event.getSource() == this.cancel )
            {
                this.dispose();
            }
        }
        catch (Exception e)
        {
            System.out.print("Oops (actionPerf. LoadScr): " + e);
        }
    }

    public void updateComboBox()
    {
        DefaultComboBoxModel model = (DefaultComboBoxModel) this.filename_dropdown.getModel();
        model.removeAllElements();

        File folder = new File(".");
        String[] filelist = folder.list();

        for (String s : filelist)
        {
            if (s.contains(".data") == true)
            {
                model.addElement(s.replace(".data", ""));
            }
        }

        this.filename_dropdown.setModel(model);
        this.repaint();
    }

    public void dealloc()
    {
        this.dispose();
    }
}