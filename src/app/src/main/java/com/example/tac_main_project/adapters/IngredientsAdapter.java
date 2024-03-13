package com.example.tac_main_project.adapters;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tac_main_project.R;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.mViewHolder> {

    private LayoutInflater layoutInflater;
    private Context context;
    private int length;
    private String[] ingredients;
    private String[] measures;
    private String theme;

    public IngredientsAdapter(Context aContext, String theme, String[] ingredients, String[] measures) {
        this.context = aContext;
        layoutInflater = LayoutInflater.from(aContext);
        this.length = 0;
        for(int i = 0; i < ingredients.length;i++) if (ingredients[i] != null && !ingredients[i].equals("null") && !ingredients[i].equals("")) this.length++;
        this.theme = theme;
        this.ingredients = ingredients;
        this.measures = measures;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = layoutInflater.inflate(R.layout.ingredient_layout, null);
        return new mViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final mViewHolder holder, final int position)
    {
        String txt;
        if (measures[position] != null && !measures[position].equals("null")) txt = measures[position].concat(" ").concat(ingredients[position]);
        else txt = ingredients[position];
        holder.ingredientName.setText(txt);

        String url = "https://www.the".concat(theme).concat("db.com/images/ingredients/").concat(ingredients[position]).concat(".png");

        if(deviceOnWifi()) Glide.with(context).load(url).into(holder.ingredientView);
        else holder.ingredientView.setImageResource(R.drawable.logo_app);
    }

    @Override
    public int getItemCount() { return this.length; }

    private boolean deviceOnWifi() {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public class mViewHolder extends RecyclerView.ViewHolder {

        ImageView ingredientView;
        TextView ingredientName;

        public mViewHolder(View view) {
            super(view);
            ingredientName = (TextView) view.findViewById(R.id.ingredient_text);
            ingredientView = (ImageView) view.findViewById(R.id.ingredient_image);
        }
    }

}
