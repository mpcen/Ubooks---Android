package materialtest.theartistandtheengineer.co.materialtest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import materialtest.theartistandtheengineer.co.materialtest.R;
import materialtest.theartistandtheengineer.co.materialtest.materialtest.ActivityUsingTabLibrary;

public class RatingsActivity extends ActionBarActivity {

    private Button submitRatingBtn;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Intent intent = getIntent();
        getSupportActionBar().setTitle(intent.getStringExtra("RECIPIENT_USER_NAME"));

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        //TODO post to user database

        submitRatingBtn = (Button) findViewById(R.id.button_rating_submit);
        submitRatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rating = ratingBar.getNumStars();
                Intent backToHom = new Intent(getApplicationContext(), ActivityUsingTabLibrary.class);
                startActivity(backToHom);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ratings, menu);
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
