package com.example.interactivestory.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.interactivestory.R;
import com.example.interactivestory.data.Choice;
import com.example.interactivestory.data.Page;
import com.example.interactivestory.data.Story;

import java.util.Stack;

public class StoryActivity extends AppCompatActivity {

    private Story story;
    private ImageView storyImageView;
    private TextView storyTextView;
    private Button choice1Button;
    private Button choice2Button;
    private String name;
    private Stack<Integer> pageStack = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        setup();
        loadPage(0);
    }

    private void setup() {
        story = new Story();
        storyImageView = findViewById(R.id.storyImageView);
        storyTextView = findViewById(R.id.storyTextView);
        choice1Button = findViewById(R.id.choice1Button);
        choice2Button = findViewById(R.id.choice2Button);

        Intent intent = getIntent();
        name = intent.getStringExtra(getString(R.string.key_name));
        if (name == null || name.isEmpty()) {
            name = "Friend";
        }
    }

    private void loadPage(int pageNumber) {
        pageStack.push(pageNumber);

        Page currentPage = story.getPage(pageNumber);

        Drawable drawable = ContextCompat.getDrawable(this, currentPage.getImageId());
        storyImageView.setImageDrawable(drawable);

        String text = String.format(getString(currentPage.getTextId()), name);
        storyTextView.setText(text);

        if (currentPage.isFinalPage()) {
            choice1Button.setVisibility(View.INVISIBLE);
            configureButton(choice2Button, null);
            return;
        }

        configureButton(choice1Button, currentPage.getChoice1());
        configureButton(choice2Button, currentPage.getChoice2());
    }

    private void configureButton(Button button, @Nullable final Choice choice) {
        button.setVisibility(View.VISIBLE);

        if (choice == null) {
            button.setText(R.string.play_again_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadPage(0);
                }
            });
            return;
        }

        button.setVisibility(View.VISIBLE);
        button.setText(getString(choice.getTextId()));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPage(choice.getNextPage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        pageStack.pop();
        if (pageStack.isEmpty()) {
            super.onBackPressed();
        } else {
            loadPage(pageStack.pop());
        }
    }
}
