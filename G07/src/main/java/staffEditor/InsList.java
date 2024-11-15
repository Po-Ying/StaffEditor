package staffEditor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class InsList extends JPanel {
    InsMenu parent;
    JButton[] instruments;
    String[] instrumentNames = {"鋼琴", "小提琴", "長笛", "薩克斯風", "法國號"};
    String[] instrumentMIDIIds = {"pianoMIDI", "violinMIDI", "fluteMIDI", "saxMIDI", "hornMIDI"}; 
    int[] instrumentOctaves = {5, 5, 4, 3, 3}; 
    JButton selectedButton = null;

    InsList(InsMenu p) {
        parent = p;
        this.setBackground(new Color(255, 255, 255));
        this.setPreferredSize(new Dimension(180, 347));


        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));


        this.add(Box.createVerticalGlue());

        instruments = new JButton[instrumentNames.length];
        for (int i = 0; i < instrumentNames.length; i++) {
      
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
            buttonPanel.setBackground(new Color(255, 255, 255));
            
            instruments[i] = new JButton(instrumentNames[i]);
            instruments[i].setForeground(new Color(0, 0, 0));
            instruments[i].setOpaque(true);
            instruments[i].setBackground(new Color(217, 217, 217));
            instruments[i].setMaximumSize(new Dimension(160, 50)); 

            final int index = i;
            instruments[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (selectedButton != null) {
                        selectedButton.setBackground(new Color(217, 217, 217));
                        selectedButton.setForeground(new Color(0, 0, 0));
                    }

                    JButton clickedButton = (JButton) e.getSource();
                    clickedButton.setBackground(new Color(168, 168, 168));
                    clickedButton.setForeground(new Color(255, 255, 255));
                    selectedButton = clickedButton;

                    System.out.println("Selected Instrument: " + instrumentNames[index]);
                    System.out.println("MIDI ID: " + instrumentMIDIIds[index]);
                    //parent.parent.MidiDevice.MIDI_Ins = instrumentMIDIIds[index];
                    //parent.parent.MidiDevice.Octave = instrumentOctaves[index];
                }
            });

            
            buttonPanel.add(Box.createHorizontalGlue());
            buttonPanel.add(instruments[i]);
            buttonPanel.add(Box.createHorizontalGlue());

            this.add(buttonPanel);
            this.add(Box.createVerticalStrut(10)); 
        }

        
        this.add(Box.createVerticalGlue());
    }
}
