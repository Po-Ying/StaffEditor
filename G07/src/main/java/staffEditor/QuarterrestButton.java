package staffEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class QuarterrestButton extends IconButton {

    Toolbar parent;
    ImageIcon imageIcon;
	public QuarterrestButton(Toolbar p) 
	{
		super(p);
        imageURL = cldr.getResource("images/quarter-note-rest.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);

        this.setToolTipText("四分休止符");
	}
    // public void doSomething(){
    //     Toolkit tk = Toolkit.getDefaultToolkit();
    //     if(parent.inputtype==inputType.Note) {
    //         imageURL = cldr.getResource("images/quarter-note-up.png");
    //         icon = new ImageIcon(imageURL);

    //         imageIcon = new ImageIcon(icon.getImage().getScaledInstance(25, 45, Image.SCALE_DEFAULT));
    //     }
    // }

    
    // Cursor cu = tk.createCustomCursor(imageIcon.getImage(),new Point(16,16),"");
    //     for(int i=0;i<parent.parent.parent.tabbedPane.getTabCount();i++) {
    //         parent.parent.parent.tabbedPane.getComponentAt(i).setCursor(cu);


    //     }

    // p.notetype = noteType.quarterR;
}
