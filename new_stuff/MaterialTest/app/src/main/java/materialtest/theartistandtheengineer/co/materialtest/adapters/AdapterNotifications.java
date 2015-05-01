package materialtest.theartistandtheengineer.co.materialtest.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import materialtest.theartistandtheengineer.co.materialtest.MessagingActivity;
import materialtest.theartistandtheengineer.co.materialtest.R;
import materialtest.theartistandtheengineer.co.materialtest.activities.SingleBuyBookActivity;
import materialtest.theartistandtheengineer.co.materialtest.helper.SQLiteHandler;
import materialtest.theartistandtheengineer.co.materialtest.helper.SessionManager;
import materialtest.theartistandtheengineer.co.materialtest.network.VolleySingleton;
import materialtest.theartistandtheengineer.co.materialtest.pojo.Book;

/**
 * Created by mpcen-desktop on 3/27/15.
 */
public class AdapterNotifications extends RecyclerView.Adapter<AdapterNotifications.ViewHolderBookSearch> {

    private ArrayList<Book> listBooks = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;
    static private SessionManager session;
    static private SQLiteHandler db;

    public AdapterNotifications(Context context) {
        layoutInflater = LayoutInflater.from(context);
        volleySingleton = VolleySingleton.getInstance();
        imageLoader = volleySingleton.getImageLoader();
    }

    public void setBookList(ArrayList<Book> listBooks){
        this.listBooks = listBooks;
        notifyItemRangeChanged(0, listBooks.size());
    }

    @Override
    public ViewHolderBookSearch onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_book_notifications, parent, false);
        ViewHolderBookSearch viewHolder = new ViewHolderBookSearch(view);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolderBookSearch holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Book currentBook = listBooks.get(position);
        String urlThumbnail = currentBook.geturlThumbnail();

        holder.url = urlThumbnail;
        holder.bookTitle.setText(currentBook.getTitle());
        holder.bookAuthor.setText(currentBook.getAuthors());
        holder.isbn_13.setText(currentBook.getISBN_13());
        holder.price.setText(currentBook.getPrice());
        holder.bcondition.setText(currentBook.getBcondition());
        holder.seller_id.setText(currentBook.getSellerId());
        holder.reputation_avg.setText(currentBook.getReputationAvg());
        holder.unique_id.setText(currentBook.getUniqueId());



        if(urlThumbnail != null){
            imageLoader.get(urlThumbnail, new ImageLoader.ImageListener(){
                // if cant load image
                @Override
                public void onErrorResponse(VolleyError error) {

                }

                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmetagdiate) {
                    holder.bookThumbnail.setImageBitmap(response.getBitmap());
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return listBooks.size();
    }

    //use implements View.OnCreateContextMenuListener for Context Menu
    static class ViewHolderBookSearch extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView bookThumbnail;
        private TextView bookTitle;
        private TextView bookAuthor;
        private TextView isbn_13;
        private String url;
        private TextView bcondition;
        private TextView transaction_status;
        private TextView reputation_avg;
        private TextView seller_id;
        private TextView price;
        private TextView unique_id;

        public ViewHolderBookSearch(View itemView) {
            super(itemView);
            bookThumbnail = (ImageView) itemView.findViewById(R.id.bookThumbnail);
            bookTitle = (TextView) itemView.findViewById(R.id.bookTitle);
            bookAuthor = (TextView) itemView.findViewById(R.id.bookAuthor);
            isbn_13 = (TextView) itemView.findViewById(R.id.isbn_13);
            price = (TextView) itemView.findViewById(R.id.price);
            bcondition = (TextView) itemView.findViewById(R.id.bcondition);
            seller_id = (TextView) itemView.findViewById(R.id.seller_id);
            reputation_avg = (TextView) itemView.findViewById(R.id.reputation_avg);
            unique_id = (TextView) itemView.findViewById(R.id.unique_id);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String[] bookDataArray = {
                    (String)bookTitle.getText(),
                    (String)bookAuthor.getText(),
                    (String)isbn_13.getText(),
                    (String)price.getText(),
                    (String)bcondition.getText(),
                    (String)seller_id.getText(),
                    (String)reputation_avg.getText(),
                    //(String)reputation_avg.getText(),
                    //seller_rating.getText();
                    url,
                    (String)unique_id.getText()

            };

            //Toast.makeText(v.getContext(), bookDataArray[0]+"\n"+bookDataArray[1]+"\n"+bookDataArray[2]+"\n"+bookDataArray[3], Toast.LENGTH_SHORT).show();

            final Context context = itemView.getContext();
            Intent intent = new Intent(context, MessagingActivity.class);
            intent.putExtra("bookTitle", bookDataArray[0]);
            intent.putExtra("bookAuthor", bookDataArray[1]);
            intent.putExtra("isbn_13", bookDataArray[2]);
            intent.putExtra("price", bookDataArray[3]);
            intent.putExtra("bcondition", bookDataArray[4]);
            intent.putExtra("seller_id", bookDataArray[5]);
            intent.putExtra("reputation_avg", bookDataArray[6]);
            intent.putExtra("url", bookDataArray[7]);
            intent.putExtra("unique_id", bookDataArray[8]);

            session = new SessionManager(context);
            db = new SQLiteHandler(context);

            HashMap<String, String> user =  db.getUserDetails();
            String buyer_email = user.get("email");


            //Open messaging activity with bogus ID's.
            Log.d("current_user_check", ParseUser.getCurrentUser().getUsername());
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            /**
             * Replace m_mcgee@knights.ucf.edu with the seller username (ask manny where this is stored and
             * how to access.
             */
            query.whereEqualTo("username", buyer_email);
            Log.d("SingleBuyBookActivity", "Seller Email: "+buyer_email);
            query.findInBackground(new FindCallback<ParseUser>() {
                public void done(List<ParseUser> user, com.parse.ParseException e) {
                    if (e == null) {
                        Log.d("Query_finder_test", "User list size" + user.size() + " Username: " + user.get(0)
                                .getUsername() + " User Id: " + user.get(0).getObjectId());
                        String recipientId = user.get(0).getObjectId();
                        Intent intent = new Intent(context, MessagingActivity.class);
                        //unique_id is the seller Id.
                        intent.putExtra("RECIPIENT_ID", recipientId);
                        intent.putExtra("RECIPIENT_USER_NAME", user.get(0).getUsername());
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context,
                                "Error finding that user",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

            //intent.putExtra("bcondition", bookDataArray[4]);
            //context.startActivity(intent);

        }




        /*
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            menu.add(0, v.getId(), 0, "Fast Sell");
            menu.add(0, v.getId(), 0, "More Info");
        }*/


    }
}