package in.ajm.sb.api.callback;


import in.ajm.sb.api.enums.ApiType;

/**
 * Created by DSD on 05/01/16.
 */
public interface APICallback
{
	void onResult(String result, ApiType apitype, int resultCode);
}
