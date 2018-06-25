package theschulk.com.gainztrain.Activity;

import android.content.Intent;
import android.widget.RemoteViewsService;

import theschulk.com.gainztrain.Adapters.GainzTrainRemoteViewsFactory;

public class GainzTrainService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GainzTrainRemoteViewsFactory(this, intent);
    }
}
