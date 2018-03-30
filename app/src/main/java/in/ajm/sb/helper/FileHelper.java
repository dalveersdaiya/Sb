package com.org.besteverflatrate.helper;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.Locale;

import okhttp3.ResponseBody;

import static android.os.Environment.DIRECTORY_PICTURES;
import static android.os.Environment.MEDIA_MOUNTED;

/**
 * Class has utility functions related to file or directory.
 *
 * @see File for more details
 */


public class FileHelper
{
	/**
	 * Returns true if app has appropriate permission to read and write permission to access External Storage,
	 * false if permission not defined in AndroidManifest.xml file.
	 * <p/>
	 * Add android.permission.WRITE_EXTERNAL_STORAGE permission to access external storage.
	 *
	 * @param context Application context
	 * @return true/false
	 */
	private static boolean hasExternalStoragePermission(Context context)
	{
		int perm = context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
		return perm == PackageManager.PERMISSION_GRANTED;
	}

	/**
	 * Return file extension from provided file path or filename.
	 * return blank string if no extension will found in provided file or filename.
	 *
	 * @param filePath File path or filename
	 */
	private static String getFileExtension(String filePath)
	{
		if (filePath == null) return null;

		int dot = filePath.lastIndexOf(".");
		if (dot >= 0) return filePath.substring(dot + 1).toLowerCase(Locale.US);
		else return "";
	}

	/**
	 * Return filename from provided file or url.
	 * return blank string if string is blank or null if string is null.
	 *
	 * @param fileUri Filepath or url
	 */
	public static String getFileNameFromUri(String fileUri)
	{
		if (fileUri == null) return null;

		int separator = fileUri.lastIndexOf(File.separator);
		if (separator >= 0) return fileUri.substring(separator + 1);
		else return "";
	}

	/**
	 * Return MIME type of provided filename.
	 * return MIME type name if found else return default value *\/*.
	 *
	 * @param filename Filename
	 */
	public static String getFileMIMEType(String filename)
	{
		String extension = FileHelper.getFileExtension(filename);

		// Let's check the official map first. Webkit has a nice
		// extension-to-MIME map.
		// Be sure to remove the first character from the extension, which is
		// the "." character.
		if (extension.length() > 0)
		{
			String webkitMimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

			if (webkitMimeType != null)
			{
				// Found one. Let's take it!
				return webkitMimeType;
			}
		}
		return "*/*";
	}

	/**
	 * Return filename only (without extension) from provide file or url.
	 * return null if provided file or uri is null.
	 *
	 * @param fileUri Filepath or url
	 */
	private static String getFileNameWithoutExtension(String fileUri)
	{
		if (fileUri == null) return null;

		int separator = fileUri.lastIndexOf(File.separator);
		if (separator >= 0)
			fileUri = fileUri.substring(separator + 1);

		int dot = fileUri.lastIndexOf(".");
		if (dot >= 0) return fileUri.substring(0, dot);
		else return fileUri;
	}

	/**
	 * Return filepath only (without extension) from provide file or url.
	 * return null if provided file or uri is null.
	 *
	 * @param fileUri Filepath or url
	 */
	public static String getFilePathWithoutExtension(String fileUri)
	{
		if (fileUri == null) return null;

		int dot = fileUri.lastIndexOf(".");
		if (dot >= 0) return fileUri.substring(0, dot);
		else return fileUri;
	}

	/**
	 * Return absolute file path.
	 * return null if provided file or uri is null.
	 *
	 * @param context        Application Context
	 * @param parentDirName  Parent Directory name (null is folder/file is placed at app root folder.)
	 * @param name           Name of file or directory for get path
	 * @param preferExternal Is file or directory location on external storage
	 * @return null if file or directory does not exists.
	 */
	public static String getDirOrFilePath(Context context, String parentDirName, String name, boolean preferExternal)
	{
		File file = getDirOrFile(context, parentDirName, name, preferExternal);
		return file != null ? file.getPath() : "";
	}

	/**
	 * Return absolute file .
	 * return null if provided file or uri is null.
	 *
	 * @param context        Application Context
	 * @param parentDirName  Parent Directory name (null is folder/file is placed at app root folder.)
	 * @param name           Name of file or directory for get path
	 * @param preferExternal Is file or directory location on external storage
	 * @return null if file or directory does not exists.
	 */
	public static File getDirOrFile(Context context, String parentDirName, String name, boolean preferExternal)
	{
		File parentDir = getAppDirectory(context, preferExternal);
		if (parentDirName != null && !parentDirName.equals(""))
		{
			File dir = new File(parentDir, parentDirName);
			if (!dir.exists())
				return null;
			else
				parentDir = dir;
		}
		File file = new File(parentDir, name);
		if (file.exists())
			return file;

		return null;
	}


	/**
	 * Returns directory from application directory. App directory will be created on SD card
	 * <i>("/Android/data/[app_package_name]")</i> (if card is mounted and app has appropriate permission) or
	 * on device's file system depending incoming parameters.
	 *
	 * @param context        Application context
	 * @param name           Name of file or directory to check it exists or not
	 * @param preferExternal Whether prefer external location
	 * @return AppDir {@link File directory}.<br />
	 * <b>NOTE:</b> Can be null in some unpredictable cases (if SD card is unmounted and
	 * {@link Context#getFilesDir() Context.getFilesDir()} returns null).
	 */
	public static boolean checkDirOrFileExists(Context context, String parentDirName, String name, boolean preferExternal)
	{
		File parentDir;
		if (parentDirName != null && !parentDirName.trim().isEmpty())
		{
			parentDir = new File(getAppDirectory(context, preferExternal), parentDirName);
			if (!parentDir.exists())
				return false;
		} else
			parentDir = getAppDirectory(context, preferExternal);

		File file = new File(parentDir, name);
		return file.exists();
	}

	/**
	 * Returns root write directory. Root directory will be SD card root or internal space
	 * <i>("/")</i> (if card is mounted and app has appropriate permission) or
	 * on device's file system depending incoming parameters.
	 *
	 * @param context        Application context
	 * @param preferExternal Whether prefer external location
	 * @return RootDir {@link File directory}.<br />
	 * <b>NOTE:</b> Can be null in some unpredictable cases (if SD card is unmounted and
	 * {@link Context#getFilesDir() Context.getFilesDir()} returns null).
	 */
	public static File getRootDirectory(Context context, boolean preferExternal)
	{
		File rootDir = null;
		String externalStorageState;
		try
		{
			externalStorageState = Environment.getExternalStorageState();
		} catch (NullPointerException e)
		{
			externalStorageState = "";
		}

		try
		{
			if (preferExternal && MEDIA_MOUNTED.equals(externalStorageState) && hasExternalStoragePermission(context))
			{
				rootDir = Environment.getExternalStorageDirectory();
				if (!rootDir.exists())
				{
					if (!rootDir.mkdirs())
						throw new FileNotFoundException("Unable to create external app directory");
				}
			} else if (preferExternal)
				LogHelper.systemErrPrint("don't have permission to access writable storage. Please grant permission to write on external storage in AndroidManifest.xml");

			if (rootDir == null)
				rootDir = context.getFilesDir();

		} catch (Exception e)
		{
			LogHelper.printStackTrace(e);
		}

		return rootDir;
	}

	/**
	 * Returns application directory. App directory will be created on SD card
	 * <i>("/Android/data/[app_package_name]")</i> (if card is mounted and app has appropriate permission) or
	 * on device's file system depending incoming parameters.
	 *
	 * @param context        Application context
	 * @param preferExternal Whether prefer external location
	 * @return AppDir {@link File directory}.<br />
	 * <b>NOTE:</b> Can be null in some unpredictable cases (if SD card is unmounted and
	 * {@link Context#getFilesDir() Context.getFilesDir()} returns null).
	 */
	public static File getAppDirectory(Context context, boolean preferExternal)
	{
		File appDir = null;
		String externalStorageState;
		try
		{
			externalStorageState = Environment.getExternalStorageState();
		} catch (NullPointerException e)
		{
			externalStorageState = "";
		}
		if (preferExternal && MEDIA_MOUNTED.equals(externalStorageState) && hasExternalStoragePermission(context))
		{
			File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
			appDir = new File(dataDir, context.getPackageName());
			if (!appDir.exists())
			{
				if (!appDir.mkdirs())
					LogHelper.systemErrPrint("Unable to create external app directory");
				try
				{
					File noMediaFile = new File(appDir, ".nomedia");
					if (!noMediaFile.createNewFile())
						LogHelper.systemErrPrint("Can't create \".nomedia\" file in application external directory");
				} catch (IOException e)
				{
					LogHelper.systemErrPrint("Can't create \".nomedia\" file in application external directory");
				}
			}
		} else if (preferExternal)
			LogHelper.systemErrPrint("don't have permission to access writable storage. Please grant permission to write on external storage in AndroidManifest.xml");

		if (appDir == null)
			appDir = context.getFilesDir();

		if (appDir == null)
		{
			String appDirPath = context.getFilesDir().getPath() + context.getPackageName() + "/";
			LogHelper.systemErrPrint("Can't define system app directory! '" + appDirPath + "' will be used.");
			appDir = new File(appDirPath);
		}

		return appDir;
	}

	public static boolean createNewFile(Context context, String parentDirName, String fileName, boolean preferExternal, boolean deleteIfExists)
	{
		try
		{
			File parentDir = getAppDirectory(context, preferExternal);
			if (parentDirName != null && !parentDirName.equals(""))
			{
				File dir = new File(parentDir, parentDirName);
				if (!dir.exists() && !dir.mkdirs())
					return false;

				parentDir = dir;
			}
			File file = new File(parentDir, fileName);
			if (file.exists())
			{
				if (deleteIfExists)
					return file.delete() && file.createNewFile();
			} else
				return file.createNewFile();
		} catch (Exception e)
		{
			LogHelper.printStackTrace(e);
		}
		return false;
	}

	public static boolean createDirectory(Context context, String parentDirName, String dirName, boolean preferExternal)
	{
		try
		{
			if (parentDirName != null && !checkDirOrFileExists(context, null, parentDirName, preferExternal))
			{
				File parentDir = new File(getAppDirectory(context, preferExternal), parentDirName);
				if (!parentDir.mkdirs())
					return false;
				File dir = new File(parentDir, dirName);
				return (dir.exists() || dir.mkdirs());
			} else if (!checkDirOrFileExists(context, parentDirName, dirName, preferExternal))
			{
				File dir = new File(getAppDirectory(context, preferExternal), dirName);
				return (dir.exists() || dir.mkdirs());
			}
			return true;
		} catch (Exception e)
		{
			LogHelper.printStackTrace(e);
			return false;
		}
	}

	private static boolean deleteFileDirectory(Context context, String parentDirName, String fileName, boolean preferExternal)
	{
		try
		{
			if (parentDirName != null && !checkDirOrFileExists(context, null, parentDirName, preferExternal))
			{
				File parentDir = new File(getAppDirectory(context, preferExternal), parentDirName);
				File dir = new File(parentDir, fileName);
				if (dir.exists())
				{
					if (dir.isDirectory())
					{
						File[] files = dir.listFiles();
						for (File file : files)
						{
							if (file.isDirectory())
							{
								if (!deleteFileDirectory(context, dir.getPath(), file.getName(), preferExternal))
									return false;
							} else
							{
								return !file.delete();
							}
						}
						return dir.delete();
					} else
						return dir.delete();
				}
			}
			return false;
		} catch (Exception e)
		{
			LogHelper.printStackTrace(e);
			return false;
		}
	}

	public static boolean deleteFileDirectory(Context context, String filePath)
	{
		try
		{
			File dir = new File(filePath);
			if (dir.exists())
			{
				if (dir.isDirectory())
				{
					File[] files = dir.listFiles();
					for (File file : files)
					{
						if (file.isDirectory())
						{
							if (!deleteFileDirectory(context, file.getAbsolutePath()))
								return false;
						} else
						{
							if (!file.delete())
								return false;

						}
					}
					return dir.delete();
				} else
					return dir.delete();
			}
			return false;
		} catch (Exception e)
		{
			LogHelper.printStackTrace(e);
			return false;
		}
	}

	private static boolean emptyDirectory(Context context, String dirName, boolean preferExternal)
	{
		try
		{
			File dir = new File(getAppDirectory(context, preferExternal), dirName);
			if (dir.exists())
			{
				File[] files = dir.listFiles();
				for (File file : files)
				{
					if (file.isDirectory())
						emptyDirectory(context, dirName + File.separator + file.getName(), true);

					if (file.exists())
						file.delete();
				}
				return true;
			} else
			{
				return dir.mkdirs();
			}
		} catch (Exception e)
		{
			LogHelper.printStackTrace(e);
			return false;
		}
	}

	public static boolean renameDirectory(Context context, String parentFolderName, String oldName, String newName, boolean preferExternal)
	{
		try
		{
			File parentDir;
			if(parentFolderName!=null && parentFolderName.length()>0)
			{
				parentDir = new File(getAppDirectory(context, preferExternal), parentFolderName);
			}
			else{
				parentDir = getAppDirectory(context, preferExternal);
			}
			File oldDir = new File(parentDir, oldName);
			File newDir = new File(parentDir, newName);

			return oldDir.exists() && oldDir.renameTo(newDir);
		} catch (Exception e)
		{
			LogHelper.printStackTrace(e);
			return false;
		}
	}

	public static String copyFileToDirectory(Context context, String sourceFilePath, String destinationFolderPath, boolean preferExternal)
	{
		File sourceFile = new File(sourceFilePath);
		File destinationFolder = new File(FileHelper.getAppDirectory(context, preferExternal), destinationFolderPath);
		if (!sourceFile.exists())
			return null;
		if (!destinationFolder.exists())
		{
			if (!destinationFolder.mkdirs())
				return null;
		}

		try
		{
			File destFile = new File(destinationFolder, sourceFile.getName());

			FileChannel srcChannel = new FileInputStream(sourceFile.getPath()).getChannel();
			FileChannel dstChannel = new FileOutputStream(destFile.getPath()).getChannel();
			dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
			srcChannel.close();
			dstChannel.close();

			return destFile.getPath();
		} catch (Exception e)
		{
			LogHelper.printStackTrace(e);
			return null;
		}

	}

	public static void copyFilesOrFolder(File sourceFile, File destinationFile) throws IOException
	{
		if (sourceFile.isDirectory())
		{
			if (!destinationFile.exists())
			{
				if (destinationFile.mkdirs())
					return;
			}

			String[] children = sourceFile.list();
			for (String child : children)
			{
				copyFilesOrFolder(new File(sourceFile, child), new File(destinationFile, child));
			}
		} else
		{
			FileChannel inputChannel = null;
			FileChannel outputChannel = null;
			try
			{
				inputChannel = new FileInputStream(sourceFile).getChannel();
				outputChannel = new FileOutputStream(destinationFile).getChannel();

				final long size = inputChannel.size();
				long pos = 0;
				long FILE_COPY_BUFFER_SIZE = 1024 * 1024 * 30;
				while (pos < size)
				{
					final long remain = size - pos;
					long count = remain > FILE_COPY_BUFFER_SIZE ? FILE_COPY_BUFFER_SIZE : remain;
					final long bytesCopied = outputChannel.transferFrom(inputChannel, pos, count);
					if (bytesCopied == 0)
					{
						break;
					}
					pos += bytesCopied;
				}
			} catch (Exception e)
			{
				LogHelper.printStackTrace(e);
			} finally
			{
				if (inputChannel != null)
					inputChannel.close();
				if (outputChannel != null)
					outputChannel.close();
			}
//
//			InputStream in = new FileInputStream(sourceFile);
//			OutputStream out = new FileOutputStream(destinationFile);
//
//			byte[] buf = new byte[1024];
//			int len;
//			while ((len = in.read(buf)) > 0)
//			{
//				out.write(buf, 0, len);
//			}
//			in.close();
//			out.close();
		}
	}

	public static boolean saveStringDataToFile(Context context, String dirName, String fileName, String stringData, boolean preferExternal, boolean didOverrideIfExists, boolean didAppendData)
	{
		try
		{
			File dir = new File(getAppDirectory(context, preferExternal), dirName);
			if (!dir.exists())
			{
				LogHelper.systemErrPrint("Directory \"" + dirName + "\" not found");
				if (!dir.mkdirs())
					throw new FileNotFoundException("Could not able to create directory: " + dirName);
			}
			File file = new File(dir, fileName);
			if (!file.exists())
			{
				if (file.createNewFile())
					throw new FileNotFoundException("Could not able to create file: " + fileName + " into directory: " + dir.getPath());
			} else if (didOverrideIfExists)
			{
				if (file.delete() && file.createNewFile())
					throw new FileNotFoundException("Could not able to create file: " + fileName + " into directory: " + dir.getPath());
			}

			BufferedWriter buf = new BufferedWriter(new FileWriter(file, didAppendData));
			buf.write(stringData + "\r\n");
			buf.newLine();
			buf.flush();
			buf.close();
			return true;
		} catch (Exception e)
		{
			LogHelper.printStackTrace(e);
			return false;
		}
	}

	public static File createTempFile(Context context, String fileName, boolean preferExternal)
	{
		try
		{
			File dir = null;
			if (FileHelper.createDirectory(context, null, "temp", preferExternal))
				dir = FileHelper.getDirOrFile(context, null, "temp", preferExternal);

			String fileNameStr = FileHelper.getFileNameWithoutExtension(fileName);
			String fileExtStr = FileHelper.getFileExtension(fileName);
			File tempFile = File.createTempFile(fileNameStr, "." + fileExtStr, dir);
			if (tempFile.exists())
			{
				return tempFile;
			}
			return null;
		} catch (Exception e)
		{
			LogHelper.printStackTrace(e);
		}
		return null;
	}

	public static String saveTempFile(Context context, String dirName, String fileName, String fileExtension, byte[] data, boolean preferExternal)
	{
		try
		{
			if (data == null)
				return null;

			if (data.length == 0)
				return null;

			File dir = null;
			if (dirName != null && dirName.length() > 0)
			{
				dir = new File(getAppDirectory(context, preferExternal), dirName);
			} else if (preferExternal)
			{
				File appDir = FileHelper.getAppDirectory(context, true);
				if (FileHelper.createDirectory(context, null, "temp", true))
					dir = new File(appDir, "temp");
			}

			File tempFile = File.createTempFile(fileName, fileExtension, dir);
			if (tempFile.exists())
			{
				BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(tempFile));
				outputStream.write(data);
				outputStream.flush();
				outputStream.close();

				return tempFile.getAbsolutePath();
			}
			return null;
		} catch (Exception e)
		{
			LogHelper.printStackTrace(e);
		}
		return null;
	}

	public static String saveImageToDirectory(Context context, String dirName, String fileName, Bitmap bitmap, boolean preferExternal)
	{
		try
		{
			if (bitmap == null)
				return null;

			if (fileName == null)
				return null;

			File dir = new File(getAppDirectory(context, preferExternal), dirName);
			if (!dir.exists())
			{
				LogHelper.systemErrPrint("Directory \"" + dirName + "\" not found");
				if (!dir.mkdirs())
					throw new FileNotFoundException("Could not able to create directory: " + dirName);
			}

			File file = new File(dir, fileName);
			FileOutputStream outputStream = new FileOutputStream(file);

			String fileExtension = MimeTypeMap.getFileExtensionFromUrl(fileName);
			if (fileExtension.equalsIgnoreCase("JPG") || fileExtension.equalsIgnoreCase("JPEG"))
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
			else
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

			outputStream.flush();
			outputStream.close();
			return file.getPath();
		} catch (Exception e)
		{
			LogHelper.printStackTrace(e);
			return null;
		}
	}

	public static boolean saveImageIntoPictures(Context context, Bitmap bitmap, String fileName)
	{
		try
		{
			File pictureFolder = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES);
			String fileExtension = MimeTypeMap.getFileExtensionFromUrl(fileName);

			File file = new File(pictureFolder, fileName);
			FileOutputStream outputStream = new FileOutputStream(file);
			if (fileExtension.equalsIgnoreCase("JPG"))
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
			else
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

//			MediaStore.Images.Media.insertImage(context.getContentResolver(),file.getAbsolutePath(), file.getName(), null);
			context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));

			outputStream.flush();
			outputStream.close();
			return true;
		} catch (Exception e)
		{
			LogHelper.printStackTrace(e);
			return false;
		}
	}


	public static boolean writeResponseBodyToDisk(Context context, ResponseBody body, String parentFolderName, String fileName, boolean preferExternal)
	{
		try
		{
			File dir;
			if (parentFolderName != null && !parentFolderName.equals(""))
			{
				dir = new File(getAppDirectory(context, preferExternal), parentFolderName);

				if (!dir.exists() && !dir.mkdirs())
					dir = getAppDirectory(context, preferExternal);
			} else
				dir = getAppDirectory(context, preferExternal);

			File file = new File(dir, fileName);
			InputStream inputStream = null;
			OutputStream outputStream = null;
			try
			{
				byte[] fileReader = new byte[4096];

				long fileSize = body.contentLength();
				long fileSizeDownloaded = 0;

				inputStream = body.byteStream();
				outputStream = new FileOutputStream(file);

				while (true)
				{
					int read = inputStream.read(fileReader);

					if (read == -1)
					{
						break;
					}

					outputStream.write(fileReader, 0, read);
					fileSizeDownloaded += read;
				}
				outputStream.flush();
				return true;
			} catch (IOException e)
			{
				return false;
			} finally
			{
				if (inputStream != null)
					inputStream.close();

				if (outputStream != null)
					outputStream.close();
			}
		} catch (IOException e)
		{
			return false;
		}
	}

	public static String getFileStringData(String filePath)
	{
		byte[] fileData = getFileBytes(filePath);
		if (fileData != null)
		{
			return new String(fileData);
		}
		return null;
	}

	private static byte[] getFileBytes(String filePath)
	{
		File file = new File(filePath);
		try
		{
			FileInputStream inputStream = new FileInputStream(file);
			byte[] fileData = new byte[inputStream.available()];
			int read = 0;
			while (read != fileData.length)
			{
				read += inputStream.read(fileData, read, fileData.length - read);
			}
			inputStream.close();
			return fileData;
		} catch (Exception e)
		{
			LogHelper.printStackTrace(e);
			return null;
		}
	}

	public static String getPathFromUri(final Context context, final Uri uri)
	{
		// DocumentProvider
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri))
		{
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri))
			{
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type))
				{
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri))
			{

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri))
			{
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type))
				{
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type))
				{
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type))
				{
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[]{
						split[1]
				};

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme()))
		{
			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme()))
		{
			return uri.getPath();
		}

		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context       The context.
	 * @param uri           The Uri to query.
	 * @param selection     (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs)
	{

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = {
				column
		};

		try
		{
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
					null);
			if (cursor != null && cursor.moveToFirst())
			{
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} finally
		{
			if (cursor != null)
				cursor.close();
		}
		return null;
	}


	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	private static boolean isExternalStorageDocument(Uri uri)
	{
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	private static boolean isDownloadsDocument(Uri uri)
	{
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	private static boolean isMediaDocument(Uri uri)
	{
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	public static String readFromAssets(Context context, String filename)
	{
		try
		{
			InputStream inputStream = context.getAssets().open(filename);
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

			// do reading, usually loop until end of file reading
			StringBuilder sb = new StringBuilder();
			String mLine = reader.readLine();
			while (mLine != null)
			{
				sb.append(mLine); // process line
				mLine = reader.readLine();
			}
			reader.close();
			return sb.toString();
		} catch (Exception e)
		{
			LogHelper.printStackTrace(e);
			return null;
		}
	}
}
