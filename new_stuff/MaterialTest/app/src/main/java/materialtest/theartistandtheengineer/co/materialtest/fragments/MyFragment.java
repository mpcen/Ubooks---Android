package materialtest.theartistandtheengineer.co.materialtest.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import materialtest.theartistandtheengineer.co.materialtest.R;

public class MyFragment extends Fragment {
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

        /*RequestQueue requestQueue = VolleySingleton.getsInstance().getmRequestQueue();
        StringRequest request = new StringRequest(Request.Method.GET, "http://php.net", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(), "RESPONSE "+response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "ERROR "+error, Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(request);*/
        return layout;
    }
}