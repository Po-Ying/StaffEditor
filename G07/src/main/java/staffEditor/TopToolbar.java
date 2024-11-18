package staffEditor;

import java.awt.*;
import javax.swing.*;

public class TopToolbar extends JPanel {
    Toolbar parent;
    StaffPage staffPage; // 確保使用 StaffPage

    private SaveFileButton saveFileBtn;
    private OpenFileButton openFileBtn;
    private NewPageButton newPageBtn;
    private MouseButton mouseBtn;
    private MusicButton musicBtn;
    private RestButton restBtn;
    private TupletButton tupletBtn;
    private ModuleButton moduleBtn;
    private LedgerLineButton ledgerLineBtn;

    private WholeButton wholeBtn;
    private HalfButton halfBtn;
    private QuarterButton quarterBtn;
    private EightButton eighthBtn;
    private SixteenthButton sixteenthBtn;
    
    private WholerestButton wholerestBtn;
    private HalfrestButton halfrestBtn;
    private QuarterrestButton quarterrestBtn;
    private EightrestButton eightrestBtn;
    private SixteenthrestButton sixteenthrestBtn;


    public TopToolbar(StaffPage staffPage) {
        this.staffPage = staffPage;
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
        quarterBtn = new QuarterButton(this.parent, staffPage);
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
 
}
