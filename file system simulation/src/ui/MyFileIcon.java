package ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

public class MyFileIcon extends JPanel{
    private final JPopupMenu menu=new JPopupMenu();
    protected final JMenuItem open=new JMenuItem("打开");
    protected final JMenuItem rename=new JMenuItem("重命名");
    protected final JMenuItem delete=new JMenuItem("删除");
    protected final JMenuItem attribute=new JMenuItem("属性");
    protected final JButton button;
    protected final JTextField textField;
    public MyFileIcon(String text,ImageIcon icon){
        Listener listener=new Listener();
        button=new JButton(icon);
        textField=new JTextField(text);
        textField.setEditable(false);
        //textField.setBackground(Color.lightGray);
        //textField.setBorder(new EmptyBorder(0,0,0,0));
        this.setLayout(new BorderLayout());
        this.add(button);
        this.add(textField,BorderLayout.SOUTH);
        //super(text,icon,SwingConstants.CENTER);
        //this.setBackground(Color.red);
        //this.setForeground(Color.GREEN);
        //this.setBorderPainted(false);
        menu.add(open);menu.add(rename);
        menu.add(delete);menu.add(attribute);
        this.addMouseListener(listener);
        button.addMouseListener(listener);
        textField.addMouseListener(listener);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
//        rename.addActionListener();
        rename.addActionListener((ActionEvent e) -> {
            textField.setEditable(true);
        });
    }
    class Listener extends MouseAdapter{
        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getButton()==3)//按下右键
                menu.show(e.getComponent(),e.getX(),e.getY());
        }
    }
}
