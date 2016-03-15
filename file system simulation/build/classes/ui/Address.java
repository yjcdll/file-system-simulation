package ui;

import FileSystem.CurrentPath;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Administrator
 */
public class Address{//界面的最上面一行的地址栏标签、地址栏和返回上一级按钮，放在panel中
    private final static JPanel panel=new JPanel();
    private final static JLabel label=new JLabel("地址");
    private final static JTextField textField=new JTextField();//此处显示地址
    private final static ImageIcon icon=new ImageIcon("image/up.png");
    private final static JButton button;//返回上一级按钮，点击时，先判断当前目录是否根目录。不是根目录则可以返回上一级
    private Address(){
    }
    static{
        ImageIcon image=new ImageIcon(icon.getImage().getScaledInstance(30,30,Image.SCALE_SMOOTH));
        button=new JButton(image);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setToolTipText("返回上一级");
        //this.setBackground(Color.red);
        //label.setFont(new Font("微软雅黑",Font.BOLD,15));
        update();
        textField.setEditable(false);
        label.setBorder(new EmptyBorder(5,5,5,0));
        panel.setLayout(new BorderLayout(5,5));
        panel.add(label,BorderLayout.WEST);
        panel.add(textField);
        panel.add(button,BorderLayout.EAST);
        button.addActionListener((ActionEvent e) -> {
            if(CurrentPath.root()!=CurrentPath.getDirectory()){
                CurrentPath.cdParent();
                Address.update();
                Content.update();
            }
        });
    }
    public static JPanel getPanel(){
        return panel;
    }
    public static void update(){
        textField.setText(CurrentPath.getString());
    }
}
