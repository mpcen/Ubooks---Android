package materialtest.theartistandtheengineer.co.materialtest.materialtest;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.HashMap;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import materialtest.theartistandtheengineer.co.materialtest.LoginActivity;
import materialtest.theartistandtheengineer.co.materialtest.activities.UserProfile;
import materialtest.theartistandtheengineer.co.materialtest.fragments.FragmentBooks;
import materialtest.theartistandtheengineer.co.materialtest.fragments.FragmentNotifications;
import materialtest.theartistandtheengineer.co.materialtest.fragments.NavigationDrawerFragment;
import materialtest.theartistandtheengineer.co.materialtest.R;
import materialtest.theartistandtheengineer.co.materialtest.helper.MessageService;
import materialtest.theartistandtheengineer.co.materialtest.helper.SQLiteHandler;
import materialtest.theartistandtheengineer.co.materialtest.helper.SessionManager;


/**
 * Created by mannymartinez on 3/24/15.
 */
public class ActivityUsingTabLibrary extends ActionBarActivity implements MaterialTabListener {

    private Toolbar toolbar;
    private MaterialTabHost tabHost;
    private ViewPager viewPager;

    private static final int BOOKS_SEARCH = 0;
    private static final int BOOKS_NOTIFICATIONS = 1;
    private static final int BOOKS_PROFILE = 2;

    //Mikes Stuff
    private TextView txtName;
    private TextView txtEmail;
    private Button btnLogout;
    private SQLiteHandler db;
    private SessionManager session;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_using_tab_library);

        // Mikes R's Stuff
        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());
        // session manager
        session = new SessionManager(getApplicationContext());
        if (!session.isLoggedIn()) {
            logoutUser();
        }
        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();
        String name = user.get("name");
        String email = user.get("email");

        // Displaying the user details on the screen
        //txtName.setText(name);
        //txtEmail.setText(email);
        // Logout button click event


        /*btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });*/
        // End of Mike R's Stuff

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().
                findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        tabHost = (MaterialTabHost) findViewById(R.id.materialTabHost);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                tabHost.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < adapter.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            // Used for text-based tabs: .setText(adapter.getPageTitle(i))
                            //setIcon is used for icon-based tabs
                            .setIcon(adapter.getIcon(i))
                            .setTabListener(this));
        }

    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        //messaging Logout pare user here.
        Log.d(ActivityUsingTabLibrary.class.toString(), "Logging out of messaging");
        stopService(new Intent(getApplicationContext(), MessageService.class));
        ParseUser.logOut();

        // Launching the login activity
        Intent intent = new Intent(ActivityUsingTabLibrary.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_log_out) {
            logoutUser();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_using_tab_library, menu);
        return true;
    }

    @Override
    public void onTabSelected(MaterialTab materialTab) {
        viewPager.setCurrentItem(materialTab.getPosition());

    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {

    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {

    }


    public void onDrawerItemClicked(int index)
    {

        switch(index) {
            case 0:
                viewPager.setCurrentItem(0);
                break;
            case 1:
                startActivity(new Intent(this, UserProfile.class));
                break;
            case 2:
                viewPager.setCurrentItem(1);
                break;
            case 3:
                Log.d(this.getClass().toString(), "Logging out user");
                logoutUser();
                break;

        }
    }

    //ViewPagerAdapter for text-based tabs
    /*
    private class ViewPagerAdapter extends FragmentStatePagerAdapter{

        public ViewPagerAdapter(FragmentManager fm){
            super(fm);
        }

        public Fragment getItem(int num){
            return MyFragment.getInstance(num);
        }

        @Override
        public int getCount() {
            return 3;
        }

        public CharSequence getPageTitle(int position){
            return getResources().getStringArray(R.array.tabs)[position];
        }
    }*/

    //ViewPagerAdapter for icon-based tabs
    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        int icons[] = {R.drawable.ic_book215, R.drawable.ic_announcement_black_48dp};
        //third icon = R.drawable.ic_timer_auto_black_48dp


        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int position) {
            Fragment fragment = null;

            switch (position){
                case BOOKS_SEARCH:
                    //fragment = FragmentSearch.newInstance("", "");
                    fragment = FragmentBooks.newInstance("", "");
                    break;
                case BOOKS_NOTIFICATIONS:
                    fragment = FragmentNotifications.newInstance("", "");
                    break;
                /*
                case BOOKS_PROFILE:
                    fragment = FragmentProfile.newInstance("", "");
                    break;*/
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getStringArray(R.array.tabs)[position];
        }

        private Drawable getIcon(int position) {
            return getResources().getDrawable(icons[position]);
        }
    }
}
