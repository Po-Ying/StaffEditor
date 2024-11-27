package staffEditor;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;

public class LedgerLineButton extends IconButton{
	LedgerLineButton(Toolbar p)
	{
		super(p);
        imageURL   = cldr.getResource("images/minus.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);

        this.setToolTipText("加譜線");
        
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MainWindow mainWindow = parent.parent;
                mainWindow.hideCopyPasteButtons();
            }
        });
	}
}
