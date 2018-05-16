package in.ajm.sb_library.customviews;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by niteshpurohit on 12/11/16.
 */

public class FontHelper {
    private static FontHelper _self = null;
    private Typeface boldFont;
    private Typeface regularFont;
    private Typeface medium;
    private Typeface regularLight;

    public static FontHelper getInstance(Context context) {
        if (_self == null) {
//			_self = new FontHelper();
//			_self.setRegularFont(Typeface.createFromAsset(context.getAssets(), "fonts/AvenirNext-Regular.ttf"));
//			_self.setBoldFont(Typeface.createFromAsset(context.getAssets(), "fonts/AvenirNext-DemiBold.ttf"));
//			_self.setMedium(Typeface.createFromAsset(context.getAssets(), "fonts/AvenirNext-Medium.ttf"));
//			_self.setRegularLight(Typeface.createFromAsset(context.getAssets(), "fonts/AvenirNext-UltraLight.ttf"));

            _self = new FontHelper();
            _self.setRegularFont(Typeface.createFromAsset(context.getAssets(), "fonts/Mark Simonson - Proxima Nova Alt Regular-webfont.ttf"));
            _self.setBoldFont(Typeface.createFromAsset(context.getAssets(), "fonts/Mark Simonson - Proxima Nova Alt Condensed Semibold-webfont.ttf"));
            _self.setMedium(Typeface.createFromAsset(context.getAssets(), "fonts/Mark Simonson - Proxima Nova Semibold-webfont.ttf"));
            _self.setRegularLight(Typeface.createFromAsset(context.getAssets(), "fonts/Mark Simonson - Proxima Nova Thin-webfont.ttf"));
        }
        return _self;
    }

    public Typeface getBoldFont() {
        return boldFont;
    }

    private void setBoldFont(Typeface boldFont) {
        this.boldFont = boldFont;
    }

    public Typeface getRegularFont() {
        return regularFont;
    }

    private void setRegularFont(Typeface regularFont) {
        this.regularFont = regularFont;
    }

    public Typeface getMedium() {
        return medium;
    }

    private void setMedium(Typeface medium) {
        this.medium = medium;
    }

    public Typeface getRegularLight() {
        return regularLight;
    }

    private void setRegularLight(Typeface regularLight) {
        this.regularLight = regularLight;
    }
}
