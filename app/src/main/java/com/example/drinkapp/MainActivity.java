package com.example.drinkapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.drinkapp.Retrofit.IDrinkShopAPI;
import com.example.drinkapp.Utils.Common;
import com.example.drinkapp.models.CheckUserResponse;
import com.example.drinkapp.models.User;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.szagurskii.patternedtextwatcher.PatternedTextWatcher;

import java.util.Arrays;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    Button btn_continue;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener listener;
    private List<AuthUI.IdpConfig> providers;
    private  static  final int REQUEST_CODE=1000;
    private static final int REQUEST_PERMISSION=1001;
    IDrinkShopAPI mService;
    MaterialEditText edt_name,edt_address,edt_birthdate;




    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(listener);
    }

    @Override
    protected void onStop() {
        if (listener!=null)
            firebaseAuth.removeAuthStateListener(listener);
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION:
            {
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission Called", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
                break;
                default:
                    break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //printKeyHash();

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this,new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
        },REQUEST_PERMISSION);
        mService= Common.getAPI();

        firebaseAuth = FirebaseAuth.getInstance();
        providers= Arrays.asList(new AuthUI.IdpConfig.PhoneBuilder().build());
        listener= firebaseAuthLocal -> {
            FirebaseUser user=firebaseAuthLocal.getCurrentUser();
            if(user!=null)
            {
                final AlertDialog alertDialog=new SpotsDialog.Builder().setCancelable(false).setContext(this).build();
                alertDialog.show();
                alertDialog.setMessage("Xin vui long doi...");
                mService.checkUserExists(user.getPhoneNumber())
                        .enqueue(new Callback<CheckUserResponse>() {
                            @Override
                            public void onResponse(Call<CheckUserResponse> call, Response<CheckUserResponse> response) {
                                CheckUserResponse userResponse=response.body();
                                if(userResponse.isExists())
                                {
                                    mService.getUserInfomation(user.getPhoneNumber())
                                            .enqueue(new Callback<User>() {
                                                @Override
                                                public void onResponse(Call<User> call, Response<User> response) {
                                                    alertDialog.dismiss();
                                                    Common.currentUser=response.body();
                                                    startActivity(new Intent(MainActivity.this,Home.class));
                                                    finish();

                                                }

                                                @Override
                                                public void onFailure(Call<User> call, Throwable t) {
                                                    alertDialog.dismiss();
                                                    Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }

                                else
                                {
                                    alertDialog.dismiss();
                                    showRegisterDialog(user.getPhoneNumber());
                                }
                            }

                            @Override
                            public void onFailure(Call<CheckUserResponse> call, Throwable t) {



                            }
                        });

            }

        };
        Anhxa();

    }



    private void UpdateTokenToServer() {

    }

    private void showRegisterDialog(final String phone) {
        AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("REGISTER");

        LayoutInflater inflater=this.getLayoutInflater();
        View register_layout=inflater.inflate(R.layout.register_layout,null);

        edt_name=(MaterialEditText)register_layout.findViewById(R.id.edt_name);
        edt_address=(MaterialEditText)register_layout.findViewById(R.id.edt_address);
        edt_birthdate=(MaterialEditText)register_layout.findViewById(R.id.edt_birthdate);

        Button btn_register=(Button)register_layout.findViewById(R.id.btn_register);
        edt_birthdate.addTextChangedListener(new PatternedTextWatcher("####-##-##"));

        builder.setView(register_layout);
        final AlertDialog dialog=builder.create();

        btn_register.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // dialog.dismiss();

                if (TextUtils.isEmpty(edt_name.getText().toString()))
                {
                    Toast.makeText(MainActivity.this, "Dien Name vao form", Toast.LENGTH_SHORT).show();
                    edt_name.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(edt_birthdate.getText().toString()))
                {
                    Toast.makeText(MainActivity.this, "Dien Birthdate vao form", Toast.LENGTH_SHORT).show();
                    edt_birthdate.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(edt_address.getText().toString()))
                {
                    Toast.makeText(MainActivity.this, "Dien Dia chi vao form", Toast.LENGTH_SHORT).show();
                    edt_address.requestFocus();
                    return;
                }

                final AlertDialog watingDialog=new SpotsDialog.Builder().setContext(MainActivity.this).build();
                watingDialog.show();
                watingDialog.setMessage("Doi trong giay lat...");
                mService.registerNewUser(phone,
                        edt_address.getText().toString(),
                        edt_name.getText().toString(),
                        edt_birthdate.getText().toString())
                        .enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                watingDialog.dismiss();
                                User user=response.body();
                                if(TextUtils.isEmpty(user.getError_msg()))
                                {
                                    Toast.makeText(MainActivity.this, "Dng ky thanh cong", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(MainActivity.this,Home.class));
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                watingDialog.dismiss();

                            }
                        });
            }
        });

        dialog.show();

    }

    private void Anhxa() {
        btn_continue=findViewById(R.id.btn_continune);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLoginPage();
            }
        });
    }

    private void startLoginPage() {
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(), REQUEST_CODE);
    }

//    private void printKeyHash() {
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo("com.example.drinkapp",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature:info.signatures) {
//                MessageDigest md=MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KEYHASH", Base64.encodeToString(md.digest(),Base64.DEFAULT));
//
//            }
//        }catch (PackageManager.NameNotFoundException e){
//            e.printStackTrace();
//        }catch (NoSuchAlgorithmException e){
//
//        }

//    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            } else {
                Toast.makeText(this, "Dang ky that bai", Toast.LENGTH_SHORT).show();
            }
        }
    }
        //Exit
        boolean isBackClick = false;
        @Override
        public void onBackPressed() {
            if(isBackClick) {
                super.onBackPressed();
                return;
            }
            this.isBackClick=true;
            Toast.makeText(this, "Click BACK neu muon EXIT", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
            isBackClick=false;
        super.onResume();
    }
}
