package com.asemonash.extract;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.asemonash.transform.QueryStringBuilder;

public class FilePicker extends JPanel implements ActionListener {
	private final static int PANEL_WIDTH = 300;
	private final static int PANEL_HEIGHT = 100;
	
	private JButton filePicker;
	private JFileChooser fileChooser;
	private HtmlParser htmlParser;
	
	public FilePicker() {
		super();
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		filePicker = new JButton("SELECT FILE");
		fileChooser = new JFileChooser("C:\\Users\\em8149\\Desktop\\ASE Big Data\\Monash Project\\Sample Data\\ASE-UseCase\\ASE-UseCase");
		filePicker.addActionListener(this);
		this.add(filePicker);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == filePicker) {
			FileNameExtensionFilter filter = new FileNameExtensionFilter("HTML files", "html", "htm");
			fileChooser.setFileFilter(filter);
			int returnVal = fileChooser.showOpenDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				htmlParser = new HtmlParser(file);
				htmlParser.initHtmlParser();
				
			}
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("FILE PICKER");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FilePicker myPanel = new FilePicker();
		frame.getContentPane().add(myPanel);  
		//add instance of MyGUI to the frame
		frame.pack(); 
		//resize frame to fit our Jpanel
		//Position frame on center of screen 
		Toolkit tk = Toolkit.getDefaultToolkit();	
		Dimension d = tk.getScreenSize();	
		int screenHeight = d.height;	
		int screenWidth = d.width;
	    frame.setLocation(new Point((screenWidth/2)-(frame.getWidth()/2),(screenHeight/2)-(frame.getHeight()/2)));
		//show the frame	
	    frame.setVisible(true);
	}
}
