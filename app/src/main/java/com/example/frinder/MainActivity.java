package com.example.frinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frinder.Cards.arrayAdapter;
import com.example.frinder.Cards.cards;
import com.example.frinder.Matches.MatchesActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private cards cards_data[];
    private com.example.frinder.Cards.arrayAdapter arrayAdapter;
    private int i;
    int userIndex = 0;
    private TextView mUserName;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseAuth mAuth;
    private String currentUid;
    private DatabaseReference usersDb;
    private Boolean option0, option1, option2, option3, option4, option5, option6, option7, option8, option9;
    private Boolean option0User2, option1User2, option2User2, option3User2, option4User2, option5User2, option6User2, option7User2, option8User2, option9User2;
    private RelativeLayout mLoadingLayout;
    private ProgressBar mProgressBar;

    List<Boolean> options = new ArrayList<>();
    List<Boolean> optionsUser2 = new ArrayList<>();
    ListView listView;
    List<cards> rowItems;
    Map<String, Integer> usersMap = new HashMap<>();
    List<String> usersListSorted = new LinkedList<>();
    LinkedHashMap<String, String> sortedMap = new LinkedHashMap<>();
    ArrayList<String> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUserName = findViewById(R.id.userName);
        mAuth = FirebaseAuth.getInstance();
        currentUid = mAuth.getCurrentUser().getUid();
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");

        mProgressBar = findViewById(R.id.progress_bar);

        Handler handler = new Handler();


        getUserOptions();
        rowItems = new ArrayList<cards>();
        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems);

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        flingContainer.setAdapter(arrayAdapter);


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Aquí va el código que quieres ejecutar después de X segundos


                        for (String e: usersListSorted)
                            System.out.println(e);
                System.out.println("Han pasado X segundos");
                mProgressBar.setVisibility(View.INVISIBLE);

                makeCards();
            }
        }, 10 * 1000);


        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                cards obj = (cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(userId).child("connections").child("no").child(currentUid).setValue(true);
                Toast.makeText(MainActivity.this, "no...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                cards obj = (cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(userId).child("connections").child("yes").child(currentUid).setValue(true);
                isMatch(userId);
                Toast.makeText(MainActivity.this, "YESSS!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void isMatch(String userId) {
        DatabaseReference currentUserConnectionDb = usersDb.child(currentUid).child("connections").child("yes").child(userId);
        currentUserConnectionDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(MainActivity.this, "Match!", Toast.LENGTH_SHORT).show();

                    String key = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();

                    usersDb.child(snapshot.getKey()).child("connections").child("matches").child(currentUid).child("ChatId").setValue(key);
                    usersDb.child(currentUid).child("connections").child("matches").child(snapshot.getKey()).child("ChatId").setValue(key);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String userSex;
    private String oppositeUserSex;

    public void getUserOptions() {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userDb = usersDb.child(user.getUid());
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mUserName.setText("Bienvenid@\n" + dataSnapshot.child("name").getValue().toString());
                    options.add( option0=dataSnapshot.child("option0").getValue(Boolean.class));
                    options.add(option1=dataSnapshot.child("option1").getValue(Boolean.class));
                    options.add(option2=dataSnapshot.child("option2").getValue(Boolean.class));
                    options.add(option3=dataSnapshot.child("option3").getValue(Boolean.class));
                    options.add(option4=dataSnapshot.child("option4").getValue(Boolean.class));
                    options.add(option5=dataSnapshot.child("option5").getValue(Boolean.class));
                    options.add(option6=dataSnapshot.child("option6").getValue(Boolean.class));
                    options.add(option7=dataSnapshot.child("option7").getValue(Boolean.class));
                    options.add(option8=dataSnapshot.child("option8").getValue(Boolean.class));
                    options.add(option9=dataSnapshot.child("option9").getValue(Boolean.class));


                    getUser2Options();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }

    public void getUser2Options() {

        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                optionsUser2.clear();

                optionsUser2.add(option0User2=snapshot.child("option0").getValue(Boolean.class));
                optionsUser2.add(option1User2=snapshot.child("option1").getValue(Boolean.class));
                optionsUser2.add(option2User2=snapshot.child("option2").getValue(Boolean.class));
                optionsUser2.add(option3User2=snapshot.child("option3").getValue(Boolean.class));
                optionsUser2.add(option4User2=snapshot.child("option4").getValue(Boolean.class));
                optionsUser2.add(option5User2=snapshot.child("option5").getValue(Boolean.class));
                optionsUser2.add(option6User2=snapshot.child("option6").getValue(Boolean.class));
                optionsUser2.add(option7User2=snapshot.child("option7").getValue(Boolean.class));
                optionsUser2.add(option8User2=snapshot.child("option8").getValue(Boolean.class));
                optionsUser2.add(option9User2=snapshot.child("option9").getValue(Boolean.class));



                int count = 0;
                for (int i = 0; i < options.size(); i++) {
                    if (options.get(i).equals(optionsUser2.get(i))) {
                        count++;
                    }
                }
                String snapshotKey = snapshot.getKey();
                usersMap.put(snapshotKey,count);

              sortUsers();




/*


                  if (snapshot.exists() && !snapshot.child("connections").child("no").hasChild(currentUid) && !snapshot.child("connections").child("yes").hasChild(currentUid)) {

                        String name = snapshot.child("name").getValue() != null ? snapshot.child("name").getValue().toString() : "Nombre no disponible";
                        String age = snapshot.child("age").getValue() != null ? snapshot.child("age").getValue().toString() : "";
                        String city = snapshot.child("city").getValue() != null ? snapshot.child("city").getValue().toString() : "";
                        String profileImageUrl = (snapshot.child("profileImageUrl").getValue() != null && !snapshot.child("profileImageUrl").getValue().equals("default")) ? snapshot.child("profileImageUrl").getValue().toString() : "https://firebasestorage.googleapis.com/v0/b/frinder-2bd56.appspot.com/o/currentImage%2Fdescarga.jpeg?alt=media&token=a3a8c9de-c706-4c5a-b4a5-36251b1063aa";


                        cards item = new cards(snapshot.getKey(), name, age, city, profileImageUrl);
                        rowItems.add(item);
                        arrayAdapter.notifyDataSetChanged();
                    }
                    */

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });


    }

    public void sortUsers() {
        usersListSorted.clear();
        Object[] a = usersMap.entrySet().toArray();
        Arrays.sort(a, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Map.Entry<String, Integer>) o2).getValue()
                        .compareTo(((Map.Entry<String, Integer>) o1).getValue());
            }

        });
        for (Object e : a) {

                usersListSorted.add(((Map.Entry<String, Integer>) e).getKey());

        }

    }


    public void makeCards(){
        usersListSorted.remove(currentUid);
        for (String key : usersListSorted ) {

            DatabaseReference userToCard = FirebaseDatabase.getInstance().getReference().child("Users").child(key);

            userToCard.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    // Lee el valor del snapshot

                    if (snapshot.exists() && !snapshot.child("connections").child("no").hasChild(currentUid) && !snapshot.child("connections").child("yes").hasChild(currentUid)) {

                        String name = snapshot.child("name").getValue() != null ? snapshot.child("name").getValue().toString() : "Nombre no disponible";
                        String age = null;
                        try {
                            age = String.valueOf(calculateAge(snapshot.child("age").getValue() != null ? snapshot.child("age").getValue().toString() : "02-01-2023"));
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        String city = snapshot.child("city").getValue() != null ? snapshot.child("city").getValue().toString() : "Valencia";
                        String profileImageUrl = (snapshot.child("profileImageUrl").getValue() != null && !snapshot.child("profileImageUrl").getValue().equals("default")) ? snapshot.child("profileImageUrl").getValue().toString() : "https://firebasestorage.googleapis.com/v0/b/frinder-2bd56.appspot.com/o/currentImage%2Fdescarga.jpeg?alt=media&token=a3a8c9de-c706-4c5a-b4a5-36251b1063aa";


                        cards item = new cards(snapshot.getKey(), name, age, city, profileImageUrl);
                        rowItems.add(item);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

    }

    public void logoutUser(View view) {
        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this, ChooseLoginRegistrationActivity.class);
        startActivity(intent);
        finish();
        return;
    }


    public void goSettings(View view) {

        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
        return;
    }

    public void goMatches(View view) {

        Intent intent = new Intent(MainActivity.this, MatchesActivity.class);
        startActivity(intent);
        return;
    }
    private int calculateAge(String birthString) throws ParseException {

        // Convertir el birthString a un objeto Calendar
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar birthDate = Calendar.getInstance();
        birthDate.setTime(dateFormat.parse(birthString));

// Calcular la edad del usuario
        Calendar currentDate = Calendar.getInstance();
        int age = currentDate.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
        return age;
    }
    @Override
    protected void onResume() {
        super.onResume();
//        getUserOptions();
    }

}