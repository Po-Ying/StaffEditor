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
    private final int STAFF_Y_START = 150;
    private final int STAFF_X_END = 1050;
    static int count = 0;
    int id=1;
    JLabel note;
    Vector<JLabel> notes;
    Vector<JLabel> trash_notes;
    public StaffDrawer staffDrawer; // 用於繪製五線譜的物件
    public MeasureManager measureManager;  // 用來管理小節的 MeasureManager
    private Set<Measure> selectedCopyMeasures; // 儲存已選取的小節
    private Set<Measure> selectedPasteMeasures; // 儲存貼上目標的小節
    private List<TupletLine> tupletLines = new ArrayList<>();  // 用來儲存所有的連音符線條
    private List<NoteData> tupletNotes; // 儲存目前選中的音符

    JButton backButton, forwardButton; 
    JComponent panel;
    StaffPage page;
    String title="曲名" ; 
    String composer="作曲家" ;
    // 用來記錄是否啟用了選擇模式
    public boolean selectionMode = false; // 初始化為 false
    public boolean tupletMode = false;

    private Measure[] measures;
    backButton back;
    forwardButton forward;
    public ClassLoader cldr;
    public URL imageURL;
    public ImageIcon icon ,imageIcon;
    StaffLabel staffTitle,authorTitle,instrumentTitle,pageCount,measure[];
    
    // 放更改的文字
    Map<String, String> labelsData = new HashMap<>();
    List<StaffPage> allPages;

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
        selectedPasteMeasures = new HashSet<>();
        
        allPages = parent.getAllStaffPages();
        
        tupletNotes = new ArrayList<>(); // 初始化連音符的選擇列表
        
        initPanel();
        setupMeasures();  // 初始化小節
        measureManager = new MeasureManager(panel, measures,notes); // 初始化 MeasureManager
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
    }
    // 檢查是否啟用了選擇模式
    public boolean isSelectionMode() {
        return selectionMode;
    }
    
    public void setTupletMode(boolean enabled) {
        this.tupletMode = enabled;
    }
    // 檢查是否啟用了選擇模式
    public boolean isTupletMode() {
        return tupletMode;
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
                
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(5));
                for (TupletLine line : tupletLines) {
                    JLabel note1 = line.getNote1();
                    JLabel note2 = line.getNote2();

                    int x1 = note1.getX() + note1.getWidth() / 2+5;
                    int y1 = note1.getY(); // 符槓略微上移
                    int x2 = note2.getX() + note2.getWidth() / 2+5;
                    int y2 = note2.getY();

                    // 根據音符時值進行判斷
                    String duration1 = (String) note1.getClientProperty("noteDuration");
                    String duration2 = (String) note2.getClientProperty("noteDuration");

                    if ("eighth".equals(duration1) && "eighth".equals(duration2)) {
                        // 繪製一條符槓
                        g2.drawLine(x1, y1, x2, y2);
                    } else if ("sixteenth".equals(duration1) && "sixteenth".equals(duration2)) {
                        // 繪製兩條符槓（第二條向下偏移 10px）
                        g2.drawLine(x1, y1, x2, y2);
                        g2.drawLine(x1, y1 + 10, x2, y2 + 10);
                    }
                }
            }
            
        };
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(0, 1400));
        this.setViewportView(panel);       
        if(id==1)
	    {
	        staffTitle = new StaffLabel(labelsData.getOrDefault("staffTitle", "Title"),SwingConstants.CENTER, this, "staffTitle");
	        staffTitle.setLocation(340,33);
	        staffTitle.setFont(new Font("標楷體",0,30));
	        staffTitle.setSize(new Dimension(500,75));
	        labelsData.put("staffTitle", staffTitle.getText()); // 初始化labelsData的"staffTitle"
	        panel.add(staffTitle);
        }
        else
        {
	        staffTitle = new StaffLabel("",SwingConstants.CENTER,this, "");
	        staffTitle.setLocation(340,33);
	        staffTitle.setFont(new Font("標楷體",0,30));
	        panel.add(staffTitle);
        }
        
        authorTitle = new StaffLabel(labelsData.getOrDefault("authorTitle", "author"),SwingConstants.RIGHT,this, "authorTitle");
        authorTitle.setLocation(750,120);
        authorTitle.setFont(new Font("標楷體",0,17));
        authorTitle.setSize(new Dimension(300,30));
        labelsData.put("authorTitle", authorTitle.getText()); // 初始化labelsData的"authorTitle"
        panel.add(authorTitle);

        instrumentTitle = new StaffLabel(labelsData.getOrDefault("instrumentTitle", "Instrument"),SwingConstants.LEFT,this, "instrumentTitle");
        instrumentTitle.setLocation(100,100);
        instrumentTitle.setFont(new Font("標楷體",0,20));
        instrumentTitle.setSize(new Dimension(150,30));
        labelsData.put("instrumentTitle", instrumentTitle.getText()); // 初始化labelsData的"instrumentTitle"
        panel.add(instrumentTitle);

        pageCount = new StaffLabel("-" + id + "-",SwingConstants.CENTER,this, "pageCount");
        pageCount.setLocation(570,1350);
        pageCount.setFont(new Font("標楷體",0,17));
        pageCount.setSize(new Dimension(60,30));
        labelsData.put("pageCount", pageCount.getText()); // 初始化labelsData的"pageCount"
        panel.add(pageCount);

        measure = new StaffLabel[10];
        int g=0;
        for(int i=0;i<10;i++){

            measure[i] = new StaffLabel(m[i],SwingConstants.CENTER,this, "measure");
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

        panel.revalidate();// 强制面板重新布局
        panel.repaint();     // 强制重绘面板   

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
        String[] pitches= new String[]{"F5", "E5", "D5", "C5", "B4", "A4", "G4", "F4", "E4", "D4", "C4"};;
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

    public JLabel findNoteLabel(NoteData note) {
        for (JLabel noteLabel : notes) {
            // 比對 X 和 Y 座標，確保找到對應的音符標籤
            if (noteLabel.getX() == note.getX() && noteLabel.getY() == note.getY()) {
                return noteLabel;
            }
        }
        return null; // 如果沒有找到，返回 null
    }

    
    public void drawTuplet(List<NoteData> notes) {
        if (notes.size() != 2) return;

        // 找到兩個音符的 JLabel
        JLabel noteLabel1 = findNoteLabel(notes.get(0));
        JLabel noteLabel2 = findNoteLabel(notes.get(1));

        if (noteLabel1 != null && noteLabel2 != null) {
            // 更新音符圖片為四分音符
            updateNoteImage(noteLabel1, "quarter", "quarter");
            updateNoteImage(noteLabel2, "quarter", "quarter");

            // 繪製符槓連線
            TupletLine tupletLine = new TupletLine(noteLabel1, noteLabel2);
            tupletLines.add(tupletLine); // 加入符槓連線集合
            panel.repaint(); // 重繪面板
        }
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
                
                // 判斷是否已進入連音符模式
                if (isTupletMode()) {
                    for (JLabel noteLabel : notes) {
                        if (noteLabel.getBounds().contains(x, y)) {
                            // 從音符標籤提取 NoteData
                            String pitch = (String) noteLabel.getClientProperty("notePitch");
                            String duration = (String) noteLabel.getClientProperty("noteDuration");
                            NoteData note = new NoteData(pitch, duration, noteLabel.getX(), noteLabel.getY());

                            // 將音符加入 tupletNotes
                            if (!tupletNotes.contains(note)) {
                                tupletNotes.add(note);
                            }

                            System.out.println("目前已選擇的音符數量：" + tupletNotes.size());
                            
                            // 如果兩個音符的時值符合連音符的條件，更新為四分音符
                            if (tupletNotes.size() == 2) {
                                System.out.println("已選擇兩個音符，開始處理連音符邏輯...");
                                /*pitch = "quarter";
                                duration = "quarter";
                                noteLabel.putClientProperty("notePitch", pitch);
                                noteLabel.putClientProperty("noteDuration", duration);
                                updateNoteImage(noteLabel, pitch, duration);*/

                                // 清空 tupletNotes，準備下一次選擇
                                tupletNotes.clear();
                            }

                            // 通知 TupletButton，將音符選入
                            parent.parent.toolbar.getTupletButton().addSelectedNote(note);
                        }
                    }
                }


                
                if ((parent.parent.toolbar.inputtype == inputType.Cursor) || (x < STAFF_X_START) || (x > STAFF_X_END) || (y < STAFF_Y_START)) {
                    return;
                }

                cldr = this.getClass().getClassLoader();
                String pitch = getPitchFromYCoordinate(y);  // 根據 y 座標獲取音高
                String duration = "";  // 設定音符的時值
                // 根據類型載入對應的圖片
                switch (parent.parent.toolbar.longtype) {
                	case line:
                		pitch = "rest";
                		imageURL = cldr.getResource("images/minus.png");
                		break;
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
                ImageIcon imageIcon = new ImageIcon();
                if(parent.parent.toolbar.longtype == longType.whole)
                {
                	imageIcon = new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
                }
                else
                {
                	imageIcon = new ImageIcon(icon.getImage().getScaledInstance(30, 40, Image.SCALE_DEFAULT));
                }

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
                
                Measure measure = getMeasureForPoint(x, y); // 根據點擊位置找到對應的小節
                if (measure != null) {
                    NoteData noteData = new NoteData(pitch, duration, note.getX(), note.getY());
                    measure.addNoteData(noteData);  // 添加音符到小節
                    System.out.println("音符已添加到小節: " + measure);
                }
            }


            public Measure getMeasureForPoint(int x, int y) {
                for (Measure measure : measures) { // 使用 measures 陣列來迭代
                    if (measure != null && measure.contains(x, y)) {  // 確保 measure 不為 null 並檢查座標
                        return measure;
                    }
                }
                return null;  // 如果沒有找到對應的小節，則返回 null
            }



			//  傳回偏移量
            private Point getNoteOffset(longType noteType) {
                switch (noteType) {
                	case line:
                	case quarter:
                    case eighth:
                    case sixteenth:
                    case half:
                        // 假設符頭位於音符的中心，因此對y軸進行調整
                        return new Point(-16, -33); // 假設的偏移值，根據圖片大小調整
                    case whole:
                        return new Point(-12, -20); // 基於音符的大小調整
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
 // 用來啟動貼上選擇的操作
    public boolean pasteToSelectedMeasures() {
        // 嘗試將剪貼簿中的音符貼到已選擇的小節中
        boolean result = measureManager.pasteToSelectedMeasures();
        if (!result) {
            System.out.println("貼上小節失敗");
            return false;
        }

        // 如果貼上成功，重新繪製畫面以顯示貼上的音符
        panel.repaint();
        System.out.println("貼上成功");
        return true;
    }

    // 清除當前的選取小節
    public void clearSelectedPasteMeasures() {
        if (selectedPasteMeasures != null) {
            selectedPasteMeasures.clear();
        }
        repaint();
    }

    private void updateNoteImage(JLabel noteLabel, String pitch, String duration) {
        // 根據音高和時值更新音符的圖片
        // 根據時值設定圖片
        String imagePath = "images/" + duration + "_note.png";
        URL imageURL = cldr.getResource(imagePath);

        if (imageURL != null) {
            ImageIcon icon = new ImageIcon(imageURL);
            noteLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(30, 40, Image.SCALE_DEFAULT)));
            System.out.println("音符圖像已更新為：" + imagePath);
        } else {
            System.out.println("音符圖片加載失敗：" + imagePath);
        }
    }

    private String getImagePath(String pitch, String duration) {
        // 根據音高和時值返回圖片路徑
        if ("quarter".equals(duration)) {
            return "images/quarter_note.png";
        } else if ("eighth".equals(duration)) {
            return "images/eighth_note.png";
        } else if ("sixteenth".equals(duration)) {
            return "images/sixteenth-note.png";
        } else if ("rest".equals(pitch)) {
            return "images/minus.png";
        }
        return null;
    }
    
    @Override
    public Dimension getPreferredSize() {
    	// 返回 panel 的首選尺寸
        return panel.getPreferredSize();
    }
    
    public void drawTupletLines(Graphics2D g2) {
        g2.setStroke(new BasicStroke(5));
        for (TupletLine line : tupletLines) {
            JLabel note1 = line.getNote1();
            JLabel note2 = line.getNote2();

            int x1 = note1.getX() + note1.getWidth() / 2 + 5;
            int y1 = note1.getY(); // 符槓略微上移
            int x2 = note2.getX() + note2.getWidth() / 2 + 5;
            int y2 = note2.getY();

            // 根據音符時值進行判斷
            String duration1 = (String) note1.getClientProperty("noteDuration");
            String duration2 = (String) note2.getClientProperty("noteDuration");

            if ("eighth".equals(duration1) && "eighth".equals(duration2)) {
                // 繪製一條符槓
                g2.drawLine(x1, y1, x2, y2);
            } else if ("sixteenth".equals(duration1) && "sixteenth".equals(duration2)) {
                // 繪製兩條符槓（第二條向下偏移 10px）
                g2.drawLine(x1, y1, x2, y2);
                g2.drawLine(x1, y1 + 10, x2, y2 + 10);
            }
        }
    }
   
    public BufferedImage renderToImage() {  

    	panel.revalidate();
        panel.repaint();
        
        // 強制立即重繪面板
        panel.paintImmediately(0, 0, panel.getWidth(), panel.getHeight());
    	
        // 獲取面板尺寸
        Dimension panelSize = panel.getPreferredSize();
        
        // 取得寬跟高
        int width = panelSize.width;
        int height = panelSize.height;
        
        // 改檔案尺寸
        if (width <= 0 || height <= 0) {
            width = 1200;
            height = 1400;
            panel.setSize(width, height); 
            panel.doLayout(); 
        }
        
        // 創建 BufferedImage 来保存渲染的内容
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        
        // 把paintComponent的每個組件都在檔案顯示出來
        for (Component component : panel.getComponents()) {
        	
        	if (component.getClass().getSimpleName().equals("backButton") || component.getClass().getSimpleName().equals("forwardButton")) 
        	{
        	    continue;  // 這兩個按鈕不用出現在儲存的檔案裡==
        	}
        	
        	if (component instanceof StaffLabel) {
        		
                StaffLabel label = (StaffLabel) component;
                String updatedText = labelsData.get(label.labelName);  // 獲取更新後的文本

                if (updatedText != null) {
                    label.setText(updatedText);  // 設置更新後的文本
                    label.repaint();  
                }
                
            }
        
        	Graphics subGraphics = g.create(component.getX(), component.getY(), component.getWidth(), component.getHeight());
            component.paint(subGraphics);
            subGraphics.dispose();
        	
        }
        
        
        
        // 這是畫五線譜
        staffDrawer.drawStaff(g, 10, new int[]{400, 630, 860, 1090}, 100, 155, 1090); 
        
        // 繪製符槓
        drawTupletLines(g);
        
        g.dispose();

        return image; // 返回渲染的圖像
        
    }
    
    public BufferedImage AllPagesToImage()
    {
    	int totalWidth = 0;
        int totalHeight = 0;
        
        // 計算總寬度和高度
        for (StaffPage page : allPages) {
            BufferedImage pageImage = page.renderToImage();
            totalWidth += pageImage.getWidth();
            totalHeight = Math.max(totalHeight, pageImage.getHeight());
        }

        // 創建一個空的圖像來放所有頁面
        BufferedImage combinedImage = new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = combinedImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int xOffset = 0;
        for (StaffPage page : allPages) {
            BufferedImage pageImage = page.renderToImage();
            g.drawImage(pageImage, xOffset, 0, null);
            xOffset += pageImage.getWidth();
        }

        g.dispose();
        return combinedImage;
    }
    
    public JComponent getPanel()
    {
    	return panel;
    }
    
    // 用來獲取所有連音符線條
    public List<TupletLine> getTupletLines() {
        return tupletLines;
    }
}