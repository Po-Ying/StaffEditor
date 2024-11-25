package staffEditor;

import javax.swing.*;
import java.awt.Toolkit;
import java.awt.Cursor;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.event.ActionListener;
public class QuarterButton extends IconButton{

    Toolbar parent;
    ImageIcon imageIcon;
    public QuarterButton(Toolbar p) {
        super(p);
        parent = p;
        //this.setBorderPainted(false);
        //this.setBorder(null);

        imageURL = cldr.getResource("iamges/quarter_note.png");

        icon = new ImageIcon(imageURL);

        this.setIcon(icon);

        this.setToolTipText("四分音符");
    }

    public void doSomething(){
        Toolkit tk = Toolkit.getDefaultToolkit();
        if(parent.inputtype==inputType.Note) {
            imageURL = cldr.getResource("images/quarter_note.png");
            icon = new ImageIcon(imageURL);

            imageIcon = new ImageIcon(icon.getImage().getScaledInstance(25, 45, Image.SCALE_DEFAULT));
        }




        Cursor cu = tk.createCustomCursor(imageIcon.getImage(),new Point(16,16),"");
        for(int i=0;i<parent.parent.tabbedPane.getTabCount();i++) {
            parent.parent.tabbedPane.getComponentAt(i).setCursor(cu);


        }

        parent.longtype = longType.quarter;
    }

}
