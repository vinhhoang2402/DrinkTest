package com.example.drinkapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.drinkapp.Adapter.CategoryAdapter;
import com.example.drinkapp.Database.DataSource.CartRepository;
import com.example.drinkapp.Database.DataSource.FavoriteRepository;
import com.example.drinkapp.Database.Local.CartDataSource;
import com.example.drinkapp.Database.Local.FavoriteDataSource;
import com.example.drinkapp.Database.Local.VinhHoangRoomDatabase;
import com.example.drinkapp.Retrofit.IDrinkShopAPI;
import com.example.drinkapp.Utils.Common;
import com.example.drinkapp.Utils.ProgressRequestBody;
import com.example.drinkapp.Utils.UploadCallBack;
import com.example.drinkapp.models.Banner;
import com.example.drinkapp.models.Category;
import com.example.drinkapp.models.Drink;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.nex3z.notificationbadge.NotificationBadge;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import edmt.dev.afilechooser.utils.FileUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, UploadCallBack {
    private static final int PICK_FILE_REQUEST =1222 ;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NotificationBadge badge;
    NavigationView navigationView;
    ImageView cart_icon;
    CircleImageView image_avatar;
    ActionBarDrawerToggle toggle;
    SliderLayout sliderLayout;
    IDrinkShopAPI mService;
    RecyclerView list_menu;
    Uri selectFileUri;
    TextView txt_user,txt_phone;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Anhxa();
        mService = Common.getAPI();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawerOpen, R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        getBannerImage();
        getMenu();
        getToppingList();
        initDB();
    }

    public  void initDB() {
        Common.vinhHoangRoomDatabase= VinhHoangRoomDatabase.getInstance(this);
        Common.cartRepository= CartRepository.getInstance(CartDataSource.getInstance(Common.vinhHoangRoomDatabase.cartDAO()));
        Common.favoriteRepository= FavoriteRepository.getInstance(FavoriteDataSource.getInstance(Common.vinhHoangRoomDatabase.favoriteDAO()));
    }

    private void getToppingList() {
        compositeDisposable.add(mService.getDrink(Common.TOPPING_MENU_ID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Drink>>() {
                    @Override
                    public void accept(List<Drink> drinks) throws Exception {
                        Common.toppingList=drinks;
                    }
                }));

    }

    private void getMenu() {
        compositeDisposable.add(mService.getCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Category>>() {
                    @Override
                    public void accept(List<Category> categories) throws Exception {
                        displayMenu(categories);
                    }
                }));
    }
    private void displayMenu(List<Category> categories) {
        CategoryAdapter adapter = new CategoryAdapter(this, categories);
        list_menu.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        list_menu.setHasFixedSize(true);
        if (adapter == null) {
            Log.e("RecyclerView", "No adapter attached; skipping layout");
        } else if (this.list_menu == null) {
            Log.e("RecyclerView", "No layout manager attached; skipping layout");
        } else {
            list_menu.setAdapter(adapter);
        }
    }


    private void getBannerImage() {
        compositeDisposable.add(mService.getBanner()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<List<Banner>>() {
            @Override
            public void accept(List<Banner> banners) throws Exception {
                displayImage(banners);
            }
        }));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_bar,menu);
        View view         = menu.findItem(R.id.cart_menu).getActionView();
        badge=(NotificationBadge)view.findViewById(R.id.baged);
        cart_icon=(ImageView)view.findViewById(R.id.cart_icon);
        cart_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this,CartActivity.class));

            }
        });
        updateCartCount();
        return true;
    }
    private void updateCartCount(){
        if(badge==null)return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(Common.cartRepository.countCartItem()==0)
                {
                    badge.setVisibility(View.INVISIBLE);
                }else{
                    badge.setVisibility(View.VISIBLE);
                    badge.setText(String.valueOf(Common.cartRepository.countCartItem()));
                }
                
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.cart_menu){
            return true;
        }else   if(id==R.id.search_menu){
            startActivity(new Intent(Home.this, SearchActivity.class));
            return true;
        }else  if(id==R.id.favorite) {
            startActivity(new Intent(Home.this,FavoriteActivity.class));
            return true;
        }else  if(id==R.id.location) {
            startActivity(new Intent(Home.this, Map.class));
            return true;
        }
        navigationView.setItemIconTintList(null);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    private void displayImage(List<Banner> banners) {
        HashMap<String,String> bannerMap=new HashMap<>();
        for(Banner item:banners)
            bannerMap.put(item.getName(),item.getLink());
        for (String name: bannerMap.keySet()){
            TextSliderView textSliderView=new TextSliderView(this);
            textSliderView.description(name)
                    .image(bannerMap.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
    }


    private void Anhxa() {
        sliderLayout=(SliderLayout)findViewById(R.id.slider);
        drawerLayout = findViewById(R.id.drawer);

        toolbar = findViewById(R.id.toolbar);
        list_menu=findViewById(R.id.recycler_menu);
        navigationView = findViewById(R.id.navigationView);
        View headerView = navigationView.getHeaderView(0);
        image_avatar=(CircleImageView)headerView.findViewById(R.id.image_avata);
        image_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
        txt_user=(TextView)headerView.findViewById(R.id.txt_user);
        txt_phone=(TextView)headerView.findViewById(R.id.txt_phone);
        if(Common.currentUser!=null) {
            txt_user.setText(Common.currentUser.getName());
            txt_phone.setText(Common.currentUser.getPhone());
        }else{
            Toast.makeText(this, "LOGIN AGAIN", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Home.this,MainActivity.class));
        }

        //set avata=tar
        if(!TextUtils.isEmpty(Common.currentUser.getAvatar()))
        {
            Picasso.with(this)
                    .load(new StringBuilder(Common.BASE_URL)
                    .append("images/")
                    .append(Common.currentUser.getAvatar()).toString())
                    .into(image_avatar);

        }

        navigationView.getBackground().setColorFilter(0x80000000, PorterDuff.Mode.MULTIPLY);
        headerView.getBackground().setColorFilter(0x80000000, PorterDuff.Mode.MULTIPLY);
        navigationView.setItemTextColor(ColorStateList.valueOf(Color.WHITE));
        //set

    }

    private void chooseImage() {
        startActivityForResult(Intent.createChooser(FileUtils.createGetContentIntent(),"Select aFile"),
                PICK_FILE_REQUEST);
    }

    //crt+o


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK)
        {
            if(requestCode==PICK_FILE_REQUEST){
                if (data!=null)
                {
                    selectFileUri=data.getData();
                    if(selectFileUri!=null && !selectFileUri.getPath().isEmpty())
                    {
                        image_avatar.setImageURI(selectFileUri);
                        uploadFile();
                    }else{
                        Toast.makeText(this, "k the upload file", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "nullllll", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void uploadFile() {
        if(selectFileUri!=null)
        {
            File file=FileUtils.getFile(this,selectFileUri);
            String fileName=new StringBuilder(Common.currentUser.getPhone())
                    .append(FileUtils.getExtension(file.toString()))
                    .toString();

            ProgressRequestBody requestFile=new ProgressRequestBody(file,this);
            MultipartBody.Part body=MultipartBody.Part.createFormData("image",fileName,requestFile);
            MultipartBody.Part userPhone=MultipartBody.Part.createFormData("phone",Common.currentUser.getPhone());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    mService.uploadFile(userPhone,body)
                            .enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    Toast.makeText(Home.this, response.body(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Log.d("vinheee",t.getMessage());

                                }
                            });
                }
            }).start();

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.home:
                startActivity(new Intent(Home.this,FavoriteActivity.class));
                break;
            case R.id.my_order:
                if(Common.currentUser!=null){
                    startActivity(new Intent(Home.this,ShowOrderActivity.class));
                }else{
                    Toast.makeText(this, "LOGIN AGAIN", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.nav_favorite:
                if(Common.currentUser!=null) {
                    startActivity(new Intent(Home.this, FavoriteActivity.class));
                }else{
                    Toast.makeText(this, "LOGIN AGAIN", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.logout:


                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("Exit App");
                builder.setMessage("Do you want to Exit App ?");
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        FirebaseAuth.getInstance().signOut();
                        Intent intent=new Intent(Home.this,MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    }
                });
                builder.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
                break;
            default:
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartCount();
        isBackClick=false;
    }

    boolean isBackClick = false;
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            if (isBackClick) {
                super.onBackPressed();
                return;
            }
            this.isBackClick = true;
            Toast.makeText(this, "Click BACK neu muon EXIT", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onProgressUpdate(int pertantage) {

    }
}
