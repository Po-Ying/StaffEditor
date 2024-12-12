package staffEditor;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.*;
import java.util.List;
import javax.swing.*;



public class StaffPage extends JScrollPane {
	TabbedPane parent; 
	
    private final int STAFF_X_START = 100;
    private final int STAFF_Y_START = 128;
    private final int STAFF_X_END = 1050;
    static int count = -2;
    int id=1;
    JLabel note;
    Vector<JLabel> notes;
    Vector<JLabel> trash_notes;
    public StaffDrawer staffDrawer; // 用於繪製五線譜的物件
    public MeasureManager measureManager;  // 用來管理小節的 MeasureManager
    private Set<Measure> selectedCopyMeasures; // 儲存已選取的小節
    private Set<Measure> selectedPasteMeasures; // 儲存貼上目標的小節

    JButton backButton, forwardButton; 
    JComponent panel;
    StaffPage page;
    String title="曲名" ; 
    String composer="作曲家" ;
    // 用來記錄是否啟用了選擇模式
    public boolean selectionMode = false; // 初始化為 false

    private Measure[] measures;
    backButton back;
    forwardButton forward;
    public ClassLoader cldr;
    public URL imageURL;
    public ImageIcon icon ,imageIcon;
    StaffLabel staffTitle,authorTitle,instrumentTitle,pageCount,measure[];

    String m[]={"1","5","9","13","17","21","25","29","33","37"};

    MouseButton Mouse;   
    public StaffPage(TabbedPane p) {
        parent = p;
        count++;
        id = count;
        back = new backButton(this);
        forward = new forwardButton(this);
        staffDrawer = new StaffDrawer();
        notes = new Vector<>();
        trash_notes = new Vector<>();
        selectedCopyMeasures = new HashSet<>();
        new ArrayList<>();
        selectedPasteMeasures = new HashSet<>();
        
        initPanel();
        setupMeasures();  // 初始化小節
        measureManager = new MeasureManager(panel, measures); // 初始化 MeasureManager
        initButtons();
        initMouseListeners();
        
        this.getVerticalScrollBar().setUnitIncrement(10);

    }
    
    public void setSelectionMode(boolean enabled) {
        this.selectionMode = enabled;
        if (!enabled) {
            selectedCopyMeasures.clear();
            selectedPasteMeasures.clear();
        }
        repaint(); // 更新畫面
        System.out.println("Selection mode " + (enabled ? "enabled." : "disabled."));
    }
    // 檢查是否啟用了選擇模式
    public boolean isSelectionMode() {
        return selectionMode;
    }
    
    public Vector<JLabel> getNotes() {
        return notes;
    }
    
    public List<NoteData> getNotesForPlayback() {
        List<NoteData> musicData = new ArrayList<>();

        // 假設您的音符列表是保存在 notes 中
        for (JLabel noteLabel : notes) {
            String pitch = (String) noteLabel.getClientProperty("notePitch");  // 從 clientProperty 中取得音高
            String duration = (String) noteLabel.getClientProperty("noteDuration");  // 從 clientProperty 中取得時值

            // 取得音符的 X 和 Y 座標
            int x = noteLabel.getX();  // 音符的 X 座標
            System.out.println(x);
            int y = noteLabel.getY();  // 音符的 Y 座標
            System.out.println(y);
            if (pitch != null && duration != null) {
                // 創建音符資料並加入音符播放列表
                NoteData noteData = new NoteData(pitch, duration, x, y);
                musicData.add(noteData);
            }
        }

        return musicData;
    }

    // 初始化面板
    public void initPanel() {
        panel = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
            	super.paintComponent(g);
                staffDrawer.drawStaff(g, 10, new int[]{400, 630, 860, 1090}, 100, 155, 1090); 
                staffDrawer.drawSelectionBoxes(g, selectionMode, measureManager.getSelectedCopyMeasures(), measureManager.getSelectedPasteMeasures());
            }
        };
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(0, 1400));
        this.setViewportView(panel);

        
        if(id==1)
	    {
	        staffTitle = new StaffLabel("Title",SwingConstants.CENTER,this);
	        staffTitle.setLocation(340,33);
	        staffTitle.setFont(new Font("標楷體",0,30));
	        staffTitle.setSize(new Dimension(500,75));
	        panel.add(staffTitle);
        }
        else
        {
	        staffTitle = new StaffLabel("",SwingConstants.CENTER,this);
	        staffTitle.setLocation(340,33);
	        staffTitle.setFont(new Font("標楷體",0,30));
	        panel.add(staffTitle);
        }
        
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

    private void setupMeasures() 
    {
        // 定義每個小節的寬度
        int measureWidth = 230; 

        // 定義五線譜的起始位置
        int staffStartY = 155; // 五線譜開始的 Y 座標
        int staffLineSpacing = 125; // 每組五線譜之間的間距

        // 定義每一組五線譜包含的小節數量
        int measuresPerStaff = 4; 

        // 初始化小節陣列
        measures = new Measure[40]; // 假設總共 10 個小節

        for (int i = 0; i < measures.length; i++) 
        {
            int staffIndex = i / measuresPerStaff; // 計算該小節所在的五線譜組
            int measureIndexInStaff = i % measuresPerStaff; // 計算小節在該五線譜組中的位置

            // 計算小節的起始和結束座標
            int startX = 170 + measureIndexInStaff * (measureWidth); // 每個小節之間留有 10 的間距
            int startY = staffStartY + staffIndex * staffLineSpacing;
            int endX = startX + measureWidth;
            int endY = startY + 40; // 小節範圍高度固定為 40，與五線譜相符

            measures[i] = new Measure(startX, startY, endX, endY);
        }
    }

    private String getPitchFromYCoordinate(int y) {
        // 每行五線譜的基準參數
        int startY = 155;  // 第一行五線譜起始 Y 座標
        int offsetPerLine = 125;  // 每行五線譜的垂直偏移量
        int lineSpacing = 5;  // 每個音符間的像素間隔

        // 每行五線譜對應的固定音符（從高到低）
        String[] pitches = {"F5", "E5", "D5", "C5", "B4", "A4", "G4", "F4", "E4", "D4", "C4"};

        // 計算屬於哪一行五線譜
        int lineIndex = (y - startY) / offsetPerLine;

        // 檢查是否在五線譜範圍內（10 行五線譜）
        if (lineIndex < 0 || lineIndex >= 10) {
            System.out.println("超出範圍");
            return null;
        }

        // 該行五線譜的基準 Y 值
        int baseY = startY + lineIndex * offsetPerLine;

        // 計算相對於該行的 Y 偏移
        int relativeY = y - baseY;

        // 計算音符的索引
        int noteIndex = relativeY / lineSpacing;

        // 檢查音符索引範圍
        if (noteIndex < 0 || noteIndex >= pitches.length) {
            System.out.println("音符索引超出範圍");
            return null;
        }

        // 返回對應音符
        String pitch = pitches[noteIndex];
        System.out.println("y=" + y + " -> pitch: " + pitch);
        return pitch;
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
            public void mousePressed(MouseEvent e) {
                // 檢查選取模式是否啟用
                if (!selectionMode) {
                    //System.out.println("選取模式未啟用，無法選取小節。");
                    return; // 選取模式未啟用時，直接返回
                }
                for (Measure measure : measures) {
                    if (measure.contains(e.getX(), e.getY())) {
                        measureManager.toggleMeasureSelection(measure);
                        break;
                    }
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY(); //+ StaffPage.this.getVerticalScrollBar().getValue();


                System.out.println("滑鼠點擊座標: X=" + x + ", Y=" + y);
                
                if ((parent.parent.toolbar.inputtype == inputType.Cursor) || (x < STAFF_X_START) || (x > STAFF_X_END) || (y < STAFF_Y_START)) {
                    return;
                }

                cldr = this.getClass().getClassLoader();
                String pitch = getPitchFromYCoordinate(y);  // 根據 y 座標獲取音高
                String duration = "";  // 設定音符的時值
                // 根據類型載入對應的圖片
                switch (parent.parent.toolbar.longtype) {
                    case quarter:
                        duration = "quarter";
                        imageURL = cldr.getResource("images/quarter_note.png");
                        break;
                    case eighth:
                        duration = "eighth";
                        imageURL = cldr.getResource("images/eighth_note.png");
                        break;
                    case sixteenth:
                        duration = "sixteenth";
                        imageURL = cldr.getResource("images/sixteenth-note.png");
                        break;
                    case half:
                        duration = "half";
                        imageURL = cldr.getResource("images/half_note.png");
                        break;
                    case whole:
                        duration = "whole";
                        imageURL = cldr.getResource("images/whole.png");
                        break;
                    case quarterR:
                        pitch = "rest";  // 休止符
                        duration = "quarterR";
                        imageURL = cldr.getResource("images/quarter-note-rest.png");
                        break;
                    case eighthR:
                        pitch = "rest";
                        duration = "eighthR";
                        imageURL = cldr.getResource("images/eight-note-rest.png");
                        break;
                    case sixteenthR:
                        pitch = "rest";
                        duration = "sixteenthR";
                        imageURL = cldr.getResource("images/sixteenth_rest.png");
                        break;
                    case halfR:
                        pitch = "rest";
                        duration = "halfR";
                        imageURL = cldr.getResource("images/half-rest.png");
                        break;
                    case wholeR:
                        pitch = "rest";
                        duration = "wholeR";
                        imageURL = cldr.getResource("images/whole_rest.png");
                        break;
                    default:
                        System.out.println("Invalid note type.");
                        return; // 無效的類型，退出
                }

                // 如果圖片加載失敗，退出
                if (imageURL == null) {
                    System.out.println("Failed to load image.");
                    return;
                }

                // 創建音符圖標
                icon = new ImageIcon(imageURL);
                ImageIcon imageIcon = new ImageIcon(icon.getImage().getScaledInstance(30, 40, Image.SCALE_DEFAULT));

                // 創建音符標籤
                note = new JLabel(imageIcon);
                note.putClientProperty("notePitch", pitch);  // 設置音高
                note.putClientProperty("noteDuration", duration);  // 設置時值

                // 設定偏移量以調整符頭位置
                Point offset = getNoteOffset(parent.parent.toolbar.longtype);
                int xOffset = offset.x;
                int yOffset = offset.y;

                // 設定音符位置
                note.setLocation(x + xOffset, y + yOffset);
                note.setVisible(true);
                note.setSize(30, 40);

                // 添加到面板
                notes.add(note);
                panel.add(notes.lastElement());
                panel.repaint();
            }


            //  傳回偏移量
            private Point getNoteOffset(longType noteType) {
                switch (noteType) {
                    case quarter:
                    case eighth:
                    case sixteenth:
                        // 假設符頭位於音符的中心，因此對y軸進行調整
                        return new Point(-16, -33); // 假設的偏移值，根據圖片大小調整
                    case half:
                    case whole:
                        return new Point(-12, -18); // 基於音符的大小調整
                    // 休止符的偏移
                    case quarterR:   
                    case eighthR:    
                    case sixteenthR:
                    case halfR:
                    case wholeR:     
                        return new Point(-20, -15); // 根據休止符的大小調整
                    default:         
                        return new Point(0, 0); // 默認偏移量
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                toggleLabels(parent.parent.toolbar.inputtype == inputType.Cursor);
                back.setVisible(!notes.isEmpty());
                forward.setVisible(!trash_notes.isEmpty());
                // 根據功能啟用或禁用控制元件
                // staffTitle.setEnabled(false);
                // authorTitle.setEnabled(false);
                // instrumentTitle.setEnabled(false);
                // pageCount.setEnabled(false);
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
    private void toggleLabels(boolean enable) {
        staffTitle.setEnabled(enable);
        authorTitle.setEnabled(enable);
        instrumentTitle.setEnabled(enable);
        pageCount.setEnabled(enable);
        for (StaffLabel m : measure) {
            m.setEnabled(enable);
        }
    }
    
    // 用來啟動複製選擇的操作
    public boolean copySelectedMeasures() {
        if (!measureManager.copySelectedMeasures()) {
            System.out.println("複製小節失敗");
            return false;
        }
        return true;
    }
    // 用來啟動貼上選擇的操作
    public boolean pasteToSelectedMeasures() {
        if (!measureManager.pasteToSelectedMeasures()) {
            System.out.println("貼上小節失敗");
            return false;
        }
        return true;
    }
    // 清除當前的選取小節
    public void clearSelectedPasteMeasures() {
        if (selectedPasteMeasures != null) {
            selectedPasteMeasures.clear();
        }
        repaint();
    }

    public BufferedImage renderToImage() {
        // 获取有效的面板尺寸
        int width = this.getWidth();
        int height = this.getHeight();

        // 如果尺寸无效，使用默认尺寸
        if (width <= 0 || height <= 0) {
            width = 1100;
            height = 1400;
        }

        // 创建图像
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // 填充白色背景
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());

        // 调整字体和大小
        Font titleFont = new Font("標楷體", Font.PLAIN, 30);
        Font authorFont = new Font("標楷體", Font.PLAIN, 17);
        Font instrumentFont = new Font("標楷體", Font.PLAIN, 20);

        // 绘制曲名（居中对齐）
        g.setColor(Color.BLACK);
        g.setFont(titleFont);
        String title = "Title"; // 曲名
        FontMetrics titleMetrics = g.getFontMetrics(titleFont);
        int titleX = (width - titleMetrics.stringWidth(title)) / 2;  // 居中
        int titleY = 70; // 调整曲名的垂直位置，向下移动
        g.drawString(title, titleX, titleY);

        // 绘制作曲家（右对齐）
        String author = "author"; // 作曲家
        g.setFont(authorFont);
        FontMetrics authorMetrics = g.getFontMetrics(authorFont);
        int authorX = width - 50 - authorMetrics.stringWidth(author);  // 右对齐
        g.drawString(author, authorX, 120);

        // 绘制乐器（左对齐）
        String instrument = "Instrument"; // 乐器
        g.setFont(instrumentFont);
        g.drawString(instrument, 100, 100);  // 乐器

        // 绘制五线谱
        //drawStaff(g);

        g.dispose();

        return image;
    }
    
    public JComponent getPanel()
    {
    	return panel;
    }
}