package staffEditor;

import java.awt.*;

public class Measure {
    public int startX, startY, endX, endY;
    private boolean isSelected;

    public Measure(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.isSelected = false;
    }

    // 判斷點是否在 Measure 內
    public boolean contains(int x, int y) {
        return x >= startX && x <= endX && y >= startY && y <= endY;
    }

    // 克隆 Measure 並添加偏移
    public Measure cloneWithOffset(int deltaX, int deltaY) {
        return new Measure(startX + deltaX, startY + deltaY, endX + deltaX, endY + deltaY);
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
