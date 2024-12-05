package staffEditor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class RestButton extends IconButton{
	
	private JPanel leftPanel;
	private JPanel rightRest;
	private TopToolbar parentTopToolbar;

	public RestButton(Toolbar p, JPanel leftPanel, JPanel rightRest, TopToolbar parentTopToolbar) 
	{
        super(p);
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
                updateBtnColor();
                MainWindow mainWindow = parent.parent;
                mainWindow.hideCopyPasteButtons();
            }
        });
 
	}
	
	private void enableRightButtons() 
	{		
		for (Component btn : rightRest.getComponents()) 
		{
            if (btn instanceof JButton) 
            {
                ((JButton) btn).setEnabled(true);  
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

	public void updateBtnColor()
	{
		for (Component btn : leftPanel.getComponents()) 
		{
            if (btn instanceof JButton) 
            {
                ((JButton) btn).setBackground(Color.WHITE);
            }
        }
		
		this.setBackground(Color.LIGHT_GRAY);
	}
}
