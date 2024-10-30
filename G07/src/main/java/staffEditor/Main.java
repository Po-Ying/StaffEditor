package staffEditor;

public class Main {
	
	public static void main(String[] args)
	{
		String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("mac")) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("apple.awt.application.name", "SECreate");
        } else if (os.contains("win")) {
            System.out.println("Running on Windows");
        } else {
            System.out.println("Running on another OS");
        }
		new SECreate();
	}
}
