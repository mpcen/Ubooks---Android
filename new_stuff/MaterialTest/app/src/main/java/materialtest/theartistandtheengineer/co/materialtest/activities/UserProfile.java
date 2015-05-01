package materialtest.theartistandtheengineer.co.materialtest.activities;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import materialtest.theartistandtheengineer.co.materialtest.R;
import materialtest.theartistandtheengineer.co.materialtest.adapters.UserBookForSaleAdapter;
import materialtest.theartistandtheengineer.co.materialtest.app.AppController;
import materialtest.theartistandtheengineer.co.materialtest.fragments.NavigationDrawerFragment;
import materialtest.theartistandtheengineer.co.materialtest.helper.SQLiteHandler;
import materialtest.theartistandtheengineer.co.materialtest.helper.SessionManager;
import materialtest.theartistandtheengineer.co.materialtest.pojo.Book;


public class UserProfile extends ActionBarActivity {

    private Toolbar toolbar;
    private SessionManager session;
    private SQLiteHandler db;
    private TextView txtName;
    private TextView txtEmail;
    private RatingBar ratingBar;
    private ArrayList<Book> listBooks = new ArrayList<>();
    private UserBookForSaleAdapter bookforsaleadapter;
    private RecyclerView listUserBooks;



    public static final String URL_UBOOKS = "http://theartistandtheengineer.co/ubooks/";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtName = (TextView) findViewById(R.id.user_name);
        txtEmail = (TextView) findViewById(R.id.email_name);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        // session manager
        session = new SessionManager(getApplicationContext());

        db = new SQLiteHandler(getApplicationContext());

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");
        String uid = user.get("uid");
        float rating = Float.parseFloat(user.get("reputation_avg"));

        txtName.setText(name);
        txtEmail.setText(email);
        ratingBar.setRating(rating);

        //CHANGE TO HISTORY OF SALES
        listUserBooks = (RecyclerView) findViewById(R.id.items_forsale);
        listUserBooks.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        bookforsaleadapter = new UserBookForSaleAdapter(getApplicationContext());
        listUserBooks.setAdapter(bookforsaleadapter);

        sendJsonRequest(uid);

    }

    private void sendJsonRequest(final String unique_id) {
        //json array req
        String tag_string_buysearch = "req_displaysale";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_UBOOKS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("User", "User Sell Response: " + response);

                try {

                    JSONObject info = new JSONObject(response);
                    //Log.d("DEBUG", "INSIDE TRY");
                    boolean error_check = info.getBoolean("error");

                    if (!error_check) {
                        listBooks = parseJSONResponse(info);
                        //Toast.makeText(getApplicationContext(), "ERRORCHECK NULL TOAST " + response.toString(), Toast.LENGTH_LONG).show();
                        bookforsaleadapter.setBookList(listBooks);

                    }
                    else {
                        String errorMsg = "No books found.";
                        //Toast.makeText(getApplicationContext(),
                                //"ERROR MSG TOAST "+errorMsg, Toast.LENGTH_LONG).show();
                        Log.d("ERROR MESSAGE!!!", errorMsg);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e(TAG, "Registration Error: " + error.getMessage());

            }
        }) {

            //JOSHJOSHJOSHJOSHJOSHJOSHJOSHJOSH
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "displaysale");
                params.put("unique_id", unique_id);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_buysearch);
    }

    //JOSHJOSHJOSHJOSHJOSHJOSHJOSHJOSH
    private ArrayList<Book> parseJSONResponse(JSONObject response) {
        ArrayList<Book> listBooks = new ArrayList<>();

        try {
            //StringBuilder data = new StringBuilder();
            // If there are results
            if (response.length() > 0) {

                JSONArray data = response.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {

                    JSONObject current = data.getJSONObject(i);

                    String author = current.getString("author");
                    String title = current.getString("title");
                    String image_url = current.getString("image_url");
                    String bcondition = current.getString("bcondition");
                    String price = current.getString("price");
                    String isbn = current.getString("isbn");
                    String transaction_status = current.getString("transaction_status");
                    String seller_id = current.getString("seller_id");
                    String tid = current.getString("tid");
                    int tid_int = Integer.parseInt(tid);

                    Book book = new Book();
                    book.setAuthors(author);
                    book.setImageLinks(image_url);
                    book.setTitle(title);
                    book.setBcondition(bcondition);
                    book.setISBN_13(isbn);
                    book.setPrice(price);
                    book.setTransactionStatus(transaction_status);
                    book.seturlThumbnail(image_url);
                    book.setSellerId(seller_id);
                    //book.setTid(tid);

                    listBooks.add(book);
                    //date stuff at end of video 37
                    //data.append(id + "\n" + volumeTitle + "\n" + author + "\n" + identifier + "\n");
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listBooks;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
