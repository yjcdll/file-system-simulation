package FileSystem;

/**
 * 文件分配表
 * @author Administrator
 */
public class FAT {
    private final static byte[] fat=new byte[Disk.DISK_BLOCK_NUMBER];
    private final static byte[] freeList=new byte[Disk.DISK_BLOCK_NUMBER];//空闲磁盘块的列表。用栈实现
    private static byte top=0;//栈顶指针，指向栈顶的下一个
    static{
        //FAT放在磁盘Disk的第0和1块(Disk[0]和Disk[1])。磁盘的第2块放根目录的内容。
        fat[0]=1;fat[1]=-1;fat[2]=-1;
        for(byte i=Disk.DISK_BLOCK_NUMBER-1;i>=3;i--) free(i);
    }
    private FAT(){
    }
    /**
     * 向FAT申请一块空闲的磁盘块。若磁盘空间已满，会提示的。
     * @return 一个空闲磁盘块号
     * @throws java.lang.Exception 若磁盘空间已满
     */
    public static byte allocate() throws Exception{//出栈
        if(top==0) throw new Exception("磁盘空间已满！");
        top--;return freeList[top];//返回一个空闲磁盘块号
    }
    /**
     * 释放第diskNum个磁盘块。
     * @param diskNum 
     */
    public static void free(byte diskNum){//入栈
        freeList[top]=diskNum;top++;fat[diskNum]=0;
    }
    /**
     * 释放以第startNum块为开始块的文件的所有磁盘块号。
     * @param startDiskNum 
     */
    public static void freeFile(byte startDiskNum){
        byte num=0,numNext=startDiskNum;
        while(numNext!=-1){
            num=numNext;numNext=next(numNext);
            free(num);
        }
    }
    /**
     * 将FAT中的第num项的值设为nextNum。即，令文件在磁盘上的第num块的下一块为第nextNum块。
     * @param num
     * @param nextNum 
     */
    public static void setNext(byte num,byte nextNum){
        fat[num]=nextNum;
    }
    /**
     * 得到第num块的下一块号
     * @param num
     * @return 
     */
    public static byte next(byte num){
        return fat[num];
    }
    /**
     * 将FAT中的第num项的值设为-1。即，令文件在磁盘上的第num块为最后一块。
     * @param num 
     */
    public static void setLast(byte num){
        fat[num]=-1;
    }
    /**
     * 判断第num块是否文件的最后一块。
     * @param num
     * @return 
     */
    public static boolean isLast(byte num){
        return fat[num]==-1;
    }
    /**
     * 判断第num块是否文件的倒数第二块。要保证文件至少有两块。
     * @param num
     * @return 
     */
    public static boolean isLastButOne(byte num){
        if(next(num)==-1) return false;//文件只有一块
        return isLast(next(num));
    }
    /**
     * 返回以第startNum块为开始块的文件的最后一块磁盘块号。
     * @param startNum
     * @return 
     */
    public static byte getLast(byte startNum){
        byte num;
        for(num=startNum;!isLast(num);num=next(num)){}
        return num;
    }
    /**
     * 返回以第startNum块为开始块的文件的倒数第二块磁盘块号。假设文件至少有2块。
     * @param startNum
     * @return 
     */
    public static byte getLastButOne(byte startNum){
        byte num;
        for(num=startNum;!isLast(next(num));num=next(num)){}
        return num;
    }
    /**
     * 返回空闲的磁盘块数。
     * @return 
     */
    public static int freeNumber(){
        return top;
    }
    /**
     * 返回以第startNum块为开始块的文件的第index块所在的磁盘块。
     * @param startNum
     * @param index
     * @return 
     */
    public static byte getBlockNum(byte startNum,int index){
        byte num=startNum;
        for(int i=0;i<index;i++)
            num=next(num);
        return num;
    }
    /**
     * 得到以第startDiskNum块为开始块的文件的所有磁盘块号
     * @param startDiskNum
     * @param length 文件所占的磁盘块数
     * @return 
     */
    public static byte[] getblocksNum(byte startDiskNum,int length){
        byte num=startDiskNum;
        byte[] blocksNum=new byte[length];
        for(int i=0;i<length;i++){
            blocksNum[i]=num;num=FAT.next(num);
        }
        return blocksNum;
    }

    public static byte[] getFat() {
        return fat;
    }
}
