package staffEditor;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import javax.swing.*;
import java.awt.Font;
import java.awt.FontFormatException;

public class StaffPage extends JScrollPane {
    private final int STAFF_X_START = 100;
    private final int STAFF_Y_START = 128;
    private final int STAFF_X_END = 1050;
    public TabbedPane parent;

    public JLabel note;
    public Vector<JLabel> notes = null;
    public Vector<JLabel> trash_notes = null;
    //記憶體會在建構子被呼叫時才分配
    // public final ArrayList<JLabel> notes = new Vector<JLabel>();
    //在類別載入時就分配記憶體
    //不推薦
    
    backButton back;
    forwardButton forward;
    public ClassLoader cldr;
    public URL imageURL;
    public ImageIcon icon ,imageIcon;
    // public String currentNoteType = "Quarter";

    public static int count = 0;
    public int id;

    // public JButton backButton, forwardButton;
    public JComponent panel;
    StaffLabel staffTitle,authorTitle,instrumentTitle,pageCount,measure[];

    String m[]={"1","5","9","13","17","21","25","29","33","37"};

    MouseButton Mouse;
    // 构造函数初始化面板
    public StaffPage(TabbedPane p) {

        parent = p;
        count++;
        id = count;
        back= new backButton(this);
        forward=new forwardButton(this);
        notes = new Vector<JLabel>() ;
        trash_notes = new Vector<JLabel>();
        
        initPanel();
        initButtons();
        initMouseListeners();
        
        this.getVerticalScrollBar().setUnitIncrement(10);
        
        Toolkit tk = Toolkit.getDefaultToolkit();
    }

    // 初始化面板
    public void initPanel() {
        panel = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                drawStaff(g);
            }
        };
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(0, 1400));
        this.setViewportView(panel);
        staffTitle = new StaffLabel("Title",SwingConstants.CENTER,this);
        staffTitle.setLocation(340,33);
        staffTitle.setFont(new Font("標楷體",0,30));
        staffTitle.setSize(new Dimension(500,75));
        panel.add(staffTitle);


        authorTitle = new StaffLabel("author",SwingConstants.RIGHT,this);
        authorTitle.setLocation(750,120);
        authorTitle.setFont(new Font("標楷體",0,17));
        authorTitle.setSize(new Dimension(300,30));
        panel.add(authorTitle);

        instrumentTitle = new StaffLabel("Instrument",SwingConstants.LEFT,this);
        instrumentTitle.setLocation(100,100);
        instrumentTitle.setFont(new Font("標楷體",0,20));
        instrumentTitle.setSize(new Dimension(150,30));
        panel.add(instrumentTitle);

        pageCount = new StaffLabel("-" + id + "-",SwingConstants.CENTER,this);
        pageCount.setLocation(570,1350);
        pageCount.setFont(new Font("標楷體",0,17));
        pageCount.setSize(new Dimension(60,30));
        panel.add(pageCount);

        measure = new StaffLabel[10];
        int g=0;
        for(int i=0;i<10;i++){

            measure[i] = new StaffLabel(m[i],SwingConstants.CENTER,this);
            measure[i].setLocation(95,135+ i*10 + g);
            measure[i].setFont(new Font("標楷體",0,12));
            measure[i].setSize(new Dimension(25,20));
            panel.add(measure[i]);

            g+=115;
        }

        this.panel.setBackground(Color.white);
        this.panel.setPreferredSize(new Dimension(0,1400));


        this.parent.setVisible(true);

        this.setViewportView(panel);
        back.setLocation(20,20);
        back.setVisible(false);
        back.setSize(new Dimension(45,45));
        panel.add(back);

        forward.setLocation(70,20);
        forward.setVisible(false);
        forward.setSize(new Dimension(45,45));
        panel.add(forward);
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
        back.setBounds(20, 20, 45, 45);
        forward.setBounds(70, 20, 45, 45);

        back.setVisible(false);
        forward.setVisible(false);

        panel.add(back);
        panel.add(forward);
    }

    // 初始化鼠标监听器
    public void initMouseListeners() {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY() + StaffPage.this.getVerticalScrollBar().getValue();
                
                if ((parent.parent.toolbar.inputtype == inputType.Cursor) || (x < STAFF_X_START) || (x > STAFF_X_END) || (y < STAFF_Y_START)) {
                    return;
                }
                
                System.out.println("滑鼠點擊座標: X=" + x + ", Y=" + y);
                
                cldr = this.getClass().getClassLoader();
                // 根據類型載入對應的圖片
                    switch (parent.parent.toolbar.longtype) {
                        case quarter:
                           imageURL = cldr.getResource("images/quarter_note.png");
                            break;
                        case eighth:
                            imageURL = cldr.getResource("images/eighth_note.png");
                            break;
                        case sixteenth:
                            imageURL = cldr.getResource("images/sixteenth-note.png");
                            break;
                        case half:
                            imageURL = cldr.getResource("images/half_note.png");
                            break;
                        case whole:
                            imageURL = cldr.getResource("images/whole.png");
                            break;


                        //休止符    
                        case quarterR:
                            imageURL = cldr.getResource("images/quarter-note-rest.png");
                            break;
                        case eighthR:
                            imageURL = cldr.getResource("images/eight-note-rest.png");
                            break;
                        case sixteenthR:
                            imageURL = cldr.getResource("images/sixteenth_rest.png");
                            break;
                        case halfR:
                            imageURL = cldr.getResource("images/half-rest.png");
                            break;
                        case wholeR:
                            imageURL = cldr.getResource("images/whole_rest.png");
                            break;
                        default:
                            System.out.println("Invalid note type.");
                            return; // 無效的類型，直接退出
                    }
                
                
                    if (imageURL == null) {
                        System.out.println("Failed to load image.");
                        return; // 圖片加載失敗，退出
                    }

                    icon = new ImageIcon(imageURL);
                    switch (parent.parent.toolbar.longtype) {
                        case quarter: 
                        case eighth: 
                        case sixteenth:
                        case sixteenthR:
                        case half:    imageIcon = new ImageIcon(icon.getImage().getScaledInstance(30, 45, Image.SCALE_DEFAULT));break;
                        case whole:   imageIcon = new ImageIcon(icon.getImage().getScaledInstance(18, 22, Image.SCALE_DEFAULT));break;
                        case quarterR: 
                        case halfR:
                        case wholeR:  imageIcon = new ImageIcon(icon.getImage().getScaledInstance(20, 30, Image.SCALE_DEFAULT));break;
                        case eighthR: imageIcon = new ImageIcon(icon.getImage().getScaledInstance(18, 24, Image.SCALE_DEFAULT));break;
                    }
                    // 創建音符標籤
                    note = new JLabel(imageIcon);
                    Point offset = getNoteOffset(parent.parent.toolbar.longtype);
                    int xOffset = offset.x;
                    int yOffset = offset.y;

                    // 設定音符位置
                    note.setLocation(
                        getMousePosition().x + xOffset,
                        getMousePosition().y + yOffset + StaffPage.this.getVerticalScrollBar().getValue()
                    );
                    note.setVisible(true);
                    note.setSize(30, 45);

                    // 添加到面板
                    notes.add(note);
                    panel.add(notes.lastElement());
                    panel.repaint();
                // }
            }
            //  傳回偏移量
            private Point getNoteOffset(longType noteType) {
                switch (noteType) {
                    case quarter:
                    case eighth:
                    case sixteenth:
                    case half:
                    case whole:
                    
                    //休止符
                    case quarterR:   
                    case eighthR:    
                    case sixteenthR:
                    case halfR:
                    case wholeR:     return new Point(-21, -18);
                    default:         return new Point(0, 0); // 默認偏移量
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);

                // 根據功能啟用或禁用控制元件
                staffTitle.setEnabled(false);
                authorTitle.setEnabled(false);
                instrumentTitle.setEnabled(false);
                pageCount.setEnabled(false);
                for (int i = 0; i < 10; i++) {
                    measure[i].setEnabled(false);
                }

                if (notes.size() != 0) {
                    back.setVisible(true);
                }
                if (trash_notes.size() != 0) {
                    forward.setVisible(true);
                }
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
            back.setLocation(20, 20 + offset);
            forward.setLocation(70, 20 + offset);
        });
    }
}