package download.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Environment;

import java.io.Closeable;
import java.io.File;
import java.net.HttpURLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {
	/**
	 * 利用签名辅助类，将字符串字节数组
	 * 
	 * @param str
	 * @return
	 */
	public static String md5(String str) {
		byte[] digest = null;
		try {
			MessageDigest md = MessageDigest.getInstance("md5");
			digest = md.digest(str.getBytes());
			return bytes2hex02(digest);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * 方式二
	 * 
	 * @param bytes
	 * @return
	 */
	public static String bytes2hex02(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		String tmp = null;
		for (byte b : bytes) {
			// 将每个字节与0xFF进行与运算，然后转化为10进制，然后借助于Integer再转化为16进制
			tmp = Integer.toHexString(0xFF & b);
			if (tmp.length() == 1)// 每个字节8为，转为16进制标志，2个16进制位
			{
				tmp = "0" + tmp;
			}
			sb.append(tmp);
		}

		return sb.toString();

	}

	public static File getDiskCacheDir(Context context, String uniqueName) {
		String cachePath = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			File cacheDir = context.getExternalCacheDir();
			if (cacheDir != null) {
				cachePath = cacheDir.getPath(); 
			}
		} else {
			cachePath = context.getCacheDir().getPath();
		}
		if (cachePath == null) {
			cachePath = Environment.getExternalStorageDirectory().getAbsolutePath();
		}
		File file = new File(cachePath + File.separator + uniqueName);
		if (!file.exists()){
			file.mkdirs();
		}
		return file;
	}
	public static String getNameFromUrl(String url){
		return url.substring(url.lastIndexOf("/")+1);
	}

	public static int getAppVersion(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 1;
	}
    public static String getFileSize(long size){
    	if (size < 1024) {
			return size + "B";
		}else if (size < 1024*1024) {
			return new java.text.DecimalFormat("#.##").format(size / 1024.0f) + "KB";
		}else {
			return new java.text.DecimalFormat("#.##").format(size / (1024 * 1024.0f)) + "M";
		}
    }

	public static void close(Closeable c) {
		try {
			if (c != null) {
				c.close();
			}
		} catch (Exception e) {
		}
	}

	public static void close(HttpURLConnection c) {
		if (c != null) {
			c.disconnect();
		}
	}

	public static void close(HttpURLConnection conn, Closeable out, Closeable in) {
		close(conn);
		close(out);
		close(in);
	}


}
