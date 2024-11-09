package staffEditor;

import java.awt.*;
import javax.swing.*;

public class TopToolbar extends JPanel {
    Toolbar parent;

    // 定义按钮
    SaveFileButton saveFileBtn;
    OpenFileButton openFileBtn;
    NewPageButton newPageBtn;
    MouseButton mouseBtn;
    MusicButton musicBtn;
    RestButton restBtn;
    TupletButton tupletBtn;
    ModuleButton moduleBtn;
    LedgerLineButton ledgerLineBtn;

    QuarterButton quarterBtn;
    EightButton eighthBtn;
    SixteenthButton sixteenthBtn;
    HalfButton halfBtn;
    WholeButton wholeBtn;

    TopToolbar(Toolbar p) {
        parent = p;
        this.setBackground(Color.DARK_GRAY);
        this.setPreferredSize(new Dimension(0, 45));
        this.setLayout(new BorderLayout()); // 使用 BorderLayout 布局

        // 创建左侧按钮面板
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // 左对齐
        leftPanel.setBackground(Color.DARK_GRAY);
        saveFileBtn = new SaveFileButton(this.parent);
        openFileBtn = new OpenFileButton(this.parent);
        newPageBtn = new NewPageButton(this.parent);
        mouseBtn = new MouseButton(this.parent);
        musicBtn = new MusicButton(this.parent);
        restBtn = new RestButton(this.parent);
        tupletBtn = new TupletButton(this.parent);
        moduleBtn = new ModuleButton(this.parent);
        ledgerLineBtn = new LedgerLineButton(this.parent);

        leftPanel.add(saveFileBtn);
        leftPanel.add(openFileBtn);
        leftPanel.add(newPageBtn);
        leftPanel.add(mouseBtn);
        leftPanel.add(musicBtn);
        leftPanel.add(restBtn);
        leftPanel.add(tupletBtn);
        leftPanel.add(moduleBtn);
        leftPanel.add(ledgerLineBtn);

        // 创建右侧按钮面板
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT)); // 右对齐
        rightPanel.setBackground(Color.DARK_GRAY);
        quarterBtn = new QuarterButton(this.parent);
        eighthBtn = new EightButton(this.parent);
        sixteenthBtn = new SixteenthButton(this.parent);
        halfBtn = new HalfButton(this.parent);
        wholeBtn = new WholeButton(this.parent);

        rightPanel.add(quarterBtn);
        rightPanel.add(eighthBtn);
        rightPanel.add(sixteenthBtn);
        rightPanel.add(halfBtn);
        rightPanel.add(wholeBtn);

        // 将左侧和右侧面板添加到主面板中
        this.add(leftPanel, BorderLayout.WEST);
        this.add(rightPanel, BorderLayout.EAST);
    }
}
