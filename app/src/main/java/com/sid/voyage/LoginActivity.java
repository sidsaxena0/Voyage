package com.sid.voyage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sid.voyage.utils.MyUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout usernameLayout,passwordLayout;
    EditText username,password;
    Button login;
    TextView forgot,signup;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private MaterialDialog progress;
    String TAG = "LOGIN";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //init views

        usernameLayout = (TextInputLayout)findViewById(R.id.username_layout);
        passwordLayout = (TextInputLayout)findViewById(R.id.password_layout);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.buttonLogin);
        forgot = (TextView)findViewById(R.id.forgot_pass);
        signup = (TextView)findViewById(R.id.create_acc);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (username.getText().toString().length()>0 && password.getText().toString().length()>0)
                {
                    //login
                    progress = new MaterialDialog.Builder(LoginActivity.this)
                            .content("Logging in....")
                            .cancelable(false)
                            .progress(true, 0)
                            .show();


                    mAuth.signInWithEmailAndPassword(username.getText().toString().replaceAll(" ",""), password.getText().toString())
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        Log.w(TAG, "signInWithEmail", task.getException());

                                        if (progress!=null && progress.isShowing())
                                            progress.dismiss();

                                        Snackbar.make(findViewById(R.id.rootView),"Unable to login",Snackbar.LENGTH_SHORT).show();

                                    }

                                    // ...
                                }
                            });
                }
                else
                {
                    if (username.getText().toString().length()==0 || !MyUtil.isEmailValid(username.getText().toString()))
                        usernameLayout.setError("Please enter email");
                    else
                        usernameLayout.setError(null);

                    if (password.getText().toString().length()<5 )
                        passwordLayout.setError("Password should be at least 5 characters long");
                    else
                        passwordLayout.setError(null);
                }


            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this,SignupActivity.class));

            }
        });


        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                forgotPassword();

            }
        });


        //init firebase auth


        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    getUserInfo(user.getUid());


                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");

                }

            }
        };


    }

    void forgotPassword()
    {
        new MaterialDialog.Builder(this)
                .title("Enter your email")
                .content("Please enter your email which associated with your account!")
                .positiveText("Ok")
                .negativeText("cancel")
                .inputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                .input("Enter email", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, final CharSequence input) {
                        // Do something

                        if (!MyUtil.isEmailValid(input.toString()))
                            Toast.makeText(LoginActivity.this, "Enter a valid email!", Toast.LENGTH_SHORT).show();
                        else
                        {

                            final MaterialDialog dialog1  = new MaterialDialog.Builder(LoginActivity.this)
                                    .content("Please wait...")
                                    .progress(true, 0)
                                    .show();

                            mAuth.sendPasswordResetEmail(input.toString().replaceAll(" ","")).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    dialog1.dismiss();

                                    if (task.isSuccessful())
                                    {
                                        new MaterialDialog.Builder(LoginActivity.this)
                                                .title("Password Reset")
                                                .content("Hi, we have sent an email on your email id "+input.toString()+" to reset the password. Please follow the link in the email in order to reset your password.")
                                                .positiveText("Ok")
                                                .show();
                                    }
                                    else
                                    {
                                        if (MyUtil.isNetworkAvailable(LoginActivity.this))
                                        {
                                            new MaterialDialog.Builder(LoginActivity.this)
                                                    .title("Password Reset")
                                                    .content("Hi, the email you entered does not belong to any valid account, please enter correct email.")
                                                    .positiveText("Ok")
                                                    .show();
                                        }
                                        else
                                        {
                                            new MaterialDialog.Builder(LoginActivity.this)
                                                    .title("Network Error")
                                                    .content("Looks like you are not connected to the internet, please try when you have a working data connection.")
                                                    .positiveText("Ok")
                                                    .show();
                                        }
                                    }

                                }
                            });

                            new MaterialDialog.Builder(LoginActivity.this)
                                    .title("Password Reset")
                                    .content("An email with the password reset link has been sent on your email id "+input.toString()+", please check your email for further instruction.")
                                    .positiveText("Ok")
                                    .show();
                        }



                    }
                }).show();


    }



    private void getUserInfo(String id)
    {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(id).addListenerForSingleValueEvent(

                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        try {
                            JSONObject object = new JSONObject();

                            object.put("name",dataSnapshot.child("name").getValue());
                            object.put("email",dataSnapshot.child("email").getValue());
                            object.put("country",dataSnapshot.child("country").getValue());
                            object.put("image",dataSnapshot.child("image").getValue());
                            object.put("username",dataSnapshot.child("username").getValue());
                            object.put("gender",dataSnapshot.child("gender").getValue());



                            MyUtil.saveUser(LoginActivity.this,object.toString());
                            MyUtil.savePref(LoginActivity.this,"image",dataSnapshot.child("image").getValue().toString());

                            if (progress!=null&&progress.isShowing())
                                progress.dismiss();

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("Tag", "getUser:onCancelled", databaseError.toException());
                    }
                });


//        user.getReference("")
    }


      public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }




    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


}
