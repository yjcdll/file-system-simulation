package FileSystem;

/**
 * 模拟的物理磁盘
 * @author Administrator
 */
public class Disk {
    public static final int DISK_BLOCK_NUMBER=128;//128个物理块
    public static final int BYTES_PER_BLOCK=64;//每个物理块64B
    private static final byte[][] disk=new byte[DISK_BLOCK_NUMBER][BYTES_PER_BLOCK];//物理磁盘
    private Disk(){
    }
    /**
     * 将bytes写入磁盘的第num块。
     * @param bytes 数组长度必须为Disk.BYTES_PER_BLOCK
     * @param num 
     */
    public static void write(byte[] bytes,byte num){
         try {
             if(bytes.length!=BYTES_PER_BLOCK) 
                throw new Exception("写入的字节数必须和每个磁盘块的字节数一致。");
             System.arraycopy(bytes,0,disk[num],0,BYTES_PER_BLOCK);
         } catch (Exception ex) {
             ex.printStackTrace();
         }
    }
    /**
     * 将bytes的前length个字节写入磁盘的第num块的第0字节到第length-1字节。
     * @param bytes 数组长度必须小于等于Disk.BYTES_PER_BLOCK
     * @param num 
     * @param length 
     */
    public static void write(byte[] bytes,byte num,int length){
         try {
             if(length>BYTES_PER_BLOCK) 
                throw new Exception("写入的字节数不能多于每个磁盘块的字节数。");
             System.arraycopy(bytes,0,disk[num],0,length);
         } catch (Exception ex) {
             ex.printStackTrace();
         }
    }
    /**
     * 将bytes的前length个字节写入磁盘的第num块的第start字节到第start+length-1字节。
     * @param bytes 数组长度必须小于等于Disk.BYTES_PER_BLOCK
     * @param num 
     * @param start 
     * @param length 
     */
    public static void write(byte[] bytes,byte num,byte start,int length){
         try {
             if(length>BYTES_PER_BLOCK) 
                throw new Exception("写入的字节数不能多于每个磁盘块的字节数。");
             System.arraycopy(bytes,0,disk[num],start,length);
         } catch (Exception ex) {
             ex.printStackTrace();
         }
    }
    /**
     * 将bytes的第srcPos个字节到第srcPos+length-1个字节写入磁盘的第num块的前length个字节。
     * @param bytes 数组长度必须小于等于Disk.BYTES_PER_BLOCK
     * @param num 
     * @param srcPos 
     * @param length 
     */
    public static void write(byte[] bytes,byte num,int srcPos,int length){
         try {
             if(length>BYTES_PER_BLOCK) 
                throw new Exception("写入的字节数不能多于和每个磁盘块的字节数。");
             System.arraycopy(bytes,srcPos,disk[num],0,length);
         } catch (Exception ex) {
             ex.printStackTrace();
         }
    }

    /**
     * 读磁盘的第num块
     * @param num
     * @return 第num块的内容
     */
    public static byte[] read(byte num){
        return disk[num];
    }
    /*
    /**
     * 读磁盘的第num块的前length个字节
     * @param num
     * @param length
     * @return 第num块的前length个字节的内容
     
    public static byte[] read(byte num,int length){
        return disk[num],0,length;
    }*/
}
