package in.co.freesoftsolutions.newz;

import android.support.v4.app.Fragment;

public class RSSFeedsActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new RSSFeedsFragment();
    }
}