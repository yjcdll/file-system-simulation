package ui;

import FileSystem.CurrentPath;
import FileSystem.Directory;
import FileSystem.MyFile;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author Administrator
 */
public class Tree{
    private final static JScrollPane pane=new JScrollPane();
    private final static DefaultMutableTreeNode root = new DefaultMutableTreeNode(CurrentPath.root());//根目录作为根结点
    private final static DefaultTreeModel model=new DefaultTreeModel(root);
    private final static JTree tree=new JTree(model);
    
    //private final JScrollPane pane=new JScrollPane(tree);
    private Tree(){
    }
    static{
        pane.setViewportView(tree);
        //this.add(pane);
        tree.setToolTipText("资源管理器");
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        //root.add(new DefaultMutableTreeNode("son"));
        //root.add(new DefaultMutableTreeNode("daughter"));
        tree.addTreeSelectionListener((TreeSelectionEvent e) -> {
            //获取被选中的结点
            DefaultMutableTreeNode selected=(DefaultMutableTreeNode)(tree.getLastSelectedPathComponent());
            if(selected==null) return ;
            MyFile myfile=(MyFile)(selected.getUserObject());//获取被选中的对象
            //System.out.println("选中了"+myfile.information());
            CurrentPath.setPath(myfile.getPath());
            if(myfile instanceof Directory){
                if((Directory)myfile!=CurrentPath.root()) CurrentPath.cd((Directory)myfile);
                Content.update();
            }
            Address.update();
            //selected.add(new DefaultMutableTreeNode("a"));
            /*//显示被选中结点的所有子文件
            if(myfile instanceof Directory){
                Directory directory=(Directory)myfile;
                Directory[] path=new Directory[0];
                if(directory!=CurrentPath.root()){//若是根目录，其子文件的path长度也是0
                    //求出该结点的子文件的path属性
                    path=new Directory[myfile.getPath().length+1];
                    //即该结点的path加上该结点
                    System.arraycopy(myfile.getPath(),0,path,0,myfile.getPath().length);
                    path[path.length-1]=directory;
                }
                
                //在JTree的该结点中添加儿子们（子文件）
                MyFile[] listFiles = directory.listFiles();
                
                //for(int i=0;i<selected.getChildCount();i++)
                    //model.removeNodeFromParent((MutableTreeNode)(selected.getChildAt(i)));
                
                for (MyFile listFile : listFiles) {
                    //System.out.println(listFile.information());
                    listFile.setPath(path);
                    //selected.add(new DefaultMutableTreeNode(listFile));
                    model.insertNodeInto(new DefaultMutableTreeNode(listFile),selected,selected.getChildCount());
                }
                tree.scrollPathToVisible(new TreePath(selected.getChildAt(0)));
                System.out.println(selected.getChildCount());
            }
            */
        });
    }
    public static JScrollPane getPane(){
        return pane;
    }
    /**
     * 将child对象加到当前路径。新建文件或新建文件夹时候用
     * @param child 
     */
    public static void addObject(Object child) {
        DefaultMutableTreeNode parent=find(CurrentPath.getPath());
        //System.out.println("parent"+parent);
        //((MyFile)child).setName("hahaha");
        DefaultMutableTreeNode childNode=new DefaultMutableTreeNode(child);
        //System.out.println("childNode"+childNode.getUserObject());
        //It is key to invoke this on the TreeModel, and NOT DefaultMutableTreeNode
        model.insertNodeInto(childNode,parent,parent.getChildCount());
        //Make sure the user can see the lovely new node.
        tree.scrollPathToVisible(new TreePath(childNode.getPath()));
    }
    
    /**
     * 删除当前路径下child对象对应的结点。文件或文件夹删除时候用
     * @param child 
     */
    public static void removeNode(Object child) {
        DefaultMutableTreeNode parent=find(CurrentPath.getPath()),childNode=null;
        //System.out.println("remove:"+parent);
        for(int i=0;i<parent.getChildCount();i++){
            DefaultMutableTreeNode node=(DefaultMutableTreeNode)parent.getChildAt(i);
            if(node.getUserObject().equals(child)) {childNode=node;break;}
        }
        model.removeNodeFromParent(childNode);
    }
    /**
     * 修改当前路径的“myfile对象对应的结点（equals）”的UserObject为myfile。（用在文件重命名时）
     * @param myfile 
     */
    public static void renameNode(MyFile myfile){
        DefaultMutableTreeNode parent=find(CurrentPath.getPath());
        for(int i=0;i<parent.getChildCount();i++){
            DefaultMutableTreeNode node=(DefaultMutableTreeNode)parent.getChildAt(i);
            if(node.getUserObject().equals(myfile)){
                node.setUserObject(myfile);break;
            }
        }
    }
    /**
     * 修改“directory对象对应的结点（equals）”的UserObject为directory。（用在文件长度被修改时）
     * @param directory 
     */
    public static void replaceNode(Directory directory){
        DefaultMutableTreeNode parent=find(directory.getPath());
        for(int i=0;i<parent.getChildCount();i++){
            DefaultMutableTreeNode node=(DefaultMutableTreeNode)parent.getChildAt(i);
            if(node.getUserObject().equals(directory)){
                node.setUserObject(directory);break;
            }
        }
    }
    /**
     * 在tree中沿着path查找对应的树结点。找到相应的树结点才能修改和删除
     * @param path
     * @return 当前路径数组最后一个元素所对应的树结点
     */
    public static DefaultMutableTreeNode find(Directory[] path){
        DefaultMutableTreeNode parent=root,son=null;
        //System.out.println("path"+Arrays.toString(path));
        for(int i=0;i<path.length;i++){
            for(int j=0;j<parent.getChildCount();j++){                
                DefaultMutableTreeNode node=(DefaultMutableTreeNode)parent.getChildAt(j);
                Object o=node.getUserObject();
                //System.out.println("path[i]"+path[i]+"   parent.getChildAt(j)"+o+"??"+path[i].equals(o));
                
                if(path[i].equals(o)){
                    son=(DefaultMutableTreeNode)parent.getChildAt(j);break;
                }
            }
            parent=son;
        }
        return parent;
    }
    public static void repaint(){
        tree.repaint();
    }
}
