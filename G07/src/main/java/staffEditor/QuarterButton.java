package staffEditor;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;

public class QuarterButton extends IconButton {
    private StaffPage staffPage;

    public QuarterButton(Toolbar toolbar, StaffPage staffPage) {
        super(toolbar);
        this.staffPage = staffPage; // 存儲頁面對象
        
        imageURL = cldr.getResource("images/quarter_note.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);
        this.setToolTipText("四分音符");
        
        // 設置按鈕的點擊事件
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 使用面板的座標系統
                staffPage.addNote("Quarter", e.getX(), e.getY());
            }
        });
    }
}