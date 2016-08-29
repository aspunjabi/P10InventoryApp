package com.example.android.p10inventoryapp;

import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by aspun_000 on 8/30/2016.
 */
public class AddNewItemActivity extends AppCompatActivity {
    public String price;
    public String countItem;
    public String name;
    public long nextID;

    public Inventory inventoryObject; //= new Inventory();
    DbHandler db = new DbHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_item);

        nextID = (db.rowCount() + 1);
        Intent intent = getIntent();
        String message = intent.getStringExtra("HEADER");
        setTitle(message);

        Button itemSubmit = (Button) findViewById(R.id.add_submitButton);
        itemSubmit.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the submit button is clicked on.
            @Override
            public void onClick(View view) {
                EditText nameText = (EditText) findViewById(R.id.add_productName);
                EditText priceText = (EditText) findViewById(R.id.add_productPrice);
                EditText quantityText = (EditText) findViewById(R.id.add_productQuantity);
                ImageView img = (ImageView) findViewById(R.id.add_imageSelected);
                ImageView imgError = (ImageView) findViewById(R.id.add_imageViewError);
                name = nameText.getText().toString();
                price = (priceText.getText().toString());
                countItem = (quantityText.getText().toString());

                if (nameText.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Name cannot be Blank", Toast.LENGTH_LONG).show();
                    nameText.setError("Name cannot be Blank");
                    return;
                } else if (priceText.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Price for the product is must", Toast.LENGTH_LONG).show();
                    priceText.setError("Invalid Price");
                    return;
                } else if (Integer.parseInt(priceText.getText().toString()) <= 0) {
                    Toast.makeText(getApplicationContext(), "Price cannot be 0 or negative", Toast.LENGTH_LONG).show();
                    priceText.setError("Invalid Price");
                    return;
                } else if (quantityText.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "You have to enter a quantity", Toast.LENGTH_LONG).show();
                    quantityText.setError("Invalid Quantity");
                    return;
                } else if (img.getDrawable() == null) {
                    Toast.makeText(getApplicationContext(), "Upload an image", Toast.LENGTH_LONG).show();
                    imgError.setVisibility(View.VISIBLE);
                    imgError.setImageResource(R.drawable.error);
                    return;
                } else {
                    db.addItem(new Inventory(name, Integer.parseInt(countItem), Double.parseDouble(price)));
                    Log.v("Item added at row: ", "" + nextID);
                    Toast.makeText(AddNewItemActivity.this, "Item Added Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddNewItemActivity.this, MainActivity.class);
                    startActivity(intent);

                    Log.v("VALUE DETAILS: ", name + " " + price + " " + countItem);
                }
            }
        });

        /* Image upload methods sourced from Github repo */
        Button imageUpload = (Button) findViewById(R.id.add_imageUploadButton);
        imageUpload.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the submit button is clicked on.
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 &&
                resultCode == RESULT_OK && null != data) {
            Toast.makeText(this, "Uploading...", Toast.LENGTH_LONG).show();
            Uri selectedImageUri = data.getData();
            try {
                Log.v("Path:", selectedImageUri.toString());
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                // Log.d(TAG, String.valueOf(bitmap));

                ImageView imageView = (ImageView) findViewById(R.id.add_imageSelected);
                imageView.setImageBitmap(bitmap);
                Log.v("TAG NOTE:", "Product before created, ID: " + nextID);

                String filename = Long.toString(nextID);
                Log.v("Image path: ", filename);
                saveToInternalStorage(bitmap, filename);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to get image", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void saveToInternalStorage(Bitmap bmp, String filename) {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File appDirectory = contextWrapper.getFilesDir();

        File currentPath = new File(appDirectory, filename);

        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(currentPath);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
