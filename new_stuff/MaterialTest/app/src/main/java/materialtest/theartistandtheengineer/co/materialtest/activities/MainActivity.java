package materialtest.theartistandtheengineer.co.materialtest.activities;

import android.content.Intent;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import materialtest.theartistandtheengineer.co.materialtest.fragments.MyFragment;
import materialtest.theartistandtheengineer.co.materialtest.fragments.NavigationDrawerFragment;
import materialtest.theartistandtheengineer.co.materialtest.R;
import materialtest.theartistandtheengineer.co.materialtest.materialtest.ActivityUsingTabLibrary;
import materialtest.theartistandtheengineer.co.materialtest.views.SlidingTabLayout;

//BEFORE
//CHANGES
//HERE
public class MainActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private ViewPager mPager;
    private SlidingTabLayout mTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().
                findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mTabs = (SlidingTabLayout) findViewById(R.id.tabs);
        mTabs.setDistributeEvenly(true);
        mTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {

            @Override
            public int getIndicatorColor(int position) {

                return getResources().getColor(R.color.colorAccent);
            }
        });
        mTabs.setCustomTabView(R.layout.custom_tab_view, R.id.tabText);
        mTabs.setViewPager(mPager);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
            return true;
        }

        // ActivityUsingTabLibrary
        if (id == R.id.tabLayout) {
            //Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ActivityUsingTabLibrary.class));
            //return true;
        }

        if (id == R.id.vectorTest) {
            //Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, VectorTestActivity.class));
            //return true;
        }


        if (id == R.id.navigate) {
            startActivity(new Intent(this, SubActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        int icons[] = {R.drawable.ic_book221, R.drawable.ic_announcement_black_48dp};
        // third tab R.drawable.ic_timer_auto_black_48dp
        String[] tabText = getResources().getStringArray(R.array.tabs);

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            tabText = getResources().getStringArray(R.array.tabs);
        }

        @Override
        public Fragment getItem(int position) {
            MyFragment myFragment = MyFragment.getInstance(position);
            return myFragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            Drawable drawable = getResources().getDrawable(icons[position]);
            drawable.setBounds(0, 0, 70, 70);
            ImageSpan imageSpan = new ImageSpan(drawable);
            SpannableString spannableString = new SpannableString(" ");
            spannableString.setSpan(imageSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableString;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int num) {
            return MyFragment.getInstance(num);
        }

        @Override
        public int getCount() {
            return 2;
        }

        public CharSequence getPageTitle(int position) {
            return getResources().getStringArray(R.array.tabs)[position];
        }
    }
/*
    public static class MyFragment extends Fragment {
        private TextView textView;

        public static MyFragment getInstance(int position) {
            MyFragment myFragment = new MyFragment();
            Bundle args = new Bundle();
            args.putInt("position", position);
            myFragment.setArguments(args);
            return myFragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View layout = inflater.inflate(R.layout.fragment_my, container, false);

            textView = (TextView) layout.findViewById(R.id.position);
            Bundle bundle = getArguments();

            if (bundle != null) {
                textView.setText("The page selected is " + bundle.getInt("position"));
            }

            return layout;
        }
    }*/
}
