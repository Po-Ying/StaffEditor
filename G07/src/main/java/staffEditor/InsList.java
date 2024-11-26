package staffEditor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class InsList extends JPanel {
    InsMenu parent;
    JButton[] instruments;
    String[] instrumentNames = {"鋼琴", "小提琴", "長笛", "薩克斯風", "豎笛"};
    String[] instrumentMIDIIds = {"pianoMIDI", "violinMIDI", "fluteMIDI", "saxMIDI", "clarinetMIDI"}; 
    int[] instrumentOctaves = {5, 5, 4, 3, 3}; 
    String[] instrumentIcons = {"images/paino.png", "images/violin.png", "images/flute.png", "images/saxophone.png", "images/clarinet.png"}; // 圖片文件路徑
    JButton selectedButton = null;

    InsList(InsMenu p) {
        parent = p;
        this.setBackground(new Color(255, 255, 255));//255, 220, 150
        this.setPreferredSize(new Dimension(180, 347));

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(Box.createVerticalGlue());

        instruments = new JButton[instrumentNames.length];
        for (int i = 0; i < instrumentNames.length; i++) {
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
            buttonPanel.setBackground(new Color(255, 255, 255));

            // 加載並縮放圖片
            ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(instrumentIcons[i]));
            Image scaledImage = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH); // 縮放圖片至 30x30
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            // 建立按鈕並設置圖標和文字
            instruments[i] = new JButton(instrumentNames[i], scaledIcon);
            instruments[i].setForeground(new Color(0, 0, 0));
            instruments[i].setOpaque(true);
            instruments[i].setBackground(new Color(217, 217, 217));
            instruments[i].setMaximumSize(new Dimension(160, 50)); // 固定按鈕寬度和高度
            instruments[i].setPreferredSize(new Dimension(160, 50));
            instruments[i].setHorizontalAlignment(SwingConstants.LEFT); // 將圖標和文字左對齊

            final int index = i;
            instruments[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (selectedButton != null) {
                        selectedButton.setBackground(new Color(217, 217, 217));
                        selectedButton.setForeground(new Color(0, 0, 0));
                    }

                    JButton clickedButton = (JButton) e.getSource();
                    clickedButton.setBackground(new Color(168, 168, 168));
                    clickedButton.setForeground(new Color(255, 255, 255));
                    selectedButton = clickedButton;

                    System.out.println("Selected Instrument: " + instrumentNames[index]);
                    System.out.println("MIDI ID: " + instrumentMIDIIds[index]);
                }
            });

            buttonPanel.add(Box.createHorizontalGlue());
            buttonPanel.add(instruments[i]);
            buttonPanel.add(Box.createHorizontalGlue());

            this.add(buttonPanel);
            this.add(Box.createVerticalStrut(10));
        }

        this.add(Box.createVerticalGlue());
    }
}
