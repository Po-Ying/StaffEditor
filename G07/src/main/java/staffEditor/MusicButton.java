package staffEditor;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class MusicButton extends IconButton{
	
	private JPanel rightPanel;

	public MusicButton(Toolbar p, JPanel rightPanel) 
	{
		super(p);
		this.rightPanel = rightPanel; 
        imageURL   = cldr.getResource("images/music-note.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);
        this.setToolTipText("選擇音符");
        
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                enableRightButtons();
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

}
