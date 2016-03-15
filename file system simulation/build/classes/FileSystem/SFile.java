package FileSystem;

import java.util.Arrays;
import ui.DiskMonitor;

/**
 * 模拟的文件数据结构。在磁盘Disk中存放其内容。
 * @author Administrator
 */
public class SFile extends MyFile{
    
    public SFile(){
        this(new Directory[0],"",(byte)0,(byte)0,(byte)0);
    }

    public SFile(Directory[] path, String name, byte attribute, byte startDiskNumber, short length) {
        super(path, name, attribute, startDiskNumber, length);
        super.setDirectory(false);
        //System.out.println(information());
    }
    
    public SFile(Directory[] path, String name, byte attribute, byte startDiskNumber, short length,long createTime) {
        super(path,name, attribute,startDiskNumber,length,createTime);
    }
    
    /**
     * 打开文件
     * @return 该文件里的内容
     */
    public String open(){
        OpenedFileList.add(this);
        String content="";
        byte num=startDiskNumber;
        for(;!FAT.isLast(num);num=FAT.next(num))
            content+=new String(Disk.read(num));
        //若最后一块磁盘是满的，则要特判，因为求余的结果是0
        int writeLen;
        if(length==0) writeLen=0;
        else{
            writeLen=length%Disk.BYTES_PER_BLOCK;
            if(writeLen==0) writeLen=Disk.BYTES_PER_BLOCK;
        }
        content+=new String(Disk.read(num),0,writeLen);
        return content;
    }
    /**
     * 将content的内容保存到文件（即磁盘Disk）
     * @param content
     * @throws java.lang.Exception
     */
    public void save(String content) throws Exception{
        byte[] bytes = content.getBytes();
        int blocksNumberNew=(bytes.length+Disk.BYTES_PER_BLOCK-1)/Disk.BYTES_PER_BLOCK;//新内容所占的磁盘块数
        int blocksNumberOld=(this.length+Disk.BYTES_PER_BLOCK-1)/Disk.BYTES_PER_BLOCK;//向上取整
        //若是空文件，也是会占一个磁盘块的
        if(bytes.length==0) blocksNumberNew=1;
        if(this.length==0) blocksNumberOld=1;
        
        if(blocksNumberOld+FAT.freeNumber()<blocksNumberNew)
            throw new Exception("Disk full");
        
        byte[] blocksNum=new byte[blocksNumberNew];//存放新内容所需之所有磁盘块号
        if(blocksNumberOld<blocksNumberNew){//旧文件所占的空间不足以存放新内容
            //要申请新的磁盘空间            
            byte num=this.startDiskNumber;
            for(int i=0;i<blocksNumberOld;i++){
                blocksNum[i]=num;num=FAT.next(num);
            }
            num=FAT.getLast(this.startDiskNumber);byte space=0;
            for(int i=blocksNumberOld;i<blocksNumberNew;i++){
                space=FAT.allocate();FAT.setNext(num,space);
                blocksNum[i]=space;num=space;
            }
            FAT.setLast(space);
        }
        else if(blocksNumberOld==blocksNumberNew){//若所需的磁盘块数不变
            byte num=this.startDiskNumber;
            for(int i=0;i<blocksNumberNew;i++){
                blocksNum[i]=num;num=FAT.next(num);
            }
        }
        else{//若新内容所需空间少了
            byte num=this.startDiskNumber;
            blocksNum[0]=num;//每个文件至少占一个磁盘块
            for(int i=1;i<blocksNumberNew;i++){
                num=FAT.next(num);blocksNum[i]=num;
            }
            byte numFree=FAT.next(num);
            FAT.setLast(num);
            //将文件所占的多余的磁盘空间释放
            for(int i=blocksNumberNew;i<blocksNumberOld;i++){
                byte numNext=FAT.next(numFree);
                FAT.free(numFree);numFree=numNext;
            }
        }
        //写入磁盘
        int remain=bytes.length;
        for(int i=0;i<blocksNumberNew;i++){
            if(remain>=Disk.BYTES_PER_BLOCK){
                Disk.write(bytes,blocksNum[i],bytes.length-remain,Disk.BYTES_PER_BLOCK);
                remain-=Disk.BYTES_PER_BLOCK;
            }
            else Disk.write(bytes,blocksNum[i],bytes.length-remain,remain);
        }
        DiskMonitor.update();
        this.length=(short)bytes.length;
        this.getParent().sonChanged(this);
    }
    /**
     * 通知其父目录：删除此文件。并释放所占磁盘资源。
     * @return 成功返回true，失败返回false
     */
    public boolean delete(){
        if(OpenedFileList.contains(this)) return false;
        else{
            FAT.freeFile(this.startDiskNumber);
            DiskMonitor.update();
            //System.out.println(this+"delete");
            this.getParent().deleteSon(this);return true;
        }
    }

}
