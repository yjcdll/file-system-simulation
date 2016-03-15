package ui;

import FileSystem.CurrentPath;
import FileSystem.Directory;
import FileSystem.FAT;
import FileSystem.MyFile;
import FileSystem.SFile;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import javax.swing.SwingUtilities;

/**
 *
 * @author Administrator
 */
public class Content{//中间放图标的滚动面板
    private final static JScrollPane pane=new JScrollPane();
    private final static JPanel panel=new JPanel(new FlowLayout(FlowLayout.LEFT));
    private final static JPopupMenu menu=new JPopupMenu();//右键弹出的菜单
    private final static JMenuItem createSFile=new JMenuItem("新建文件");
    private final static JMenuItem createDirectory=new JMenuItem("新建文件夹");
    private Content(){
    }
    static{
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setLayout(new WrapLayout(FlowLayout.LEFT));
        //panel.setAutoscrolls(true);
        pane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        pane.setViewportView(panel);

        //pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        menu.add(createSFile);menu.add(createDirectory);
        //createDirectory.setMnemonic(KeyEvent.VK_N);
        //createSFile.setMnemonic(KeyEvent.VK_Q);
        panel.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getButton()==3)//按下右键
                    menu.show(e.getComponent(),e.getX(),e.getY());
            }
        });
        createSFile.addActionListener((ActionEvent e) -> {
            try {
                byte space = FAT.allocate(); FAT.setLast(space);
                
                String name="新建文件";//查找适合的重命名
                int suffix=1;
                MyFile[] listFiles = CurrentPath.getDirectory().listFiles();
                while(true){
                    int i;
                    for(i=0;i<listFiles.length;i++)
                        if(listFiles[i].getName().equals(name+suffix))break;
                    if(i==listFiles.length) break;
                    suffix++;
                }
                
                SFile sFile = new SFile(CurrentPath.getPath(), "新建文件"+suffix, (byte) 0, space, (short) 0);    
                CurrentPath.getDirectory().create(sFile);

                panel.add(new SFileIcon(sFile)); 
                panel.updateUI();
                Tree.addObject(sFile);  
                if(CurrentPath.getDirectory()!=CurrentPath.root())//要更新新建的文件的父亲的对象，因为它长度变大了
                    Tree.replaceNode(CurrentPath.getDirectory());
                DiskMonitor.update();
        } catch (Exception ex) {
            if ("磁盘空间已满！".equals(ex.getMessage())) 
                JOptionPane.showMessageDialog(null, "磁盘空间已满！");
            else ex.printStackTrace();
        }
        });
        createDirectory.addActionListener((ActionEvent e) -> {
            try {
                byte space=FAT.allocate();FAT.setLast(space);
                String name="新建文件夹";//查找适合的重命名
                int suffix=1;
                MyFile[] listFiles = CurrentPath.getDirectory().listFiles();
                while(true){
                    int i;
                    for(i=0;i<listFiles.length;i++)
                        if(listFiles[i].getName().equals(name+suffix))break;
                    if(i==listFiles.length) break;
                    suffix++;
                }
                
                Directory directory = new Directory(CurrentPath.getPath(),"新建文件夹"+suffix,(byte)0,space,(short)0);
                CurrentPath.getDirectory().create(directory);
                
                panel.add(new DirectoryIcon(directory));//在面板上增加图标
                panel.updateUI();
                Tree.addObject(directory);//在tree上增加图标
                if(CurrentPath.getDirectory()!=CurrentPath.root())//要更新新建的文件的父亲的对象，因为它长度变大了
                    Tree.replaceNode(CurrentPath.getDirectory());
                DiskMonitor.update();
                
            } catch (Exception ex) {
                if("磁盘空间已满！".equals(ex.getMessage())) JOptionPane.showMessageDialog(null,"磁盘空间已满！");
                else ex.printStackTrace();
            }
        });
    }
    /**
     * 在JPanel上显示CurrentPath.getDirectory()下的所有文件 
     */
    public static void update(){
        //if(directory!=CurrentPath.root()) CurrentPath.cd(directory);
        //Address.update();//更新地址栏
        //MyFile[] listFiles = directory.listFiles();
        MyFile[] listFiles = CurrentPath.getDirectory().listFiles();
        //更新图标面板
        panel.removeAll();
        //System.out.println("open:");
        for(int i=0;i<listFiles.length;i++){    
            if(listFiles[i] instanceof Directory)
                panel.add(new DirectoryIcon((Directory) listFiles[i]));
            else panel.add(new SFileIcon((SFile) listFiles[i]));
            listFiles[i].setPath(CurrentPath.getPath());
            //System.out.println(listFiles[i].information());
        }
        //System.out.println(":OK");
        panel.updateUI();
    }
    /**
     * 在面板上删除icon图标
     * @param icon 
     */
    public static void remove(MyFileIcon icon){
        panel.remove(icon);panel.updateUI();
    }
    public static JPanel getPanel(){
        return panel;
    }
    public static JScrollPane getPane(){
        return pane;
    }
    
}


/**
 * @author https://tips4java.wordpress.com/2008/11/06/wrap-layout/
 *  FlowLayout subclass that fully supports wrapping of components.
 */
class WrapLayout extends FlowLayout
{
	private Dimension preferredLayoutSize;

	/**
	* Constructs a new <code>WrapLayout</code> with a left
	* alignment and a default 5-unit horizontal and vertical gap.
	*/
	public WrapLayout()
	{
		super();
	}

	/**
	* Constructs a new <code>FlowLayout</code> with the specified
	* alignment and a default 5-unit horizontal and vertical gap.
	* The value of the alignment argument must be one of
	* <code>WrapLayout</code>, <code>WrapLayout</code>,
	* or <code>WrapLayout</code>.
	* @param align the alignment value
	*/
	public WrapLayout(int align)
	{
		super(align);
	}

	/**
	* Creates a new flow layout manager with the indicated alignment
	* and the indicated horizontal and vertical gaps.
	* <p>
	* The value of the alignment argument must be one of
	* <code>WrapLayout</code>, <code>WrapLayout</code>,
	* or <code>WrapLayout</code>.
	* @param align the alignment value
	* @param hgap the horizontal gap between components
	* @param vgap the vertical gap between components
	*/
	public WrapLayout(int align, int hgap, int vgap)
	{
		super(align, hgap, vgap);
	}

	/**
	* Returns the preferred dimensions for this layout given the
	* <i>visible</i> components in the specified target container.
	* @param target the component which needs to be laid out
	* @return the preferred dimensions to lay out the
	* subcomponents of the specified container
	*/
	@Override
	public Dimension preferredLayoutSize(Container target)
	{
		return layoutSize(target, true);
	}

	/**
	* Returns the minimum dimensions needed to layout the <i>visible</i>
	* components contained in the specified target container.
	* @param target the component which needs to be laid out
	* @return the minimum dimensions to lay out the
	* subcomponents of the specified container
	*/
	@Override
	public Dimension minimumLayoutSize(Container target)
	{
		Dimension minimum = layoutSize(target, false);
		minimum.width -= (getHgap() + 1);
		return minimum;
	}

	/**
	* Returns the minimum or preferred dimension needed to layout the target
	* container.
	*
	* @param target target to get layout size for
	* @param preferred should preferred size be calculated
	* @return the dimension to layout the target container
	*/
	private Dimension layoutSize(Container target, boolean preferred)
	{
	synchronized (target.getTreeLock())
	{
		//  Each row must fit with the width allocated to the containter.
		//  When the container width = 0, the preferred width of the container
		//  has not yet been calculated so lets ask for the maximum.

		int targetWidth = target.getSize().width;
		Container container = target;

		while (container.getSize().width == 0 && container.getParent() != null)
		{
			container = container.getParent();
		}

		targetWidth = container.getSize().width;

		if (targetWidth == 0)
			targetWidth = Integer.MAX_VALUE;

		int hgap = getHgap();
		int vgap = getVgap();
		Insets insets = target.getInsets();
		int horizontalInsetsAndGap = insets.left + insets.right + (hgap * 2);
		int maxWidth = targetWidth - horizontalInsetsAndGap;

		//  Fit components into the allowed width

		Dimension dim = new Dimension(0, 0);
		int rowWidth = 0;
		int rowHeight = 0;

		int nmembers = target.getComponentCount();

		for (int i = 0; i < nmembers; i++)
		{
			Component m = target.getComponent(i);

			if (m.isVisible())
			{
				Dimension d = preferred ? m.getPreferredSize() : m.getMinimumSize();

				//  Can't add the component to current row. Start a new row.

				if (rowWidth + d.width > maxWidth)
				{
					addRow(dim, rowWidth, rowHeight);
					rowWidth = 0;
					rowHeight = 0;
				}

				//  Add a horizontal gap for all components after the first

				if (rowWidth != 0)
				{
					rowWidth += hgap;
				}

				rowWidth += d.width;
				rowHeight = Math.max(rowHeight, d.height);
			}
		}

		addRow(dim, rowWidth, rowHeight);

		dim.width += horizontalInsetsAndGap;
		dim.height += insets.top + insets.bottom + vgap * 2;

		//	When using a scroll pane or the DecoratedLookAndFeel we need to
		//  make sure the preferred size is less than the size of the
		//  target containter so shrinking the container size works
		//  correctly. Removing the horizontal gap is an easy way to do this.

		Container scrollPane = SwingUtilities.getAncestorOfClass(JScrollPane.class, target);

		if (scrollPane != null && target.isValid())
		{
			dim.width -= (hgap + 1);
		}

		return dim;
	}
	}

	/*
	 *  A new row has been completed. Use the dimensions of this row
	 *  to update the preferred size for the container.
	 *
	 *  @param dim update the width and height when appropriate
	 *  @param rowWidth the width of the row to add
	 *  @param rowHeight the height of the row to add
	 */
	private void addRow(Dimension dim, int rowWidth, int rowHeight)
	{
		dim.width = Math.max(dim.width, rowWidth);

		if (dim.height > 0)
		{
			dim.height += getVgap();
		}

		dim.height += rowHeight;
	}
}