package staffEditor;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class MouseButton extends IconButton{
	
	private JPanel leftPanel;
	private JPanel rightPanel;
	private JPanel rightRest;

	public MouseButton(Toolbar p, JPanel leftPanel, JPanel rightPanel, JPanel rightRest) 
	{
		super(p);
		this.leftPanel = leftPanel;
		this.rightPanel = rightPanel; 
		this.rightRest = rightRest;
        imageURL   = cldr.getResource("images/direct-selection.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);
        this.setToolTipText("滑鼠");
        
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                disableRightButtons();
                updateBtnColor();
            }
        });
	}
		
	private void disableRightButtons() 
	{		
		for (Component btn : rightPanel.getComponents()) 
		{
            if (btn instanceof JButton) 
            {
                ((JButton) btn).setEnabled(false);  
            }
        }
		
		for (Component btn : rightRest.getComponents()) 
		{
            if (btn instanceof JButton) 
            {
                ((JButton) btn).setEnabled(false);  
            }
        }
    }
	
	private void updateBtnColor()
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
