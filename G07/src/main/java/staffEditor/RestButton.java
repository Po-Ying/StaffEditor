package staffEditor;//restmode

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;

public class RestButton extends ToggleButton{
	
	private JPanel leftPanel;
	private JPanel rightRest;
	private TopToolbar parentTopToolbar;
    private Toolbar parent;

	public RestButton(Toolbar p, JPanel leftPanel, JPanel rightRest, TopToolbar parentTopToolbar) 
	{
        super(p);
        parent = p;
        this.leftPanel = leftPanel; 
        this.rightRest = rightRest;
        this.parentTopToolbar = parentTopToolbar;
        
        imageURL   = cldr.getResource("images/quarter-note-rest.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);

        this.setToolTipText("選擇休止符");
        
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	enableRightButtons();
                updateRightPanel();
                doSomething();
            }
        });
 
	}
	
	private void enableRightButtons() 
	{		
		for (Component btn : rightRest.getComponents()) 
		{
            if (btn instanceof ToggleButton) 
            {
                ((ToggleButton) btn).setEnabled(true);  
            }
        }
    }

	public void updateRightPanel() 
	{
		parentTopToolbar.removeAll();
		
		parentTopToolbar.add(leftPanel, BorderLayout.WEST);
        parentTopToolbar.add(rightRest, BorderLayout.EAST);
        
        parentTopToolbar.revalidate();
        parentTopToolbar.repaint();

	}
    public void doSomething(){
        parentTopToolbar.setLengthEnable(true);
        if(parentTopToolbar.inputtype != inputType.Note) { //如果不是音符模式
            // parentTopToolbar.resetlongButtongroup();
            parentTopToolbar.longtype=longType.non;
            for(int i=0;i<parentTopToolbar.parent.parent.tabbedPane.getTabCount();i++) {
                parentTopToolbar.parent.parent.tabbedPane.getComponentAt(i).setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

        }

        parent.inputtype = inputType.Note; //切換成音符模式
    }

}
