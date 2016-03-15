package ui;

import FileSystem.OpenedFileList;
import FileSystem.SFile;
import com.sun.glass.events.KeyEvent;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

public class Open extends JFrame {
    JMenuItem jMenuItem = new JMenuItem("保存");
    JMenuBar jMenuBar = new JMenuBar();
    JMenu jMenu = new JMenu();
    JTextArea jTextArea = new JTextArea();

    public Open(SFile sFile) {
        super(sFile.getName()+(sFile.isReadonly()?" [只读]":""));
        jTextArea.setTabSize(4);
        jTextArea.setLineWrap(true);// 激活自动换行功能  
        jTextArea.setWrapStyleWord(true);// 激活断行不断字功能  
        jTextArea.setBackground(Color.WHITE);
        jTextArea.setText(sFile.open());
        JScrollPane jscrollPane = new JScrollPane(jTextArea);
        this.add(jscrollPane);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                OpenedFileList.remove(sFile);
            }
        });

        jMenu.setText("文件");
        jMenu.add(jMenuItem);
        jMenuBar.add(jMenu);
        this.setJMenuBar(jMenuBar);
        jMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));
        jMenuItem.setEnabled(!sFile.isReadonly());//只读时，保存菜单不可用
        jMenuItem.addActionListener((ActionEvent e) -> {
            try {
                sFile.save(jTextArea.getText());
            } catch (Exception ex) {
                if("Disk full".equals(ex.getMessage())) JOptionPane.showMessageDialog(null,"磁盘空间不足！");
                else ex.printStackTrace();
            }
        });

        this.setSize(400, 300);
        this.setLocation(400, 200);
        this.setVisible(true);

    }
}
