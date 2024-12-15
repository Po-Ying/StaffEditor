package staffEditor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.Iterator;

public class forwardButton extends JButton{

    ClassLoader cldr ;
    URL imageURL;
    public ImageIcon icon ;
    StaffPage parent;

    forwardButton(StaffPage p){

        parent = p;

        //隱藏按鈕外誆

        this.setBorderPainted(false);
        this.setBorder(null);
        this.setFocusable(false);
        cldr = this.getClass().getClassLoader();
        imageURL   = cldr.getResource("images/arrow.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);

        this.setToolTipText("返回復原前");
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
                if(parent.trash_notes.size()!=0||parent.getTrashTupletLines().size()!=0)
                    forwardButton.this.setVisible(true);
            }
        });

    }
    public void doSomething() {
        // 確保當前模式是 Cursor 模式
        if ((parent.parent.parent.toolbar.inputtype == inputType.Cursor)) {
            // 確保垃圾桶中有內容（音符或符槓）
            if (parent.trash_notes.size() > 0 || parent.getTrashTupletLines().size() > 0) {
                parent.back.setVisible(true); // 顯示 "返回上一步" 按鈕
                boolean restoredLine = false; // 標記是否復原了符槓

                // 嘗試復原符槓
                if (parent.getTrashTupletLines().size() > 0) {
                    Iterator<TupletLine> iterator = parent.getTrashTupletLines().iterator();
                    while (iterator.hasNext()) {
                        TupletLine line = iterator.next();

                        // 檢查符槓的音符是否已經復原
                        boolean note1Restored = parent.notes.contains(line.getNote1());
                        boolean note2Restored = parent.notes.contains(line.getNote2());

                        if (note1Restored && note2Restored) {
                            // 當符槓的兩個音符都已經復原，則可以復原符槓
                            parent.tupletLines.add(line); // 將符槓從垃圾桶移回主線條容器
                            iterator.remove(); // 安全地從垃圾桶中移除符槓
                            restoredLine = true; // 設定符槓復原標記
                            break; // 只復原一條符槓
                        }
                    }
                }

                // 如果沒有復原符槓，則嘗試復原音符
                if (!restoredLine && parent.trash_notes.size() > 0) {
                    JLabel lastNote = parent.trash_notes.lastElement(); // 取得最後一個音符
                    parent.notes.add(lastNote); // 將音符添加回主音符列表
                    parent.panel.add(lastNote); // 將音符添加到面板
                    parent.trash_notes.remove(lastNote); // 從垃圾桶中移除音符
                }

                // 重繪面板以顯示最新變化
                parent.panel.repaint();

                // 如果垃圾桶清空，隱藏 "復原" 按鈕
                if (parent.trash_notes.size() == 0 && parent.getTrashTupletLines().size() == 0) {
                    this.setVisible(false);
                }
            }
        }
    }


}
