package staffEditor;//editbar

import java.awt.*;
import javax.swing.*;

public class TopToolbar extends JPanel {
    TabbedPane tabbedPane;
    StaffPage staffPage;
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

    WholeButton wholeBtn;
    HalfButton halfBtn;
    QuarterButton quarterBtn;
    EightButton eighthBtn;
    SixteenthButton sixteenthBtn;
    
    WholerestButton wholerestBtn;
    HalfrestButton halfrestBtn;
    QuarterrestButton quarterrestBtn;
    EightrestButton eightrestBtn;
    SixteenthrestButton sixteenthrestBtn;

    longType longtype;
    inputType inputtype;


    TopToolbar(Toolbar p) {
        this.parent = p;
        this.parent.inputtype= staffEditor.inputType.Cursor;
        this.parent.longtype = longType.non;

        this.staffPage = this.parent.parent.staffPage;
        this.setBackground(Color.DARK_GRAY);
        this.setPreferredSize(new Dimension(0, 45));
        this.setLayout(new BorderLayout());

        JPanel rightPanel = new JPanel();
        JPanel rightRest = new JPanel();
        JPanel leftPanel = new JPanel();

        
        leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(Color.DARK_GRAY);

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

   
        //JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(Color.DARK_GRAY);
        wholeBtn = new WholeButton(this.parent);
        halfBtn = new HalfButton(this.parent);
        quarterBtn = new QuarterButton(this.parent);
        eighthBtn = new EightButton(this.parent);
        sixteenthBtn = new SixteenthButton(this.parent);
        
        rightRest.setLayout(new FlowLayout(FlowLayout.RIGHT));
        rightRest.setBackground(Color.DARK_GRAY);
        wholerestBtn = new WholerestButton(this.parent);
        halfrestBtn = new HalfrestButton(this.parent);
        quarterrestBtn = new QuarterrestButton(this.parent);
        eightrestBtn = new EightrestButton(this.parent);
        sixteenthrestBtn = new SixteenthrestButton(this.parent);

        rightPanel.add(wholeBtn);
        rightPanel.add(halfBtn);
        rightPanel.add(quarterBtn);
        rightPanel.add(eighthBtn);
        rightPanel.add(sixteenthBtn);
        
        rightRest.add(wholerestBtn);
        rightRest.add(halfrestBtn);
        rightRest.add(quarterrestBtn);
        rightRest.add(eightrestBtn);
        rightRest.add(sixteenthrestBtn);
        
        this.add(leftPanel, BorderLayout.WEST);
        this.add(rightPanel, BorderLayout.EAST);
        
        for (Component btn : rightPanel.getComponents()) 
		{
            if (btn instanceof JButton) 
            {
                ((JButton) btn).setEnabled(false);  
            }
        }
        
        this.revalidate();
        this.repaint();
        
    }
    public void setLengthEnable(boolean b){
        this.halfBtn.setEnabled(b);
        this.quarterBtn.setEnabled(b);
        this.eighthBtn.setEnabled(b);
        this.sixteenthBtn.setEnabled(b);
        this.wholeBtn.setEnabled(b);
    }
    public void setTypeEnable(boolean b){
        this.musicBtn.setEnabled(b);
        this.restBtn.setEnabled(b);
        this.mouseBtn.setEnabled(b);
    }
    // public void resetlongButtongroup(){
    //     length.remove(quarterBtn);
    //     length.remove(eighthBtn);
    //     length.remove(sixteenthBtn);
    //     length.remove(halfBtn);
    //     length.remove(wholeBtn);
    //     quarter.setSelected(false);
    //     eighth.setSelected(false);
    //     sixteenth.setSelected(false);
    //     half.setSelected(false);
    //     whole.setSelected(false);
    //     length.add(quarterBtn);
    //     length.add(eighthBtn);
    //     length.add(sixteenthBtn);
    //     length.add(halfBtn);
    //     length.add(wholeBtn);
    // }
}
