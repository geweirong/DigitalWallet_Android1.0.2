package com.megvii.idcardlib.util;

import android.graphics.Bitmap;

import com.megvii.livenesslib.util.Constant;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 文件工具类
 */
public class IdCardFile
{

	public IdCardFile() {
	}

	/**
	 * 把图片保存到文件夹
	 */
	public static boolean save(Bitmap mBitmap, String key, String fileName, String session,
							   JSONObject jsonObject) {
		try {
			String dirPath = Constant.dirName + "/" + session;
			File dir = new File(dirPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(dir, fileName +".jpg");
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
			fileOutputStream.flush();
			fileOutputStream.close();
			jsonObject.put(key, file.getAbsoluteFile());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 把LOG保存到本地
	 */
	public boolean saveLog(String session, String name) {
		try {
			String dirPath = Constant.dirName + "/" + session;
			File dir = new File(dirPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(dir, "Log.txt");
			FileOutputStream fileOutputStream = new FileOutputStream(file, true);
			String str = "\n" + session + ",  " + name;
			fileOutputStream.write(str.getBytes());
			fileOutputStream.flush();
			fileOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
