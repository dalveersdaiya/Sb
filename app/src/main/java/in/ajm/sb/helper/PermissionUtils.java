package com.org.besteverflatrate.helper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public abstract class PermissionUtils
{
	/**
	 * Requests the fine location permission. If a rationale with an additional explanation should
	 * be shown to the user, displays a dialog that triggers the request.
	 */
	public static void requestPermission(FragmentActivity activity, int requestId, String permission, boolean finishActivity, String msg)
	{
		if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission))
		{
			// Display a dialog with rationale.
			RationaleDialog.newInstance(requestId, finishActivity, permission, msg).show(activity.getSupportFragmentManager(), "dialog");
		} else
		{
			// Location permission has not been granted yet, request it.
			ActivityCompat.requestPermissions(activity, new String[]{permission}, requestId);

		}
	}

	/**
	 * Checks if the result contains a {@link PackageManager#PERMISSION_GRANTED} result for a
	 * permission from a runtime permissions request.
	 *
	 * @see ActivityCompat.OnRequestPermissionsResultCallback
	 */
	public static boolean isPermissionGranted(String[] grantPermissions, int[] grantResults, String permission)
	{
		for (int i = 0; i < grantPermissions.length; i++)
		{
			if (permission.equals(grantPermissions[i]))
			{
				return grantResults[i] == PackageManager.PERMISSION_GRANTED;
			}
		}
		return false;
	}

	/**
	 * A dialog that displays a permission denied message.
	 */
	public static class PermissionDeniedDialog extends DialogFragment
	{
		private static final String ARGUMENT_FINISH_ACTIVITY = "finish";
		private static final String ARGUMENT_MESSAGE = "msg";
		private String mMsg;
		private boolean mFinishActivity = false;

		/**
		 * Creates a new instance of this dialog and optionally finishes the calling Activity
		 * when the 'Ok' button is clicked.
		 */
		public static PermissionDeniedDialog newInstance(boolean finishActivity, String msg)
		{
			Bundle arguments = new Bundle();
			arguments.putBoolean(ARGUMENT_FINISH_ACTIVITY, finishActivity);
			arguments.putString(ARGUMENT_MESSAGE, msg);

			PermissionDeniedDialog dialog = new PermissionDeniedDialog();
			dialog.setArguments(arguments);
			return dialog;
		}

		@NonNull
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState)
		{
			mFinishActivity = getArguments().getBoolean(ARGUMENT_FINISH_ACTIVITY);
			mMsg = getArguments().getString(ARGUMENT_MESSAGE);

			return new AlertDialog.Builder(getActivity())
					.setMessage(mMsg)
					.setPositiveButton(android.R.string.ok, null)
					.create();
		}

		@Override
		public void onDismiss(DialogInterface dialog)
		{
			super.onDismiss(dialog);
			if (mFinishActivity)
			{
				Toast.makeText(getActivity(), "Permission is required.", Toast.LENGTH_SHORT).show();
				getActivity().finish();
			}
		}
	}

	/**
	 * A dialog that explains the use of the location permission and requests the necessary
	 * permission.
	 * <p>
	 * The activity should implement
	 * {@link ActivityCompat.OnRequestPermissionsResultCallback}
	 * to handle permit or denial of this permission request.
	 */
	public static class RationaleDialog extends DialogFragment
	{
		private static final String ARGUMENT_PERMISSION_REQUEST_CODE = "requestCode";
		private static final String ARGUMENT_FINISH_ACTIVITY = "finish";
		private static final String ARGUMENT_MESSAGE = "message";
		private static final String ARGUMENT_PERMISSION = "permission";

		private String mMsg;
		private String mPermission;
		private boolean mFinishActivity = false;

		/**
		 * Creates a new instance of a dialog displaying the rationale for the use of the location
		 * permission.
		 * <p>
		 * The permission is requested after clicking 'ok'.
		 *
		 * @param requestCode    Id of the request that is used to request the permission. It is
		 *                       returned to the
		 *                       {@link ActivityCompat.OnRequestPermissionsResultCallback}.
		 * @param finishActivity Whether the calling Activity should be finished if the dialog is
		 *                       cancelled.
		 */
		public static RationaleDialog newInstance(int requestCode, boolean finishActivity, String permission, String msg)
		{
			Bundle arguments = new Bundle();
			arguments.putInt(ARGUMENT_PERMISSION_REQUEST_CODE, requestCode);
			arguments.putBoolean(ARGUMENT_FINISH_ACTIVITY, finishActivity);
			arguments.putString(ARGUMENT_MESSAGE, msg);
			arguments.putString(ARGUMENT_PERMISSION, permission);
			RationaleDialog dialog = new RationaleDialog();
			dialog.setArguments(arguments);
			return dialog;
		}

		@NonNull
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState)
		{
			Bundle arguments = getArguments();
			final int requestCode = arguments.getInt(ARGUMENT_PERMISSION_REQUEST_CODE);
			mFinishActivity = arguments.getBoolean(ARGUMENT_FINISH_ACTIVITY);
			mMsg = arguments.getString(ARGUMENT_MESSAGE);
			mPermission = arguments.getString(ARGUMENT_PERMISSION);

			return new AlertDialog.Builder(getActivity())
					.setMessage(mMsg)
					.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							// After click on Ok, request the permission.
							ActivityCompat.requestPermissions(getActivity(), new String[]{mPermission}, requestCode);
							// Do not finish the Activity while requesting permission.
							mFinishActivity = false;
						}
					})
					.setNegativeButton(android.R.string.cancel, null)
					.create();
		}

		@Override
		public void onDismiss(DialogInterface dialog)
		{
			super.onDismiss(dialog);
			if (mFinishActivity)
			{
				Toast.makeText(getActivity(), "Permission is required.", Toast.LENGTH_SHORT).show();
				getActivity().finish();
			}
		}
	}
}
