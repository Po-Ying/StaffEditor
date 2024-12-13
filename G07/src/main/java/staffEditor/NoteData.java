package staffEditor;

public class NoteData {
    private int pitch;    // 音高，例如 "C4"
    private String duration; // 音符持續時間，例如 "q"（四分音符）
    private int x;           // X 座標
    private int y;           // Y 座標
    private String[] rpitch= {"F5", "E5", "D5", "C5", "B4", "A4", "G4", "F4", "E4", "D4", "C4",
    						"F7", "E7", "D7", "C7", "B6", "A6", "G6", "F6", "E6", "D6", "C6",
    						"F8", "E8", "D8", "C8", "B7", "A7", "G7", "F7", "E7", "D7", "C7",
    						"F5", "E5", "D5", "C5", "B4", "A4", "G4", "F4", "E4", "D4", "C4",
    						"F6", "E6", "D6", "C6", "B5", "A5", "G5", "F5", "E5", "D5", "C5",
    						"F5", "E5", "D5", "C5", "B4", "A4", "G4", "F4", "E4", "D4", "C4"};
    public NoteData(int pitch, String duration, int x, int y) {
        this.pitch = rpitch;
        this.duration = duration;
        this.x = x;
        this.y = y;
    }

    public String getPitch() {
        return pitch;
    }

    public String getDuration() {
        return duration;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return pitch + duration + " at (" + x + "," + y + ")";
    }
}

