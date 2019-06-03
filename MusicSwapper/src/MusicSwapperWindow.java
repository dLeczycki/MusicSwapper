import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Random;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class MusicSwapperWindow {
    public static int SWAP_AMOUNT = 100000;

    private JFrame frame;
    private DirectoryChooser directoryChooser;
    private JFrame fileChooserFrame;
    private JTextField musicDirPath;
    private JButton musicDirPathChooser;
    private JButton firstNumerationButton;
    private JPanel mainPanel;
    private JButton numerationButton;
    private JButton deleteNumerationButton;

    private File musicDirectory;

    public MusicSwapperWindow(){
        frame = new JFrame("Music Swapper");
        frame.setContentPane(mainPanel);
        int width = 650;
        int height = 180;

        initLayout();
        initActions();

        frame.setSize(width, height);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void initLayout(){
        directoryChooser = new DirectoryChooser();
        fileChooserFrame = new JFrame();
        fileChooserFrame.add(directoryChooser);
        fileChooserFrame.setVisible(false);
        fileChooserFrame.setLocationRelativeTo(null);
        fileChooserFrame.setResizable(false);
    }

    public void initActions(){

        musicDirPathChooser.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setEnabled(false);
                fileChooserFrame.setVisible(true);

                if(directoryChooser.showOpenDialog(fileChooserFrame) == JFileChooser.APPROVE_OPTION){
                    musicDirPath.setText(directoryChooser.getSelectedFile().toString());
                    musicDirectory = new File(musicDirPath.getText());
                    checkForMP3();
                }else{
                    //no selection
                }
                fileChooserFrame.setVisible(false);
                frame.setEnabled(true);
                frame.setVisible(true);
            }
        });

        firstNumerationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(musicDirPath.getText().equals("")) {
                    noPathError();
                    return;
                }

                File[] listOfFiles = musicDirectory.listFiles();
                int size = listOfFiles.length;

                int[] swappedIndexes = swapIndexes(size);
                renameFiles(listOfFiles, swappedIndexes, musicDirectory.getPath());
            }
        });

        numerationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(musicDirPath.getText().equals("")) {
                    noPathError();
                    return;
                }

                File[] listOfFiles = musicDirectory.listFiles();
                int size = listOfFiles.length;

                listOfFiles = deleteIndexes(listOfFiles, musicDirectory.getPath());
                int[] swappedIndexes = swapIndexes(size);
                renameFiles(listOfFiles, swappedIndexes, musicDirectory.getPath());
            }
        });

        deleteNumerationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(musicDirPath.getText().equals("")) {
                    noPathError();
                    return;
                }

                File[] listOfFiles = musicDirectory.listFiles();
                deleteIndexes(listOfFiles, musicDirectory.getPath());
            }
        });
    }

    private int[] swapIndexes(int arraySize){
        int[] array = new int[arraySize];
        for(int i=0; i<arraySize; i++){
            array[i] = i+1;
        }
        Random generator = new Random();
        int idx1, idx2, temp;
        for(int i=0; i<SWAP_AMOUNT; i++){
            idx1 = generator.nextInt(arraySize);
            idx2 = generator.nextInt(arraySize);
            temp = array[idx1];
            array[idx1] = array[idx2];
            array[idx2] = temp;
        }
        return array;
    }

    private File[] deleteIndexes(File[] filesList, String dirPath){
        File newFile;
        File[] newFilesArray = new File[filesList.length];
        String fileName;
        char[] charArray;
        int underscoreIdx=0;
        for(int i=0; i<filesList.length; i++){
            fileName = filesList[i].getName();
            charArray = fileName.toCharArray();
            for(int j=0; j<charArray.length; j++){
                if(charArray[j] == '_'){
                    underscoreIdx = j+1;
                    break;
                }
            }
            fileName = fileName.substring(underscoreIdx);
            newFile = new File(dirPath + "/" + fileName);
            filesList[i].renameTo(newFile);
            newFilesArray[i] = newFile;
        }
        return newFilesArray;
    }

    private void renameFiles(File[] listOfFiles, int[] swappedIndexes, String dirPath){
        File tempFile;
        File newFile;
        for (int i = 0; i < swappedIndexes.length; i++) {
            tempFile=listOfFiles[i];
            newFile = new File(dirPath + "/" + swappedIndexes[i] + "_" +tempFile.getName());
            tempFile.renameTo(newFile);
        }
    }

    private void noPathError(){
        JOptionPane.showMessageDialog(frame, "Nie wskazano ścieżki katalogu", "Błąd ścieżki",JOptionPane.ERROR_MESSAGE );
    }

    private void checkForMP3(){
        File[] listOfFiles = musicDirectory.listFiles();
        for(File f : listOfFiles){
            if(getExtension(f.getName()).equals("mp3")) return;
        }
        JOptionPane.showMessageDialog(frame, "W tym katalogu nie znaleziono pliku MP3", "Ostrzeżenie",JOptionPane.WARNING_MESSAGE );
    }

    private String getExtension(String fileName){
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            return fileName.substring(i+1);
        }else{
            return "";
        }
    }

}
