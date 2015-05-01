package materialtest.theartistandtheengineer.co.materialtest.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.volley.Request.Method;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import materialtest.theartistandtheengineer.co.materialtest.R;
import materialtest.theartistandtheengineer.co.materialtest.activities.BuyActivity;
import materialtest.theartistandtheengineer.co.materialtest.activities.SellActivity;
import materialtest.theartistandtheengineer.co.materialtest.adapters.AdapterBuy;
import materialtest.theartistandtheengineer.co.materialtest.app.AppController;
import materialtest.theartistandtheengineer.co.materialtest.network.VolleySingleton;
import materialtest.theartistandtheengineer.co.materialtest.pojo.Book;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentSell.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentSell#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentBuy extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String URL_BOOK = "https://www.googleapis.com/books/v1/volumes";

    public static final String URL_UBOOKS = "http://theartistandtheengineer.co/ubooks/";

    public static final String URL_BOOK_SEARCH = "q=";
    private static String URL_BOOK_CONTENTS = "";
    public static final String URL_BOOK_START_INDEX = "startIndex=";
    public static final String URL_BOOK_MAX_RESULTS = "maxResults=";
    public static final String URL_BOOK_PARAM_API_KEY = "key=";
    public static final String URL_CHAR_QUESTION = "?";
    public static final String URL_CHAR_AMPERSAND = "&";
    private static final String STATE_BOOKS = "state_books";



    private Spinner spinner1;
    private Spinner spinner2;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageLoader imageLoader;
    private RequestQueue requestQueue;
    private VolleySingleton volleySingleton;
    private ProgressDialog pDialog;
    private ArrayList<Book> listBooks = new ArrayList<>();
    private RecyclerView listSearchedBooks;
    private AdapterBuy adapterBuy;
    private EditText search_book;
    private Button button_search;

    // TODO: Rename and change types and number of parameters
    public static FragmentBuy newInstance(String param1, String param2) {
        FragmentBuy fragment = new FragmentBuy();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_BOOKS, listBooks);
    }

    public static String getRequestUrl(int startIndex, int maxResults) {

        return URL_BOOK
                + URL_CHAR_QUESTION
                + URL_BOOK_SEARCH
                + URL_BOOK_CONTENTS
                + URL_CHAR_AMPERSAND
                + URL_BOOK_START_INDEX
                + startIndex
                + URL_CHAR_AMPERSAND
                + URL_BOOK_MAX_RESULTS
                + maxResults
                + URL_CHAR_AMPERSAND
                + URL_BOOK_PARAM_API_KEY
                + AppController.API_KEY_GOOGLE_BOOKS;
    }

    public static String getRequestUrl(String author) {
        return URL_UBOOKS + "?author="+author;
    }



    public FragmentBuy() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        //sendJsonRequest();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_buy, container, false);

        spinner1 = (Spinner) view.findViewById(R.id.spinner1);
        spinner2 = (Spinner) view.findViewById(R.id.spinner2);

        spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        spinner2.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        search_book = (EditText) view.findViewById(R.id.search_book);
        button_search = (Button) view.findViewById(R.id.button_search);
        button_search.setOnClickListener(this);

        listSearchedBooks = (RecyclerView) view.findViewById(R.id.listSearchedBooks);
        listSearchedBooks.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterBuy = new AdapterBuy(getActivity());
        listSearchedBooks.setAdapter(adapterBuy);

        if(savedInstanceState != null){
            listBooks = savedInstanceState.getParcelableArrayList(STATE_BOOKS);
            adapterBuy.setBookList(listBooks);
        }else if(listBooks != null) {

        }
        else {
            //sendJsonRequest();
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        //searchBook(search_book.getText().toString(), String.valueOf(spinner1.getSelectedItem()), String.valueOf(spinner2.getSelectedItem()));
        //String.valueOf(spinner1.getSelectedItem());
        //Log.d("BUTTON!!! ", String.valueOf(v));
        Log.d("BOOK INFO", search_book.getText().toString());
        Log.d("SEARCH TYPE", String.valueOf(spinner1.getSelectedItem()));
        Log.d("CONDITION TYPE", String.valueOf(spinner2.getSelectedItem()));
        URL_BOOK_CONTENTS = search_book.getText().toString();
        sendJsonRequest(search_book.getText().toString(), String.valueOf(spinner1.getSelectedItem()), String.valueOf(spinner2.getSelectedItem()));

    }

    // JOSHJOSHJOSHJOSHJOSHJOSHJOSH
    private void sendJsonRequest(final String search_string, final String search_type, final String book_condition) {
        //json array req
        String tag_string_buysearch = "req_buysearch";
        StringRequest strReq = new StringRequest(Method.POST,
                URL_UBOOKS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("BUY SEARCH", "BUY SEARCH Response: " + response);


                try {

                    JSONObject info = new JSONObject(response);
                    Log.d("DEBUG", "INSIDE TRY");
                    boolean error_check = info.getBoolean("error");

                    if (!error_check) {
                        listBooks = parseJSONResponse(info);


                        //Toast.makeText(getActivity(), "ERRORCHECK NULL TOAST "+response.toString(), Toast.LENGTH_LONG).show();
                        adapterBuy.setBookList(listBooks);

                    }
                    else {
                        String errorMsg = "No books found.";
                        //Toast.makeText(getActivity(),
                        //        "ERROR MSG TOAST "+errorMsg, Toast.LENGTH_LONG).show();
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
                //Toast.makeText(getActivity(),
                //        "ONERRORRESPONSE TOAST "+error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
            }
        }) {

            //JOSHJOSHJOSHJOSHJOSHJOSHJOSHJOSH
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "buysearch");
                params.put("search_type", search_type);
                params.put("string_query", search_string);
                params.put("bcondition", book_condition);
                //params.put("bcondition", bcondition);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_buysearch);
    }

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
                    String unique_id = current.getString("unique_id");


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
                    book.setUniqueId(unique_id);

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
    public void onDetach() {
        super.onDetach();

    }

    private class CustomOnItemSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
