package com.project.rishabhsingh.dWarden.DonorSearchingSystem;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.rishabhsingh.dWarden.AppDataPreferences;
import com.project.rishabhsingh.dWarden.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DonorListFragment extends Fragment {

    private RecyclerView recyclerView;
    private static TextView textNoDonors;
    private static String URL = AppDataPreferences.URL + "bloodsearch";
    private static ProgressDialog progressDialog;
    public static DonorViewAdapter adapter;
    public static List<DonorData> donorList;
    private static RequestQueue requestQueue;
    private static Context context;

    public DonorListFragment() {

    }

    @SuppressLint("ValidFragment")
    public DonorListFragment(Context context) {
        DonorListFragment.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_donor_list, container, false);

        textNoDonors = (TextView) v.findViewById(R.id.noDonorsTextView);
        recyclerView = (RecyclerView) v.findViewById(R.id.donorListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        donorList = new ArrayList<>();
        textNoDonors.setText("Please select a blood group to search donors...");
        adapter = new DonorViewAdapter(getActivity(), donorList);
        recyclerView.setAdapter(adapter);
        return v;
    }

    public static void loadDonors(final String bloodGroup) {

        progressDialog = new ProgressDialog(context,R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Fetching donors...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        AsyncTask<Integer, Void, Void> task = new AsyncTask<Integer, Void, Void>() {

            @Override
            protected Void doInBackground(Integer... params) {

                requestQueue = Volley.newRequestQueue(context);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response.length() != 2) {
                                textNoDonors.setVisibility(View.GONE);
                                JSONArray donorsArray = new JSONArray(response);
                                for (int i = 0; i < donorsArray.length(); i++) {
                                    JSONObject donorObject = donorsArray.getJSONObject(i);
                                    DonorData donorData = new DonorData();
                                    donorData.setDonorName(donorObject.getString("studentname"));
                                    donorData.setDonorBranch(donorObject.getString("studentbranch"));
                                    String year = donorObject.getString("studentyear");
                                    if(year.equals("1")) {
                                        year="1st Year";
                                    }
                                    if(year.equals("2")) {
                                        year="2nd Year";
                                    }
                                    if(year.equals("3rd Year")) {
                                        year="3rd";
                                    }
                                    if(year.equals("Final")) {
                                        year="Final Year";
                                    }
                                    donorData.setDonorYear(year);
                                    donorData.setDonorEmail(donorObject.getString("studentemailid"));
                                    donorData.setDonorContact(BigDecimal.valueOf(donorObject.getDouble("studentmobileno")).toString());
                                    donorList.add(donorData);
                                }
                            } else {
                                textNoDonors.setText("No donors available...");
                                textNoDonors.setVisibility(View.VISIBLE);
                            }
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("studentbloodgp", bloodGroup);
                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type", "application/x-www-form-urlencoded");
                        params.put("authorization", "token ce3fe9a203703c7ea3da8727ff8fbafec8ddbf44");
                        return params;
                    }
                };
                requestQueue.add(stringRequest);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        };
        task.execute();
    }
}
