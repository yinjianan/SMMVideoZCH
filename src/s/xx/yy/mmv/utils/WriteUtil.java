package s.xx.yy.mmv.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;

public class WriteUtil {
	public static void WriteTxtFile(String strcontent, String strFilePath) {
		String strContent = strcontent + "\n";
		try {
			File file = new File(strFilePath);
			if (!file.exists()) {
				MyLog.i("info", "Create the file:" + strFilePath);
				file.createNewFile();
			}
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			raf.seek(file.length());
			raf.write(strContent.getBytes());
			raf.close();
		} catch (Exception e) {
			MyLog.e("info", "Error on write File." + e);
		}
	}

	public static void writeToText(String content, String filePath) {
		if (!filePath.endsWith(".txt") && !filePath.endsWith(".log"))
			filePath += ".txt";
		File file = new File(filePath);
		try {
			OutputStream outstream = new FileOutputStream(file);
			OutputStreamWriter out = new OutputStreamWriter(outstream);
			out.write(content);
			out.close();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeHtml(String content, String filePath) {
		// if (!filePath.endsWith(".txt") && !filePath.endsWith(".log"))
		// filePath += ".txt";
		File file = new File(filePath);
		try {
			OutputStream outstream = new FileOutputStream(file, false);
			OutputStreamWriter out = new OutputStreamWriter(outstream);
			out.write(content);
			out.close();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
	}
}
