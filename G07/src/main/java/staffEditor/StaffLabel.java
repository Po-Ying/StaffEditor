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
    String labelName; // 为每个标签添加唯一标识符

    StaffLabel(String text, int horizontalAlignment, StaffPage p, String labelName){
        parent = p;
        this.labelName = labelName; // 设置标签的唯一标识符
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
                    updateText(labelName, ren);
                    parent.labelsData.put(labelName, ren);  // 使用标签的标识符来更新文本
                    System.out.println(parent.labelsData);
                    parent.panel.revalidate();  // 强制重新布局
                    parent.panel.repaint();
                }
                }
        });

    }
    
    // 更新文本的方法，這將在渲染時用來同步 labelsData
    public void updateText(String labelName, String updatedText) {
        if (updatedText != null && !updatedText.equals(getText())) {
            setText(updatedText);  // 更新標籤的文本
            repaint();  // 強制重繪該標籤
        }
    }
    
}
