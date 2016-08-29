package com.example.android.p10inventoryapp;

import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

/**
 * Created by aspun_000 on 8/30/2016.
 */
public class ItemFullDisplayActivity extends AppCompatActivity {

    public static double priceProduct;
    public int rowID;
    public Inventory inv; // = new Inventory();
    int quantity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_details);

        TextView nameView = (TextView) findViewById(R.id.details_productName);
        TextView priceView = (TextView) findViewById(R.id.details_productPrice);
        TextView quantityView = (TextView) findViewById(R.id.details_productQuantity);
        quantityView.setText("0");

        // Get data passed in from Fragment
        Intent details = getIntent();
        final String productName = details.getStringExtra("productName");
        final int productQuantity = details.getIntExtra("quantity", 0);
        final double productPrice = details.getDoubleExtra("price", 0);

        setTitle(productName);

        nameView.setText(productName);
        //priceView.setText(String.valueOf(productPrice));

        final int productId = details.getIntExtra("id", 0);
        ContextWrapper cw = new ContextWrapper(this);
        File dir = cw.getFilesDir();

        // Load the item image
        final DbHandler db = new DbHandler(this);
        String imageLocationDir = dir.toString();
        rowID = details.getIntExtra("id", 0);
        Log.v("IMAGE ACCESS ID!!!!!!! ", String.valueOf(rowID));
        String imagePath = imageLocationDir + "/" + rowID;
        Log.v("Image path: ", "After click Item" + imagePath);

        ImageView imageView = (ImageView) findViewById(R.id.details_productImage);

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        // Set the image view
        imageView.setImageBitmap(bitmap);

        quantityView.setText("" + details.getIntExtra("productQuantity", 0));
 //       priceProduct = details.getDoubleExtra("productPrice", 0.0);
        priceView.setText("" + productPrice);

        Button orderQtyIncrement = (Button) findViewById(R.id.details_buttonIncrement);
        orderQtyIncrement.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the "+" button is clicked on.
            @Override
            public void onClick(View view) {
                // Create a new intent to open the {@link FamilyActivity}
                if (quantity == 1000) {
                    Toast.makeText(ItemFullDisplayActivity.this,
                            "Cannot increase quantity further!", Toast.LENGTH_SHORT).show();
                    return;
                }
                quantity = quantity + 1;
                displayOrderQuantity(quantity);
            }
        });

        Button orderQtyDecrement = (Button) findViewById(R.id.details_buttonDecrement);
        orderQtyDecrement.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the "+" button is clicked on.
            @Override
            public void onClick(View view) {
                // Create a new intent to open the {@link FamilyActivity}
                if (quantity == 0) {
                    Toast.makeText(ItemFullDisplayActivity.this,
                            "Cannot decrease quantity further!", Toast.LENGTH_SHORT).show();
                    return;
                }
                quantity = quantity - 1;
                displayOrderQuantity(quantity);
            }
        });

        Button qtyIncrement = (Button) findViewById(R.id.qtyUpdatePlus);
        qtyIncrement.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the "+" button is clicked on.
            @Override
            public void onClick(View view) {
                // Create a new intent to open the {@link FamilyActivity}
                Inventory currentInv = new Inventory(productId, productName, productQuantity + 1, productPrice);
                DbHandler db = new DbHandler(ItemFullDisplayActivity.this);
                db.updateInventoryRow(productId, currentInv);

                displayQuantity(productQuantity + 1);
            }
        });

        Button qtyDecrement = (Button) findViewById(R.id.qtyUpdateMinus);
        qtyIncrement.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the "+" button is clicked on.
            @Override
            public void onClick(View view) {
                // Create a new intent to open the {@link FamilyActivity}
                Inventory currentInv = new Inventory(productId, productName, productQuantity - 1, productPrice);
                DbHandler db = new DbHandler(ItemFullDisplayActivity.this);
                db.updateInventoryRow(productId, currentInv);

                displayQuantity(productQuantity - 1);
            }
        });


        Button deleteItem = (Button) findViewById(R.id.details_deleteNow);
        deleteItem.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the "delete item" button is clicked on.
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ItemFullDisplayActivity.this)
                        .setTitle("Warning")
                        .setMessage("Are you sure you want to delete this record permanently?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteItemPermanently(rowID);
                                Toast.makeText(ItemFullDisplayActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ItemFullDisplayActivity.this, MainActivity.class);
                                ItemFullDisplayActivity.this.startActivity(intent);
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        Button submitOrder = (Button) findViewById(R.id.details_orderNow);
        submitOrder.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the "Submit order" button is clicked on.
            @Override
            public void onClick(View view) {
                String subject = "URGENT: ORDER MORE ITEMS";
                TextView orderQuantity = (TextView) findViewById(R.id.details_orderQuantity);
                int orderSubmitQuantity = Integer.parseInt(orderQuantity.getText().toString());
                String message = "Product Name: " + productName +
                        "\nat price: " + productPrice +
                        "\nQuantity To be ordered: " + quantity +
                        "\n\n--From Inventory App";
                Log.v("Message:", message);
                String[] emails = {"SendOrderTo@gmail.com"};

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, emails);
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, message);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }

        });
    }

    public void deleteItemPermanently(int rowID) {
        DbHandler db = new DbHandler(this);
        db.deleteHabitRow(rowID);
    }

    private void displayOrderQuantity(int count) {
        TextView quantity = (TextView) findViewById(R.id.details_orderQuantity);
        quantity.setText("" + count);
    }

    private void displayQuantity(int count) {
        TextView quantity = (TextView) findViewById(R.id.details_productQuantity);
        quantity.setText("" + count);
    }
}
