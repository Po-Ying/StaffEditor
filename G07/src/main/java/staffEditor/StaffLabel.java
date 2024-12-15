package staffEditor;

import javax.swing.*;
import javax.swing.plaf.metal.MetalButtonUI;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Cursor;
import java.net.URL;
import java.util.Map;

public class StaffLabel extends JLabel {

    StaffPage parent;
    String labelName; // 給要能改文本的添加名字

    StaffLabel(String text, int horizontalAlignment, StaffPage p, String labelName){
        parent = p;
        this.labelName = labelName; 
        this.setText(text);
        this.repaint();
        this.setHorizontalAlignment(horizontalAlignment);

        this.setVisible(true);

        this.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e){

                if(parent.parent.parent.toolbar.inputtype==inputType.Cursor) {

                    //msLabel.this.setEnabled(true);

                    Cursor c = new Cursor(Cursor.HAND_CURSOR);
                    StaffLabel.this.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
                    StaffLabel.this.setCursor(c);
                }//else {
                  //  msLabel.this.setEnabled(false);

               // }

            }
            public void mouseExited(MouseEvent e){
                if(parent.parent.parent.toolbar.inputtype==inputType.Cursor) {
                    StaffLabel.this.setBorder(null);
                }

            }
            public void mouseClicked(MouseEvent e) {
                if (parent.parent.parent.toolbar.inputtype == inputType.Cursor) {
                    String ren = JOptionPane.showInputDialog("輸入預更改之文字");

                    if (ren != null)
                    {
                    	StaffLabel.this.setText(ren);
                    }
                    // 在 StaffPage 中同步更新文本
                    parent.labelsData.put(labelName, ren);  // 使用标签的标识符来更新文本
                    parent.panel.revalidate();  
                    parent.panel.repaint();
                }
                }
        });
    }
}
