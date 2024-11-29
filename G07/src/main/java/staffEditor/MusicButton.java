package staffEditor;//notemode

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;

public class MusicButton extends ToggleButton{
	
	private JPanel leftPanel;
	private JPanel rightPanel;
	private TopToolbar parentTopToolbar;
    private Toolbar parent;

	public MusicButton(Toolbar p, JPanel leftPanel, JPanel rightPanel, TopToolbar parentTopToolbar) 
	{
		super(p);
        parent = p;
		this.leftPanel = leftPanel;
		this.rightPanel = rightPanel; 
		this.parentTopToolbar = parentTopToolbar;
		
        imageURL   = cldr.getResource("images/music-note.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);
        this.setToolTipText("選擇音符");
        
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
		for (Component btn : rightPanel.getComponents()) 
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
        parentTopToolbar.add(rightPanel, BorderLayout.EAST);
        
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
