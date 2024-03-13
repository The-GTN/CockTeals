package com.example.tac_main_project.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tac_main_project.R;
import com.example.tac_main_project.activities.DetailsActivity;
import com.example.tac_main_project.models.objects.Item;
import com.example.tac_main_project.models.view.ListViewModel;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class MainAdapter extends BaseAdapter {

    private List<Item> listCocktail;
    private LayoutInflater layoutInflater;
    private Context context;
    private ListViewModel model;

    public MainAdapter(ListViewModel model, Context aContext, List<Item> listCocktail) {
        this.context = aContext;
        this.listCocktail = listCocktail;
        layoutInflater = LayoutInflater.from(aContext);
        this.model = model;
    }

    @Override
    public int getCount() {
        return listCocktail.size();
    }

    @Override
    public Object getItem(int position) {
        return listCocktail.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            if(model.getCurrentViewMode().getValue().equals("grid"))
                convertView = layoutInflater.inflate(R.layout.item_layout_grid, null);
            else
                convertView = layoutInflater.inflate(R.layout.item_layout_list, null);
            holder = new ViewHolder();
            holder.cocktailName = (TextView) convertView.findViewById(R.id.cocktail_name);
            holder.cocktailDescription = (TextView) convertView.findViewById(R.id.cocktail_description);
            holder.cocktailView = (ImageView) convertView.findViewById(R.id.Cocktail_view);
            holder.favButton = (ImageView) convertView.findViewById(R.id.favButton);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Item cocktail = this.listCocktail.get(position);
        holder.cocktailName.setText(cocktail.getName());
        String formatDescription;

        if(cocktail.getDescription().length() > 50){
            formatDescription = cocktail.getDescription().substring(0,50).concat("...");
        }else{
            formatDescription = cocktail.getDescription();
        }
        holder.cocktailDescription.setText("Notice: " + formatDescription);

        if(model.getAllNamesOfFavList().contains(cocktail.getName())){
            holder.favButton.setImageResource(R.drawable.bookmark_on);
            if(cocktail.getByteArray() != null) {
                Glide.with(context).load(cocktail.getByteArray()).into(holder.cocktailView);
            }else{
                Glide.with(context).load(cocktail.getViewUrl()).into(holder.cocktailView);
            }

        }else{
            holder.favButton.setImageResource(R.drawable.bookmark_off);
            Glide.with(context).load(cocktail.getViewUrl()).into(holder.cocktailView);
        }


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("imageUrl", cocktail.getViewUrl());
                intent.putExtra("name",cocktail.getName());
                intent.putExtra("description",cocktail.getDescription());
                intent.putExtra("ingredients",cocktail.getIngredients());
                intent.putExtra("measures",cocktail.getMeasures());
                intent.putExtra("theme",cocktail.getTheme());
                intent.putExtra("bytearray",cocktail.getByteArray());

                context.startActivity(intent);
            }
        });

        holder.favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cocktail.setFav(!model.getAllNamesOfFavList().contains(cocktail.getName()));


                List<Item> favList = new ArrayList<Item>();
                if (model.getCurrentFavList(cocktail.getTheme()).getValue() != null) {
                    favList.addAll(model.getCurrentFavList(cocktail.getTheme()).getValue());
                }
                if (cocktail.getFav()) {
                    favList.add(cocktail);

                    Bitmap bitmap = ((BitmapDrawable) holder.cocktailView.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageInByte = baos.toByteArray();

                    model.insertCocktailLocaly(cocktail,imageInByte);
                    holder.favButton.setImageResource(R.drawable.bookmark_on);
                } else {
                    for(int i=0;i<favList.size();i++) {
                        if(favList.get(i).getName().equals(cocktail.getName())){
                            favList.remove(i);
                        }
                    }
                    model.deleteCocktailLocaly(cocktail);
                    holder.favButton.setImageResource(R.drawable.bookmark_off);
                }
                model.getCurrentFavList(cocktail.getTheme()).setValue(favList);
            }
        });


        return convertView;
    }


    static class ViewHolder {
        ImageView cocktailView;
        ImageView favButton;
        TextView cocktailName;
        TextView cocktailDescription;
    }

}
