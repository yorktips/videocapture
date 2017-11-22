

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.WebcamCaptureFrameworkDestroyer;

//public class WebCang extends JFrame implements ActionListener {
public class WebCang extends JApplet implements ActionListener {

	@SuppressWarnings("serial")
	private class WebCameraPicker extends JDialog {

		private String chosedDeviceName = "";
		private List<Webcam> webcams = Webcam.getWebcams();
		private JButton btStart = new JButton(new StartAction());
		private JComboBox<String> deviceList;
		private JFrame parentWindow;
		
		private class StartAction extends AbstractAction {

			public StartAction() {
				super("Next");
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				String name = deviceList.getSelectedItem().toString();
				System.out.println("you choosed " + name);
				CameraName=name;
				chosedDeviceName=name;
				dispose();
			}
		}

		public WebCameraPicker(JFrame frame,String title) {
			super(frame,title);
			parentWindow=frame;
			
			// Get all camer devices list
			if (webcams.size() >= 1) {
				ArrayList<String> devices = new ArrayList<String>();
				for (Webcam webcam : webcams) {
					String name = webcam.getDevice().getName();
					devices.add(name);
				}
				String[] devicesArray = devices.toArray(new String[devices.size()]);
				deviceList = new JComboBox<>(devicesArray);

			} else {
				deviceList = new JComboBox<>(new String[] {});
			}

			deviceList.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent event) {
					if (event.getStateChange() == ItemEvent.SELECTED) {
						Object item = event.getItem();

						System.out.println("value=" + item.toString());
						

					}
					chosedDeviceName = deviceList.getSelectedItem().toString();
					//parentWindow.setCameraDevice(chosedDeviceName);
					
					System.out.println("chosedDeviceName=" + chosedDeviceName);
				}
			});

			setLayout(new FlowLayout());

			add(deviceList);
			add(btStart);
			pack();

		}

		public String getChosedDeviceName() {
			this.setVisible(false);
			// this.dispose();
			return chosedDeviceName;
		}


	}
	
	
	
	private WebcamPanel player = null;

	private JButton savetodb, capture, exit;

	private JTextField num = null;

	private Image img = null;

	private ImagePanel imgpanel = null;

	private Panel videoPanel = null;

	private JComboBox formatList = null;

	private Component videoComp = null;

	// york start
	private String CameraName = "";
	private static final long serialVersionUID = 3517366452510566929L;
	// private Dimension size = WebcamResolution.QVGA.getSize();

	private Webcam webcam = null;
	private WebcamPanel webcampanel = null;

	// york end

	public WebCang() {
		super();
		System.out.println("Construct");
	}

	@Override
	public void start() {

		System.out.println("Start");

		super.start();

		webcam = Webcam.getDefault();
		Dimension size = new Dimension(640,480);
		webcam.setViewSize(size);

		webcampanel = new WebcamPanel(webcam, false);
		webcampanel.setFPSDisplayed(true);

		videoPanel.add(webcampanel);

		if (webcam.isOpen()) {
			webcam.close();
		}

		int i = 0;
		do {
			if (webcam.getLock().isLocked()) {
				System.out.println("Waiting for lock to be released " + i);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					return;
				}
			} else {
				break;
			}
		} while (i++ < 3);

		webcam.open();
		webcampanel.start();
	}

	@Override
	public void destroy() {
		System.out.println("Destroy");
		webcam.close();
		WebcamCaptureFrameworkDestroyer.destroy();
		System.out.println("Destroyed");

		if (videoComp != null) {

			videoPanel.remove(videoComp);

		}

		System.out.println("You have exit gracefully.");

		super.destroy(); 
	}

	@Override
	public void stop() {
		System.out.println("Stop");
		webcam.close();
		System.out.println("Stopped");
	}

	@Override
	public void init() {
		System.out.println("Init");

		CameraName = "Acer Crystal Eye webcam 0";

		Panel topPanel = new Panel();

		topPanel.setLayout(new GridLayout(1, 2));

		// video panel

		videoPanel = new Panel();

		topPanel.add(videoPanel);

		// image panel

		imgpanel = new ImagePanel();
		imgpanel.setPreferredSize(new Dimension(ImagePanel.TARGET_WIDTH, ImagePanel.TARGET_HEIGHT));
		imgpanel.addMouseMotionListener(imgpanel);

		topPanel.add(imgpanel);

		// video panel

		getContentPane().setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.BOTH;

		c.gridx = 0;

		c.gridy = 0;

		c.weightx = 1.0;

		c.weighty = 1.0;

		getContentPane().add(topPanel, c);

		// bottom panel

		c.fill = GridBagConstraints.HORIZONTAL;

		c.gridx = 0;

		c.gridy = 1;

		c.weightx = 1.0;

		c.weighty = 0.0;

		c.anchor = GridBagConstraints.SOUTHWEST;

		Panel bottomPanel = new Panel();

		getContentPane().add(bottomPanel, c);

		// layout of bottom panel

		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

		Panel botPanel1 = new Panel();

		bottomPanel.add(botPanel1);

		Panel botPanel2 = new Panel();

		bottomPanel.add(botPanel2);

		// format list in first row

		formatList = new JComboBox();
		//formatList.setVisible(false);//fan
		botPanel1.add(formatList);

		formatList.setLightWeightPopupEnabled(false);

		// Add buttons to bottom panel

		capture = new JButton("Capture");

		capture.addActionListener(this);

		botPanel2.add(capture);

		savetodb = new JButton("Save");

		savetodb.addActionListener(this);

		botPanel2.add(savetodb);

		exit = new JButton("Exit");

		exit.addActionListener(this);

		botPanel2.add(exit);

		botPanel2.add(new Label("Student ID:"));

		String id =  getParameter("id");

		num = new JTextField(id, 20);

		botPanel2.add(num);

		// String str1 = "vfw:Logitech USB Video Camera:0";

		// String str2 = "vfw:Microsoft WDM Image Capture (Win32):0";

		// di = CaptureDeviceManager.getDevice(str1);

		// ml = di.getLocator();

		// videoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		videoPanel.setPreferredSize(new Dimension(ImagePanel.TARGET_WIDTH, ImagePanel.TARGET_HEIGHT));

		Component comp = webcampanel;

		if (comp != null) {

			videoPanel.setLayout(new BorderLayout());

			videoPanel.add(comp, BorderLayout.CENTER);

			// set the window size to somewhat preferred sze

			Dimension d = videoPanel.getPreferredSize();

			if (d != null) {

				double w = d.getWidth();

				double h = d.getHeight();

				System.out.println("this.width=" + ((int) (w * 2 + 100)) + ";this.widt=" + ((int) (h + 100)));
				this.setSize((int) (w * 2 + 100), (int) (h + 100));

			}

		}

		// Get all supported format

		// FormatControl formatControl =
		// (FormatControl)player.getControl("javax.media.control.FormatControl");

		// String curFormat = formatControl.getFormat().toString().trim();

		String preferredFormat = Integer.toString(ImagePanel.TARGET_WIDTH) + " X " + Integer.toString(ImagePanel.TARGET_HEIGHT);
										// getParameter("PreferredFormat");

		formatList.addItem(preferredFormat);
		int preferredIndex = -1;

		int currentIndex = -1;

		ArrayList tmpList = new ArrayList();
		//
		// Format[] formats = formatControl.getSupportedFormats();
		//
		// for (int i=0; i<formats.length; i++) {
		//
		// if (formats[i] instanceof RGBFormat){
		//
		// String tmp = formats[i].toString().trim();
		//
		// formatList.addItem(tmp);
		//
		// if (tmp.equals(preferredFormat)){
		//
		// preferredIndex = tmpList.size() - 1;
		//
		// }
		//
		// if (tmp.equals(curFormat)){
		//
		// currentIndex = tmpList.size() - 1;
		//
		// }
		//
		// }
		//
		// }

		// if (preferredIndex >= 0){
		//
		// player.stop();
		//
		// formatControl.setFormat(formats[preferredIndex]);
		//
		// player.start();
		//
		// formatList.setSelectedIndex(preferredIndex);
		//
		// } else if (currentIndex >= 0){
		//
		// formatList.setSelectedIndex(currentIndex);
		//
		// }
		//
		// formatList.addActionListener(this);
		//
		// } catch (Exception e) {
		//
		// e.printStackTrace();
		//
		// }

	}

	/**

  *

  */

	public void actionPerformed(ActionEvent e) {

		JComponent c = (JComponent) e.getSource();

		if (c == capture) {

			System.out.println("capture starting....");
			// Grab a frame
			BufferedImage subImgage = webcam.getImage().getSubimage(157, 12,
					ImagePanel.TARGET_WIDTH, ImagePanel.TARGET_HEIGHT);
			System.out.println("capture got image buf");
			
			// FrameGrabbingControl fgc = (FrameGrabbingControl) player
			//
			// .getControl("javax.media.control.FrameGrabbingControl");
			//
			// Buffer buf = fgc.grabFrame(); // Convert it to an image
			//
			//
			//
			// Format format = buf.getFormat();
			//
			// System.out.println("Captured format : " + format);
			//
			// BufferToImage btoi = new BufferToImage((VideoFormat)format);

			img = subImgage;// btoi.createImage(buf); // show the image

			System.out.println("capture set image buf");
			imgpanel.setImage(img); // save image

			System.out.println("capture set image buf done");
			//imgpanel.repaint();
			//this.repaint();
			
			System.out.println("capture repain image  done");
		} else if (c == savetodb) {

			String id = num.getText();

			if (id != null && id.trim().length() > 0 && img != null) {

				try {

					byte[] data =  getOps();

					if (data != null) {

						System.out.println("Before call sentToServlet");

						sendToServlet(data, id); 

						System.out.println("After call sentToServlet");

					}

				} catch (Exception ex) {

					ex.printStackTrace();

				}

			} else {

				JOptionPane
						.showMessageDialog(this,
								"You must capture picture and fill in student id before saving data.");

			}

		} else if (c == exit) {

			try {

				 URL homeurl = new URL(getCodeBase().toString());

				if (player != null) {

					getAppletContext().showDocument(homeurl);

				}

			} catch (Exception ee) {

				JOptionPane

				.showMessageDialog(this,
						"Error when showing home: " + ee.getMessage());

			}

		} else if (c == formatList) {

			System.out.println("player restart");
			player.stop();
			player.start();

		}

	}

	public byte[] getOps() {

		return imgpanel.getOps();

	}

	public void sendToServlet(byte[] data, String id) {

		OutputStream outtoServlet = null;

		InputStream is = null;

		try {

			String base = "http://127.0.0.1:9999/card4/getPhotofromApplet";

			// URL url = new URL(getCodeBase(), "getPhotofromApplet?id="+id);

			URL url = new URL(base + "?id=" + id);

			System.out.println("Prepare to send data...");

			// System.out.println("Server url: " + base);

			System.out.println("Student id: " + id);

			System.out.println("Image: " + data.length + " bytes");

			JOptionPane.showMessageDialog(this, url.toString());

			URLConnection urlCon = url.openConnection();

			// inform the connection that we will send output and accept input

			urlCon.setDoInput(true);

			urlCon.setDoOutput(true);

			// Don't use a cached version of URL connection.

			urlCon.setUseCaches(false);

			urlCon.setDefaultUseCaches(false);

			// Specify the content type that we will send binary data

			urlCon.setRequestProperty("Content-Type",

			"application/octet-stream");

			outtoServlet = urlCon.getOutputStream();

			outtoServlet.write(data);

			outtoServlet.flush();

			outtoServlet.close();

			outtoServlet = null;

			System.out.println("Prepare to read data...");

			int len = urlCon.getContentLength();

			System.out.println("Response length: " + len);

			is = urlCon.getInputStream();

			ByteArrayOutputStream bos = new ByteArrayOutputStream();

			byte[] buf = new byte[1024];

			int rtn = is.read(buf);

			while (rtn >= 0) {

				bos.write(buf, 0, rtn);

				rtn = is.read(buf);

			}

			is.close();

			is = null;

			urlCon = null;

			String resp = new String(bos.toByteArray(), "utf-8");

			System.out.println("Response content:" + resp);

			JOptionPane.showMessageDialog(this, "Sent the picture to servlet: "
					+ resp);

		} catch (IOException e) {

			JOptionPane.showMessageDialog(this,
					"Error when sending data: " + e.getMessage());

		} finally {

			if (is != null) {

				try {

					is.close();

				} catch (Exception e) {

					// ignore the error, there is nothing we can do

				}

			}

			if (outtoServlet != null) {

				try {

					outtoServlet.close();

				} catch (Exception e) {

					// ignore the error, there is nothing we can do

				}

			}

		}

	}

	/**

*

*/

	class ImagePanel extends Panel implements MouseMotionListener {

		private Image myimg = null;

		private int rectX;

		private int rectY;

		private int rectWidth = TARGET_WIDTH;

		private int rectHeight = TARGET_HEIGHT;

		private int w, h, x, y;

		private double ratio = 1;

		private static final int TARGET_WIDTH = 326;

		private static final int TARGET_HEIGHT = 456;

		private static final int MARGIN = 10;

		public ImagePanel() {

			setLayout(null);

			// setSize(imgWidth,imgHeight);

			// setSize(320,240);

			// setSize(640,480);

		}

		public void setImage(Image img) {

			this.myimg = img;

			repaint();

		}

		public void update(Graphics g) {

			BufferedImage bufferedImage = new BufferedImage(getWidth(),
					getHeight(), BufferedImage.TYPE_INT_BGR);

			Graphics bufg = bufferedImage.getGraphics();

			bufg.setColor(Color.GRAY);

			bufg.fillRect(0, 0, getWidth(), getHeight());

			if (myimg != null) {

				// bufg.drawImage(myimg, 0, 0, getWidth(), getHeight(), this);

				w = myimg.getWidth(null);

				h = myimg.getHeight(null);

				if (w + MARGIN > getWidth() || h + MARGIN > getHeight()) {

					// image size is larger than the panel size, needs to scale
					// it

					double rw = (double) ((double) (w + MARGIN) / (double) getWidth());

					double rh = (double) ((double) (h + MARGIN) / (double) getHeight());

					ratio = Math.max(rw, rh);

					w = (int) (w / ratio);

					h = (int) (h / ratio);

				} else {

					ratio = 1;

				}

				x = (getWidth() - w) / 2;

				y = (getHeight() - h) / 2;

				bufg.drawImage(myimg, x, y, w, h, this);

				bufg.setColor(Color.RED);

				if (rectX < x) {

					rectX = x;

				}

				if (rectY < y) {

					rectY = y;

				}

				if (((double) w / (double) h) > ((double) TARGET_WIDTH / (double) TARGET_HEIGHT)) {

					rectHeight = h;

					rectWidth = (int) (h * ((double) TARGET_WIDTH / (double) TARGET_HEIGHT));

				} else {

					rectWidth = w;

					rectHeight = (int) (w * ((double) TARGET_HEIGHT / (double) TARGET_WIDTH));

				}

				if (rectX + rectWidth > x + w) {

					rectX = x + w - rectWidth;

				}

				if (rectY + rectHeight > y + h) {

					rectY = y + h - rectHeight;

				}

				bufg.drawRect(rectX, rectY, rectWidth, rectHeight);

			}

			g.drawImage(bufferedImage, 0, 0, getWidth(), getHeight(), this);

		}

		public void paint(Graphics g) {

			update(g);

		}

		public void mouseDragged(MouseEvent e) {

			rectX = e.getX() - 50;

			rectY = e.getY() - 50;

			repaint();

		}

		public void mouseMoved(MouseEvent e) {

		}

		public byte[] getOps() {

			if (myimg == null) {

				return null;

			}

			try {

				BufferedImage bufferedImage = new BufferedImage(
						myimg.getWidth(null), myimg.getHeight(null),
						BufferedImage.TYPE_INT_BGR);

				Graphics bufg = bufferedImage.getGraphics();

				bufg.drawImage(myimg, 0, 0, this);

				System.out.println("after Save a copy of original image ff");

				// cut the selected portion from the original image

				int rx = (int) ((rectX - x) * ratio);

				int ry = (int) ((rectY - y) * ratio);

				int rw = (int) (rectWidth * ratio);

				int rh = (int) (rectHeight * ratio);

				BufferedImage tmpImage = bufferedImage.getSubimage(rx, ry, rw,
						rh);

				System.out.println("Cut Image: (" + rw + " x " + rh + ")");

				// scale the selected portion to final image size and save it

				bufferedImage = new BufferedImage(TARGET_WIDTH, TARGET_HEIGHT,
						BufferedImage.TYPE_INT_BGR);

				bufg = bufferedImage.getGraphics();

				bufg.drawImage(tmpImage, 0, 0, TARGET_WIDTH, TARGET_HEIGHT,
						this);

				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				// System.out.println("33");
				// File f = new File("local_image.jpg");

				// System.out.println("Saved Image: local_image.png (" +
				// TARGET_WIDTH + " x " + TARGET_HEIGHT + ")");

				// ImageIO.write(bufferedImage, "png", f);

				ImageIO.write(bufferedImage, "png", baos);

				// System.out.println("44");

				return baos.toByteArray();

			} catch (Exception e) {

				e.printStackTrace();

				JOptionPane.showMessageDialog(this,
						"Error when caculating image: " + e.getMessage());

				return null;

			}

		}

	}

}
