package FileSystem;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 当前目录
 * @author Administrator
 */
public class CurrentPath {
    private static final Directory root=new Directory(new Directory[0],"C:",(byte)0,(byte)2,(short)0);//根目录。根目录的控制信息只放在内存里，没有放在磁盘里。
    //private static final ArrayList <Directory>path=new ArrayList<>();//当前目录的路径
    private static Directory[] path=new Directory[0];//当前路径。数组的第i项是第i+1项的父目录，对于i=0到path.length-2都成立。即将祖先目录放在数组前面，越祖先越前面。数组最后一项就是当前目录。根目录不存放。所以第0项必定是根目录下的文件夹。
    private CurrentPath(){
    }
    
    /**
     * 进入指定目录d。该目录必须为当前目录的子目录。
     * @param  d
     */
    public static void cd(Directory d){
        Directory[] newPath=new Directory[path.length+1];
        System.arraycopy(path,0,newPath,0,path.length);
        newPath[newPath.length-1]=d;
        path=newPath;
    }
    /**
     * 进入父目录。若已在根目录，则不需进入。
     */
    public static void cdParent(){
        if(path.length>0){
            Directory[] newPath=new Directory[path.length-1];
            System.arraycopy(path,0,newPath,0,path.length-1);
            path=newPath;
        }
    }
    /**
     * 返回当前路径。
     * @return 
     *//*
    public static Directory[] getPath(){
        Object[] array=path.toArray();
        Directory[] directoryArray=new Directory[array.length];
        for(int i=0;i<array.length;i++)
            directoryArray[i]=(Directory)array[i];
        return directoryArray;
    }*/
    /**
     * 返回当前目录。
     * @return 
     */
    public static Directory getDirectory(){
        if(path.length==0) return root;//当前目录为根目录
        return path[path.length-1];
    }
    
    public static String getString(){
        return getString(path);
    }
    /**
     * 将一个path数组转化成地址栏应该显示的字符串
     * @param path
     * @return 
     */
    public static String getString(Directory[] path){
        String s=root.getName();
        for (Directory path1 : path) {
            s += "\\" + path1.getName();
        }
        return s;
    }
    /**
     * 返回根目录
     * @return 
     */
    public static Directory root(){
        return root;
    }
    
    public static void setPath(Directory[] newPath){
        path=newPath;
    }
    
    public static Directory[] getPath(){
        return path;
    }
}
