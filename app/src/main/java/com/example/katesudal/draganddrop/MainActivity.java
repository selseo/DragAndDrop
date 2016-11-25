package com.example.katesudal.draganddrop;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;


public class MainActivity extends AppCompatActivity implements Realm.Transaction {

    public static final String GROUP_NAME_RESULT = "tt";
    private static final String TAG = "dddd";
    String nameList[] = {"Bob","Anny","Lantern","Gilbert","Robert",
            "Angle","Fisher","Bernard","Poppin","Nick",
            "Hannibal","Lucifer","David","Rob","William"};
    private ViewGroup containerA;
    private ViewGroup containerB;
    private ViewGroup containerC;
    private Party party;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setTitle("");
        setSupportActionBar(myToolbar);
        ViewGroup itemLayout= (ViewGroup) findViewById(R.id.itemContainer);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        containerA = (ViewGroup) findViewById(R.id.containerA);
        containerB = (ViewGroup) findViewById(R.id.containerB);
        containerC = (ViewGroup) findViewById(R.id.containerC);

        createItem(itemLayout, inflater);
        setListener(itemLayout);
    }

    private void setListener(ViewGroup itemLayout) {
        containerA.setOnDragListener(new OnDragItem());
        containerB.setOnDragListener(new OnDragItem());
        containerC.setOnDragListener(new OnDragItem());
        itemLayout.setOnDragListener(new OnDragItem());
    }

    private void createItem(ViewGroup itemLayout, LayoutInflater inflater) {
        for(int i=0;i<nameList.length;i++){
            View itemView = inflater.inflate(R.layout.item_name, null);
            TextView itemName = (TextView) itemView.findViewById(R.id.textViewNameItem);
            itemName.setText(nameList[i]);
            itemLayout.addView(itemView);
            itemView.setOnTouchListener(new OnTouchItem());
            if(i%2==0) itemView.setBackgroundColor(Color.parseColor("#29b5db"));
        }
    }


    public void submit(View view){
        Party party = new Party();
        DateFormat df = new SimpleDateFormat("d MMM");
        String date = df.format(Calendar.getInstance().getTime());
        party.setDate(date);
        RealmList<Section> sections = new RealmList<>();
        String text = getNameFromContainer(containerA, "");
        RealmList<Human> humansA = new RealmList<>();
        String []nameList=text.split(" ");
        for (String name: nameList) {
            Human human = new Human(name);
            humansA.add(human);
        }
        Section sectionA = new Section();
        sectionA.setSectionName("A");
        sectionA.setMemberList(humansA);
        sections.add(sectionA);
        
        text = getNameFromContainer(containerB, "");
        nameList=text.split(" ");
        RealmList<Human> humansB = new RealmList<>();
        for (String name: nameList) {
            Human human = new Human(name);
            humansB.add(human);
        }
        Section sectionB = new Section();
        sectionB.setSectionName("B");
        sectionB.setMemberList(humansB);
        sections.add(sectionB);

        text = getNameFromContainer(containerC, "");
        nameList=text.split(" ");
        RealmList<Human> humansC = new RealmList<>();
        for (String name: nameList) {
            if(name.isEmpty()) continue;
            Human human = new Human(name);
            humansC.add(human);
        }
        Section sectionC = new Section();
        sectionC.setSectionName("C");
        sectionC.setMemberList(humansC);
        sections.add(sectionC);

        party.setSectionList(sections);

        PreferencesService.savePreferences("Party", party, this);
        createRealm(party);
        Intent intent = new Intent(this, ShowResult.class);
        startActivity(intent);
    }

    public void createRealm(Party party) {
        this.party = party;
        Realm.init(getApplicationContext());
        Realm realm = Realm.getDefaultInstance();
        this.execute(realm);

        RealmResults<Party> results = realm.where(Party.class)
                .contains("date", party.getDate())
                .findAll();

        Log.d("ZZZZ" , results.toString());
    }

    @Override
    public void execute(Realm realm) {

        realm.beginTransaction();
//        Party partyRealm = realm.createObject(Party.class);
//        partyRealm.setDate(party.getDate());
//        partyRealm.setSectionList(new RealmList<Section>());
//        for(int i=0; i<party.getSectionList().size(); i++) {
//            Section section = party.getSectionList().get(i);
//            partyRealm.getSectionList().add(section);
//        }
        realm.copyToRealm(party);
        realm.commitTransaction();

        RealmResults<Party> parties = realm.where(Party.class).findAll();
        Log.i(TAG, "execute: " + parties.toString());
        for(int i=0; i<parties.size(); i++) {
            Party party = parties.get(i);
            for (int j = 0; j<party.getSectionList().size(); j++) {
                Section section = party.getSectionList().get(j);
                Log.i(TAG, section.getSectionName() + " : " + section.getMemberList().size());
                for(int k=0; k<section.getMemberList().size(); k++) {
                    Human human = section.getMemberList().get(k);
                    Log.i(TAG,  "=========>" + human.getName());
                }
            }
        }
    }

    private String getNameFromContainer(ViewGroup containerName, String text) {
        for(int i=0 ; i<containerName.getChildCount();i++){
            View rootContainerView = containerName.getChildAt(i);
            TextView textViewNameItem = (TextView) rootContainerView.findViewById(R.id.textViewNameItem);
            String name = (String) textViewNameItem.getText();
            text = text +name+" ";
        }
        return text;
    }

    class OnTouchItem implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent arg1) {
            ClipData data = ClipData.newPlainText("", "");
            DragShadowBuilder shadow = new DragShadowBuilder(v);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                v.startDragAndDrop(data, shadow, v, 0);
            } else {
                v.startDrag(data, shadow, v, 0);
            }
            v.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    class OnDragItem implements View.OnDragListener{

        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {
            final int action = dragEvent.getAction();
            switch(action) {

                case DragEvent.ACTION_DRAG_STARTED:
                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    break;

                case DragEvent.ACTION_DRAG_ENTERED:
                    break;

                case DragEvent.ACTION_DROP:{
                    View viewState = (View) dragEvent.getLocalState();
                    ViewGroup viewgroup = (ViewGroup) viewState.getParent();
                    viewgroup.removeView(viewState);
                    ViewGroup containView = (ViewGroup) view;
                    containView.addView(viewState);
                    viewState.setVisibility(View.VISIBLE);
                    break;
                }
                case DragEvent.ACTION_DRAG_ENDED:{
                    return(true);
                }
                default:
                    break;
            }
            return true;
        }
    }

}
