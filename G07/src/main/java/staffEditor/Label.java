package staffEditor;

import javax.swing.*;
import javax.swing.plaf.metal.MetalButtonUI;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Cursor;
import java.net.URL;

public class Label extends JLabel {

    StaffPage parent;

    public Label(String text, int horizontalAlignment, StaffPage p){
        parent = p;
        this.setText(text);
        this.setHorizontalAlignment(horizontalAlignment);

        this.setVisible(true);




        this.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e){

                if(parent.parent.parent.toolbar.topToolbar.inputtype==inputType.Cursor) {

                    //msLabel.this.setEnabled(true);

                    Cursor c = new Cursor(Cursor.HAND_CURSOR);
                    Label.this.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
                    Label.this.setCursor(c);
                }//else {
                  //  msLabel.this.setEnabled(false);

               // }

            }
            public void mouseExited(MouseEvent e){
                if(parent.parent.parent.toolbar.topToolbar.inputtype==inputType.Cursor) {
                    Label.this.setBorder(null);
                }

            }
            public void mouseClicked(MouseEvent e) {
                if (parent.parent.parent.toolbar.topToolbar.inputtype == inputType.Cursor) {
                    String ren = JOptionPane.showInputDialog("輸入預更改之文字");

                    if (ren != null)
                        Label.this.setText(ren);
                }
                }
        });

    }



}
