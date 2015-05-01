package materialtest.theartistandtheengineer.co.materialtest.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import  materialtest.theartistandtheengineer.co.materialtest.app.AppConfig;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import materialtest.theartistandtheengineer.co.materialtest.R;
import materialtest.theartistandtheengineer.co.materialtest.adapters.MenuAdapter;
import materialtest.theartistandtheengineer.co.materialtest.app.AppConfig;
import materialtest.theartistandtheengineer.co.materialtest.app.AppController;
import materialtest.theartistandtheengineer.co.materialtest.helper.SQLiteHandler;
import materialtest.theartistandtheengineer.co.materialtest.helper.SessionManager;
import materialtest.theartistandtheengineer.co.materialtest.logging.L;
import materialtest.theartistandtheengineer.co.materialtest.materialtest.ActivityUsingTabLibrary;
import materialtest.theartistandtheengineer.co.materialtest.pojo.MainMenuInformation;


/**
 * A simple {@link Fragment} subclass.
 */


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment {

    private RecyclerView recyclerView;
    public static final String PREF_FILE_NAME = "testpref";
    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private HashMap<String, String> mUserInfo;
    private MenuAdapter adapter;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private SQLiteHandler db;
    private static final String TAG = SQLiteHandler.class.getSimpleName();
    private String name;
    private String email;
    private SessionManager session;


    private View containerView;

    public NavigationDrawerFragment()
    {
        // Required empty public constructor
    }

    /*
        The method in which Icons and Titles are added into the the Main Menu screen.
    */
    public List<MainMenuInformation> getData()
    {
        List<MainMenuInformation> data = new ArrayList<>();

        int[] icons = {R.drawable.ic_home_grey600_24dp, R.drawable.ic_my_library_add_grey600_24dp, R.drawable.ic_chat_grey600_24dp,  R.drawable.ic_shopping_cart_grey600_24dp, R.drawable.ic_settings_applications_grey600_24dp};

        String[] titles = getResources().getStringArray(R.array.drawer_tabs);

        for(int i = 0; i < titles.length; i++)
        {
            MainMenuInformation current = new MainMenuInformation();

            current.iconId = icons[i];

            current.title = titles[i];

            data.add(current);
        }

        return data;
    }

    public String userName()
    {
        return name;
    }

    public String Email()
    {
        return email;
    }

    public static void saveToPreferences(Context context, String preferenceName, String preferenceValues)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValues);

        editor.apply();
    }

    public static String readFromPreferences(Context context, String preferenceName, String preferenceValues)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(preferenceName, preferenceValues);
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        session = new SessionManager(getActivity());

        db = new SQLiteHandler(getActivity());

        HashMap<String, String> userData = db.getUserDetails();

        name = userData.get("name");

        email = userData.get("email");

        super.onCreate(savedInstanceState);

        mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, "false"));


        if(savedInstanceState != null)
        {
            mFromSavedInstanceState = true;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);

        adapter = new MenuAdapter(getActivity(), getData(), Email(), userName());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {

            @Override
            public void onClick(View view, int position)
            {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                ((ActivityUsingTabLibrary) getActivity()).onDrawerItemClicked(position-1);
            }

            @Override
            public void onLongClick(View view, int position)
            {

            }

        }));

        return layout;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {


        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        //Toast.makeText(getActivity(), userData.toString(), Toast.LENGTH_SHORT).show();
        //TextView name = (TextView) getView().findViewById(R.id.user_name);
        //String username = userData.get("name");
        //Toast.makeText(getActivity(), username.toString(), Toast.LENGTH_SHORT).show();
        //name.setText(username.toString());
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                if(!mUserLearnedDrawer)
                {
                    mUserLearnedDrawer = true;
                    saveToPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, mUserLearnedDrawer+"");
                }

                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

                toolbar.setAlpha(1 - slideOffset / 2);

            }
        };

        if(!mUserLearnedDrawer && !mFromSavedInstanceState)
        {
            mDrawerLayout.openDrawer(containerView);
        }

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    public static interface ClickListener
    {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener
    {
        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener)
        {
            Log.d("MENU", "constructor invoked ");
            this.clickListener = clickListener;

            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener()
            {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {

                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());

                    if(child != null && clickListener != null)
                    {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());

            if(child != null && clickListener != null && gestureDetector.onTouchEvent(e))
            {
                clickListener.onClick(child, rv.getChildPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }
    }
}