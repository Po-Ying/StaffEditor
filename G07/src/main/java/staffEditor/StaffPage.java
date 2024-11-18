package staffEditor;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.*;
import javax.swing.*;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

public class StaffPage extends JScrollPane {
    
    TabbedPane parent; 
    static int count = 0;
    int id;
    JLabel note;
    
    private Vector<JLabel> trash_notes;
    private ArrayList<JLabel> noteLabels = new ArrayList<>();
    private ArrayList<Note> notes = new ArrayList<>();
    
    JButton backButton, forwardButton; 
    JComponent panel;
    
    public StaffPage(TabbedPane p) {
        parent = p;
        count++;
        id = count;
        trash_notes = new Vector<>();

        initPanel();
        initLabels();
        initButtons();
        initMouseListeners();
        this.getVerticalScrollBar().setUnitIncrement(10);
    }

    private void initPanel() {
        panel = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                drawStaff(g); 
            }
        };
        
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(0, 1400));
        this.setViewportView(panel);
    }
    
    private void drawStaff(Graphics g) {
        int offset = 0;
        int[] measurePositions = {370, 600, 830, 1050}; 

        g.setColor(Color.BLACK);

        // 載入 Bravura 字體，用於低音譜號
        Font bassClefFont = null;
        try (InputStream is = getClass().getResourceAsStream("resources/fonts/Bravura.otf")) {
            if (is != null) {
                bassClefFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(50f);
            } else {
                System.out.println("字體檔案未找到");
            }
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return;
        }

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
    
    private void initLabels() {
        JLabel staffTitle = new JLabel("Title", SwingConstants.CENTER);
        staffTitle.setFont(new Font("標楷體", Font.PLAIN, 30));
        staffTitle.setBounds(340, 33, 500, 75);
        panel.add(staffTitle);

        JLabel authorTitle = new JLabel("author", SwingConstants.RIGHT);
        authorTitle.setFont(new Font("標楷體", Font.PLAIN, 17));
        authorTitle.setBounds(750, 120, 300, 30);
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
            public void mouseClicked(MouseEvent e) {
                // 此處可進行音符新增的處理
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

    // 修改 addNote 方法以接受音符的類型、位置 (x, y)
    public void addNotePublic(String noteType, int x, int y) {
        addNote(noteType, x, y);
    }

    public void addNote(String noteType, int x, int y) {
        // 創建新的音符並添加到 notes 列表
        Note newNote = new Note(noteType, x, y);
        notes.add(newNote);

        // 創建對應的 JLabel 顯示音符
        JLabel noteLabel = new JLabel(noteType);
        noteLabel.setBounds(x, y, 50, 50);
        noteLabels.add(noteLabel);

        revalidate(); // 更新布局
        repaint(); // 重新繪製畫面
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Note note : notes) {
            note.draw(g); // 假設 Note 類別有一個繪製方法
        }
    }
}
