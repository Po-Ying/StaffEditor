package staffEditor;

import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;
import java.net.URL;
import java.util.Vector;
import javax.swing.*;

public class StaffPage extends JScrollPane {
    private static int count = 0;
    private final int id;
    private final TabbedPane parent;
    private final Vector<JLabel> notes = new Vector<>();
    private final Vector<JLabel> trashNotes = new Vector<>();
    private final backButton back;
    private final forwardButton forward;
    private final JComponent panel;
    private final Label staffTitle, authorTitle, instrumentTitle, pageCount;
    private final Label[] measureLabels;
    private static final int[] MEASURE_POSITIONS = {370, 600, 830, 1050};
    private static final String[] MEASURE_TEXTS = {"1", "5", "9", "13", "17", "21", "25", "29", "33", "37"};

    public StaffPage(TabbedPane p) {
        this.parent = p;
        this.id = ++count;
        this.back = new backButton(this);
        this.forward = new forwardButton(this);

        panel = createPanel();
        staffTitle = createLabel("Title", 340, 33, 500, 75, 30, SwingConstants.CENTER);
        authorTitle = createLabel("author", 750, 120, 300, 30, 17, SwingConstants.RIGHT);
        instrumentTitle = createLabel("Instrument", 100, 100, 150, 30, 20, SwingConstants.LEFT);
        pageCount = createLabel("-" + id + "-", 570, 1350, 60, 30, 17, SwingConstants.CENTER);
        measureLabels = createMeasureLabels();

        initPanel();
        initNavigationButtons();
        initMouseListeners();
        this.getVerticalScrollBar().setUnitIncrement(10);
    }

    private JComponent createPanel() {
        return new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                drawStaff(g);
            }
        };
    }

    private Label createLabel(String text, int x, int y, int width, int height, int fontSize, int alignment) {
        Label label = new Label(text, alignment, this);
        label.setBounds(x, y, width, height);
        label.setFont(new Font("標楷體", Font.PLAIN, fontSize));
        return label;
    }

    private Label[] createMeasureLabels() {
        Label[] labels = new Label[10];
        int offset = 0;
        for (int i = 0; i < MEASURE_TEXTS.length; i++) {
            labels[i] = createLabel(MEASURE_TEXTS[i], 95, 135 + offset, 25, 20, 12, SwingConstants.CENTER);
            offset += 115;
        }
        return labels;
    }

    private void initPanel() {
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(0, 1400));
        panel.setBackground(Color.white);

        panel.add(staffTitle);
        panel.add(authorTitle);
        panel.add(instrumentTitle);
        panel.add(pageCount);
        for (Label measure : measureLabels) {
            panel.add(measure);
        }

        this.setViewportView(panel);
    }

    private void initNavigationButtons() {
        setupButton(back, 20, 20, 45, 45);
        setupButton(forward, 70, 20, 45, 45);
    }

    private void setupButton(JButton button, int x, int y, int width, int height) {
        button.setBounds(x, y, width, height);
        button.setVisible(false);
        panel.add(button);
    }

    private void initMouseListeners() {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleNoteAddition(e.getPoint());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                toggleUIElements(false);
                updateNavigationButtonVisibility();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                toggleUIElements(true);
                back.setVisible(false);
                forward.setVisible(false);
            }
        });

        this.addMouseWheelListener(e -> updateNavigationButtonPosition());
    }

    private void toggleUIElements(boolean enable) {
        staffTitle.setEnabled(enable);
        authorTitle.setEnabled(enable);
        instrumentTitle.setEnabled(enable);
        pageCount.setEnabled(enable);
        for (Label label : measureLabels) {
            label.setEnabled(enable);
        }
    }

    private void updateNavigationButtonVisibility() {
        back.setVisible(!notes.isEmpty());
        forward.setVisible(!trashNotes.isEmpty());
    }

    private void updateNavigationButtonPosition() {
        int scrollOffset = this.getVerticalScrollBar().getValue();
        back.setLocation(20, 20 + scrollOffset);
        forward.setLocation(70, 20 + scrollOffset);
    }

    private void drawStaff(Graphics g) {
        int offset = 0;
        for (int i = 0; i < 10; i++) {
            drawStaffLines(g, offset);
            drawClef(g, i, offset);
            drawMeasureBars(g, offset);
            offset += 125;
        }
    }

    private void drawStaffLines(Graphics g, int offset) {
        g.setColor(Color.BLACK);
        for (int j = 0; j < 5; j++) {
            g.drawLine(100, 155 + j * 10 + offset, 1050, 155 + j * 10 + offset);
        }
    }

    private void drawClef(Graphics g, int staffIndex, int offset) {
        String clefSymbol = (staffIndex % 2 == 0) ? "\uD834\uDD1E" : "\uD834\uDD22";
        g.setFont(new Font("Default", Font.PLAIN, 90));
        g.drawString(clefSymbol, (staffIndex % 2 == 0) ? 110 : 125, (staffIndex % 2 == 0) ? 202 + offset : 175 + offset);
    }

    private void drawMeasureBars(Graphics g, int offset) {
        for (int pos : MEASURE_POSITIONS) {
            g.drawLine(pos, 155 + offset, pos, 195 + offset);
        }
    }

    private void handleNoteAddition(Point mousePoint) {
        if (parent.inputType != inputType.Note) return;
        URL imageURL = loadNoteImageURL();
        if (imageURL == null) return;

        ImageIcon imageIcon = createScaledIcon(imageURL, 30, 45);
        JLabel noteLabel = createNoteLabel(imageIcon, mousePoint);
        notes.add(noteLabel);
        panel.add(noteLabel);
        panel.repaint();
    }

    private URL loadNoteImageURL() {
        String resourcePath = switch (parent.parent.toolbar.longType) {
            case quarter -> "images/quarter_note.png";
            case eighth -> "images/eighth_note.png";
            case sixteenth -> "images/sixteenth_note.png";
            case half -> "images/half_note.png";
            case whole -> "images/whole.png";
            default -> null;
        };
        return resourcePath != null ? getClass().getClassLoader().getResource(resourcePath) : null;
    }

    private ImageIcon createScaledIcon(URL imageURL, int width, int height) {
        ImageIcon icon = new ImageIcon(imageURL);
        return new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }

    private JLabel createNoteLabel(ImageIcon imageIcon, Point mousePoint) {
        JLabel label = new JLabel(imageIcon);
        label.setBounds(mousePoint.x - 20, mousePoint.y - 20 + this.getVerticalScrollBar().getValue(), 30, 45);
        label.setVisible(true);
        return label;
    }
}
