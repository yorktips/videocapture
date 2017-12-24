package tct;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
//import java.util.Base64;
import javax.xml.bind.DatatypeConverter;
import javax.imageio.ImageIO;

public class ImageUploadTool {

	public static byte[] toByteArray(final String filename) {
		try {
			BufferedImage originalImage = ImageIO.read(new File(filename));

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(originalImage, "jpg", baos);
			baos.flush();
			byte[] imageInByte = baos.toByteArray();
			baos.close();
			return imageInByte;
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public static boolean saveImagetoFile(String path, String file, byte[] b) {

		boolean flag = false;

		try {

			ByteArrayInputStream bais = new ByteArrayInputStream(b);

			BufferedImage bi = ImageIO.read((InputStream) bais);

			File f1 = new File(path);

			if (!f1.exists())
				f1.mkdirs();

			ImageIO.write(bi, "jpg", new File(path, file));

			flag = true;

		} catch (Exception e) {

			e.printStackTrace();

		}

		return flag;

	}

	public static String encodImage(byte[] data) {
		if (data == null)
			return null;

		
		String head = "data:image\\/png;base64,";
		//String encodedStr = Base64.getEncoder().encodeToString(data);
		//byte[] data = "hello world".getBytes("UTF-8");
		String encodedStr = DatatypeConverter.printBase64Binary(data);
		System.out.println(encodedStr);
		return head + encodedStr;
	}

	// String imageDataBytes =
	// completeImageData.substring(completeImageData.indexOf(",")+1);
	// InputStream stream = new
	// ByteArrayInputStream(Base64.decode(imageDataBytes.getBytes(),
	// Base64.DEFAULT));
	// "data:image\/png;base64,iVBORw0KGgoAAAANSUhEUgAAAVI..."
	// import java.util.Base64;
	public static byte[] getImageData(byte[] data) {
		final String BASE64KEYWORD = "base64,";
		String imgBuf = "";

		if (data == null)
			return data;

		String str = new String(data);
		int idx = str.indexOf(BASE64KEYWORD);

		if (idx > 10 && idx < 1000) {
			imgBuf = str.toString().substring(idx + BASE64KEYWORD.length());
			imgBuf = imgBuf.split("\n")[0];
			//return Base64.getDecoder().decode(imgBuf);
			return DatatypeConverter.parseBase64Binary(imgBuf);

			// => hello world
		}

		return data;
	}

}


