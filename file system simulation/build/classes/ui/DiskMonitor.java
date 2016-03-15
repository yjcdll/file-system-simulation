package ui;

import FileSystem.FAT;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class DiskMonitor{//磁盘监视的JTable
    private final static JScrollPane pane=new JScrollPane();
    private final static String[] cols={"磁盘块号","值"};
    private final static DefaultTableModel model=new DefaultTableModel(){
            @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final static JTable table=new JTable(model);
    static{
        pane.setViewportView(table);
        update();
        table.setToolTipText("磁盘监视");
    }
    private DiskMonitor(){
    }
    public static JScrollPane getPane(){
        return pane;
    }
    /**
     * 只要FAT被刷新（即FAT.allocate()或FAT.free()被调用了），就要调用这个方法。
     * 我在外面调用这个方法后，table就显示fat的内容。
     */
    public static void update(){
        byte[] fat=FAT.getFat();
        Object[][] data = new Object[fat.length][2];
        for(int i=0;i<fat.length;i++){
            data[i][0] = i;  data[i][1] = fat[i];
        }
        model.setDataVector(data,cols);
    }
    
}
