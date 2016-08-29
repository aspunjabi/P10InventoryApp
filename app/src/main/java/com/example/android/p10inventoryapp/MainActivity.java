package com.example.android.p10inventoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView list;
    public ArrayList<Inventory> inventoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DbHandler db = new DbHandler(this);
        Log.v("DB", "OK");
        Log.v("QUERY:", DbContract.Table.CREATE_TABLE);


        ListView listView = (ListView) findViewById(R.id.main_listView);
        // Set the view corresponding to whether items are in inventory or not
        TextView empty = (TextView) findViewById(R.id.main_empty);

        inventoryList = db.readInventory();
        if (inventoryList.size() == 0) {
            empty.setText("No Items in the Inventory :(");
        } else {
            empty.setText("");
        }

        /* To debug item display
        Log.v("Size : ", String.valueOf(inventoryList.size()));
        for (Inventory H : inventoryList) {
            String log = "Id: " + H.getProductId() + " ,Title: " + H.getName()
            "\nImage Path: "+H.getImagePath();
            Log.d("Name: ", log);
        }
        */

        ListViewAdapter customAdapter = new ListViewAdapter(inventoryList);
        customAdapter.notifyDataSetChanged();

        listView.setAdapter(customAdapter);

        /*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent details = new Intent(parent.getContext(), ItemFullDisplayActivity.class);

                Inventory currentItem = inventoryList.get(position);

                details.putExtra("productName", currentItem.getName());
                details.putExtra("productQuantity", currentItem.getQuantity());
                details.putExtra("id", currentItem.getProductId());

                parent.getContext().startActivity(details);
            }
        });*/

        final Button addItem = (Button) findViewById(R.id.main_addItemButton);

        addItem.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the "add item" button is clicked on.
            @Override
            public void onClick(View view) {
                // Create a new intent to open the {@link FamilyActivity}
                Intent addItemIntent = new Intent(MainActivity.this, AddNewItemActivity.class);
                addItemIntent.putExtra("HEADER", "Add a new item");

                // Start the new activity
                startActivity(addItemIntent);
            }
        });

    }

}
