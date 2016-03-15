package ui;

import FileSystem.CurrentPath;
import FileSystem.OpenedFileList;
import FileSystem.SFile;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class SFileIcon extends MyFileIcon{
    private final SFile sFile;
    public SFileIcon(SFile sFile){
        super(sFile.getName(),new ImageIcon("image/file.jpg"));
        //this.setRolloverIcon(new ImageIcon("image/file1.jpg"));
        this.sFile=sFile;
                
        open.addActionListener((ActionEvent e) -> {
            openAction();
        });
        delete.addActionListener((ActionEvent e) -> {
            if(!sFile.delete()){
                JOptionPane.showMessageDialog(null,"文件已打开，不可以删除哩！");
                return;
            }
            Content.remove(this);//在面板上删除此图标
            Tree.removeNode(sFile);
            if(CurrentPath.getDirectory()!=CurrentPath.root())//要更新新建的文件的父亲的对象，因为它长度变小了
                Tree.replaceNode(CurrentPath.getDirectory());
        });
        attribute.addActionListener((ActionEvent e) -> {
            FileAttribute fileAttribute = new FileAttribute(sFile);
        });
        rename.addActionListener((ActionEvent e) -> {
            String text = textField.getText();
            if(OpenedFileList.contains(sFile))
                JOptionPane.showMessageDialog(null,"文件已打开，不可以重命名哩！");
            if("".equals(text)){
                JOptionPane.showMessageDialog(null,"请输入文件名。");
                textField.setText(sFile.getName());
            }
            else if(text.getBytes().length>20){
                JOptionPane.showMessageDialog(null,"文件名不能超过20字节。");
                textField.setText(sFile.getName());
            }
            else if(sFile.getParent().isExisted(sFile,text)){
                JOptionPane.showMessageDialog(null,"此文件名已存在。请换个文件名！");
                textField.setText(sFile.getName());
            }
            else{//成功重命名
                sFile.setName(text);sFile.getParent().sonChanged(sFile);
                Tree.renameNode(sFile);
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
        Open open1 = new Open(sFile);
    }
}
