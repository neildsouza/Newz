package in.co.freesoftsolutions.newz;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import java.util.List;

import in.co.freesoftsolutions.newz.model.RSSFeed;
import in.co.freesoftsolutions.newz.model.RSSFeeds;

public class RSSFeedsFragment extends Fragment {
    private static final String DIALOG_ADD_RSS_FEED = "AddRSSFeedDialog";
    private static final int REQUEST_ADD_RSS_FEED = 0;

    private RSSFeedsAdapter mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rss_feeds, container, false);

        mRecyclerView = view.findViewById(R.id.rss_feeds_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_ADD_RSS_FEED) {
            int x = data.getIntExtra(RSSFeedDialog.EXTRA_FEED_ADDED, 0);
            if (x == 1) {
                updateUI();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_rss_feeds, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.add_rss_feed_menu_item:
                FragmentManager fm = getFragmentManager();
                RSSFeedDialog addRssFeedDialog = new RSSFeedDialog();
                addRssFeedDialog.setTargetFragment(RSSFeedsFragment.this, REQUEST_ADD_RSS_FEED);
                addRssFeedDialog.show(fm, DIALOG_ADD_RSS_FEED);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class RSSFeedHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private RSSFeed mRSSFeed;
        private TextView mChannel;
        private TextView mUrl;

        RSSFeedHolder(View itemView) {
            super(itemView);

            mChannel = itemView.findViewById(R.id.rss_feed_channel_text_view);
            mUrl = itemView.findViewById(R.id.rss_feed_url_text_view);

            itemView.setOnClickListener(this);
        }

        void bindRssFeed(RSSFeed rssFeed) {
            mRSSFeed = rssFeed;
            mChannel.setText(mRSSFeed.getChannel());
            mUrl.setText(mRSSFeed.getUrl());
        }

        @Override
        public void onClick(View view) {
            Intent i = RSSPagerActivity.newIntent(getActivity(), mRSSFeed.getUUID());
            startActivity(i);
        }
    }

    private class RSSFeedsAdapter extends RecyclerView.Adapter<RSSFeedHolder> {
        private List<RSSFeed> mRSSFeeds;

        RSSFeedsAdapter(List<RSSFeed> rssFeeds) {
            mRSSFeeds = rssFeeds;
        }

        @Override
        public RSSFeedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity())
                    .inflate(R.layout.list_item_rss_feeds, parent, false);

            return new RSSFeedHolder(view);
        }

        @Override
        public void onBindViewHolder(RSSFeedHolder holder, int pos) {
            RSSFeed rssFeed = mRSSFeeds.get(pos);
            holder.bindRssFeed(rssFeed);
        }

        @Override
        public int getItemCount() {
            return mRSSFeeds.size();
        }

        void setRSSFeeds(List<RSSFeed> rssFeeds) {
            mRSSFeeds = rssFeeds;
        }

        public void remove(int pos) {
            RSSFeed rssFeed = mRSSFeeds.get(pos);
            RSSFeeds.get(getActivity()).deleteRSSFeed(rssFeed.getUUID());
            mRSSFeeds.remove(pos);
            notifyItemRemoved(pos);
        }
    }

    private void updateUI() {
        RSSFeeds rssFeeds = RSSFeeds.get(getActivity());
        List<RSSFeed> rssFeedsList = rssFeeds.getRSSFeeds();

        if (mAdapter == null) {
            mAdapter = new RSSFeedsAdapter(rssFeedsList);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setRSSFeeds(rssFeedsList);
            mAdapter.notifyDataSetChanged();
        }

        ItemTouchHelper.Callback callback = new RSSFeedsTouchHelper();
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);
    }

    private class RSSFeedsTouchHelper extends ItemTouchHelper.SimpleCallback {
        RSSFeedsTouchHelper(){
            super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            //TODO: Not implemented here
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            mAdapter.remove(viewHolder.getAdapterPosition());
        }
    }
}
