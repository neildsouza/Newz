package in.co.freesoftsolutions.newz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

import in.co.freesoftsolutions.newz.model.RSSFeed;
import in.co.freesoftsolutions.newz.model.RSSFeeds;

public class RSSPagerActivity extends AppCompatActivity {
    private static final String EXTRA_RSS_UUID =
            "freesoftsolutions.co.in.thenewz.rsspageractivity.rss_uuid";

    private List<RSSFeed> mRSSFeeds;

    public static Intent newIntent(Context context, UUID rssUUID) {
        Intent i = new Intent(context, RSSPagerActivity.class);
        i.putExtra(EXTRA_RSS_UUID, rssUUID);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss_pager);

        UUID rssFeedUUID = (UUID) getIntent().getSerializableExtra(EXTRA_RSS_UUID);

        mRSSFeeds = RSSFeeds.get(this).getRSSFeeds();

        FragmentManager fm = getSupportFragmentManager();

        ViewPager mViewPager = (ViewPager) findViewById(R.id.view_pager_rss_pager);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                RSSFeed rssFeed = mRSSFeeds.get(position);
                return RSSFeedFragment.newInstance(rssFeed.getUUID());
            }

            @Override
            public int getCount() {
                return mRSSFeeds.size();
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position,
                                       float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                RSSFeed rssFeed = mRSSFeeds.get(position);
                getSupportActionBar().setTitle(rssFeed.getChannel());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        for (int i = 0; i < mRSSFeeds.size(); i++) {
            RSSFeed rssFeed = mRSSFeeds.get(i);
            if (rssFeed.getUUID().equals(rssFeedUUID)) {
                mViewPager.setCurrentItem(i);
                getSupportActionBar().setTitle(rssFeed.getChannel());
                break;
            }
        }
    }
}
