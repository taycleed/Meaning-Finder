package org.bigcamp4edu.meaningfinder;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import android.widget.ImageView;

public class Image_Downloader {
	public static final int IMGAE_CACHE_LIMIT_SIZE = 50;
	public static HashMap<String, Bitmap> mImageCache = new HashMap<String, Bitmap>();

	public static void download(final String url, ImageView imageView) {
//		Bitmap cachedImage = mImageCache.get(url);
		/*if (cachedImage != null) {
			imageView.setImageBitmap(cachedImage);
		} else */
		if (cancelPotentialDownload(url, imageView)) {
			if (mImageCache.size() > IMGAE_CACHE_LIMIT_SIZE) {
				mImageCache.clear();
			}

			final Image_DownloaderTask task = new Image_DownloaderTask(url, imageView);
			DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
			imageView.setImageDrawable(downloadedDrawable);
			// RecycleUtils.recursiveRecycle(imageView);
			try {
				task.execute(url);
			} catch (Exception e) {
				task.cancel(true);
//				Log.e("ID", "download e " + e);
			}
		}
	}

	private static boolean cancelPotentialDownload(String url,
			ImageView imageView) {
		Image_DownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);

		if (bitmapDownloaderTask != null) {
			String bitmapUrl = bitmapDownloaderTask.url;
			if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
				bitmapDownloaderTask.cancel(true);
			} else {
				return false;
			}
		}
		return true;
	}

	private static Image_DownloaderTask getBitmapDownloaderTask(
			ImageView imageView) {
		if (imageView != null) {
			Drawable drawable = imageView.getDrawable();
			if (drawable instanceof DownloadedDrawable) {
				DownloadedDrawable downloadedDrawable = (DownloadedDrawable) drawable;
				return downloadedDrawable.getBitmapDownloaderTask();
			}
		}
		return null;
	}

	static class DownloadedDrawable extends ColorDrawable {
		private final WeakReference<Image_DownloaderTask> bitmapDownloaderTaskReference;

		public DownloadedDrawable(Image_DownloaderTask bitmapDownloaderTask) {
			super(Color.TRANSPARENT);
			bitmapDownloaderTaskReference = new WeakReference<Image_DownloaderTask>(
					bitmapDownloaderTask);
		}

		public Image_DownloaderTask getBitmapDownloaderTask() {
			return bitmapDownloaderTaskReference.get();
		}
	}
}
