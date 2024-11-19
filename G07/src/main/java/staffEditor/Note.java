package staffEditor;

import java.awt.*;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;


public class Note {
    private String noteType; // 音符類型，例如 "quarter"
    private int x, y; // 音符的位置

    public Note(String noteType, int x, int y) {
        this.noteType = noteType;
        this.x = x; // 隨機產生一個位置，這裡的 X 值在 50 到 750 之間
        this.y = y; // 固定 Y 值來簡單顯示在五線譜中
    }

    public void draw(Graphics g) {
        if (noteType == null) {
            System.out.println("Warning: noteType is null, cannot draw this note.");
            return;
        }   
        else if(noteType.equals("Quarter")) {
            g.setColor(Color.BLACK);
            g.fillOval(x - 6, y - 6, 12, 12);  // 音符頭
            g.fillRect(x + 6, y - 30, 2, 30);  // 音符桿
        }
        // 可以根據需要擴展其他音符類型
    }
}
