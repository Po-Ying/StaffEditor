package staffEditor;

import java.awt.*;
import javax.swing.*;

public class TopToolbar extends JPanel {
    Toolbar parent;

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
        this.setLayout(new BorderLayout());

        JPanel rightPanel = new JPanel();
        
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(Color.DARK_GRAY);
        saveFileBtn = new SaveFileButton(this.parent);
        openFileBtn = new OpenFileButton(this.parent);
        newPageBtn = new NewPageButton(this.parent);
        mouseBtn = new MouseButton(this.parent, rightPanel);
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

   
        //JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
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

        this.add(leftPanel, BorderLayout.WEST);
        this.add(rightPanel, BorderLayout.EAST);
    }
}
