package com.example.katesudal.draganddrop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class ShowResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setTitle("");
        setSupportActionBar(myToolbar);

        Party party = (Party) PreferencesService.getPreferences("Party",Party.class,this);
        String result = "";
        for(Section section: party.getSectionList()){
            String sectionName = section.getSectionName();
            String sectionList = "";
            for(Human human: section.getMemberList()){
                sectionList = sectionList+human.getName()+" ";
            }
            result = result+sectionName+" "+sectionList+"\n";
        }
        TextView textViewResult = (TextView) findViewById(R.id.textViewResult);
        textViewResult.setText(result);
    }
}
