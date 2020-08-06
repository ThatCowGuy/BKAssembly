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

public class SaveScreen extends JDialog implements ActionListener
{
    public JLabel prompt;
    public JTextField name;
    public JButton confirm;
    public JButton cancel;

    public GUI owner;

    public SaveScreen(String suggestion, GUI owner)
    {
        super(owner, true);
        String title = String.format(Locale.ROOT, " Saving File");
        this.setTitle(title);
        this.owner = owner;

        int x_off;
        int y_off;

        // create the input fields
        x_off = 15;
        y_off = 10;
        //========================
        this.prompt = new JLabel("Save as:");
        this.prompt.setBounds(x_off, y_off, 60, 20);
        this.add(this.prompt);
        //========================
        String finished = suggestion;
        this.name = new JTextField(finished);
        this.name.setBounds(x_off+60, y_off, 200, 20);
        this.add(this.name);
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
                String filename = this.name.getText();
                if (filename.contains(".asm") == false)
                    filename += ".asm";

                this.owner.bkasm.assemble(this.owner.hblist, filename);
                this.dispose();
            }
            else if ( event.getSource() == this.cancel )
            {
                this.dispose();
            }
        }
        catch (Exception e)
        {
            System.out.print("Oops: " + e);
        }
    }

    public void dealloc()
    {
        this.dispose();
    }
}