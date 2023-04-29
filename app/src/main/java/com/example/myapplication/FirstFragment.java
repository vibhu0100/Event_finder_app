package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FirstFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FirstFragment newInstance(String param1, String param2) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }


    }
    public void searchEvent(View view){
        Gson gson = new Gson();

        TextView textView = view.findViewById(R.id.location_label);
        textView.setText("That didn't work in fragment!");
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        EditText keyword = view.findViewById(R.id.keyword);
        EditText distance = view.findViewById(R.id.distance);
        EditText location = view.findViewById(R.id.location);
        Switch auto_detect = view.findViewById(R.id.autoDetect);
//        TextView textView = findViewById(R.id.location_label);
        Log.d("ewws",Boolean.toString(auto_detect.isChecked()));
        if(auto_detect.isChecked()){
            StringRequest stringRequest = new StringRequest(Request.Method.GET,"https://ipinfo.io?token=27876ba56b3792",new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Display the first 500 characters of the response string.

                    JsonObject ip_info = gson.fromJson(response, JsonObject.class);
                    String search_url = "https://node-vibhuti123.wl.r.appspot.com/tasks?keyword="+keyword.getText().toString()+
                            "&distance=" + distance.getText().toString()+
                            "&location=" + location.getText().toString()+
                            "&auto-detect="+Boolean.toString(auto_detect.isChecked())+
                            "&loc="+ ip_info.get("loc").getAsString();
                    StringRequest searchRequest = new StringRequest(Request.Method.GET,search_url,new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            //JSONObject ip_info = new JSONObject(response);

                            Log.d("search",response);

                            //textView.setText("Response is: " + response);
                            JsonObject search_result = gson.fromJson(response, JsonObject.class);
                            Intent intent = new Intent(getContext(), MainActivity2.class);
                            intent.putExtra("data", response);
                            startActivity(intent);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            textView.setText("That didn't work!");
                        }
                    });
                    queue.add(searchRequest);


                    textView.setText("Response is: " + response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    textView.setText("That didn't work!");
                }
            });
            queue.add(stringRequest);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first, container, false);
//        Button search = view.findViewById(R.id.search);
//        search.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Log.d("BUTTONS", "User tapped the Supabutton");
//                //searchEvent(v);
//            }
//        });
        return view;

    }
}