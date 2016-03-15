package ui;

import FileSystem.CurrentPath;
import FileSystem.Directory;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;

public class FolderAttribute{

    JFrame frame=new JFrame();
    JTextField textField;
    JLabel labelGetPath;
    JLabel labelType;
    JLabel labelPath;
    JLabel labelSpace;
    JLabel labelGetSpace;
    JLabel labelCreate;
    JLabel labelTime;
    JLabel label7;
    JLabel label8;
    JLabel labelAtt;
    JButton bTrue;
    JButton bFalse;
    JSeparator s1;
    JSeparator s2;
    JSeparator s3;
    JCheckBox checkBox1;
    JCheckBox checkBox2;

    public FolderAttribute(Directory directory) {
               //UIManager.put("Button.font", new Font("宋体", Font.PLAIN, 12));
        //UIManager.put("Label.font", new Font("宋体", Font.PLAIN, 12));
        //UIManager.put("TextField.font", new Font("宋体", Font.PLAIN, 12));
        //UIManager.put("CheckBox.font", new Font("宋体", Font.PLAIN, 12));

        frame.setTitle("属性");
        frame.setResizable(false);
        frame.setSize(330, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        JLabel labelIcon = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Icon/directoryAtrribute.jpg")).getImage().getScaledInstance(25, 30, Image.SCALE_SMOOTH)));
        textField = new JTextField(directory.getName());
        textField.setEditable(false);
        
        labelType = new JLabel("类型   :   Folder ");
        labelPath = new JLabel(" 路径   :");
        labelGetPath = new JLabel(CurrentPath.getString(directory.getPath()));
        labelSpace = new JLabel("占用空间大小：");
        labelGetSpace = new JLabel(String.format("%d", directory.getLength()));
        labelCreate = new JLabel("创建日期：   ");
        labelAtt = new JLabel("属性：");
        bTrue = new JButton("确定");
        bFalse = new JButton("取消");
        s1 = new JSeparator();
        s2 = new JSeparator();
        s3 = new JSeparator();
        checkBox1 = new JCheckBox("只读");
        checkBox2 = new JCheckBox("隐藏");

        JPanel panelAll = new JPanel(new GridLayout(7, 1));//整体的大框

        JPanel panelName = new JPanel(new FlowLayout());//把文件图标和位置添加到框里
        panelName.add(labelIcon);
        panelName.add(textField);
        panelName.setLayout(null);
        panelName.setPreferredSize(new Dimension(50, 600));
        labelIcon.setBounds(10, 5, 35, 30);
        textField.setBounds(70, 9, 150, 20);

        panelAll.setLayout(null);//把文件名这个框添加到最大的框里
        panelAll.add(panelName);
        panelName.setBounds(10, 10, 300, 40);

        panelAll.add(s1);//添加横线
        s1.setBounds(15, 65, 280, 5);

        JPanel panelType = new JPanel(new FlowLayout());//添加类型到小框里
        panelType.add(labelType);
        panelAll.add(panelType);//将类型放进去
        panelType.setBounds(5, 80, 150, 30);

        JPanel panelPath = new JPanel(new FlowLayout());//添加路径进小框
        panelPath.add(labelPath);
        panelPath.add(labelGetPath);
        panelPath.setLayout(null);
        labelPath.setBounds(15, 5, 60, 30);
        labelGetPath.setBounds(85, 5, 200, 30);
        panelAll.add(panelPath);//将路径放进去
        panelPath.setBounds(5, 100, 282, 30);

        panelAll.add(s2);//添加横线
        s2.setBounds(15, 150, 280, 5);

        JPanel panelSpace = new JPanel(new FlowLayout());//添加空间大小进小框
        panelSpace.add(labelSpace);
        panelSpace.add(labelGetSpace);
        panelSpace.setLayout(null);
        labelSpace.setBounds(20, 2, 100, 30);
        labelGetSpace.setBounds(120, 2, 50, 30);
        panelAll.add(panelSpace);//将空间大小放进去
        panelSpace.setBounds(5, 163, 200, 30);

        JPanel panelCreate = new JPanel(new FlowLayout());//添加时间进小框
        panelCreate.add(labelCreate);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long createTime = directory.getCreateTime();
        Date d = new Date(createTime);
        //System.out.println(format.format(d));
        labelTime = new JLabel(format.format(d));//把创建时间加进去
        panelCreate.add(labelTime);
        panelCreate.setLayout(null);
        labelCreate.setBounds(20, 5, 100, 30);
        labelTime.setBounds(95, 5, 250, 30);
        panelAll.add(panelCreate);//将时间放进去
        panelCreate.setBounds(5, 200, 238, 30);

        panelAll.add(s3);//添加横线
        s3.setBounds(15, 240, 280, 5);

        //JPanel panelAtt = new JPanel(new FlowLayout());//添加只读隐藏进小框                                                   
        panelAll.add(labelAtt);//将属性放进去
        labelAtt.setBounds(25, 260, 40, 30);
        panelAll.add(checkBox1);
        checkBox1.setBounds(90, 260, 60, 30);//只读
        panelAll.add(checkBox2);
        checkBox2.setBounds(160, 260, 60, 30);//隐藏
        if(directory.isReadonly()) checkBox1.setSelected(true);
        else checkBox1.setSelected(false);
        if(directory.isHidden()) checkBox2.setSelected(true);
        else checkBox2.setSelected(false);
        
        /*JPanel panelButton = new JPanel();//添加按钮
        panelButton.add(bTrue);
        // bTure.setBounds(10,10,30,20);
        panelButton.add(bFalse);
        //  bFalse.setBounds(60,10,40,40);
        panelAll.add(panelButton);//将按钮放进去
        panelButton.setBounds(50, 320, 220, 30);*/
        panelAll.add(bTrue);
        bTrue.setBounds(70,310,80,30);
        panelAll.add(bFalse);
        bFalse.setBounds(170,310,80,30);

        frame.add(panelAll);

        bTrue.addActionListener((ActionEvent e) -> {
            if(checkBox1.isSelected()) directory.setReadonly(true);
            else directory.setReadonly(false);
            if(checkBox2.isSelected()) directory.setHidden(true);
            else directory.setHidden(false);
            if(directory!=CurrentPath.root())//若是根目录，则信息就不用写回磁盘了
                directory.getParent().sonChanged(directory);
            frame.dispose();
        });

        bFalse.addActionListener((ActionEvent e) -> {
            frame.dispose();
        });

    }

    class JLabel extends javax.swing.JLabel {

        public JLabel() {
            this.setFont(new Font("宋体", Font.PLAIN, 12));
        }

        public JLabel(String text) {
            super(text);
            this.setFont(new Font("宋体", Font.PLAIN, 12));
        }

        public JLabel(Icon image) {
            super(image);
            this.setFont(new Font("宋体", Font.PLAIN, 12));
        }
    }

    class JButton extends javax.swing.JButton {

        public JButton() {
            this.setFont(new Font("宋体", Font.PLAIN, 12));
        }

        public JButton(String text) {
            super(text);
            this.setFont(new Font("宋体", Font.PLAIN, 12));
        }
    }

    class JTextField extends javax.swing.JTextField {

        public JTextField() {
            this.setFont(new Font("宋体", Font.PLAIN, 12));
        }

        public JTextField(String text) {
            super(text);
            this.setFont(new Font("宋体", Font.PLAIN, 12));
        }
    }

    class JCheckBox extends javax.swing.JCheckBox {

        public JCheckBox() {
            this.setFont(new Font("宋体", Font.PLAIN, 12));
        }

        public JCheckBox(String text) {
            super(text);
            this.setFont(new Font("宋体", Font.PLAIN, 12));
        }
    }
}
