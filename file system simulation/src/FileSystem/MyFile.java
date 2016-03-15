package FileSystem;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

/**
 * 模拟的文件结构
 * @author Administrator
 */
public class MyFile {
    protected Directory[] path;//此文件的绝对路径。并不写入磁盘Disk，只在内存中。第0个是根目录的名字name，接下来是根目录的子目录（此文件的祖先目录），最后一个是此文件的父目录。
    protected String name;//文件名。最多20个字符(B)。
    protected byte attribute;//文件属性。第2位:文件0,目录1；第1位:非隐藏0,隐藏1；第0位:普通0,只读1。其他5位任意，设为0。
    protected byte startDiskNumber;//起始磁盘盘块号
    protected short length;//文件长度（字节数）
    protected final long createTime;//创建时间
    /**
     * 每个文件控制项在磁盘中所占长度。
     */
    public final static int ITEM_LENGTH=32;
    public MyFile(){
        this(new Directory[0],"",(byte)0,(byte)0,(byte)0);
    }
    public MyFile(String name, byte attribute, byte startDiskNumber, short length,long createTime) {
        this(new Directory[0],name,attribute,startDiskNumber,length,createTime);
    }
    public MyFile(Directory[] path, String name, byte attribute, byte startDiskNumber, short length) {
        this(path,name,attribute,startDiskNumber,length,System.currentTimeMillis());
    }
    public MyFile(Directory[] path, String name, byte attribute, byte startDiskNumber, short length,long createTime) {
        this.path = path;
        this.name = name;
        this.attribute = attribute;
        this.startDiskNumber = startDiskNumber;
        this.length = length;
        this.createTime=createTime;
    }

    /**
     * 将该文件的控制信息变成字节流
     * @return 
     */
    public byte[] toBytes(){
        byte[] info=new byte[ITEM_LENGTH];
        byte[] sname=new byte[20];//name的长度可能不是20
        byte[] nameBytes=name.getBytes();
        System.arraycopy(nameBytes,0,sname,0,nameBytes.length);
        //未初始化的数组元素默认为0
        System.arraycopy(sname,0,info,0,20);//write name
        info[20]=attribute;info[21]=startDiskNumber;
        System.arraycopy(short2byte(length),0,info,22,2);//write length
        System.arraycopy(long2byte(createTime),0,info,24,8);//write createTime
        return info;
    }
    /**
     * 将字节流（32B）变成一个文件的控制信息。
     * @param bytes
     * @return 
     */
    public static MyFile toInfo(byte[] bytes){
        if(bytes.length!=32) return null;
        byte[] name=new byte[20];
        System.arraycopy(bytes,0,name,0,20);
        int namelen;//求出name的实际长度
        for(namelen=0;namelen<20;namelen++) if(name[namelen]==0) break;
        byte[] length=new byte[2];
        System.arraycopy(bytes,22,length,0,2);
        byte[] createTime=new byte[8];
        System.arraycopy(bytes,24,createTime,0,8);
        return new MyFile(new String(name,0,namelen),bytes[20],bytes[21],byte2short(length),byte2long(createTime));
    }
    /**
     * 设置为目录或文件。attribute的第2位。
     * @param b true目录，false文件
     */
    protected final void setDirectory(boolean b){
        if(b) this.attribute|=4;
        else this.attribute&=-5;
    }
    public boolean isDirectory(){
        return (this.attribute&4)!=0;
    }
    /**
     * 设置文件隐藏或非隐藏。attribute的第1位。
     * @param b true隐藏，false非隐藏
     */
    public final void setHidden(boolean b){
        if(b) this.attribute|=2;
        else this.attribute&=-3;
    }
    public boolean isHidden(){
        return (this.attribute&2)!=0;
    }
    /**
     * 设置文件只读或普通。attribute的第0位。
     * @param b true只读，false普通
     */
    public final void setReadonly(boolean b){
        if(b) this.attribute|=1;
        else this.attribute&=-2;
    }
    public boolean isReadonly(){
        return (this.attribute&1)!=0;
    }
    /**
     * 返回该文件或目录的父目录。
     * @return 
     */
    public Directory getParent(){
        if(path.length==0) return CurrentPath.root();//若它属于根目录
        else return path[path.length-1];
    }
    public Directory[] getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public byte getStartDiskNumber() {
        return startDiskNumber;
    }

    public short getLength() {
        return length;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setLength(short length) {
        this.length = length;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(Directory[] path) {
        this.path = path;
    }
   
    public static byte[] long2byte(long n){//高高低低
        byte[] b=new byte[8];
        b[0]=(byte)n;b[1]=(byte)(n>>8);
        b[2]=(byte)(n>>16);b[3]=(byte)(n>>24);
        b[4]=(byte)(n>>32);b[5]=(byte)(n>>40);
        b[6]=(byte)(n>>48);b[7]=(byte)(n>>56);
        return b;
    }
    public static long byte2long(byte[] b){//高高低低
        return ((long)b[0]&255)|((long)b[1]&255)<<8|((long)b[2]&255)<<16|((long)b[3]&255)<<24|((long)b[4]&255)<<32|((long)b[5]&255)<<40|((long)b[6]&255)<<48|((long)b[7]&255)<<56;
    }
    public static short byte2short(byte[] b){//高高低低
        return (short)(((short)b[0]&255)|((short)b[1]&255)<<8);
    }
    public static byte[] short2byte(short n){//高高低低
        byte[] b=new byte[2];
        b[0]=(byte)n;b[1]=(byte)(n>>8);
        return b;
    }
    @Override
    public String toString(){
        return name;
    }
    public String information(){
        return (Arrays.toString(path)+name+" 属性"+attribute+" 磁盘号"+startDiskNumber+" 长度"+length+" "+new Date(createTime));
    }
    /**
     * 转换成SFile类型
     * @return 
     */
    public SFile toSFile(){
        return new SFile(path,name, attribute,startDiskNumber,length,createTime);
    }

    /**
     * 转换成Directory类型
     * @return 
     */
    public Directory toDirectory(){
        return new Directory(path,name, attribute,startDiskNumber,length,createTime);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MyFile other = (MyFile) obj;
        if (this.startDiskNumber != other.startDiskNumber) {
            return false;
        }
        return true;
    }

}
