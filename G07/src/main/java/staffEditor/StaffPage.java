package staffEditor;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import javax.swing.*;
import java.awt.Font;
import java.awt.FontFormatException;

public class StaffPage extends JScrollPane {
    public TabbedPane parent;

    public JLabel note;
    public final Vector<JLabel> notes = null;
    public final Vector<JLabel> trash_note = null;
    //記憶體會在建構子被呼叫時才分配
    // public final ArrayList<JLabel> notes = new Vector<JLabel>();
    //在類別載入時就分配記憶體
    //不推薦
    
    
    public ClassLoader cldr;
    public URL imageURL;
    public ImageIcon icon ,imageIcon;
    // public String currentNoteType = "Quarter";

    public static int count = 0;
    public int id;

    public JButton backButton, forwardButton;
    public JComponent panel;

    // 构造函数初始化面板
    public StaffPage(TabbedPane p) {

        parent = p;
        count++;
        id = count;
        notes = new Vector<JLabel>() ;
        trash_notes = new Vector<JLabel>();
        
        initPanel();
        initButtons();
        initMouseListeners();
        
        this.getVerticalScrollBar().setUnitIncrement(10);
    }

    // 初始化面板
    public void initPanel() {
        panel = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                drawStaff(g);

                // super.paintComponent(g);
                // for( Note note : q_notes) {
                //     note.draw(g);
                // }
                // for( Note note : h_notes){
                //     note.draw(g);
                // }
            }
        };
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(0, 1400));
        this.setViewportView(panel);
    }

    // 绘制五线谱
    public void drawStaff(Graphics g) {
        int offset = 0;
        int[] measurePositions = {370, 600, 830, 1050}; 
        g.setColor(Color.BLACK);

        // 加载 Bravura 字体，用于低音谱号
        Font bassClefFont = loadBassClefFont();
        
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 5; j++) {
                g.drawLine(100, 155 + j * 10 + offset, 1050, 155 + j * 10 + offset);
            }

            if (i % 2 == 0) {
                g.setFont(new Font("Default", Font.PLAIN, 90)); 
                g.drawString("\uD834\uDD1E", 110, 202 + offset); 
            } else {
                if (bassClefFont != null) {
                    g.setFont(bassClefFont); 
                }
                g.drawString("\uD834\uDD22", 125, 175 + offset); 
            }

            for (int pos : measurePositions) {
                g.drawLine(pos, 155 + offset, pos, 195 + offset);
            }

            offset += 125;
        }
    }

    // 加载 Bravura 字体
    public Font loadBassClefFont() {
        try (InputStream is = getClass().getResourceAsStream("resources/fonts/Bravura.otf")) {
            if (is != null) {
                return Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(50f);
            } else {
                System.out.println("字体文件未找到");
                return null;
            }
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 初始化按钮
    public void initButtons() {
        backButton = new JButton("←");
        forwardButton = new JButton("→");

        backButton.setBounds(20, 20, 45, 45);
        forwardButton.setBounds(70, 20, 45, 45);

        backButton.setVisible(false);
        forwardButton.setVisible(false);

        panel.add(backButton);
        panel.add(forwardButton);
    }

    // 初始化鼠标监听器
    public void initMouseListeners() {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cldr = this.getClass().getClassLoader();
                if((parent.parent.toolbar.topToolbar.inputtype==inputType.Note)&&(parent.parent.toolbar.topToolbar.longtype!=longType.non)) {
                    if(parent.parent.toolbar.topToolbar.longtype==longType.quarter){
                        imageURL = cldr.getResource("images/quarter_note.png");
                        icon = new ImageIcon(imageURL);
                        imageIcon = new ImageIcon(icon.getImage().getScaledInstance(25, 45, Image.SCALE_DEFAULT));

                    }
                    else if(parent.parent.toolbar.topToolbar.longtype==longType.eighth){
                        imageURL = cldr.getResource("images/eighth_note.png");
                        icon = new ImageIcon(imageURL);
                        imageIcon = new ImageIcon(icon.getImage().getScaledInstance(30, 45, Image.SCALE_DEFAULT));
                    }
                    else if(parent.parent.toolbar.topToolbar.longtype==longType.sixteenth){
                        imageURL = cldr.getResource("images/sixteenth_note.png");
                        icon = new ImageIcon(imageURL);
                        imageIcon = new ImageIcon(icon.getImage().getScaledInstance(30, 45, Image.SCALE_DEFAULT));
                    }
                    else if(parent.parent.toolbar.topToolbar.longtype==longType.half){
                        imageURL = cldr.getResource("images/half_note.png");
                        icon = new ImageIcon(imageURL);
                        imageIcon = new ImageIcon(icon.getImage().getScaledInstance(30, 45, Image.SCALE_DEFAULT));
                    }
                    else if(parent.parent.toolbar.topToolbar.longtype==longType.whole){
                        imageURL = cldr.getResource("images/whole.png");
                        icon = new ImageIcon(imageURL);
                        imageIcon = new ImageIcon(icon.getImage().getScaledInstance(80, 60, Image.SCALE_DEFAULT));
                    }

                }
                else if((parent.parent.toolbar.topToolbar.inputtype==inputType.rest)&&(parent.parent.toolbar.topToolbar.longtype!=longType.non)) {
                    if(parent.parent.toolbar.topToolbar.longtype==longType.quarter) {
                        imageURL = cldr.getResource("images/quarter-note-rest.png");
                        icon = new ImageIcon(imageURL);
                        imageIcon = new ImageIcon(icon.getImage().getScaledInstance(25, 28, Image.SCALE_DEFAULT));
                    }
                    else if(parent.parent.toolbar.topToolbar.longtype==longType.eighth){
                        imageURL = cldr.getResource("images/eighth-note-rest.png");
                        icon = new ImageIcon(imageURL);
                        imageIcon = new ImageIcon(icon.getImage().getScaledInstance(30, 35, Image.SCALE_DEFAULT));
                    }
                    else if(parent.parent.toolbar.topToolbar.longtype==longType.sixteenth){
                        imageURL = cldr.getResource("images/sixteenth_rest.png");
                        icon = new ImageIcon(imageURL);
                        imageIcon = new ImageIcon(icon.getImage().getScaledInstance(32, 35, Image.SCALE_DEFAULT));
                    }
                    else if(parent.parent.toolbar.topToolbar.longtype==longType.half){
                        imageURL = cldr.getResource("images/half-rest.png");
                        icon = new ImageIcon(imageURL);
                        imageIcon = new ImageIcon(icon.getImage().getScaledInstance(80, 60, Image.SCALE_DEFAULT));
                    }
                    else if(parent.parent.toolbar.topToolbar.longtype==longType.whole){
                        imageURL = cldr.getResource("images/whole_rest.png");
                        icon = new ImageIcon(imageURL);
                        imageIcon = new ImageIcon(icon.getImage().getScaledInstance(80, 60, Image.SCALE_DEFAULT));
                    }

                }

                if((parent.parent.toolbar.topToolbar.inputtype!=inputType.Cursor)&&(parent.parent.toolbar.topToolbar.longtype!=longType.non)) {


                    note = new JLabel(imageIcon);
                    if((parent.parent.toolbar.topToolbar.longtype==longType.whole)||((parent.parent.toolbar.topToolbar.longtype==longType.half)&&(parent.parent.toolbar.topToolbar.inputtype!=inputType.Note))){

                        note.setLocation(getMousePosition().x +7, getMousePosition().y - 10 + msStaffPage.this.getVerticalScrollBar().getValue());
                    }
                    else if(parent.parent.toolbar.topToolbar.longtype==longType.quarter){
                        if(parent.parent.toolbar.topToolbar.inputtype==inputType.Note)
                            note.setLocation(getMousePosition().x-21 , getMousePosition().y - 18 + msStaffPage.this.getVerticalScrollBar().getValue());
                        else
                            note.setLocation(getMousePosition().x-21 , getMousePosition().y - 26 + msStaffPage.this.getVerticalScrollBar().getValue());
                    }
                    else if(parent.parent.toolbar.topToolbar.longtype==longType.eighth){
                        if(parent.parent.toolbar.topToolbar.inputtype==inputType.Note)
                            note.setLocation(getMousePosition().x-18 , getMousePosition().y - 18 + msStaffPage.this.getVerticalScrollBar().getValue());
                        else
                            note.setLocation(getMousePosition().x-18 , getMousePosition().y - 23 + msStaffPage.this.getVerticalScrollBar().getValue());
                    }
                    else if(parent.parent.toolbar.topToolbar.longtype==longType.sixteenth){
                        if(parent.parent.toolbar.topToolbar.inputtype==inputType.Note)
                            note.setLocation(getMousePosition().x-18 , getMousePosition().y - 18 + msStaffPage.this.getVerticalScrollBar().getValue());
                        else
                            note.setLocation(getMousePosition().x-17 , getMousePosition().y - 23 + msStaffPage.this.getVerticalScrollBar().getValue());
                    }
                    else {
                        note.setLocation(getMousePosition().x - 18, getMousePosition().y - 18 + msStaffPage.this.getVerticalScrollBar().getValue());
                    }
                    note.setVisible(true);

                    back.setVisible(true);
                    note.setSize(30, 45);
                    notes.add(note);


                    panel.add(notes.lastElement());


                    panel.repaint();

                }
                    System.out.println("新增音符StaffPage的雜湊值: " + System.identityHashCode(this));
            } 

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                if(parent.parent.toolbar.editBar.inputtype==inputType.Cursor){
                    staffTitle.setEnabled(true);
                    authorTitle.setEnabled(true);
                    instrumentTitle.setEnabled(true);
                    pageCount.setEnabled(true);
                    for(int i=0;i<10;i++){
                        measure[i].setEnabled(true);
                    }

                }
                else {
                    staffTitle.setEnabled(false);
                    authorTitle.setEnabled(false);
                    instrumentTitle.setEnabled(false);
                    pageCount.setEnabled(false);
                    for(int i=0;i<10;i++){
                        measure[i].setEnabled(false);
                    }
                }
              if(notes.size()!=0)
                back.setVisible(true);
              if(trash_notes.size()!=0)
                forward.setVisible(true);

            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);

                  back.setVisible(false);

                  forward.setVisible(false);
            }
        });

        this.addMouseWheelListener(e -> {
            int offset = this.getVerticalScrollBar().getValue();
            backButton.setLocation(20, 20 + offset);
            forwardButton.setLocation(70, 20 + offset);
        });
    }

    //添加音符
    public void addNote(String noteType, int x, int y) {
        if (noteType == null) {
            System.out.println("Error: noteType is null");
            return;
        }

        Note newNote = new Note(noteType, x, y);
        notes.add(newNote);
        printNotes();

        JLabel noteLabel = new JLabel(noteType);
        noteLabel.setBounds(x, y, 50, 50);
        noteLabels.add(noteLabel);

        // System.out.println("Added note: " + newNote + ", notes.size: " + notes.size());
        // System.out.println("Adding note to StaffPage notes instance hash: " + System.identityHashCode(notes));

        this.revalidate();
        this.repaint();
    }
    public void printNotes() {
        if (q_notes.isEmpty() && h_notes.isEmpty()) {
            System.out.println("音符列表為空");
        } else {
            System.out.println("音符列表如下：");
            for (JLabel note : notes) {
                System.out.printf(
                    "音符類型: %s, 座標: (x=%d, y=%d), 哈希值: %d, <q_notes> size = %d%n",
                    note.noteType, note.x, note.y, System.identityHashCode(note), notes.size()
                );
            }
        }
    }

}
