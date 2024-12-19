package staffEditor;

import java.awt.*;
import javax.swing.*;

public class TopToolbar extends JPanel {
    Toolbar parent;
    StaffPage page;
    
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
    TabbedPane tabbedPane;
    longType longtype;
    inputType inputtype;


    TopToolbar(Toolbar p) {
        parent = p;
        if (parent != null && parent.getMainWindow() != null) {
            tabbedPane = parent.getMainWindow().getTabbedPane();  // 確保 tabbedPane 被正確獲取
        }

        // 如果 tabbedPane 是 null，則顯示錯誤或進行處理
        if (tabbedPane == null) {
            System.out.println("Error: tabbedPane is null.");
        }
        page = parent.getMainWindow().getTabbedPane().getSelectedStaffPage();
        this.setBackground(new Color(255, 220, 150));
        this.setPreferredSize(new Dimension(0, 45));
        this.setLayout(new BorderLayout());

        JPanel rightPanel = new JPanel();
        JPanel rightRest = new JPanel();
        
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(new Color(255, 220, 150));
        tabbedPane = new TabbedPane(parent.getMainWindow());

        saveFileBtn = new SaveFileButton(this.parent,this.page);
        openFileBtn = new OpenFileButton(this.parent,this.page);
        newPageBtn = new NewPageButton(this.parent);
        mouseBtn = new MouseButton(this.parent, leftPanel, rightPanel, rightRest);
        musicBtn = new MusicButton(this.parent, leftPanel, rightPanel, rightRest, this);
        restBtn = new RestButton(this.parent, leftPanel, rightPanel, rightRest, this);
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
        wholeBtn = new WholeButton(this.parent, rightPanel);
        halfBtn = new HalfButton(this.parent, rightPanel);
        quarterBtn = new QuarterButton(this.parent, rightPanel);
        eighthBtn = new EightButton(this.parent, rightPanel);
        sixteenthBtn = new SixteenthButton(this.parent, rightPanel);
        
        rightRest.setLayout(new FlowLayout(FlowLayout.RIGHT));
        rightRest.setBackground(new Color(255, 220, 150));
        wholerestBtn = new WholerestButton(this.parent, rightRest);
        halfrestBtn = new HalfrestButton(this.parent, rightRest);
        quarterrestBtn = new QuarterrestButton(this.parent, rightRest);
        eightrestBtn = new EightrestButton(this.parent, rightRest);
        sixteenthrestBtn = new SixteenthrestButton(this.parent, rightRest);

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
        
        for (Component btn : leftPanel.getComponents()) 
		{
            if (btn instanceof JButton) 
            {  
                if (btn instanceof MouseButton)
                {
                	((JButton) btn).setBackground(Color.LIGHT_GRAY);
                }
                else
                {
                	((JButton) btn).setBackground(Color.WHITE);
                }
            }
            
        }
        for (Component btn : rightPanel.getComponents()) 
		{
            if (btn instanceof JButton) 
            {
                ((JButton) btn).setEnabled(false);  
                ((JButton) btn).setBackground(Color.WHITE);
            }
        }
        for (Component btn : rightRest.getComponents()) 
		{
            if (btn instanceof JButton) 
            {  
                ((JButton) btn).setBackground(Color.WHITE);
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


}
