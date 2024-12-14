package staffEditor;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.*;
import java.util.List;
import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.awt.Font;
import java.awt.FontFormatException;

public class StaffPage extends JScrollPane {
	TabbedPane parent; 
	
    private final int STAFF_X_START = 100;
    private final int STAFF_Y_START = 128;
    private final int STAFF_X_END = 1050;
    private static int count = 0;
    private int id = 1;
    JLabel note;
    Vector<JLabel> notes;
    Vector<JLabel> trash_notes;
    private Set<Measure> selectedCopyMeasures; // 儲存已選取的小節
    private List<Measure> clipboard; // 暫存區，用於存儲複製的小節
    private Set<Measure> selectedPasteMeasures; // 儲存貼上目標的小節

    JButton backButton, forwardButton; 
    JComponent panel;
    StaffPage page;
    String title="曲名" ; 
    String composer="作曲家" ;
    // 用來記錄是否啟用了選擇模式
    private boolean selectionMode = false; // 初始化為 false
    private boolean pasteSelectionEnabled = false; // 控制貼上選取是否啟用
    private Measure[] measures;
    
    backButton back;
    forwardButton forward;
    public ClassLoader cldr;
    public URL imageURL;
    public ImageIcon icon ,imageIcon;
    StaffLabel staffTitle,authorTitle,instrumentTitle,pageCount,measure[];
    
    // 假设你有一个保存所有文本的集合
    Map<String, String> labelsData = new HashMap<>();

    String m[]={"1","5","9","13","17","21","25","29","33","37"};

    MouseButton Mouse;

    public StaffPage(TabbedPane p) {

        parent = p;
        count++;
        id = count;
        back= new backButton(this);
        forward=new forwardButton(this);
        notes = new Vector<JLabel>() ;
        trash_notes = new Vector<JLabel>();
        selectedCopyMeasures = new HashSet<>();
        clipboard = new ArrayList<>(); // 初始化暫存區
        selectedPasteMeasures = new HashSet<>();

        
        initPanel();
        setupMeasures();  // 初始化小節
        initButtons();
        initMouseListeners();
        
        this.getVerticalScrollBar().setUnitIncrement(10);
        
        Toolkit tk = Toolkit.getDefaultToolkit();
        System.out.print("New StaffPage, id=" + id + "\n");
        System.out.println("Initialized StaffPage with labelsData: " + labelsData); // 打印检查
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

    // 初始化面板
    public void initPanel() {
        panel = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {   
            	super.paintComponent(g);
                drawStaff(g);
            }
        };
        panel.setLayout(null);
        //panel.setPreferredSize(new Dimension(0, 1400));
        //this.setViewportView(panel);

	    staffTitle = new StaffLabel(labelsData.getOrDefault("staffTitle", "Title"),SwingConstants.CENTER,this, "staffTitle");
	    staffTitle.setLocation(340,33);
	    staffTitle.setFont(new Font("標楷體",0,30));
	    staffTitle.setSize(new Dimension(500,75));
	    labelsData.put("staffTitle", staffTitle.getText()); // 初始化时填充 labelsData
	    //if(id == 1) 
	    panel.add(staffTitle);

        authorTitle = new StaffLabel(labelsData.getOrDefault("authorTitle", "author"),SwingConstants.RIGHT,this, "authorTitle");
        authorTitle.setLocation(750,120);
        authorTitle.setFont(new Font("標楷體",0,17));
        authorTitle.setSize(new Dimension(300,30));
        labelsData.put("authorTitle", authorTitle.getText()); // 初始化时填充 labelsData
        if(id == 1) panel.add(authorTitle);

        instrumentTitle = new StaffLabel(labelsData.getOrDefault("instrumentTitle", "Instrument"),SwingConstants.LEFT,this, "instrumentTitle");
        instrumentTitle.setLocation(100,100);
        instrumentTitle.setFont(new Font("標楷體",0,20));
        instrumentTitle.setSize(new Dimension(150,30));
        labelsData.put("instrumentTitle", instrumentTitle.getText()); // 初始化时填充 labelsData
        if(id == 1) panel.add(instrumentTitle);

        pageCount = new StaffLabel("-" + id + "-",SwingConstants.CENTER,this, "pageCount");
        pageCount.setLocation(570,1350);
        pageCount.setFont(new Font("標楷體",0,17));
        pageCount.setSize(new Dimension(60,30));
        labelsData.put("pageCount", pageCount.getText()); // 初始化时填充 labelsData
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
        this.panel.setPreferredSize(new Dimension(1200,1400));

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
        
        System.out.println("Initialized labelsData in initPanel: " + labelsData);

        panel.revalidate();// 强制面板重新布局
        panel.repaint();     // 强制重绘面板   

    }

    // 绘制五线谱
    public void drawStaff(Graphics g) {
        int offset = 0;
        int[] measurePositions = {400, 630, 860, 1090}; // 每行的小節起始 X 座標
        g.setColor(Color.BLACK);

        Font bassClefFont = null;
        try (InputStream is = getClass().getResourceAsStream("/fonts/Bravura.otf")) {
            if (is != null) {
                bassClefFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(50f);
            } else {
                System.out.println("字體檔案未找到");
            }
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return;
        }
        // 繪製五線譜和小節
        for (int i = 0; i < 10; i++) { // 假設有 10 行五線譜
            for (int j = 0; j < 5; j++) {
                g.drawLine(100, 155 + j * 10 + offset, 1090, 155 + j * 10 + offset);
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
        

        // 繪製複製選取框
        if (selectionMode && selectedCopyMeasures != null) {
        	for (Measure measure : selectedCopyMeasures) {
        	    g.setColor(new Color(255, 255, 0, 128)); // 半透明黃色背景
        	    g.fillRect(
        	        measure.startX,
        	        measure.startY,
        	        measure.endX - measure.startX,
        	        measure.endY - measure.startY
        	    );
        	    g.setColor(Color.RED); // 紅色邊框
        	    g.drawRect(
        	        measure.startX,
        	        measure.startY,
        	        measure.endX - measure.startX,
        	        measure.endY - measure.startY
        	    );
        	}

        }
        //繪製貼上選取框
        if (selectionMode && selectedPasteMeasures != null) {
            for (Measure measure : selectedPasteMeasures) {
                g.setColor(new Color(0, 255, 0, 128)); // 半透明綠色背景
                g.fillRect(
                    measure.startX,
                    measure.startY,
                    measure.endX - measure.startX,
                    measure.endY - measure.startY
                );
                g.setColor(Color.BLUE); // 藍色邊框
                g.drawRect(
                    measure.startX,
                    measure.startY,
                    measure.endX - measure.startX,
                    measure.endY - measure.startY
                );
            }
        }
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
                    System.out.println("選取模式未啟用，無法選取小節。");
                    return; // 選取模式未啟用時，直接返回
                }

                for (Measure measure : measures) {
                    if (measure.contains(e.getX(), e.getY())) {
                        if (pasteSelectionEnabled) {
                            // 當貼上選取功能啟用時，左鍵選取貼上目標
                            if (selectedPasteMeasures.contains(measure)) {
                                selectedPasteMeasures.remove(measure);
                            } else {
                                selectedPasteMeasures.add(measure);
                            }
                        } else {
                            // 當貼上選取功能未啟用時，左鍵選取複製區塊
                            if (selectedCopyMeasures.contains(measure)) {
                                selectedCopyMeasures.remove(measure);
                            } else {
                                selectedCopyMeasures.add(measure);
                            }
                        }
                        repaint();
                        break;
                    }
                }
            }

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
    
    private class Measure 
    {
    	
        int startX, startY, endX, endY;
        boolean isSelected;

        Measure(int startX, int startY, int endX, int endY) {
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
            this.isSelected = false;
        }

        boolean contains(int x, int y) {
            return x >= startX && x <= endX && y >= startY && y <= endY;
        }
        // 複製小節並應用偏移量
        Measure cloneWithOffset(int deltaX, int deltaY) {
            return new Measure(startX + deltaX, startY + deltaY, endX + deltaX, endY + deltaY);
        }
    }    
    
    public boolean copySelectedMeasure() {
        if (selectedCopyMeasures == null || selectedCopyMeasures.isEmpty()) {
            System.out.println("No measure selected for copying.");
            return false;
        }

        clipboard.clear(); // 清空之前的剪貼簿
        clipboard.addAll(selectedCopyMeasures);
        System.out.println("Copied " + selectedCopyMeasures.size() + " measure(s) to clipboard.");
        
        pasteSelectionEnabled = true; // 啟用貼上選取功能
        selectedCopyMeasures.clear(); // 清除已選取的複製區塊
        repaint();
        return true;
    }

    
    public boolean pasteToSelectedMeasures() {
        if (clipboard.isEmpty()) {
            System.out.println("Clipboard is empty. Nothing to paste.");
            return false;
        }

        if (selectedPasteMeasures.isEmpty()) {
            System.out.println("No target measures selected for pasting.");
            return false;
        }

        if (selectedPasteMeasures.size() != clipboard.size()) {
            System.out.println("The number of clipboard measures and target measures do not match.");
            return false;
        }

        Iterator<Measure> pasteTargets = selectedPasteMeasures.iterator();
        for (Measure clipboardMeasure : clipboard) {
            if (pasteTargets.hasNext()) {
                Measure targetMeasure = pasteTargets.next();
                int deltaX = targetMeasure.startX - clipboardMeasure.startX;
                int deltaY = targetMeasure.startY - clipboardMeasure.startY;
                Measure newMeasure = clipboardMeasure.cloneWithOffset(deltaX, deltaY);
                int index = findMeasureIndex(targetMeasure);
                if (index != -1) {
                    measures[index] = newMeasure;
                }
            }
        }

        System.out.println("Pasted to " + selectedPasteMeasures.size() + " target measures.");
        pasteSelectionEnabled = false; // 禁用貼上選取功能
        selectedPasteMeasures.clear(); // 清除貼上選取集合
        repaint();
        return true;
    }
    // 工具方法：尋找某個小節在 measures 陣列中的索引
    private int findMeasureIndex(Measure measure) {
        for (int i = 0; i < measures.length; i++) {
            if (measures[i] == measure) {
                return i;
            }
        }
        return -1;
    }

    // 清除當前的選取小節
    public void clearSelectedPasteMeasures() {
        if (selectedPasteMeasures != null) {
            selectedPasteMeasures.clear();
        }
        repaint();
    }
    
    @Override
    public Dimension getPreferredSize() {
    	// 返回 panel 的首選尺寸
        return panel.getPreferredSize();
    }
    
    // 用来保存更改后的文本
    public void updateText(String labelName, String updatedText) {
        labelsData.put(labelName, updatedText);  // 更新指定标签的文本
        System.out.println("更新后的文本存储在 " + labelName + ": " + updatedText);
        System.out.println(labelsData);
        
        
    }

    // 在渲染或保存时，获取存储的文本
    public String getTextFromLabels(String labelName) {
    	System.out.println("lb" + labelName);
        return labelsData.get(labelName);
    }
    
    public BufferedImage renderToImage() {  

    	panel.revalidate();
        panel.repaint();
        
        // 強制立即重繪面板
        panel.paintImmediately(0, 0, panel.getWidth(), panel.getHeight());
    	
        // 获取并打印面板的尺寸
        Dimension panelSize = panel.getPreferredSize();
        
        // 获取面板的宽度和高度
        int width = panelSize.width;
        int height = panelSize.height;
        
        // 創建 BufferedImage 来保存渲染的内容
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        /*
        if (id < 1) id += 1;
        String pageCount = Integer.toString(id);
        labelsData.put("pageCount", "-" + pageCount + "-");
 		*/
        // 把paintComponent的每個組件都在檔案顯示出來
        for (Component component : panel.getComponents()) {
        	
        	if (component.getClass().getSimpleName().equals("backButton") || component.getClass().getSimpleName().equals("forwardButton")) 
        	{
        	    continue;  // 這兩個按鈕不用出現在儲存的檔案裡==
        	}
        	
        	if (component instanceof StaffLabel) {
        		
                StaffLabel label = (StaffLabel) component;

                //System.out.println(labelsData);
                
                String updatedText = labelsData.get(label.labelName);  // 获取更新后的文本
                //label.updateText(label.labelName, updatedText);  // 更新標籤文本
                System.out.println("Fetching updated text for " + label.labelName + ": " + updatedText);
                System.out.println(labelsData);

                if (updatedText != null) {
                    label.setText(updatedText);  // 设置更新后的文本
                    label.repaint();  // 强制重绘该标签
                }
                
            }
        
        	Graphics subGraphics = g.create(component.getX(), component.getY(), component.getWidth(), component.getHeight());
            component.paint(subGraphics);
            subGraphics.dispose();
        	
        }
        
        drawStaff(g);
        
        g.dispose();

        return image; // 返回渲染的圖像
        
    }

    /*
    public void paint()
    {
    	
    }
    
    public void paint(Graphics g)
    {
    	super.paint(g);
    	
    	// 自定义绘制内容
        Graphics2D g2 = (Graphics2D) g;
        
     	// 设置抗锯齿
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        for (Component component : panel.getComponents()) {
        	Graphics subGraphics = g.create(component.getX(), component.getY(), component.getWidth(), component.getHeight());
        	component.paint(subGraphics);
            subGraphics.dispose();
        }
    }
    */
}