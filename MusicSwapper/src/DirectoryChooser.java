import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;


public class DirectoryChooser extends JFileChooser {
    public DirectoryChooser(){
        setDialogTitle("Wybierz katalog");
        setFileSelectionMode(DIRECTORIES_ONLY);
        setApproveButtonText("Wybierz");
    }
}