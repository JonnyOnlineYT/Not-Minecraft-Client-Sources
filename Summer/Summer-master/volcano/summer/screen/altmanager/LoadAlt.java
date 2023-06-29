package volcano.summer.screen.altmanager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import volcano.summer.base.Summer;
import volcano.summer.base.manager.file.files.Alts;

public class LoadAlt extends JPanel implements ActionListener {

    public JButton openButton;
    private JFileChooser fc;

    public LoadAlt() {
        fc = new JFileChooser();

        fc.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));

        openButton = new JButton("Open a File...");
        openButton.addActionListener(this);

        add(openButton);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == openButton) {
            int returnVal = fc.showOpenDialog(LoadAlt.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fc.getSelectedFile()))) {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        String[] arguments = line.split(":");
                        for (int i = 0; i < 2; i++)
                            arguments[i].replace(" ", "");
                        if (arguments.length > 2)
                            AltManager.registry.add(new Alt(arguments[0], arguments[1], arguments[2]));
                        else
                            AltManager.registry.add(new Alt(arguments[0], arguments[1]));
                    }
                    Summer.fileManager.getFile(Alts.class).saveFile();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "An error happened.", "ERROR", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }
}