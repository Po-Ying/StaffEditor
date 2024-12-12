package staffEditor;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;

public class TupletButton extends IconButton{
	
	TupletButton(Toolbar p)
	{
		super(p);
        imageURL   = cldr.getResource("images/drawing.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);

        this.setToolTipText("音符連線");
        
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MainWindow mainWindow = parent.parent;
                mainWindow.hideCopyPasteButtons();
            }
        });
	}
}
