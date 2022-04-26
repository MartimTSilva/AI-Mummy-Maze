package showSolution;

import javax.swing.*;
import java.awt.*;

public class Main {

    public Main() {

	MainFrame frame = new MainFrame();

	// Center the window
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	Dimension frameSize = frame.getSize();
	if (frameSize.height > screenSize.height) {
		frameSize.height = screenSize.height;
	}
	if (frameSize.width > screenSize.width) {
		frameSize.width = screenSize.width;
	}
	frame.setLocation((screenSize.width - frameSize.width) / 2,
			  (screenSize.height - frameSize.height) / 2);

	frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException |
                         InstantiationException |
                         IllegalAccessException | 
                         UnsupportedLookAndFeelException exception) {
                    exception.printStackTrace(System.err);
   		}
   		new Main();
            }
   	});
    }
}
