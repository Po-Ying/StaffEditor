package staffEditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class MusicButton extends IconButton{
	
	private JPanel leftPanel;
	private JPanel rightPanel;
	private TopToolbar parentTopToolbar;

	public MusicButton(Toolbar p, JPanel leftPanel, JPanel rightPanel, TopToolbar parentTopToolbar) 
	{
		super(p);
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
                MainWindow mainWindow = parent.parent;
                mainWindow.hideCopyPasteButtons();
            }
        });
        
	}
	
	private void enableRightButtons() 
	{		
		for (Component btn : rightPanel.getComponents()) 
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
        parentTopToolbar.add(rightPanel, BorderLayout.EAST);
        
        parentTopToolbar.revalidate();
        parentTopToolbar.repaint();

	}

}
