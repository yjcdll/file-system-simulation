package FileSystem;

import java.util.Arrays;
import ui.DiskMonitor;

/**
 * 模拟的目录数据结构。在磁盘Disk中存放子目录和文件的控制信息。
 * @author Administrator
 */
public class Directory extends MyFile{
    
    public Directory(){
        this(new Directory[0],"",(byte)0,(byte)0,(byte)0);
    }

    public Directory(Directory[] path, String name, byte attribute, byte startDiskNumber, short length) {
        super(path, name, attribute, startDiskNumber, length);
        super.setDirectory(true);
        //System.out.println(information());
    }

    public Directory(Directory[] path, String name, byte attribute, byte startDiskNumber, short length,long createTime) {
        super(path,name, attribute,startDiskNumber,length,createTime);
    }
    
    /**
     * 在该目录下创建文件或目录f。即，将f的控制信息写入该目录（磁盘）。
     * @param f 
     * @throws java.lang.Exception 
     */
    public void create(MyFile f) throws Exception{
        short filesLength=(short)(this.length/MyFile.ITEM_LENGTH);//该目录下的子文件或子目录数
        //System.out.println("filesLength"+filesLength);
        if((filesLength&1)==0&&filesLength!=0){//若有偶数(且非0)个子文件或子目录
            //要申请一块新的磁盘空间
            byte space=FAT.allocate();
            //System.out.println("space"+space);
            Disk.write(f.toBytes(),space,MyFile.ITEM_LENGTH);
            byte last=FAT.getLast(this.startDiskNumber);
            FAT.setNext(last,space);FAT.setLast(space);
        }
        else if(filesLength==0){
            Disk.write(f.toBytes(),this.startDiskNumber,MyFile.ITEM_LENGTH);
        }else{
            byte last=FAT.getLast(this.startDiskNumber);
            Disk.write(f.toBytes(),last,(byte)MyFile.ITEM_LENGTH,MyFile.ITEM_LENGTH);
        }
        this.length+=MyFile.ITEM_LENGTH;
        DiskMonitor.update();
        //该目录下新增文件之后，该目录的length属性改变了，故要在磁盘上通知该目录的父目录修改其控制信息
        if(this!=CurrentPath.root())//若是根目录，则信息就不用写回磁盘了
            this.getParent().sonChanged(this);
    }

    /**
     * 根目录不允许删除。非空目录不允许删除。
     * 通知其父目录：删除该目录。并释放所占磁盘资源。
     * @return 
     */
    public boolean delete(){
        if(this.length!=0) return false;//非空目录不允许删除。
        FAT.freeFile(this.startDiskNumber);
        DiskMonitor.update();
        //System.out.println(this+"delete");
        this.getParent().deleteSon(this);
        return true;
    }
    /**
     * 删除该目录下的子文件或子目录son
     * @param son
     */
    public void deleteSon(MyFile son){
        /*
        int deleteIndex=0;MyFile[] files=this.listFiles();
        for(int i=0;i<files.length;i++)//查找子目录或子文件son在该目录下存放的编号。从0开始。
        if(files[i].name.equals(son.name)){deleteIndex=i;break;}
        if(deleteIndex!=files.length-1){//要删除的文件不是最后一个文件
        //找到被删除的文件所在的磁盘块
        byte deleteBlockNum=FAT.getBlockNum(this.startDiskNumber,deleteIndex/2);
        //用最后一个文件的控制信息替换被删除的文件的控制信息
        int offset=deleteIndex&1;//判断写在磁盘块的前半块(0)还是后半块(1)
        Disk.write(files[files.length-1].tobytes(),deleteBlockNum,(byte)(offset*MyFile.ITEM_LENGTH),MyFile.ITEM_LENGTH);
        this.length-=MyFile.ITEM_LENGTH;
        if((files.length&1)!=0){ //若有奇数个子文件或子目录
        if(!FAT.isLast(this.startDiskNumber)){ //若不是只占有一个磁盘块
        //要释放掉所占用的最后一块磁盘块
        FAT.free(FAT.getLast(this.startDiskNumber));
        //倒数第二块变成最后一块
        FAT.setLast(FAT.getLastButOne(this.startDiskNumber));
        }
        }
        }*/
        //System.out.println(this+"deleteSon");
        MyFile[] listFiles = this.listFiles();
        /*System.out.println(this+"listfile:");
        for(MyFile m:listFiles){
            System.out.println(m.information());
        }
        System.out.println(":listfinish");*/
        //可以知道该目录下至少有一个文件
        MyFile[] newlistFiles=new MyFile[listFiles.length-1];
        //newlistFiles存放除被删除文件外的所有子文件
        for(int i=0,cur=0;i<listFiles.length;i++){
            //System.out.println("son"+son+"  i"+i+"  cur"+cur);
            if(!listFiles[i].equals(son)){
                newlistFiles[cur++]=listFiles[i];
            }
        }
        
        //将newlistFiles写回磁盘
        if(listFiles.length!=1){ //若该目录只有1个子文件，就直接把占用的磁盘块清空
            byte[] blocksNum = FAT.getblocksNum(this.startDiskNumber,(listFiles.length+1)/2);
            //System.out.println(Arrays.toString(blocksNum));
            for(int i=0,cur=0;i<blocksNum.length-1;i++){
                Disk.write(newlistFiles[cur++].toBytes(),blocksNum[i],(byte)0,MyFile.ITEM_LENGTH);
                Disk.write(newlistFiles[cur++].toBytes(),blocksNum[i],(byte)MyFile.ITEM_LENGTH,MyFile.ITEM_LENGTH);
            }
                
            if((listFiles.length&1)==0){//原来有偶数个文件，那么所占的磁盘块不变
                Disk.write(newlistFiles[newlistFiles.length-1].toBytes(),blocksNum[blocksNum.length-1],(byte)0,MyFile.ITEM_LENGTH);
            } else{//原来有奇数个文件，那么所占磁盘块数要少1
                FAT.setLast(blocksNum[blocksNum.length-2]);//倒数第二个磁盘块变成最后一块磁盘块（这里已保证至少有2个磁盘块）
                FAT.free(blocksNum[blocksNum.length-1]);//释放最后一块磁盘块
            }
        }
        this.length-=MyFile.ITEM_LENGTH;
        DiskMonitor.update();
        if(this!=CurrentPath.root())
            this.getParent().sonChanged(this);
    }
    

    /**
     * 获取该目录下的所有子文件或子目录
     * @return 该目录下的所有子文件或子目录列表
     */
    public MyFile[] listFiles(){
        byte[][] bytes=this.toArray();
        MyFile[] files=new MyFile[bytes.length];
        
        for(int i=0;i<bytes.length;i++){
            files[i]=toInfo(bytes[i]);
            //System.out.println(Arrays.toString(bytes[i]));
            //System.out.println(files[i].information());
            if(files[i].isDirectory()) files[i]=files[i].toDirectory();
            else files[i]=files[i].toSFile();
        }
        
        return files;
    }
    /*不需要save，因为每次修改都是直接在磁盘上进行的*/

    /**
     * 将该目录下，在磁盘上的内容，以一个子目录或子文件的控制信息为单位，读出。
     * 第二维是子文件数，第一维是每个文件控制信息的长度（32B）。
     * @return 
     */
    public byte[][] toArray(){
        byte[][] bytes=new byte[length/32][32];
        byte num=startDiskNumber;int times=length/64;
        int cur=0;//当前读到的文件数
        
        for(int i=0;i<times;i++){
            //System.out.println("磁盘块"+num+"   i"+i+"   times"+times);
            System.arraycopy(Disk.read(num),0,bytes[cur++],0,32);
            System.arraycopy(Disk.read(num),32,bytes[cur++],0,32);
            num=FAT.next(num);
        }
        if(length%64!=0) System.arraycopy(Disk.read(num),0,bytes[cur],0,32);//有奇数个文件
        /*System.out.println("open:");
        for(int i=0;i<bytes.length;i++){
            System.out.println(Arrays.toString(bytes[i]));
            System.out.println(toInfo(bytes[i]).information());
        }
        System.out.println(":OK");*/
        return bytes;
    }
    /**
     * 判断该目录下是否有文件名为name的文件，且和myfile不是同一个文件的。
     * @param myfile
     * @param name
     * @return 有则true，无则false
     */
    public boolean isExisted(MyFile myfile,String name){
        MyFile[] files=this.listFiles();
        for(MyFile f:files)
            if(f.name.equals(name)&&!f.equals(myfile)) return true;
        return false;
    }
    /**
     * 该目录下的子文件或子目录son的控制信息改变了，要写回磁盘。
     * @param son 
     */
    public void sonChanged(MyFile son){
        int sonIndex=0;MyFile[] files=this.listFiles();
        for(int i=0;i<files.length;i++)//查找子目录或子文件son在该目录下存放的编号。从0开始。
            if(files[i].equals(son)){sonIndex=i;break;}
        byte sonBlockNum=FAT.getBlockNum(this.startDiskNumber,sonIndex/2);
        Disk.write(son.toBytes(),sonBlockNum,(byte)((sonIndex&1)*MyFile.ITEM_LENGTH),MyFile.ITEM_LENGTH);
    }
}
