package ui;

import FileSystem.CurrentPath;
import FileSystem.Directory;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class DirectoryIcon extends MyFileIcon{//图标面板上的文件夹图标
    private final Directory directory;
    
    public DirectoryIcon(Directory directory){
        super(directory.getName(),new ImageIcon("image/directory.png"));
        //this.setRolloverIcon(new ImageIcon("image/folder1.jpg"));
        this.directory=directory;    
        
        delete.addActionListener((ActionEvent e) -> {
            if(!directory.delete()){
                JOptionPane.showMessageDialog(null,"非空文件夹不可以删除哩！");
                return;
            }
            Content.remove(this);//在面板上删除此图标
            Tree.removeNode(directory);
            if(CurrentPath.getDirectory()!=CurrentPath.root())//要更新新建的文件的父亲的对象，因为它长度变小了
                Tree.replaceNode(CurrentPath.getDirectory());
        });
        open.addActionListener((ActionEvent e) -> {
            openAction();
        });
        attribute.addActionListener((ActionEvent e) -> {
            FolderAttribute folderAttribute = new FolderAttribute(directory);
        });
        rename.addActionListener((ActionEvent e) -> {
            String text = textField.getText();
            if("".equals(text)){
                JOptionPane.showMessageDialog(null,"请输入文件名。");
                textField.setText(directory.getName());
            }
            else if(text.getBytes().length>20){
                JOptionPane.showMessageDialog(null,"文件名不能超过20字节。");
                textField.setText(directory.getName());
            }
            else if(directory.getParent().isExisted(directory,text)){
                JOptionPane.showMessageDialog(null,"此文件名已存在。请换个文件名！");
                textField.setText(directory.getName());
            }            
            else{//成功重命名
                directory.setName(text);directory.getParent().sonChanged(directory);
                Tree.renameNode(directory);
                Tree.repaint();Content.getPanel().updateUI();
            }
        });
        button.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2)//双击
                    openAction();
            }
        });
    }
    private void openAction(){
        if(directory!=CurrentPath.root()) CurrentPath.cd(directory);
        Address.update();
        Content.update();
    }
}
