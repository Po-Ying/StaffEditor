package staffEditor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Measure {
    public int startX, startY, endX, endY;
    private boolean isSelected;
    private List<NoteData> notes; // 新增音符列表

    public Measure(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.isSelected = false;
        this.notes = new ArrayList<>(); // 初始化音符列表
    }

    // 增加音符到小節中
 // 增加音符到小節中，避免重複添加
    public void addNoteData(NoteData note) {
        if (note == null) {
            System.out.println("試圖添加空音符");
            return;
        }

        // 檢查是否已經存在相同的音符
        for (NoteData existingNote : notes) {
            if (existingNote.getPitch().equals(note.getPitch()) &&
                existingNote.getDuration().equals(note.getDuration()) &&
                existingNote.getX() == note.getX() &&
                existingNote.getY() == note.getY()) {
                System.out.println("音符已存在，跳過添加: " + note);
                return;
            }
        }

        // 如果不存在，則添加音符
        this.notes.add(note);
    }



    // 獲取小節中的所有音符
    public List<NoteData> getNotes() {
        return notes;
    }

    // 判斷點是否在 Measure 內
    public boolean contains(int x, int y) {
        return x >= startX && x <= endX && y >= startY && y <= endY;
    }

    // 克隆 Measure 並添加偏移
    public Measure cloneWithOffset(int deltaX, int deltaY) {
        Measure newMeasure = new Measure(startX + deltaX, startY + deltaY, endX + deltaX, endY + deltaY);
        for (NoteData note : notes) {
            // 複製音符並添加偏移
            newMeasure.addNoteData(new NoteData(note.getPitch(), note.getDuration(),
                    note.getX() + deltaX, note.getY() + deltaY));
        }
        return newMeasure;
    }

    // 設置選中狀態
    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    // 繪製 Measure
    public void draw(Graphics g) {
        g.setColor(isSelected ? Color.RED : Color.BLACK); // 選中時顯示紅色
        g.drawRect(startX, startY, endX - startX, endY - startY);
        if (isSelected) {
            g.fillRect(startX + 1, startY + 1, endX - startX - 1, endY - startY - 1);
        }
    }
    
    
}

