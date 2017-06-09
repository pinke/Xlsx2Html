package ru.ntmedia;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class MainDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField srcTextField;
    private JButton setSrcFolderButton;
    private JTextField destTextField;
    private JButton setDestFolderButton;
    private JSeparator separator;
    //private String srcFolder;
    //private String destFolder;

    public MainDialog() {
        System.out.println( getClass().getClassLoader().getResource("convert-16x16.png") );
        Image icon = new ImageIcon(getClass().getClassLoader().getResource("convert-16x16.png")).getImage();
        this.setIconImage(icon);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setComponentsText();
        buttonOK.setEnabled(false);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        setSrcFolderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser fileDialog = new JFileChooser(System.getProperty("user.dir"));
                fileDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if( fileDialog.showOpenDialog(MainDialog.this) == JFileChooser.APPROVE_OPTION ) {
                    srcTextField.setText(fileDialog.getSelectedFile().toString());
                }
            }
        });
        setDestFolderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser fileDialog = new JFileChooser(System.getProperty("user.dir"));
                fileDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if( fileDialog.showSaveDialog(MainDialog.this) == JFileChooser.APPROVE_OPTION ) {
                    destTextField.setText(fileDialog.getSelectedFile().toString());
                }
            }
        });
        srcTextField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent documentEvent) {
                updateOkButton();
            }

            public void removeUpdate(DocumentEvent documentEvent) {
                updateOkButton();
            }

            public void changedUpdate(DocumentEvent documentEvent) {
                updateOkButton();
            }
        });
        destTextField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent documentEvent) {
                updateOkButton();
            }

            public void removeUpdate(DocumentEvent documentEvent) {
                updateOkButton();
            }

            public void changedUpdate(DocumentEvent documentEvent) {
                updateOkButton();
            }
        });


        //
        //setDebugParameters();
    }

    private void updateOkButton() {
        if(srcTextField.getText().equals("") || destTextField.getText().equals("")) {
            buttonOK.setEnabled(false);
        } else {
            buttonOK.setEnabled(true);
        }
    }

    private void setDebugParameters() {
        srcTextField.setText("e:\\tmp\\Excel\\");
        destTextField.setText("e:\\tmp\\Excel\\");
        updateOkButton();
    }
    private void onOK() {
        //System.err.println(srcTextField.getText());
        if(!Files.exists(Paths.get(srcTextField.getText()))) {
            JOptionPane.showMessageDialog(null, "Excel指定的文件目录不存在。请选择另一个目录.", "转换文件", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if(!Files.exists(Paths.get(destTextField.getText()))) {
            JOptionPane.showMessageDialog(null, "保存HTML文件指定的目录不存在。请选择另一个目录.", "转换文件", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        App app = new App(srcTextField.getText(), destTextField.getText());
        try {
            app.convertAllFiles();
            JOptionPane.showMessageDialog(null, "转换成功完成。", "转换文件", JOptionPane.INFORMATION_MESSAGE);
        } catch(Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "转换错误：\n" + e.getMessage(), "转换文件", JOptionPane.WARNING_MESSAGE);
        }

        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    private void setComponentsText() {
//        UIManager.put("FileChooser.openDialogTitleText", "打开");
//        UIManager.put("FileChooser.saveDialogTitleText", "保存");
//        UIManager.put("FileChooser.lookInLabelText", "目录");
//        UIManager.put("FileChooser.openButtonText", "打开");
//        UIManager.put("FileChooser.saveButtonText", "保存");
//        UIManager.put("FileChooser.cancelButtonText", "取消");
//        UIManager.put("FileChooser.fileNameLabelText", "文件名称");
//        UIManager.put("FileChooser.folderNameLabelText", "目录名称");
//        UIManager.put("FileChooser.filesOfTypeLabelText", "文件类型");
//        UIManager.put("FileChooser.openButtonToolTipText", "打开");
//        UIManager.put("FileChooser.cancelButtonToolTipText","取消");
//        UIManager.put("FileChooser.fileNameHeaderText","选择文件");
//        UIManager.put("FileChooser.upFolderToolTipText", "向上");
//        UIManager.put("FileChooser.homeFolderToolTipText","桌面");
//        UIManager.put("FileChooser.newFolderToolTipText","CreateNewFolder");
//        UIManager.put("FileChooser.listViewButtonToolTipText","List");
//        UIManager.put("FileChooser.newFolderButtonText","CreateNewFolder");
//        UIManager.put("FileChooser.renameFileButtonText", "RenameFile");
//        UIManager.put("FileChooser.deleteFileButtonText", "DeleteFile");
//        UIManager.put("FileChooser.filterLabelText", "Типы файлов");
//        UIManager.put("FileChooser.detailsViewButtonToolTipText", "Details");
//        UIManager.put("FileChooser.fileSizeHeaderText","Size");
//        UIManager.put("FileChooser.fileDateHeaderText", "DateModified");
//        UIManager.put("FileChooser.acceptAllFileFilterText", "Все файлы");
    }
}
