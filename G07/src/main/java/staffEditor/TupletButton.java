package staffEditor;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

public class TupletButton extends IconButton {
    private List<NoteData> selectedNotes;
    private StaffPage staffPage;

    TupletButton(Toolbar p, StaffPage page) {
        super(p);
        this.staffPage = page;
        this.selectedNotes = new ArrayList<>();

        imageURL = cldr.getResource("images/drawing.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);
        this.setToolTipText("音符連線");

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 啟用選取模式
                if (staffPage != null) {
                    staffPage.setTupletMode(true);  // 呼叫 staffPage 的 setTupletMode
                    System.out.println("進入連音符模式，請選擇兩個音符。");
                } else {
                    System.out.println("staffPage 為 null，無法進入連音符模式。");
                }
            }
        });
    }

    // 新增方法來處理音符選擇
    public void addSelectedNote(NoteData note) {
        if (!selectedNotes.contains(note)) {
            selectedNotes.add(note);
            System.out.println("音符選中：" + note);
        }

        // 當選取兩個音符後，建立連音符
        if (selectedNotes.size() == 2) {
            createTuplet();
        }
    }

    // 建立連音符，並結束選擇模式
    private void createTuplet() {
        System.out.println("建立連音符，包含音符：" + selectedNotes);

        // 驗證音符是否符合條件（例如都是八分音符）
        NoteData note1 = selectedNotes.get(0);
        NoteData note2 = selectedNotes.get(1);

        if (!note1.getDuration().equals(note2.getDuration()) || 
            (!note1.getDuration().equals("eighth") && !note1.getDuration().equals("sixteenth"))) {
            System.out.println("音符類型不一致或不支持的音符類型，無法建立連音符。");
            selectedNotes.clear();
            return;
        }

        // 通知 StaffPage 更新音符圖片並繪製符槓
        staffPage.drawTuplet(selectedNotes);

        // 清空選取狀態並結束選擇模式
        selectedNotes.clear();
        staffPage.setTupletMode(false);
        System.out.println("結束連音符模式。");
    }
    
}
