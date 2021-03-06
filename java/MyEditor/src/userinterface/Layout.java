package userinterface;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import mylistenner.ProcessFileListenner;
import mylistenner.TabButtonListenner;
import mylistenner.TabPanelListener;

public class Layout extends JFrame
{
	private static final long serialVersionUID = 1L;
	private static final Font font = new Font("DejaVu Sans Mono Book", Font.PLAIN, 18);

	private static int new_file_count = 0;  //现有标签页的数量（关闭标签页则减少）
	private static int tab_number = 0;  //标签页序号（关闭标签页不会减少，关闭程序归零）
	
	private static JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
    private static JMenuBar menuBar = new JMenuBar();
    
    private ProcessFileListenner processFileListenner;
    private TabButtonListenner tabButtonListenner;
    private TabPanelListener tabPanelListener;
    
    public Layout() throws HeadlessException {
		super();
    	processFileListenner = new ProcessFileListenner(this);
    	tabButtonListenner = new TabButtonListenner(this);
    	tabPanelListener = new TabPanelListener(this);
	}

    public int getNewFileCount() {
    	return new_file_count;
    }
    public int getTabNumber() {
    	return tab_number;
    }
    public JTabbedPane getTabbedPane() {
    	return tabbedPane;
    }

    public void init() {
        JMenu file = new JMenu("File"), settings = new JMenu("Settings");
        
    	JMenuItem openFile = null, newFile = null, saveFile = null;
    	
    	setTitle("MyEditor");  //设置标题
        setBounds(0, 0, 1850, 1050);  //设置边界及窗口位置
        setLayout(new GridLayout(1, 1));
    	
        /*菜单栏*/
        //子菜单
        openFile = new JMenuItem("Open File");
        openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));  //子菜单和快捷键CTRL-O绑定
        openFile.addActionListener(processFileListenner);  //子菜单注册processFileListenner监视器，下同
        
        newFile = new JMenuItem("New File");
        newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));  //子菜单和快捷键CTRL-N绑定
        newFile.addActionListener(processFileListenner);
        
        saveFile = new JMenuItem("Save File");
        saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));  //子菜单和快捷键CTRL-S绑定
        saveFile.addActionListener(processFileListenner);
        
        //菜单项
        file.add(openFile);
        file.add(newFile);
        file.add(saveFile);
        //菜单项加到菜单栏中
        menuBar.add(file);
        menuBar.add(settings);
        //将菜单栏添加到窗口
        setJMenuBar(menuBar);

        /*主面板*/
        add(tabbedPane);
        createTab("Welcome Page", "This is the welcome page.");
        
        setVisible(true);  //使窗口可见
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);  //点击关闭按钮销毁窗口
    }
    
    public void createTab(String tabName, String Text){  //Text为页面的内容
    	
    	JPanel tabPane = new JPanel(new FlowLayout());
    	JPanel pagePane = new JPanel(new BorderLayout());
    	JPanel tipsPane = new JPanel(new GridLayout(1, 3));
    	
    	JButton tabButton = null;
        JLabel tabLabel = new JLabel(tabName), tipsRowLabel = new JLabel("行:"), tipsColumnLabel = new JLabel("列:"), tipsFencLabel = new JLabel("file enconding");
    	
    	JTextArea page = new JTextArea(Text);
    	JScrollPane scrollPane = new JScrollPane(page);

    	page.setFont(font);

    	/*标签头面板*/
        tabButton = new JButton();
        tabButton.addActionListener(tabButtonListenner);
        tabButton.addMouseListener(tabPanelListener);
        tabButton.setToolTipText("close "+tabName);
        tabButton.setContentAreaFilled(false);  //填充透明化
        tabButton.setBorderPainted(false);  //取消边界显示
        tabButton.setPreferredSize(new Dimension(13, 13));
                
        tabPane.addMouseListener(tabPanelListener);
        tabPane.add(tabLabel);
        tabPane.add(tabButton);
        tabPane.setToolTipText(tabName);
        
        /*主体面板*/
        //tips小面板
        tipsPane.add(tipsRowLabel);
        tipsPane.add(tipsColumnLabel);
        tipsPane.add(tipsFencLabel);
        //主体面板
        pagePane.add(scrollPane, BorderLayout.CENTER);
        pagePane.add(tipsPane, BorderLayout.SOUTH);
        
        
        tabbedPane.addTab(tabName, null, pagePane, tabName);
        tabbedPane.setTabComponentAt(tabbedPane.indexOfTab(tabName), tabPane);
        setActiveTabNamed(tabName);
        
        new_file_count++;
        tab_number++;
    }
    
    public void deleteTabNamed(String tabName) {
		int index = tabbedPane.indexOfTab(tabName);  //按标签页名字找到对应的标签页索引
		tabbedPane.remove(index);  //移除这一标签页
		new_file_count--;
    }
    
    public void setActiveTabNamed(String tabName) {  //将指定标签页设为当前页
		int index = tabbedPane.indexOfTab(tabName);  //按标签页名字找到对应的标签页索引
		tabbedPane.setSelectedIndex(index);
    }
    
    public Component getTextArea() {  //返回当前标签页的textArea
    	JPanel pagePane = (JPanel)tabbedPane.getSelectedComponent();
    	BorderLayout borderLayout = (BorderLayout)pagePane.getLayout();
    	JScrollPane scrollPane = (JScrollPane)borderLayout.getLayoutComponent(BorderLayout.CENTER);
    	
    	return scrollPane.getViewport().getView();
    }
    public Component getTextArea(String tabName) {  //返回指定标签页的textArea
		int index = tabbedPane.indexOfTab(tabName);  //按标签页名字找到对应的标签页索引
		JPanel pagePane = (JPanel)tabbedPane.getComponentAt(index);
    	BorderLayout borderLayout = (BorderLayout)pagePane.getLayout();
    	JScrollPane scrollPane = (JScrollPane)borderLayout.getLayoutComponent(BorderLayout.CENTER);
    	
    	return scrollPane.getViewport().getView();
    }
    
	public Component getTabComponent() {  //返回当前标签页的TabPanel
		int index = tabbedPane.getSelectedIndex();
		
		return tabbedPane.getTabComponentAt(index);
	}
}
