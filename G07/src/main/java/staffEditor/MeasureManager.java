package staffEditor;

import javax.swing.*;
import java.awt.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector; 

public class MeasureManager {
    private Measure[] measures; // 小節陣列
    private List<Measure> selectedCopyMeasures; // 改為 List，存儲選中的複製小節
    private List<Measure> selectedPasteMeasures; // 改為 List，存儲選中的貼上小節
    private List<NoteData> clipboard; // 剪貼簿，存儲複製的音符
    private boolean pasteSelectionEnabled; // 用來啟用/禁用貼上選取功能
    private List<JLabel> notes; // 存儲所有音符的 JLabel

    private JComponent panel; // StaffPage 的面板

    public MeasureManager(JComponent panel, Measure[] measures, Vector<JLabel> notes) {
        this.panel = panel;
        this.measures = measures;
        this.selectedCopyMeasures = new ArrayList<>(); // 使用 List 確保順序
        this.selectedPasteMeasures = new ArrayList<>(); // 使用 List 確保順序
        this.clipboard = new ArrayList<>(); // 改為使用 List
        this.pasteSelectionEnabled = false;
        this.notes = notes; // 初始化音符列表
    }

    // 新增或移除選中的小節
    public void toggleMeasureSelection(Measure measure) {
        List<Measure> targetSet = pasteSelectionEnabled ? selectedPasteMeasures : selectedCopyMeasures;

        if (targetSet.contains(measure)) {
            targetSet.remove(measure);
            System.out.println("移除選中小節：" + measure);
        } else {
            targetSet.add(measure);
            System.out.println("新增選中小節：" + measure);
        }

        panel.repaint();
    }
    
    public void printClipboardContent() {
        if (clipboard.isEmpty()) {
            System.out.println("剪貼簿目前是空的。");
            return;
        }

        System.out.println("剪貼簿內容：");
        int index = 1;
        for (NoteData note : clipboard) {
            if (note != null) { // 確保音符不為 null
                System.out.println(index + ". 音高: " + note.getPitch() +
                                   ", 時值: " + note.getDuration() +
                                   ", 座標: (" + note.getX() + ", " + note.getY() + ")");
                index++;
            }
        }
    }


    // 複製選中的小節
    public boolean copySelectedMeasures() {
        if (selectedCopyMeasures.isEmpty()) {
            System.out.println("未選中任何小節進行複製。");
            return false;
        }

        clipboard.clear(); // 清空剪貼簿
        for (Measure measure : selectedCopyMeasures) {
            for (NoteData note : measure.getNotes()) {
                clipboard.add(new NoteData(note.getPitch(), note.getDuration(), note.getX(), note.getY()));
            }
        }

        System.out.println("已複製 " + clipboard.size() + " 個音符到剪貼簿。");
        printClipboardContent(); // 列印剪貼簿內容
        pasteSelectionEnabled = true; // 啟用貼上模式

        panel.repaint();
        return true;
    }
    
    private String getImagePath(String pitch, String duration) {
        // 根據音高和時值返回圖片路徑
        if ("quarter".equals(duration)) {
            return "images/quarter_note.png";
        } else if ("eighth".equals(duration)) {
            return "images/eighth_note.png";
        } else if ("sixteenth".equals(duration)) {
            return "images/sixteenth_note.png";
        } else if ("rest".equals(pitch)) {
            return "images/minus.png";
        }
        return null;
    }

    public boolean pasteToSelectedMeasures() {
        if (clipboard.isEmpty()) {
            System.out.println("Paste button clicked");
            System.out.println("Clipboard is empty. Nothing to paste.");
            return false;
        }

        if (selectedPasteMeasures.isEmpty()) {
            System.out.println("No measures selected for pasting.");
            return false;
        }

        if (selectedCopyMeasures.size() != selectedPasteMeasures.size()) {
            System.out.println("複製區塊和貼上區塊數量不一致，無法進行一對一貼上。");
            System.out.println("複製區塊數量: " + selectedCopyMeasures.size());
            System.out.println("貼上區塊數量: " + selectedPasteMeasures.size());
            return false;
        }

        // 轉換 Set 為 List 以便進行一對一映射
        List<Measure> copyMeasuresList = new ArrayList<>(selectedCopyMeasures);
        List<Measure> pasteMeasuresList = new ArrayList<>(selectedPasteMeasures);

        System.out.println("開始貼上一對一的音符...");
        int pastedNotesCount = 0;

        for (int i = 0; i < copyMeasuresList.size(); i++) {
            Measure sourceMeasure = copyMeasuresList.get(i);
            Measure targetMeasure = pasteMeasuresList.get(i);

            System.out.println("處理對應的區塊: 複製區塊 " + (i + 1) + " -> 貼上區塊 " + (i + 1));

            // 計算偏移量
            int measureOffsetX = targetMeasure.startX - sourceMeasure.startX;
            int measureOffsetY = targetMeasure.startY - sourceMeasure.startY;

            for (NoteData note : sourceMeasure.getNotes()) {
                // 計算新位置
                int newX = note.getX() + measureOffsetX;
                int newY = note.getY() + measureOffsetY;

                // 創建新音符並添加到目標小節
                NoteData newNote = new NoteData(
                        note.getPitch(),
                        note.getDuration(),
                        newX,
                        newY
                );

                    // 添加到目標小節
                    targetMeasure.addNoteData(newNote);

                    // **新增顯示邏輯**
                    // 加載對應的圖片
                    String imagePath = getImagePath(newNote.getPitch(), newNote.getDuration());
                    URL imageURL = getClass().getClassLoader().getResource(imagePath);
                    if (imageURL == null) {
                        System.out.println("Failed to load image for note: " + newNote);
                        continue; // 跳過加載失敗的音符
                    }
                    ImageIcon icon = new ImageIcon(imageURL);
                    ImageIcon scaledIcon = new ImageIcon(icon.getImage().getScaledInstance(30, 40, Image.SCALE_DEFAULT));

                    // 創建音符的 JLabel
                    JLabel noteLabel = new JLabel(scaledIcon);
                    noteLabel.setLocation(newX, newY);
                    noteLabel.setSize(30, 40);
                    noteLabel.setVisible(true);

                    // 為 JLabel 添加屬性
                    noteLabel.putClientProperty("notePitch", newNote.getPitch());
                    noteLabel.putClientProperty("noteDuration", newNote.getDuration());

                    // 添加到面板和音符列表
                    notes.add(noteLabel); // 假設 notes 是音符標籤的列表
                    panel.add(noteLabel);

                    pastedNotesCount++;

                    System.out.println("音符已成功顯示: "
                            + newNote.getPitch() + newNote.getDuration()
                            + " at (" + newNote.getX() + "," + newNote.getY() + ")");
                }
        }
            
		

        System.out.println("貼上一對一完成，共貼上 " + pastedNotesCount + " 個音符。");

        // 清空選中的貼上小節
        selectedPasteMeasures.clear();
        selectedCopyMeasures.clear(); // 清空選中的複製小節
        pasteSelectionEnabled = false;

        // 刷新面板，顯示貼上的音符
        panel.repaint();
        //notes.clear();

        return true;
    }
    // 清空選中的貼上小節
    public void clearSelectedPasteMeasures() {
        selectedPasteMeasures.clear();
        panel.repaint();
    }

    // 返回選中的複製小節
    public List<Measure> getSelectedCopyMeasures() {
        return selectedCopyMeasures;
    }

    // 返回選中的貼上小節
    public List<Measure> getSelectedPasteMeasures() {
        return selectedPasteMeasures;
    }
}
