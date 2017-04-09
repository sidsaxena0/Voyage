package com.sid.voyage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sid.voyage.utils.MyUtil;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SignupActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    TextInputLayout usernameLayout,nameLayout,passwordLayout,confirmPasswordLayout,emailLayout;
    EditText username,name,password,confirmPassword,email;
    boolean _username,_name,_password,_confirmPassword,_email;
    Button signup;
    MaterialDialog progress;
    RadioGroup gender;
    String TAG = "Register";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    boolean isUserChecked;
    String usrGender="na";
    AppCompatSpinner spinner;
    ArrayList<String> countries = new ArrayList<>();
    TextView bday;
    int age = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //init views
        usernameLayout = (TextInputLayout)findViewById(R.id.username_layout);
        passwordLayout = (TextInputLayout)findViewById(R.id.password_layout);
        nameLayout = (TextInputLayout)findViewById(R.id.name_layout);
        confirmPasswordLayout = (TextInputLayout)findViewById(R.id.cnf_password_layout);
        emailLayout = (TextInputLayout)findViewById(R.id.email_layout);
        spinner = (AppCompatSpinner)findViewById(R.id.countries);
        gender = (RadioGroup)findViewById(R.id.gender);
        bday = (TextView)findViewById(R.id.bday);

        username = (EditText)findViewById(R.id.username);
        name = (EditText)findViewById(R.id.name);
        password = (EditText)findViewById(R.id.password);
        confirmPassword = (EditText)findViewById(R.id.cnf_password);
        email = (EditText)findViewById(R.id.email);

        signup = (Button)findViewById(R.id.signup);

        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                if (checkedId==R.id.male)
                {
                    usrGender = "Male";
                }
                else if (checkedId == R.id.female)
                {
                    usrGender = "Female";
                }

            }
        });


        String[] locales = Locale.getISOCountries();
        for (String countryCode : locales) {

            Locale obj = new Locale("", countryCode);
            countries.add(obj.getDisplayCountry());

        }



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,countries);
        spinner.setAdapter(adapter);


        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().length()>4)
                {
                    usernameLayout.setError(null);
                    _username = true;
                }
                else
                    usernameLayout.setError("Username should be 5 character long");

            }
        });


        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (MyUtil.isEmailValid(s.toString()))
                {
                    emailLayout.setError(null);
                    _email = true;
                }
                else
                    emailLayout.setError("Please enter a valid email");

            }
        });

        bday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        SignupActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Select your birthday");

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().length()>4)
                {
                    passwordLayout.setError(null);
                    _password = true;
                }
                else
                    passwordLayout.setError("Password should be 5 character long");


            }
        });

        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().equals(password.getText().toString()))
                {
                    confirmPasswordLayout.setError(null);
                    _confirmPassword = true;
                }
                else
                    confirmPasswordLayout.setError("Passwords do not match");


            }
        });


        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().length()>3)
                {
                    nameLayout.setError(null);
                    _name = true;
                }
                else
                    nameLayout.setError("Please enter a valid name");

            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Error","Confrm "+_confirmPassword+" Pass "+_password+ "Usename "+_username+" gender "+usrGender+" Spinnrer "+spinner.getSelectedItemPosition()+"Age "+ age);

                if (_confirmPassword&&_email&&_name && _password && _username && !usrGender.equalsIgnoreCase("na") && spinner.getSelectedItemPosition()>0 && age>0)
                {
                    //Signup
                    checkUser();
                }
                else
                {
                    if (usrGender.equalsIgnoreCase("na"))
                        Toast.makeText(SignupActivity.this, "Please choose your gender", Toast.LENGTH_SHORT).show();
                    else if (age==0)
                        Toast.makeText(SignupActivity.this, "Please select your bday!", Toast.LENGTH_SHORT).show();
                    else
                    {
                        Log.d("Error","Confrm "+_confirmPassword+" Pass "+_password+ "Usename "+_username+" gender "+usrGender);
                        Toast.makeText(SignupActivity.this, "Please check form for errors!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });



        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();


                    mDatabase.child("users").child(user.getUid()).child("id").setValue(user.getUid());
                    mDatabase.child("users").child(user.getUid()).child("name").setValue(name.getText().toString());
                    mDatabase.child("users").child(user.getUid()).child("email").setValue(email.getText().toString());
                    mDatabase.child("users").child(user.getUid()).child("username").setValue(username.getText().toString());
                    mDatabase.child("users").child(user.getUid()).child("image").setValue("no_image");
                    mDatabase.child("users").child(user.getUid()).child("gender").setValue(usrGender);
                    mDatabase.child("users").child(user.getUid()).child("country").setValue(spinner.getSelectedItem().toString());
                    mDatabase.child("users").child(user.getUid()).child("bday").setValue(bday.getText().toString());
                    mDatabase.child("users").child(user.getUid()).child("age").setValue(age);



                    Toast.makeText(SignupActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignupActivity.this,LoginActivity.class));



                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==android.R.id.home)
        {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void checkUser()
    {

        progress = new MaterialDialog.Builder(this)
                .title("Checking username")
                .content("Please wait while checking availability of username....")
                .progress(true, 0)
                .show();


        isUserChecked = false;

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabase.child("users").orderByChild("username").equalTo(username.getText().toString());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren())
                {
                    if (!isUserChecked)
                    {
                        isUserChecked = true;
                        Toast.makeText(SignupActivity.this, "Username already exist!", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    if (!isUserChecked)
                    {
                        isUserChecked = true;
                        signup();
                    }

                }

                try{

                    progress.dismiss();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


                try{

                    progress.dismiss();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });


    }

    void signup()
    {

        progress = new MaterialDialog.Builder(this)
                .title("Creating account")
                .content("Please wait while creating your account....")
                .progress(true, 0)
                .show();



        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.

                        if (progress!=null&&progress.isShowing())
                            progress.dismiss();

                        if (!task.isSuccessful())
                        {

                            //Error
                            new MaterialDialog.Builder(SignupActivity.this)
                                    .title("Signup error")
                                    .content("Hi, we encountered an error while creating your account, please try again later and make sure your account does not exist, if you lost your password try forgot password!")
                                    .positiveText("Retry")
                                    .negativeText("cancel")
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                            //

                                        }
                                    })
                                    .show();
                        }


                    }
                });
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


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = +dayOfMonth+"-"+(monthOfYear+1)+"-"+year;
        bday.setText(date);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Log.d("Formt","Date "+year+"-"+monthOfYear+"-"+dayOfMonth);
            Date date1 = format.parse(year+"-"+monthOfYear+"-"+dayOfMonth);
            age = MyUtil.getDiffYears(date1,new Date());
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
