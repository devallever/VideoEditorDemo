package com.allever.videoeditordemo.drag;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import com.allever.videoeditordemo.R;


public class DemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        DragLinearLayout dragLinearLayout = (DragLinearLayout) findViewById(R.id.container);
        HorizontalScrollView scrollView = findViewById(R.id.id_scroll);
        dragLinearLayout.setContainerScrollView(scrollView);
        // set all children draggable except the first (the header)
        for(int i = 1; i < dragLinearLayout.getChildCount(); i++){
            View child = dragLinearLayout.getChildAt(i);
            dragLinearLayout.setViewDraggable(child, child); // the child is its own drag handle
        }

        findViewById(R.id.noteDemoButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DemoActivity.this, NoteActivity.class));
            }
        });
    }
}
