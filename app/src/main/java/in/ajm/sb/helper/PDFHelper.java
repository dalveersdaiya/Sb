package com.org.besteverflatrate.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.org.besteverflatrate.activities.InvoicePdfViewer;
import com.org.besteverflatrate.activities.PrinterOptions;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class PDFHelper
{
	private String title;
	private String callid;
	private String org_id;
	private String call_type;
	private Context context;
	private File fileToSave;
	private ProgressDialog progressDialog;

	public static PDFHelper getInstance(Context context)
	{
		PDFHelper _self = new PDFHelper();
		_self.context = context;

		return _self;
	}

	public static PDFHelper getInstance(Context context, String callid, String org_id, String title, String call_type)
	{
		PDFHelper _self = new PDFHelper();
		_self.context = context;
		_self.callid = callid;
		_self.org_id = org_id;
		_self.title = title;
		_self.call_type = call_type;

		return _self;

	}

	public void openPdf(final String url, final File fileToSave, final String Title)
	{
		if (fileToSave.exists())
		{
			((Activity) context).runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					if (title.equals("PRINTOPTION"))
					{
						Intent intent = new Intent(context, PrinterOptions.class);
						intent.putExtra("OPENVIEW", "pdf");
						intent.putExtra("filename", fileToSave.getAbsolutePath());
						intent.putExtra("title", Title);
						intent.putExtra("callid", callid);
						intent.putExtra("org_id", org_id);
						intent.putExtra("url", url);
						intent.putExtra("PASTCALL_TYPE", call_type);
						context.startActivity(intent);
					} else
					{
						Intent intent = new Intent(context, InvoicePdfViewer.class);
						intent.putExtra("OPENVIEW", "pdf");
						intent.putExtra("filename", fileToSave.getAbsolutePath());
						intent.putExtra("title", Title);
						intent.putExtra("callid", callid);
						intent.putExtra("org_id", org_id);
						intent.putExtra("url", url);
						intent.putExtra("PASTCALL_TYPE", call_type);
						context.startActivity(intent);
					}
				}
			});
		} else
		{
			downloadFile(url, fileToSave, Title);
		}
	}

	private void downloadFile(String url, File fileToSave, String Title)
	{
		this.fileToSave = fileToSave;
		new DownloadTask().execute(url);
	}

	private void showProgressDialog()
	{
		dissmissProgressDialog();
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage("Please wait...\nDownloading PDF File");
		progressDialog.setCancelable(false);
		progressDialog.show();

	}

	private void dissmissProgressDialog()
	{
		try
		{
			progressDialog.dismiss();
		} catch (Exception e)
		{
			LogHelper.printStackTrace(e);
		}
	}

	private class DownloadTask extends AsyncTask<String, Void, String>
	{


		@Override
		protected void onPreExecute()
		{
			showProgressDialog();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(final String path)
		{
			dissmissProgressDialog();
			if (path != null)
			{
				((Activity) context).runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						if (title.equals("PRINTOPTION"))
						{
							Intent intent = new Intent(context, PrinterOptions.class);
							intent.putExtra("OPENVIEW", "pdf");
							intent.putExtra("filename", fileToSave.getAbsolutePath());
							intent.putExtra("title", title);
							intent.putExtra("callid", callid);
							intent.putExtra("org_id", org_id);
							intent.putExtra("url", path);
							intent.putExtra("title", title);
							intent.putExtra("PASTCALL_TYPE", call_type);
							context.startActivity(intent);
						} else
						{
							Intent intent = new Intent(context, InvoicePdfViewer.class);
							intent.putExtra("OPENVIEW", "pdf");
							intent.putExtra("filename", fileToSave.getAbsolutePath());
							intent.putExtra("title", title);
							intent.putExtra("callid", callid);
							intent.putExtra("org_id", org_id);
							intent.putExtra("url", path);
							intent.putExtra("title", title);
							intent.putExtra("PASTCALL_TYPE", call_type);
							context.startActivity(intent);
						}
					}
				});
			}
			super.onPostExecute(path);
		}

		@Override
		protected String doInBackground(String... params)
		{
			String s_url = params[0];
			int count;
			try
			{

				URL url = new URL(s_url);
				URLConnection conexion = url.openConnection();
				conexion.connect();

				int lenghtOfFile = conexion.getContentLength();
				InputStream input = new BufferedInputStream(url.openStream());
				OutputStream output = new FileOutputStream(fileToSave.getAbsolutePath());

				byte data[] = new byte[1024];

				long total = 0;
				while ((count = input.read(data)) != -1)
				{
					total += count;
					output.write(data, 0, count);
				}

				output.flush();
				output.close();
				input.close();
				return fileToSave.getAbsolutePath();
			} catch (Exception e)
			{
				LogHelper.printStackTrace(e);
				fileToSave.delete();
			}
			return null;
		}
	}
}
