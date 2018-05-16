package in.ajm.sb.schoolwidget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class TestWidgetService  extends RemoteViewsService{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new TestWidgetDataProvider(this, intent);
    }
}
