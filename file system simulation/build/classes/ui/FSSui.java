package ui;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.api.SubstanceConstants;
import org.jvnet.substance.api.SubstanceSkin;
import org.jvnet.substance.skin.CremeSkin;
import org.jvnet.substance.skin.SubstanceRavenGraphiteGlassLookAndFeel;
import org.jvnet.substance.watermark.SubstanceImageWatermark;

public class FSSui extends JFrame{//主界面
    //private final Tree tree=new Tree();
    //private final Content content=new Content();
    final JSplitPane splitPane1=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,false,Tree.getPane(),Content.getPane());//分割面板，可以拖动组件所占的空间。
    //这样可以自由地调整资源管理器，图标面板，磁盘监视的大小

    //private final DiskMonitor diskMonitor=new DiskMonitor();
    final JSplitPane splitPane2=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,false,splitPane1,DiskMonitor.getPane());
    //private final Address address=new Address();
    public FSSui(){
        this.setLayout(new BorderLayout(5,5));
        //this.add(tree,BorderLayout.WEST);
        //this.add(content,BorderLayout.CENTER);
        //this.add(tree);
        this.add(Address.getPanel(),BorderLayout.NORTH);//地址栏放在最上面
        splitPane1.setDividerSize(6);
        //this.add(splitPane1,BorderLayout.WEST);
        splitPane2.setDividerSize(6);
        this.add(splitPane2);//资源管理器，图标面板，磁盘监视放中间
        this.setTitle("模拟文件系统");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(900,700);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        //splitPane2.setDividerLocation(0.9);
        splitPane1.setDividerLocation(0.2); 
        splitPane2.setDividerLocation(0.8); 
        this.addComponentListener(new ComponentAdapter(){ 
            @Override
            public void componentResized(ComponentEvent e) { 
                splitPane1.setDividerLocation(0.2); 
                splitPane2.setDividerLocation(0.8); 
            } 
        }); 
    }

    public static void main(String[] args) {
        
        JFrame.setDefaultLookAndFeelDecorated(true); //加上此语句连同窗体外框也改变
        JDialog.setDefaultLookAndFeelDecorated(true); //加上此语句会使弹出的对话框也改变
         try {
                
                SubstanceImageWatermark watermark = new SubstanceImageWatermark(FSSui.class.getResourceAsStream("/img/4.jpg"));
                //SubstanceImageWatermark watermark = new SubstanceImageWatermark(FSSui.class.getResourceAsStream(""));
                watermark.setKind(SubstanceConstants.ImageWatermarkKind.SCREEN_CENTER_SCALE);
                watermark.setOpacity((float) 1.0); //更改水印透明度
                UIManager.setLookAndFeel(new SubstanceRavenGraphiteGlassLookAndFeel ());
                SubstanceSkin skin = new CremeSkin().withWatermark(watermark);

                //此语句设置外观
                SubstanceLookAndFeel.setSkin(skin);

            } catch (UnsupportedLookAndFeelException ex) {
                Logger.getLogger(FSSui.class.getName()).log(Level.SEVERE, null, ex);
            }
            java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    FSSui frame = new FSSui();//实例化窗体对象
                }
            });    
    }
}
