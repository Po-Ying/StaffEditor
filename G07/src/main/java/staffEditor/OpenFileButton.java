package staffEditor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class OpenFileButton extends IconButton {

    OpenFileButton(Toolbar p) {
        super(p);
        imageURL   = cldr.getResource("images/open-folder.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);

        this.setToolTipText("開啟檔案");

        
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        });
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            JOptionPane.showMessageDialog(this, "選擇的檔案: " + selectedFile.getAbsolutePath());
        }
    }
}
