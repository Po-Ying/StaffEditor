package staffEditor;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.Font;
import java.awt.FontFormatException;

public class StaffPage extends JScrollPane {

    private final ArrayList<Note> notes = new ArrayList<>();
    private final ArrayList<JLabel> noteLabels = new ArrayList<>();

    private TabbedPane parent;
    private static int count = 0;
    private int id;
    
    private JButton backButton, forwardButton;
    private JComponent panel;

    // 构造函数初始化面板
    public StaffPage(TabbedPane p) {
        parent = p;
        count++;
        id = count;
        
        initPanel();
        initButtons();
        initMouseListeners();
        
        this.getVerticalScrollBar().setUnitIncrement(10);
    }

    // 初始化面板
    private void initPanel() {
        panel = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                drawStaff(g); 

                super.paintComponent(g);
                System.out.println("Repainting StaffPage, notes size: " + notes.size());
                System.out.println("StaffPage instance hash(paint): " + System.identityHashCode(this));
                System.out.println("Repainting StaffPage notes instance hash: " + System.identityHashCode(notes));

                for ( Note note : notes) {
                    note.draw(g);
                }
            }
        };
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(0, 1400));
        this.setViewportView(panel);
    }

    // 绘制五线谱
    private void drawStaff(Graphics g) {
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
    private Font loadBassClefFont() {
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

    // 初始化鼠标监听器
    private void initMouseListeners() {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                addNote("Quarter", e.getX(), e.getY());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                backButton.setVisible(!notes.isEmpty());
                forwardButton.setVisible(false); // 如有必要, trash_notes 逻辑可恢复
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

    // 添加音符
    public void addNote(String noteType, int x, int y) {
        if (noteType == null) {
            System.out.println("Error: noteType is null");
            return;
        }

        Note newNote = new Note(noteType, x, y);
        notes.add(newNote);

        JLabel noteLabel = new JLabel(noteType);
        noteLabel.setBounds(x, y, 50, 50);
        noteLabels.add(noteLabel);

        System.out.println("Drawing note at x=" + x + ", y=" + y + ", noteType = " + noteType);
        System.out.println("Added note: " + newNote + ", notes.size: " + notes.size());
        System.out.println("Adding note to StaffPage notes instance hash: " + System.identityHashCode(notes));

        this.revalidate();
        this.repaint();
    }

}
