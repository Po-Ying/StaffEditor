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
    
    WholerestButton wholerestBtn;
    HalfrestButton halfrestBtn;
    QuarterrestButton quarterrestBtn;
    EightrestButton eightrestBtn;
    SixteenthrestButton sixteenthrestBtn;


    TopToolbar(Toolbar p) {
        parent = p;
        this.setBackground(new Color(255, 220, 150));
        this.setPreferredSize(new Dimension(0, 45));
        this.setLayout(new BorderLayout());

        JPanel rightPanel = new JPanel();
        JPanel rightRest = new JPanel();
        
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(new Color(255, 220, 150));
        saveFileBtn = new SaveFileButton(this.parent);
        openFileBtn = new OpenFileButton(this.parent);
        newPageBtn = new NewPageButton(this.parent);
        mouseBtn = new MouseButton(this.parent, rightPanel, rightRest);
        musicBtn = new MusicButton(this.parent, leftPanel, rightPanel, this);
        restBtn = new RestButton(this.parent, leftPanel, rightRest, this);
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

   

        rightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(new Color(255, 220, 150));
        quarterBtn = new QuarterButton(this.parent);
        eighthBtn = new EightButton(this.parent);
        sixteenthBtn = new SixteenthButton(this.parent);
        halfBtn = new HalfButton(this.parent);
        wholeBtn = new WholeButton(this.parent);
        
        rightRest.setLayout(new FlowLayout(FlowLayout.RIGHT));
        rightRest.setBackground(Color.DARK_GRAY);
        wholerestBtn = new WholerestButton(this.parent);
        halfrestBtn = new HalfrestButton(this.parent);
        quarterrestBtn = new QuarterrestButton(this.parent);
        eightrestBtn = new EightrestButton(this.parent);
        sixteenthrestBtn = new SixteenthrestButton(this.parent);

        rightPanel.add(quarterBtn);
        rightPanel.add(eighthBtn);
        rightPanel.add(sixteenthBtn);
        rightPanel.add(halfBtn);
        rightPanel.add(wholeBtn);
        
        rightRest.add(wholerestBtn);
        rightRest.add(halfrestBtn);
        rightRest.add(quarterrestBtn);
        rightRest.add(eightrestBtn);
        rightRest.add(sixteenthrestBtn);
        
        this.add(leftPanel, BorderLayout.WEST);
        this.add(rightPanel, BorderLayout.EAST);
        
        this.revalidate();
        this.repaint();
        
    }
 
}
