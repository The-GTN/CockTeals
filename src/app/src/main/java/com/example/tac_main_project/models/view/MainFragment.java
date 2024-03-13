package com.example.tac_main_project.models.view;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tac_main_project.R;
import com.example.tac_main_project.adapters.MainAdapter;
import com.example.tac_main_project.models.objects.Item;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    /**
     * The fragment initialisation parameter for position (index of the tab)
     */
    private static final String ARG_POSITION = "param1";

    /**
     * The fragment initialisation parameter for the background color
     */
    private static final String ARG_COLOR = "param2";

    private int controlLast=0;

    /**
     * The first tab list data
     */
    private List<Item> list = new ArrayList<Item>();

    /**
     * The viewModel containing the mutable LiveData lists
     */
    private ListViewModel model;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param position Parameter 1.
     * @param color Parameter 2.
     * @return A new instance of fragment PageFragment.
     */
    public static MainFragment newInstance(int position, int color) {

        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        args.putInt(ARG_COLOR, color);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Creation of the fragment view
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_page, container, false);

        LinearLayout rootView = (LinearLayout) view.findViewById(R.id.fragment_view_root);

        /**
        * Color of the fragment background defined in the constructor
        */
        int color = getArguments().getInt(ARG_COLOR, -1);

        /**
        * Index of the fragment defined in the constructor
        */
        int position = getArguments().getInt(ARG_POSITION, -1);

        /**
        * Set the background color of the fragment view
        */
        rootView.setBackgroundColor(color);

        /**
        * Retrieve the viewModel from the main activity
        */
        this.model = new ViewModelProvider(getActivity()).get(ListViewModel.class);

        /**
        * The two display mode Grid and List
        */
        final ListView listView = (ListView) view.findViewById(R.id.cocktails_list);
        final GridView gridView = (GridView) view.findViewById(R.id.cocktails_grid);

        /**
        * Create the adapter for the listView
        */
        MainAdapter cocktailListAdapter = new MainAdapter(this.model, getContext(), this.list);

        /**
        * Set the adpater to the two grid and list views
        */
        listView.setAdapter(cocktailListAdapter);
        gridView.setAdapter(cocktailListAdapter);

        /**
        * The observer link to the Mutable Live Data for the display mode Gird or List
        * When the viewMode change, the other view is GONE
        */
        final Observer<String> currentViewMode = new Observer<String>() {
            @Override
            public void onChanged(final String mode) {

                if(mode.equals("grid")){
                    listView.setVisibility(View.GONE);
                    gridView.setVisibility(View.VISIBLE);

                }
                else{
                    listView.setVisibility(View.VISIBLE);
                    gridView.setVisibility(View.GONE);
                }
            }
        };


        /**
        * Set the observer to the currentView
        */
        model.getCurrentViewMode().observe(getViewLifecycleOwner(), currentViewMode);


        /**
        * If the tab is index 0, actions for the first tab
        */
        if(position == 0) {

            /**
            * Check for internet to know what to display
            */
            checkInternet(view,cocktailListAdapter);
            checkTheme(view,cocktailListAdapter,true);

            /**
            * Create the observer of the Mutable Live Data main list, each time the Mutable list change, the view change and adapter is notified
            */
            final Observer<List<Item>> listObserver = new Observer<List<Item>>() {
                @Override
                public void onChanged(final List<Item> cocktailsList) {

                    /**
                    * If the returned list is null or empty, removeAll data and notify user with empty view and adapter
                    */
                    if (cocktailsList == null || cocktailsList.size() == 0 || cocktailsList.get(0) == null) {
                        View no = view.findViewById(R.id.no_cocktails_layout);
                        no.setVisibility(View.VISIBLE);
                        checkInternet(view,cocktailListAdapter);
                        checkTheme(view,cocktailListAdapter,false);
                        list.removeAll(list);
                        cocktailListAdapter.notifyDataSetChanged();
                        return;
                    }
                    /**
                    * Undisplay empty list icon and notify list has changed to the adapter
                    */
                    view.findViewById(R.id.no_cocktails_layout).setVisibility(View.GONE);
                    if(!MainFragment.IsForRandom(list,cocktailsList)) list.removeAll(list);
                    list.addAll(cocktailsList);
                    cocktailListAdapter.notifyDataSetChanged();
                }
            };

            /**
            * Link the observer to the list
            */
            model.getCurrentList().observe(getViewLifecycleOwner(), listObserver);

            /**
            * Scroll event, detect the end of the list
            */
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {

                }

                @Override
                public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    {

                        switch (absListView.getId()) {
                            case R.id.cocktails_list:

                                final int lastItem = firstVisibleItem + visibleItemCount;

                                if (lastItem == totalItemCount) {
                                    if (controlLast != lastItem) {
                                        cocktailListAdapter.notifyDataSetChanged();

                                        controlLast = lastItem;
                                    }
                                }
                        }
                    }
                }
            });
        }

        
        if(position == 1) {

            final Observer<List<Item>> listObserver = new Observer<List<Item>>() {
                @Override
                public void onChanged(final List<Item> cocktailsList) {
                    if (cocktailsList == null || cocktailsList.size() == 0 || cocktailsList.get(0) == null) {
                        noFavorites(view);
                        list.removeAll(list);
                        cocktailListAdapter.notifyDataSetChanged();
                        return;
                    }
                    view.findViewById(R.id.no_cocktails_layout).setVisibility(View.GONE);
                    list.removeAll(list);
                    list.addAll(cocktailsList);
                    cocktailListAdapter.notifyDataSetChanged();
                }
            };
            String theme = this.getTheme();
            model.getCurrentFavList(theme).observe(getViewLifecycleOwner(), listObserver);

            if (!this.model.hasFavorites(theme)) noFavorites(view);
            else {
                try {
                    model.getCurrentFavList(theme).setValue(this.model.getAllCocktailsLocaly(theme));
                    list.removeAll(list);
                    list.addAll(this.model.getAllCocktailsLocaly(theme));
                    cocktailListAdapter.notifyDataSetChanged();
                }
                catch(Exception e) {}
            }

        }


        return view;
    }

    public static boolean IsForRandom(List<Item> list, List<Item> cocktailsList) {
        return cocktailsList.size() == 1 && list.size() == 1 && !cocktailsList.get(0).getTheme().equals(list.get(0).getTheme());
    }

    private boolean deviceOnWifi(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void checkInternet(View view, MainAdapter cocktailListAdapter) {
        if (!deviceOnWifi(getActivity().getBaseContext())) {
            View no = view.findViewById(R.id.no_cocktails_layout);
            no.setVisibility(View.VISIBLE);
            TextView txt = (TextView) view.findViewById(R.id.noItems);
            txt.setText("No connection");
            ImageView img = (ImageView) view.findViewById(R.id.no_cocktail_vector);
            img.setImageResource(R.drawable.wifi_icon);

            list.removeAll(list);
            cocktailListAdapter.notifyDataSetChanged();
        }
    }

    private void checkTheme(View view, MainAdapter cocktailListAdapter, boolean init) {
        if (deviceOnWifi(getActivity().getBaseContext())) {
            String theme = getTheme();
            View no = view.findViewById(R.id.no_cocktails_layout);
            TextView txt = (TextView) view.findViewById(R.id.noItems);
            ImageView img = (ImageView) view.findViewById(R.id.no_cocktail_vector);
            no.setVisibility(View.VISIBLE);
            if (init && !theme.equals("random")) {
                if (theme.equals("cocktail"))
                    txt.setText("Search for cocktails");
                else txt.setText("Search for meals");
                img.setImageResource(R.drawable.search_icon);
            }
            else if (theme.equals("cocktail")) {
                txt.setText("No cocktails found");
                img.setImageResource(R.drawable.cocktail_icon);
            }
            else if (theme.equals("meal")){
                txt.setText("No meals found");
                img.setImageResource(R.drawable.meal_icon);
            }
            else no.setVisibility(View.GONE);
            list.removeAll(list);
            cocktailListAdapter.notifyDataSetChanged();
        }
    }

    private void noFavorites(View view) {
        View no = view.findViewById(R.id.no_cocktails_layout);
        no.setVisibility(View.VISIBLE);
        TextView txt = (TextView) view.findViewById(R.id.noItems);
        ImageView img = (ImageView) view.findViewById(R.id.no_cocktail_vector);
        txt.setText("Add some favorites");
        img.setImageResource(R.drawable.bookmark_on);
    }

    private String getTheme() {
        String theme = getActivity().getLocalClassName();
        if(theme.equals("activities.MainActivityCocktails")) return "cocktail";
        else if(theme.equals("activities.MainActivityMeals")) return "meal";
        else return "random";
    }

}