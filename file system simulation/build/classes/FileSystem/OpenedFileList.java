package FileSystem;

import java.util.ArrayList;

/**
 *已打开的文件列表
 * @author Administrator
 */
public class OpenedFileList {
    private static final ArrayList<SFile> list=new ArrayList<>();
    
    private OpenedFileList(){
    }
    /**
     * 文件f是否已打开。即list是否有f
     * @param f
     * @return 
     */
    public static boolean contains(SFile f){
        return list.contains(f);
    }
    /**
     * 打开文件f。即list添加f
     * @param f 
     */
    public static void add(SFile f){
        list.add(f);
    }
    /**
     * 关闭文件f。即list移除f
     * @param f 
     */
    public static void remove(SFile f){
        list.remove(f);
    }
}
