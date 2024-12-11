package staffEditor;

import javax.swing.*;
import java.util.*;

public class MeasureManager {
    private Measure[] measures;
    private Set<Measure> selectedCopyMeasures;
    private Set<Measure> selectedPasteMeasures;
    private List<Measure> clipboard;
    private boolean pasteSelectionEnabled; // 用來啟用/禁用貼上選取功能
    private JComponent panel;  // StaffPage 的面板

    public MeasureManager(JComponent panel, Measure[] measures) {
        this.panel = panel; // 來自 StaffPage 的面板
        this.measures = measures;
        
        selectedCopyMeasures = new HashSet<>();
        selectedPasteMeasures = new HashSet<>();
        clipboard = new ArrayList<>();
        pasteSelectionEnabled = false; // 預設禁用貼上選取功能
    }

    // 新增或移除選中的小節
    public void toggleMeasureSelection(Measure measure) {
        Set<Measure> targetSet = pasteSelectionEnabled ? selectedPasteMeasures : selectedCopyMeasures;

        if (targetSet.contains(measure)) {
            targetSet.remove(measure);
            System.out.println("移除選中小節：" + measure);
        } else {
            targetSet.add(measure);
            System.out.println("新增選中小節：" + measure);
        }

        panel.repaint();
    }


    // 複製選中的小節
    public boolean copySelectedMeasures() {
        System.out.println("Selected measures before copying: " + selectedCopyMeasures);

        if (selectedCopyMeasures.isEmpty()) {
            System.out.println("No measures selected for copying.");
            return false;
        }

        clipboard.clear();
        clipboard.addAll(selectedCopyMeasures);
        System.out.println("Copied " + selectedCopyMeasures.size() + " measure(s) to clipboard.");

        pasteSelectionEnabled = true;
        selectedCopyMeasures.clear();

        panel.repaint();
        return true;
    }

    // 貼上小節
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
        selectedPasteMeasures.clear();
        
        panel.repaint();
        pasteSelectionEnabled = false;
        return true;
    }

    // 尋找 Measure 的索引
    private int findMeasureIndex(Measure measure) {
        for (int i = 0; i < measures.length; i++) {
            if (measures[i] == measure) {
                return i;
            }
        }
        return -1;
    }

    // 清空選中的貼上目標
    public void clearSelectedPasteMeasures() {
        selectedPasteMeasures.clear();
        panel.repaint();
    }

    // 返回選中的複製小節
    public Set<Measure> getSelectedCopyMeasures() {
        return selectedCopyMeasures;
    }

    // 返回選中的貼上小節
    public Set<Measure> getSelectedPasteMeasures() {
        return selectedPasteMeasures;
    }
}
