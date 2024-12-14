package staffEditor;

import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import org.jfugue.player.Player;

public class PlayButton extends JButton {
    MainWindow parent;
    
    public PlayButton(MainWindow parent) {
        this.parent = parent;

        // 設定按鈕屬性
        this.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/play-button-arrowhead.png")));  // 您可以替換為自己的圖片
        this.setToolTipText("播放音樂");
        this.setContentAreaFilled(false);
        this.setBorderPainted(false);

        // 添加按鈕功能
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Play button clicked");

                // 獲取當前的 TabbedPane
                TabbedPane tabbedPane = parent.getTabbedPane();

                // 檢查當前的頁面是否為 StaffPage
                if (tabbedPane.getSelectedComponent() instanceof StaffPage) {
                    StaffPage staffPage = (StaffPage) tabbedPane.getSelectedComponent();

                    // 呼叫 StaffPage 的播放邏輯
                    playMusic(staffPage);
                } else {
                    System.out.println("Current tab is not a StaffPage.");
                }
            }
        });
    }

    // 播放音樂的邏輯
    private void playMusic(StaffPage staffPage) {
        // 獲取音符資料
        List<NoteData> musicData = staffPage.getNotesForPlayback();
        StringBuilder music = new StringBuilder();
        String instrumentPattern = parent.instrumentMenu.instrumentList.getSelectedInstrumentPattern();
        /*String[] pitches;
        switch(instrumentPattern)
        {
        	case "I[Piano] ":
        		pitches = new String[]{"F5", "E5", "D5", "C5", "B4", "A4", "G4", "F4", "E4", "D4", "C4"};
        		System.out.println("1號");
        		break;
        	case "I[Violin] ":
        		pitches = new String[]{"F7", "E7", "D7", "C7", "B6", "A6", "G6", "F6", "E6", "D6", "C6"};
        		System.out.println("2號");
        		break;
        	case "I[Flute] ":
        		pitches = new String[]{"F8", "E8", "D8", "C8", "B7", "A7", "G7", "F7", "E7", "D7", "C7"};
        		System.out.println("3號");
        		break;
        	case "I[Alto_Sax] ":
        		pitches = new String[]{"F5", "E5", "D5", "C5", "B4", "A4", "G4", "F4", "E4", "D4", "C4"};
        		System.out.println("4號");
        		break;
        	case "I[Clarinet] ":
        		pitches = new String[]{"F6", "E6", "D6", "C6", "B5", "A5", "G5", "F5", "E5", "D5", "C5"};
        		System.out.println("5號");
        		break;
        	default:
        		pitches = new String[]{"F5", "E5", "D5", "C5", "B4", "A4", "G4", "F4", "E4", "D4", "C4"};
        		System.out.println("55號");
        		break;
        		
        }*/
        music.append(instrumentPattern).append(" ");
        // 根據音符資料生成音樂字符串
        for (NoteData note : musicData) {
            String pitch = note.getPitch();
            String duration = note.getDuration();
            music.append(pitch).append(duration).append(" ");
        }

        // 輸出音樂字符串並播放
        System.out.println("播放音樂: " + music.toString());  // 用於調試
        if (music.isEmpty()) {
            System.out.println("No music to play!");
            return;
        }
        playWithJFugue(music.toString());
    }

    // 使用 JFugue 播放音樂
    private void playWithJFugue(String music) {
        Player player = new Player();
        player.play(music);
    }
}
