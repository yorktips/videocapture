package com.tca.webcam;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;


@SuppressWarnings("serial")
public class TakeSnapshotFromWebCam extends JFrame {

	private class SnapMeAction extends AbstractAction {

		public SnapMeAction() {
			super("Snapshot");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			List<Webcam> webcams = Webcam.getWebcams();

			for (int i = 0; i < webcams.size(); i++) {
				Webcam webcam = webcams.get(i);
				if (webcam.getName().equalsIgnoreCase(webCamName)) {
					String fileName = getWorkingPath();
					System.out.println("snapshot_working=" + fileName);
					File file = new File(fileName);

					BufferedImage subImgage = webcam.getImage().getSubimage(
							157, 12, 326, 456);
					try {
						ImageIO.write(subImgage, "JPG", file);
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(getContentPane(),
								e2.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
						System.out.println(e2.getMessage());
						return;
					}
					ImageIcon icon1 = new ImageIcon(subImgage);
					resultImage.setIcon(icon1);
					// resultPanel.repaint();
					System.out.format("Image for %s saved in %s \n",
							webcam.getName(), file);
					System.out.println("");
					btUpload.setEnabled(true);
					btSave.setEnabled(true);
					return;
				}
			}

		}
	}

	private class StartAction extends AbstractAction implements Runnable {

		public StartAction() {
			super("Start");
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			btStart.setEnabled(false);
			btSnapMe.setEnabled(true);
			btUpload.setEnabled(false);
			btSave.setEnabled(false);

			executor.execute(this);
		}

		@Override
		public void run() {

			btStop.setEnabled(true);

			for (WebcamPanel panel : panels) {
				panel.start();
			}
		}
	}

	private class StopAction extends AbstractAction {

		public StopAction() {
			super("Stop");
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			btStart.setEnabled(true);
			btSnapMe.setEnabled(false);
			btStop.setEnabled(false);
			btUpload.setEnabled(false);
			btSave.setEnabled(false);

			for (WebcamPanel panel : panels) {
				panel.stop();
			}
		}
	}

	private class UploadAction extends AbstractAction {

		public UploadAction() {
			super("Upload");
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			if (studentID.equalsIgnoreCase("")) {
				JOptionPane.showMessageDialog(getContentPane(),
						"Please specify a student ID.", "Warning",
						JOptionPane.ERROR_MESSAGE);

				return;

			}

			btStart.setEnabled(false);
			btSnapMe.setEnabled(false);
			btStop.setEnabled(false);

		}
	}

	private class SaveAction extends AbstractAction {

		public SaveAction() {
			super("Save");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String fileName = "";
			JFileChooser c = new JFileChooser();

			c.addChoosableFileFilter(new FileNameExtensionFilter("Images",
					"jpg", "jpeg", "png", "gif", "bmp"));
			int rVal = c.showSaveDialog(TakeSnapshotFromWebCam.this);
			if (rVal == JFileChooser.APPROVE_OPTION) {
				fileName = c.getSelectedFile().getAbsolutePath();
				if (fileName != null && !fileName.equalsIgnoreCase("")) {
					saveImageToFile(fileName);
				}
			}

			if (rVal == JFileChooser.CANCEL_OPTION) {
				fileName = "";
			}
		}
	}

	private String webCamName = Webcam.getDefault().getName();

	private Executor executor = Executors.newSingleThreadExecutor();

	private Dimension size = WebcamResolution.QQVGA.getSize();

	private List<WebcamPanel> panels = new ArrayList<WebcamPanel>();
	private JButton btSnapMe = new JButton(new SnapMeAction());
	private JButton btStart = new JButton(new StartAction());
	private JButton btStop = new JButton(new StopAction());
	private JButton btSave = new JButton(new SaveAction());

	private JButton btUpload = new JButton(new UploadAction());

	private JLabel resultImage = new JLabel();

	private JComboBox<String> studentList;
	private String studentID = "";

	public TakeSnapshotFromWebCam() {
		super("Test Snap Different Size");
		TakeSnapshotFromVideoExample2(webCamName);
	}

	public TakeSnapshotFromWebCam(String camName) {
		super("TCA WebCam Capture");
		webCamName = camName;
		TakeSnapshotFromVideoExample2(webCamName);
	}

	private String getWorkingPath() {
//		String ret = this.getClass().getProtectionDomain().getCodeSource()
//				.getLocation().toString()
//				+ "snapshot_working.jpeg";
//		if (ret.startsWith("file:/") || ret.startsWith("file:\\")) {
//			ret = ret.substring(6, ret.length());
//		}
//		return ret;
		
		try {

			// create a temp file
			File temp = File.createTempFile("snapshot_working.jpeg", ".tmp");
			String ret=temp.getAbsolutePath();

			// Get tempropary file path
			String absolutePath = temp.getAbsolutePath();
			String tempFilePath = absolutePath.substring(0,
					absolutePath.lastIndexOf(File.separator));
			ret=tempFilePath + File.separator + "snapshot_working.jpeg";
			System.out.println("Temp file : " + ret);
			return ret;
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
			//e.printStackTrace();
			return "snapshot_working.jpeg";
		}
	}

	private int loadStudentList() {
		studentList = new JComboBox<>(
				new String[] { "", "1111", "2222", "3333" });
		
		String workingsnapfile = getWorkingPath();
		System.out.println("workingsnapfile=" + workingsnapfile);

		studentList.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					Object item = event.getItem();
					System.out.println("value=" + item.toString());
					if (item != null && !item.toString().equals("")) {

					}
					studentID = item.toString();
				}

			}
		});

		return 3;
	}

	public boolean saveImageToFile(String fileName) {
		System.out.println("saveImageToFile=" + fileName);
		File file = new File(fileName);

		String snamshot = getWorkingPath();
		if (snamshot.equalsIgnoreCase(fileName))
			return true;

		if (file.exists()) {
			int dialogResult = JOptionPane.showConfirmDialog(
					TakeSnapshotFromWebCam.this,
					"File exists. Would you like to overwrite it?", "Warning",
					JOptionPane.YES_NO_OPTION);

			if (dialogResult != JOptionPane.YES_OPTION) {
				return true;
			}
			file.delete();
		}

		FileCopy fc = new FileCopy();
		fc.copy(snamshot, fileName);

		File fileTar = new File(fileName);

		if (fileTar.exists()) {
			JOptionPane.showMessageDialog(getContentPane(),
					"Image has been saved to \"" + fileName
							+ "\" successfully.", "Saved",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(getContentPane(),
					"Failed to save image to \"" + fileName + "\".", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}

	public void TakeSnapshotFromVideoExample2(String camName) {

		btUpload.setEnabled(false);
		btSave.setEnabled(false);

		loadStudentList();

		Container contentPane = getContentPane();
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		contentPane.setLayout(gridbag);
		c.fill = GridBagConstraints.HORIZONTAL;

		// GridLayout gbl=new GridLayout(1,3);

		this.setPreferredSize(new Dimension(900, 600));
		List<Webcam> webcams = Webcam.getWebcams();
		for (Webcam webcam : webcams) {
			if (webcam.getName().equalsIgnoreCase(camName)) {
				size = new Dimension(640, 480);
				webcam.setViewSize(size);
				WebcamPanel panel = new WebcamPanel(webcam, size, false);
				panel.setFPSDisplayed(true);
				panel.setFillArea(true);
				panels.add(panel);
				break;
			}
		}

		btSnapMe.setEnabled(false);
		btStop.setEnabled(false);

		JPanel cameraPanel = new JPanel();
		cameraPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		cameraPanel.setPreferredSize(new Dimension(336, 476));
		WebcamPanel panel = panels.get(0);
		cameraPanel.add(panel);

		contentPane.add(cameraPanel);

		resultImage.setSize(326, 456);
		String IMG_PATH = "c://go//default22.jpg";
		try {
			BufferedImage img = ImageIO.read(new File(IMG_PATH));
			ImageIcon icon = new ImageIcon(img);
			resultImage.setIcon(icon);
			// JLabel label = new JLabel(icon);
		} catch (Exception e) {
			// JLabel label = new JLabel(icon);
		}

		JPanel apiPanel = new JPanel();
		// apiPanel.setPreferredSize(new Dimension(300, 60));
		apiPanel.setSize(new Dimension(80, 60));
		// add(apiPanel);

		JPanel munuPanel = new JPanel();
		munuPanel.setSize(new Dimension(80, 60));
		// munuPanel.setLayout(new BoxLayout(munuPanel, BoxLayout.Y_AXIS));

		GridLayout layout = new GridLayout(12, 1);
		munuPanel.setLayout(layout);

		// 1.
		munuPanel.add(new JLabel("Student ID"));
		// 2.
		munuPanel.add(studentList);

		// 3.
		munuPanel.add(new JLabel(" "));

		JPanel pathPanel = new JPanel();
		GridLayout layoutPath = new GridLayout(2, 2);
		pathPanel.setLayout(layoutPath);

		pathPanel.add(new JLabel("Work Path"));
		pathPanel.add(new JLabel());
		java.io.File f = new java.io.File(".");
		JLabel defaultPath = new JLabel(f.getPath());
		pathPanel.add(defaultPath);

		// 4.
		munuPanel.add(btSnapMe);

		munuPanel.add(new JLabel(" "));

		// 5.
		munuPanel.add(btStart);

		munuPanel.add(new JLabel(" "));

		// 6.
		munuPanel.add(btStop);

		munuPanel.add(new JLabel(" "));

		munuPanel.add(btSave);
		munuPanel.add(new JLabel(" "));
		munuPanel.add(btUpload);

		add(munuPanel);

		// resultImage.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		JPanel resultPanel = new JPanel();
		resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));

		resultPanel.setPreferredSize(new Dimension(326, 456));
		resultPanel.setBackground(Color.BLACK);
		resultPanel.add(resultImage);

		gridbag.setConstraints(resultPanel, c);
		contentPane.add(resultPanel);

		pack();
		this.setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {

		List<Webcam> webcams = Webcam.getWebcams();
		String name = "";
		if (webcams.size() == 0) {
			JOptionPane.showMessageDialog(null, "No available webcam found.");
		}
		if (webcams.size() == 1) {
			name = webcams.get(0).getName();
			new TakeSnapshotFromWebCam(name);
		} else {
			PickupWebCamera dialog = new PickupWebCamera(null,
					"Select Camer Device");
			dialog.setLocationRelativeTo(null);
			dialog.setVisible(true);
			System.out.println("dailog=" + "end");

		}

	}
}
