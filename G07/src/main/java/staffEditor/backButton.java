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
                boolean lineRemoved = false; // 標記是否處理符槓

                // 優先檢查符槓
                if (parent.getTupletLines().size() > 0) {

                    Iterator<TupletLine> iterator = parent.getTupletLines().iterator();
                    while (iterator.hasNext()) {
                        TupletLine line = iterator.next();
                        // 如果符槓的音符都存在，則移除符槓並恢復音符樣式
                        if(parent.notes.lastElement()==line.getNote1()||parent.notes.lastElement()==line.getNote2())
                        {    
                        	if (parent.notes.contains(line.getNote1()) && parent.notes.contains(line.getNote2())) {
	                            System.out.println("找到符槓，音符：" + line.getNote1() + " 和 " + line.getNote2());
	
	                            // 將符槓移到垃圾桶
	                            parent.getTrashTupletLines().add(line);
	                            iterator.remove(); // 從主符槓列表移除該符槓
	                            lineRemoved = true;
	                            // 恢復音符圖片
	                            if(line.getType()==1)//得到八分音符時
	                            {
		                            parent.updateNoteImage(line.getNote1(), "eighth", "eighth");
		                            parent.updateNoteImage(line.getNote2(), "eighth", "eighth");
	                            }
	                            else if(line.getType()==2)
	                            {
		                            parent.updateNoteImage(line.getNote1(), "sixteenth", "sixteenth");
		                            parent.updateNoteImage(line.getNote2(), "sixteenth", "sixteenth");
	                            }
	                
	                            lineRemoved = true; // 設置符槓已被移除
	                            break; // 處理一條符槓後退出
	                        }
                    	}
                    }
                }

                // 如果沒有符槓需要處理，則嘗試移除音符
                if (!lineRemoved && parent.notes.size() > 0) {
                    JLabel lastNote = parent.notes.lastElement();
                    parent.panel.remove(lastNote);
                    parent.trash_notes.add(lastNote);
                    parent.notes.remove(lastNote);
                }

                // 更新畫面
                System.out.println("重繪五線譜...");
                parent.panel.repaint();

                // 如果垃圾桶清空，隱藏按鈕
                if (parent.notes.size() == 0 && parent.getTrashTupletLines().size() == 0) {
                    System.out.println("垃圾桶清空，隱藏返回按鈕");
                    this.setVisible(false);
                }
            }
        }
    }

}
