package staffEditor;

public class NoteData {
    private String pitch;    // 音高，例如 "C4"
    private String duration; // 音符持續時間，例如 "q"（四分音符）
    private int x;           // X 座標
    private int y;           // Y 座標
    private longType type;
    public NoteData(String pitch, String duration, int x, int y) {
        this.pitch = pitch;
        this.duration = duration;
        this.x = x;
        this.y = y;
    }
    public NoteData(int x, int y, longType type) {
            this.x = x;
            this.y = y;
            this.type = type;
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

