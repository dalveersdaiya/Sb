package in.ajm.sb.helper;

import android.support.design.widget.CoordinatorLayout;
import android.view.View;

public class SlideMenuHelper {
    /**
     * Drawer view is the view in Drawer onDrawerSlide method
     * slideOffset is the float in Drawer onDrawerSlide method
     * appbarmain is the parent layout of your app bar main layout
     * @param drawerView
     * @param slideOffset
     * @param app_bar_main
     */
    public static void setSlideMenu(View drawerView, float slideOffset, CoordinatorLayout app_bar_main){
        float moveFactor = 0;
        moveFactor = (drawerView.getWidth() * slideOffset);
        app_bar_main.setTranslationX(moveFactor);
    }

    /**
     * Step 1 : add drawer lister
     * Step 2 : provide id to the main layout of the appBarmain layout in your drawer layout
     * Step 3 : On Drawer slide method call this method
     * Step 4 : pass params like : above mentioned
     */

}
