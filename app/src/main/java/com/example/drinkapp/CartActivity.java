package com.example.drinkapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.example.drinkapp.Adapter.CartAdapter;
import com.example.drinkapp.Database.ModelDB.Cart;
import com.example.drinkapp.Retrofit.IDrinkShopAPI;
import com.example.drinkapp.Utils.Common;
import com.example.drinkapp.Utils.RecyclerItemTouchHelperListener;
import com.example.drinkapp.Utils.RecycleritemTouchHelper;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    private static final int PAYMENT_REQUEST_CODE=7777;
    RecyclerView recyclerView_cart;
    Button btn_order;
    CartAdapter cartAdapter;
    RelativeLayout rootLayout;
    CompositeDisposable compositeDisposable;
    List<Cart> cartList=new ArrayList<>();
    IDrinkShopAPI mService;
    IDrinkShopAPI mServiceScalars;
    String token,amount,orderAddress,orderComment;
    HashMap<String,String> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        compositeDisposable=new CompositeDisposable();
        mService=Common.getAPI();
        mServiceScalars=Common.getScalarsAPI();
        recyclerView_cart=(RecyclerView)findViewById(R.id.rc_giohang);
        recyclerView_cart.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_cart.setHasFixedSize(true);
        rootLayout=findViewById(R.id.rootLayout);
        ItemTouchHelper.SimpleCallback simpleCallback=new RecycleritemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView_cart);
        btn_order=(Button)findViewById(R.id.btn_order);
        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               placeOrder();
                //Toast.makeText(CartActivity.this, "submit", Toast.LENGTH_SHORT).show();
            }
        });
        loadCartItem();
        loadToken();

    }

    private void loadToken() {
        final android.app.AlertDialog watingDialog=new SpotsDialog.Builder().setContext(CartActivity.this).build();
        watingDialog.show();
        watingDialog.setMessage("Doi trong giay lat...");
        AsyncHttpClient client=new AsyncHttpClient();
        client.get(Common.API_TOKEN_URL, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                watingDialog.dismiss();
                btn_order.setEnabled(false);
                Toast.makeText(CartActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                watingDialog.dismiss();
                btn_order.setEnabled(true);
                token=responseString;

            }
        });
    }

    private void placeOrder() {
        // create dialong
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Submit Order");
        View submit_order_layout= LayoutInflater.from(this).inflate(R.layout.submit_order_layout,null);

         final EditText edt_comment=(EditText) submit_order_layout.findViewById(R.id.edt_comment);
        final EditText edt_other_address=(EditText) submit_order_layout.findViewById(R.id.edt_other_address);

        final RadioButton rdi_user_address=(RadioButton)submit_order_layout.findViewById(R.id.rdi_user_address);
        final RadioButton rdi_other_address=(RadioButton)submit_order_layout.findViewById(R.id.rdi_other_address);
        //event
        rdi_user_address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    edt_other_address.setEnabled(false);

            }
        });
        rdi_other_address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    edt_other_address.setEnabled(true);
            }
        });
        builder.setView(submit_order_layout);
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                orderComment=edt_comment.getText().toString();
                if(rdi_user_address.isChecked())
                    orderAddress=Common.currentUser.getAddress();
                else if(rdi_other_address.isChecked())
                    orderAddress=edt_other_address.getText().toString();
                else
                    orderAddress="";
                Toast.makeText(CartActivity.this, "click", Toast.LENGTH_SHORT).show();

                //payment
                DropInRequest dropInRequest=new DropInRequest().clientToken(token);
                startActivityForResult(dropInRequest.getIntent(CartActivity.this),PAYMENT_REQUEST_CODE);

                //submit order


            }
        });
        builder.show();
    }
    //


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYMENT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodNonce nonce = result.getPaymentMethodNonce();
                String strNonce = nonce.getNonce();
                if (Common.cartRepository.sumPrice() > 0) {
                    amount = String.valueOf(Common.cartRepository.sumPrice());
                    params = new HashMap<>();
                    params.put("amount", amount);
                    params.put("nonce", strNonce);
                    sendPayment();
                } else {
                    Toast.makeText(this, "Payment amount is 0", Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == RESULT_CANCELED)
                Toast.makeText(this, "payment cancel", Toast.LENGTH_SHORT).show();
            else {
                Exception exception = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.d("hoang_error", exception.getMessage());
            }
        }
    }

    private void sendPayment() {
        mServiceScalars.payment(params.get("nonce"),params.get("amount"))
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.body().contains("Successful"))
                        {
                            Toast.makeText(CartActivity.this, "Transaction Successful", Toast.LENGTH_SHORT).show();

                            //submit order

                            compositeDisposable.add(
                                    Common.cartRepository.getCartItem()
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribeOn(Schedulers.io())
                                            .subscribe(new Consumer<List<Cart>>() {
                                                @Override
                                                public void accept(List<Cart> carts) throws Exception {
                                                    if(!TextUtils.isEmpty(orderAddress))
                                                        sendOderToServer(Common.cartRepository.sumPrice(),
                                                                carts,
                                                                orderComment,orderAddress);
                                                    else
                                                        Toast.makeText(CartActivity.this, "Address Null", Toast.LENGTH_SHORT).show();

                                                }
                                            })
                            );
                        }else{
                            Toast.makeText(CartActivity.this, "Transaction failed", Toast.LENGTH_SHORT).show();
                        }
                        Log.d("vinhhoang_info",response.body());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("vinhhoang_info",t.getMessage());

                    }
                });

    }

    private void sendOderToServer(float sumPrice, List<Cart> carts, String orderCommment, String orderAddress) {
        if(carts.size()>0)
        {
            String orderDetail=new Gson().toJson(carts);
            mService.submitOrder(sumPrice,orderDetail,orderCommment,orderAddress,Common.currentUser.getPhone())
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Toast.makeText(CartActivity.this, "Order submit", Toast.LENGTH_SHORT).show();

                            //Clear cart
                            Common.cartRepository.entyCart();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.e("ERROR",t.getMessage());

                        }
                    });
        }

    }


    private void loadCartItem() {
        compositeDisposable.add(
                Common.cartRepository.getCartItem()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Cart>>() {
                    @Override
                    public void accept(List<Cart> carts) throws Exception {
                        displayCartItem(carts);
                    }
                })
        );
    }

    private void displayCartItem(List<Cart> carts) {
        cartList=carts;
       cartAdapter=new CartAdapter(this,carts);
        recyclerView_cart.setAdapter(cartAdapter);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartAdapter.CartViewHolder)
        {
            String name=cartList.get(viewHolder.getAdapterPosition()).name;
            final Cart deleteItem=cartList.get(viewHolder.getAdapterPosition());
            final  int deletedIndex=viewHolder.getAdapterPosition();
            cartAdapter.removeItem(deletedIndex);
            Common.cartRepository.deleteToCart(deleteItem);
            Snackbar snackbar=Snackbar.make(rootLayout,new StringBuilder(name).append("Xoa san pham nay?"),5000);
            snackbar.setAction("khong", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cartAdapter.restoreItem(deleteItem,deletedIndex);
                    Common.cartRepository.insertToCart(deleteItem);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }

    }

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
        super.onResume();
        loadCartItem();
        isBackClick=false;
    }

}

