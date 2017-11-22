package com.tca.webcam;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;

import com.github.sarxos.webcam.Webcam;

@SuppressWarnings("serial")
public class PickupWebCamera extends JDialog {

	private String chosedDeviceName = "";
	private List<Webcam> webcams = Webcam.getWebcams();
	private JButton btStart = new JButton(new StartAction());
	private JComboBox<String> deviceList;

	private class StartAction extends AbstractAction {

		public StartAction() {
			super("Next");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String name = deviceList.getSelectedItem().toString();
			System.out.println("you choosed " + name);
			new TakeSnapshotFromWebCam(name);
			getChosedDeviceName();
			// btStart.setEnabled(false);
		}
	}

	public PickupWebCamera(JFrame frame,String title) {
		super(frame,title);

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

	public static void main(String[] args) {

		//new PickupWebCamera(null, "Device");
	}
}
