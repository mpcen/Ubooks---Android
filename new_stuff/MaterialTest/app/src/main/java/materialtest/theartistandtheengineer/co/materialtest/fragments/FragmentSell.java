package materialtest.theartistandtheengineer.co.materialtest.fragments;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import materialtest.theartistandtheengineer.co.materialtest.R;
import materialtest.theartistandtheengineer.co.materialtest.activities.BuyActivity;
import materialtest.theartistandtheengineer.co.materialtest.activities.SellActivity;
import materialtest.theartistandtheengineer.co.materialtest.adapters.AdapterSearch;
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
public class FragmentSell extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String URL_BOOK = "https://www.googleapis.com/books/v1/volumes";
    public static final String URL_BOOK_SEARCH = "q=";
    private static String URL_BOOK_CONTENTS = "";
    public static final String URL_BOOK_START_INDEX = "startIndex=";
    public static final String URL_BOOK_MAX_RESULTS = "maxResults=";
    public static final String URL_BOOK_PARAM_API_KEY = "key=";
    public static final String URL_CHAR_QUESTION = "?";
    public static final String URL_CHAR_AMPERSAND = "&";
    private static final String STATE_BOOKS = "state_books";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private ImageLoader imageLoader;
    private RequestQueue requestQueue;
    private VolleySingleton volleySingleton;
    private ArrayList<Book> listBooks = new ArrayList<>();
    private RecyclerView listSearchedBooks;
    private AdapterSearch adapterSearch;
    private EditText search_book;
    private Button button_search;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSell.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSell newInstance(String param1, String param2) {
        FragmentSell fragment = new FragmentSell();
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

    public FragmentSell() {
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        search_book = (EditText) view.findViewById(R.id.search_book);
        button_search = (Button) view.findViewById(R.id.button_search);
        button_search.setOnClickListener(this);

        listSearchedBooks = (RecyclerView) view.findViewById(R.id.listSearchedBooks);
        listSearchedBooks.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterSearch = new AdapterSearch(getActivity());
        listSearchedBooks.setAdapter(adapterSearch);

        if(savedInstanceState != null){
            listBooks = savedInstanceState.getParcelableArrayList(STATE_BOOKS);
            adapterSearch.setBookList(listBooks);
        }else if(listBooks != null) {

        }
        else {
            //sendJsonRequest();
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        //Log.d("BUTTON!!! ", String.valueOf(v));
        Log.d("BOOK INFO!!! ", search_book.getText().toString());
        URL_BOOK_CONTENTS = search_book.getText().toString();
        sendJsonRequest();
    }

    private void sendJsonRequest() {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                getRequestUrl(0, 20),
                (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listBooks = parseJSONResponse(response);
                        adapterSearch.setBookList(listBooks);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(request);
    }

    private ArrayList<Book> parseJSONResponse(JSONObject response) {
        ArrayList<Book> listBooks = new ArrayList<>();

        try {
            StringBuilder data = new StringBuilder();
            // If there are results
            if (response.has("items")) {
                // store all of the results in an JSON array
                JSONArray arrayBooks = response.getJSONArray("items");
                // loop through each of the results(array)
                for (int i = 0; i < arrayBooks.length(); i++) {

                    JSONObject currentBook = arrayBooks.getJSONObject(i);
                    String id = currentBook.getString("id");
                    // make the volumeInfo JSON Object
                    JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");

                    // title
                    String volumeTitle = volumeInfo.getString("title");
                    // author
                    JSONArray volumeAuthor = volumeInfo.getJSONArray("authors");
                    String author = volumeAuthor.getString(0);
                    // isbn's
                    JSONArray volumeIndustryIdentifier = volumeInfo.getJSONArray("industryIdentifiers");
                    JSONObject isbn_type1 = volumeIndustryIdentifier.getJSONObject(0);
                    JSONObject isbn_type2 = volumeIndustryIdentifier.getJSONObject(1);

                    String isbn1 = isbn_type1.getString("identifier");
                    String isbn2 = isbn_type2.getString("identifier");
                    String isbn = null;

                    if (isbn1.length() > 10) {
                        isbn = isbn1;
                    } else {
                        isbn = isbn2;
                    }

                    JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                    String urlThumbnail = imageLinks.getString("thumbnail");
                    Book book = new Book();
                    book.setTitle(volumeTitle);
                    book.setAuthors(author);
                    book.setISBN_13(isbn);
                    book.seturlThumbnail(urlThumbnail);

                    listBooks.add(book);
                    //date stuff at end of video 37
                    //data.append(id + "\n" + volumeTitle + "\n" + author + "\n" + identifier + "\n");
                }
                //L.T(getActivity(), listBooks.toString());
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



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public static interface ClickListener{
        public void onClick(View view, int position);
        public void onLongClick(View view, int position);
    }

}
