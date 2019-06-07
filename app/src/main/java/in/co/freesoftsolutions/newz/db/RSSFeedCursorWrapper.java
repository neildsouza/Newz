package in.co.freesoftsolutions.newz.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.UUID;

import in.co.freesoftsolutions.newz.db.RSSFeedsSchema.RSSFeedsTable;
import in.co.freesoftsolutions.newz.model.RSSFeed;

public class RSSFeedCursorWrapper extends CursorWrapper {
    public RSSFeedCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public RSSFeed getRSSFeed() {
        String uuidString = getString(getColumnIndex(RSSFeedsTable.Cols.UUID));
        String channel = getString(getColumnIndex(RSSFeedsTable.Cols.CHANNEL));
        String url = getString(getColumnIndex(RSSFeedsTable.Cols.URL));

        RSSFeed rssFeed = new RSSFeed(UUID.fromString(uuidString));
        rssFeed.setChannel(channel);
        rssFeed.setUrl(url);

        return rssFeed;
    }
}
