package staffEditor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.Iterator;

public class backButton extends JButton{

    StaffPage parent;

    ClassLoader cldr ;
    URL imageURL;
    public ImageIcon icon ;

    backButton(StaffPage p){

        this.setFocusable(false);
        parent =p;

        //隱藏按鈕外誆

        this.setBorderPainted(false);
        this.setBorder(null);
        cldr = this.getClass().getClassLoader();
        imageURL  = cldr.getResource("images/back.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);

        this.setToolTipText("返回上一步");
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doSomething();
            }
        });
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                if(parent.notes.size()!=0)
                    backButton.this.setVisible(true);
            }
        });

    }
    public void doSomething() {
        if ((parent.parent.parent.toolbar.inputtype == inputType.Cursor)) {
            if (parent.notes.size() > 0 || parent.getTupletLines().size() > 0) {
                parent.forward.setVisible(true);
                boolean py = false;

                // 取得最後一個音符
                JLabel lastNote = parent.notes.lastElement();

                // 檢查符槓是否與該音符相關
                Iterator<TupletLine> iterator = parent.getTupletLines().iterator();
                while (iterator.hasNext()) {
                    TupletLine line = iterator.next();
                    if (line.getNote1() == lastNote || line.getNote2() == lastNote) {
                        // 將符槓移到垃圾桶
                        parent.trash_lines.add(line);
                        iterator.remove(); // 安全地從符槓列表移除
                        
                        // 恢復符槓相關音符的原始樣式
                        restoreOriginalNoteImage(line.getNote1());
                        restoreOriginalNoteImage(line.getNote2());

                        py = true;
                    }
                }

                // 如果沒有符槓，則移除音符
                if (!py) {
                    parent.panel.remove(lastNote);
                    parent.trash_notes.add(lastNote);
                    parent.notes.remove(lastNote);
                }

                // 重繪五線譜
                parent.panel.repaint();

                // 如果音符和符槓都被移除完畢，隱藏按鈕
                if (parent.notes.size() == 0 && parent.getTupletLines().size() == 0) {
                    this.setVisible(false);
                }
            }
        }
    }

    private void restoreOriginalNoteImage(JLabel note) {
        String originalDuration = (String) note.getClientProperty("noteDuration");
        System.out.println(originalDuration);
        if (originalDuration != null) {
            String imagePath = null;
            switch (originalDuration) {
                case "eighth":
                    imagePath = "images/eighth_note.png";
                    break;
                case "sixteenth":
                    imagePath = "images/sixteenth_note.png";
                    break;
                default:
                    return; // 如果不是八分或十六分音符，不做任何修改
            }

            // 更新音符的圖片和時值
            note.setIcon(new ImageIcon(imagePath));
            note.putClientProperty("noteDuration", originalDuration);
        }
    }


}
