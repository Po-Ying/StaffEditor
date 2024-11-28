package staffEditor;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Vector;
import javax.swing.*;

import java.io.IOException;
import java.io.InputStream;

public class StaffPage extends JScrollPane {
    
    TabbedPane parent; 
    static int count = 0;
    int id;
    JLabel note;
    Vector<JLabel> notes;
    Vector<JLabel> trash_notes;
    JButton backButton, forwardButton; 
    JComponent panel;

    // 用來記錄是否啟用了選擇模式
    private boolean selectionMode = false; // 初始化為 false
    private Measure[] measures;
    private Measure selectedMeasure = null;
    private boolean moduleButtonClicked = false;

    StaffPage(TabbedPane p) {
        parent = p;
        count++;
        id = count;
        notes = new Vector<>();
        trash_notes = new Vector<>();
        initPanel();
        setupMeasures();  // 初始化小節
        initLabels();
        initButtons();
        initMouseListeners();
        this.getVerticalScrollBar().setUnitIncrement(10);
    }

    public void setSelectionMode(boolean enabled) {
        this.selectionMode = enabled;  // 更新選擇模式
        repaint();  // 重新繪製頁面
    }

    // 檢查是否啟用了選擇模式
    public boolean isSelectionMode() {
        return selectionMode;
    }

    private void initPanel() {
        panel = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawStaff(g); 
            }
        };
        
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(0, 1400));
        this.setViewportView(panel);
    }
    
    private void drawStaff(Graphics g) {
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

        // 繪製選取框
        if (selectionMode && selectedMeasure != null) {
            g.setColor(new Color(255, 255, 0, 128)); // 半透明黃色
            g.fillRect(
                selectedMeasure.startX, 
                selectedMeasure.startY, 
                selectedMeasure.endX - selectedMeasure.startX, 
                selectedMeasure.endY - selectedMeasure.startY
            );
            g.setColor(Color.RED); // 紅色邊框
            g.drawRect(
                selectedMeasure.startX, 
                selectedMeasure.startY, 
                selectedMeasure.endX - selectedMeasure.startX, 
                selectedMeasure.endY - selectedMeasure.startY
            );
        }
    }


    private void setupMeasures() {
        // 定義每個小節的寬度
        int measureWidth = 230; 

        // 定義五線譜的起始位置
        int staffStartY = 155; // 五線譜開始的 Y 座標
        int staffLineSpacing = 125; // 每組五線譜之間的間距

        // 定義每一組五線譜包含的小節數量
        int measuresPerStaff = 4; 

        // 初始化小節陣列
        measures = new Measure[40]; // 假設總共 10 個小節

        for (int i = 0; i < measures.length; i++) {
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


    private void initLabels() {
        JLabel staffTitle = new JLabel("Title", SwingConstants.CENTER);
        staffTitle.setFont(new Font("標楷體", Font.PLAIN, 30));
        staffTitle.setBounds(340, 33, 500, 75);
        panel.add(staffTitle);

        JLabel authorTitle = new JLabel("author", SwingConstants.RIGHT);
        authorTitle.setFont(new Font("標楷體", Font.PLAIN, 17));
        authorTitle.setBounds(800, 120, 300, 30);
        panel.add(authorTitle);

        JLabel instrumentTitle = new JLabel("Instrument", SwingConstants.LEFT);
        instrumentTitle.setFont(new Font("標楷體", Font.PLAIN, 20));
        instrumentTitle.setBounds(100, 100, 150, 30);
        panel.add(instrumentTitle);

        JLabel pageCount = new JLabel("-" + id + "-", SwingConstants.CENTER);
        pageCount.setFont(new Font("標楷體", Font.PLAIN, 17));
        pageCount.setBounds(570, 1350, 60, 30);
        panel.add(pageCount);
    }

    private void initButtons() {
        backButton = new JButton("←");
        forwardButton = new JButton("→");

        backButton.setBounds(20, 20, 45, 45);
        forwardButton.setBounds(70, 20, 45, 45);

        backButton.setVisible(false);
        forwardButton.setVisible(false);

        panel.add(backButton);
        panel.add(forwardButton);
    }

    private void initMouseListeners() {
    	panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // 檢查選取模式是否啟用
                if (!selectionMode) {
                    System.out.println("選取模式未啟用，無法選取小節。");
                    return; // 選取模式未啟用時，直接返回
                }

                // 如果選取模式啟用，檢查點擊的小節
                for (Measure measure : measures) {
                    if (measure.contains(e.getX(), e.getY())) {
                        selectedMeasure = measure;  // 設置選中小節
                        repaint();                  // 重繪以顯示選中狀態
                        break;
                    }
                }
            }

    	    @Override
    	    public void mouseEntered(MouseEvent e) {
    	        backButton.setVisible(!notes.isEmpty());
    	        forwardButton.setVisible(!trash_notes.isEmpty());
    	    }

    	    @Override
    	    public void mouseExited(MouseEvent e) {
    	        backButton.setVisible(false);
    	        forwardButton.setVisible(false);
    	    }
    	});

        this.addMouseWheelListener(e -> {
            int offset = this.getVerticalScrollBar().getValue();
            backButton.setLocation(20, 20 + offset);
            forwardButton.setLocation(70, 20 + offset);
        });
    }

    private class Measure {
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
    }
//Buttest
    public BufferedImage renderToImage() {
        int width = this.getWidth();
        int height = this.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = image.createGraphics();
        this.paint(g); // 繪製當前內容
        g.dispose();

        return image;
    }

}
