package com.dm.rosary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.viewpagerindicator.CirclePageIndicator;

import me.kaelaela.verticalviewpager.VerticalViewPager;

public class MainActivity extends AppCompatActivity {

    private static final String[] CONTENT = new String[] { "Recent", "Artists", "Albums", "Songs", "Playlists", "Genres" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VerticalViewPager pager = (VerticalViewPager)findViewById(R.id.pager);
        CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.indicator);
        FragmentPagerAdapter adapter = new GoogleMusicAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        indicator.setViewPager(pager);

    }

    class GoogleMusicAdapter extends FragmentPagerAdapter {
        public GoogleMusicAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return LandingFirstFragment.newInstance(CONTENT[position % CONTENT.length]);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT[position % CONTENT.length].toUpperCase();
        }

        @Override
        public int getCount() {
            return CONTENT.length;
        }
    }

    /* PagerAdapter class */
    public class SlidePagerAdapter extends FragmentPagerAdapter {
        public SlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
			/*
			 * IMPORTANT: This is the point. We create a RootFragment acting as
			 * a container for other fragments
			 */
            if (position == 0)
                return new TestFragment();
            else
                return new TestFragment();
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
