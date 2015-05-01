package materialtest.theartistandtheengineer.co.materialtest.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

import materialtest.theartistandtheengineer.co.materialtest.R;
import materialtest.theartistandtheengineer.co.materialtest.activities.SingleBookActivity;
import materialtest.theartistandtheengineer.co.materialtest.network.VolleySingleton;
import materialtest.theartistandtheengineer.co.materialtest.pojo.Book;

/**
 * Created by mpcen-desktop on 3/27/15.
 */
public class AdapterSearch extends RecyclerView.Adapter<AdapterSearch.ViewHolderBookSearch> {

    private ArrayList<Book> listBooks = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;

    public AdapterSearch(Context context) {
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
        View view = layoutInflater.inflate(R.layout.custom_book, parent, false);
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

        if(urlThumbnail != null){
            imageLoader.get(urlThumbnail, new ImageLoader.ImageListener(){
                // if cant load image
                @Override
                public void onErrorResponse(VolleyError error) {

                }

                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
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

        public ViewHolderBookSearch(View itemView) {
            super(itemView);
            bookThumbnail = (ImageView) itemView.findViewById(R.id.bookThumbnail);
            bookTitle = (TextView) itemView.findViewById(R.id.bookTitle);
            bookAuthor = (TextView) itemView.findViewById(R.id.bookAuthor);
            isbn_13 = (TextView) itemView.findViewById(R.id.isbn_13);
            itemView.setOnClickListener(this);
            //itemView.setOnCreateContextMenuListener(this);



        }

        @Override
        public void onClick(View v) {
            String[] bookDataArray = {
                    (String)bookTitle.getText(),
                    (String)bookAuthor.getText(),
                    (String)isbn_13.getText(),
                    url
            };

            //Toast.makeText(v.getContext(), bookDataArray[0]+"\n"+bookDataArray[1]+"\n"+bookDataArray[2]+"\n"+bookDataArray[3], Toast.LENGTH_SHORT).show();

            Context context = itemView.getContext();
            Intent intent = new Intent(context, SingleBookActivity.class);
            intent.putExtra("bookTitle", bookDataArray[0]);
            intent.putExtra("bookAuthor", bookDataArray[1]);
            intent.putExtra("isbn_13", bookDataArray[2]);
            intent.putExtra("url", bookDataArray[3]);
            context.startActivity(intent);

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