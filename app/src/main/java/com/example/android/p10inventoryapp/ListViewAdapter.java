package com.example.android.p10inventoryapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by aspun_000 on 8/29/2016.
 */
public class ListViewAdapter extends BaseAdapter {

    private static final String TAG = ListViewAdapter.class.getSimpleName();
    ArrayList<Inventory> mListArray;

    public ListViewAdapter(ArrayList<Inventory> listArray) {
        mListArray = new ArrayList<Inventory>(listArray);
    }

    @Override
    public int getCount() {
        return mListArray.size();    // total number of elements in the list
    }

    @Override
    public Object getItem(int i) {
        return mListArray.get(i);    // single item in the list
    }

    @Override
    public long getItemId(int i) {
        return i;                   // index number
    }

    @Override
    public View getView(int index, View view, final ViewGroup parent) {

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.list_item_view, parent, false);
        }

        final Inventory dataModel = mListArray.get(index);

        final TextView productName = (TextView) view.findViewById(R.id.list_productName);
        productName.setText(dataModel.getName());

        final TextView productAvailable = (TextView) view.findViewById(R.id.list_productAvailable);
        productAvailable.setText("" + dataModel.getQuantity());

        final TextView productPrice = (TextView) view.findViewById(R.id.list_productPrice);
        productPrice.setText("Rs." + dataModel.getPrice());

        /*final ImageView productImage = (ImageView) view.findViewById(R.id.list_imageView);
        ContextWrapper cw = new ContextWrapper(this);
        File dir = cw.getFilesDir();

        String imageLocationDir = dir.toString();
        rowID = details.getIntExtra("id", 0) - 1;
        String imagePath = imageLocationDir + "/" + rowID;
        Log.v("Image path: ","After click Item"+imagePath);

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        // Set the image view
        productImage.setImageBitmap(bitmap);
        */


        Button button = (Button) view.findViewById(R.id.list_itemSale);

        this.notifyDataSetChanged();

        // button click listener
        // this chunk of code will run, if user click the button
        // because, we set the click listener on the button only

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DbHandler db = new DbHandler(view.getContext());
                dataModel.productSale();
                if (dataModel.getQuantity() == 0) {
                    Toast.makeText(parent.getContext(), "No more item(s) of " +
                            dataModel.getName() + " left in stock.\n Order Now !!! ",
                            Toast.LENGTH_SHORT).show();
                }
                db.updateInventoryRow(dataModel.getProductId(), dataModel);

                productAvailable.setText("" + dataModel.getQuantity());

                Toast.makeText(parent.getContext(), "Quantity of  " +
                        dataModel.getName() + " is reduced by 1.\n" +
                        "Available quantity is now: " + dataModel.getQuantity(),
                        Toast.LENGTH_SHORT).show();
            }
        });


        // Note to reviewer: Not sure why the below section doesn't work when inserted in MainActivity
        // as an setOnItemClickListener(); Can you please help me understand this??


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent details = new Intent(parent.getContext(), ItemFullDisplayActivity.class);
                details.putExtra("productName", dataModel.getName());
                details.putExtra("productQuantity", dataModel.getQuantity());
                details.putExtra("id", dataModel.getProductId());
                details.putExtra("price", dataModel.getPrice());
                parent.getContext().startActivity(details);
            }
        });


        return view;
    }
}