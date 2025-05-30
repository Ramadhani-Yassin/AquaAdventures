package com.jokhakhamisjuma.AquaAdventures.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jokhakhamisjuma.AquaAdventures.adapter.CategoryAdapter;
import com.jokhakhamisjuma.AquaAdventures.adapter.SubCategoryAdapter;
import com.jokhakhamisjuma.AquaAdventures.interfaces.CategorySelectCallBacks;
import com.jokhakhamisjuma.AquaAdventures.model.Category;
import com.jokhakhamisjuma.AquaAdventures.model.SubCategory;
import com.google.gson.Gson;
import com.jokhakhamisjuma.AquaAdventures.R;
import com.jokhakhamisjuma.AquaAdventures.api.clients.RestClient;
import com.jokhakhamisjuma.AquaAdventures.helper.Data;
import com.jokhakhamisjuma.AquaAdventures.model.CategoryResult;
import com.jokhakhamisjuma.AquaAdventures.model.Token;
import com.jokhakhamisjuma.AquaAdventures.model.User;
import com.jokhakhamisjuma.AquaAdventures.util.localstorage.LocalStorage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryFragment extends Fragment implements CategorySelectCallBacks {
    Data data;
    View progress;
    LocalStorage localStorage;
    Gson gson = new Gson();
    User user;
    Token token;
    private List<Category> homeCategoryList = new ArrayList<>();
    private List<SubCategory> subCategoryList = new ArrayList<>();
    private RecyclerView recyclerView,subCateRecyclerview;
    private CategoryAdapter mAdapter;
    private SubCategoryAdapter sAdapter;

    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        recyclerView = view.findViewById(R.id.category_rv);
        subCateRecyclerview = view.findViewById(R.id.sub_category_rv);
        progress = view.findViewById(R.id.progress_bar);

        localStorage = new LocalStorage(getContext());
        user = gson.fromJson(localStorage.getUserLogin(), User.class);
        token = new Token(user.getToken());

        getCategoryData();


        return view;
    }

    private void getCategoryData() {

        showProgressDialog();

        Call<CategoryResult> call = RestClient.getRestService(getContext()).allCategory(token);
        call.enqueue(new Callback<CategoryResult>() {
            @Override
            public void onResponse(Call<CategoryResult> call, Response<CategoryResult> response) {
                Log.d("Response :=>", response.body() + "");
                if (response != null) {

                    CategoryResult categoryResult = response.body();
                    if (categoryResult.getStatus() == 200) {

                        homeCategoryList = categoryResult.getCategoryList();
                        setupCategoryRecycleView();
                        if(homeCategoryList.size()>0){
                            subCategoryList = homeCategoryList.get(0).getSubCategory();
                            setupSubCategoryRecycleView();
                        }

                    }

                }

                hideProgressDialog();
            }

            @Override
            public void onFailure(Call<CategoryResult> call, Throwable t) {
                Log.d("Error==>", t.getMessage());
            }
        });

    }

    private void setupCategoryRecycleView() {
        mAdapter = new CategoryAdapter(homeCategoryList, getActivity(), this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    private void hideProgressDialog() {
        progress.setVisibility(View.GONE);
    }

    private void showProgressDialog() {
        progress.setVisibility(View.VISIBLE);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Tour Sites 🚤");
    }

    @Override
    public void onCategorySelect(int position) {
       subCategoryList = homeCategoryList.get(position).getSubCategory();
        setupSubCategoryRecycleView();
    }

    private void setupSubCategoryRecycleView() {
        sAdapter = new SubCategoryAdapter(subCategoryList, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        subCateRecyclerview.setLayoutManager(mLayoutManager);
        subCateRecyclerview.setItemAnimator(new DefaultItemAnimator());
        subCateRecyclerview.setAdapter(sAdapter);
    }
}
