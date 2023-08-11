package com.fpoly.assigment_mob403.Activity;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fpoly.assigment_mob403.Adapter.MessAdapter;
import com.fpoly.assigment_mob403.Adapter.StoryContentAdapter;
import com.fpoly.assigment_mob403.ContainAPI;
import com.fpoly.assigment_mob403.DTO.Comment;
import com.fpoly.assigment_mob403.DTO.Story;
import com.fpoly.assigment_mob403.Fragment.ReadStoryFragment;
import com.fpoly.assigment_mob403.GeneralFunc;
import com.fpoly.assigment_mob403.R;
import com.fpoly.assigment_mob403.ValuesSave;
import com.fpoly.assigment_mob403.databinding.ActivityDetailStoryBinding;
import com.fpoly.assigment_mob403.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailStory extends AppCompatActivity {
    private ActivityDetailStoryBinding binding;

    private List<Comment> commentList;
    private MessAdapter messAdapter;

    private Story story;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailStoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        HandleShow(true);

        String _id = getIntent().getStringExtra(ReadStoryFragment.KEYBUNDLE);

        Call<Story> call = ContainAPI.STORY().GetElement(_id);



        call.enqueue(new Callback<Story>() {
            @Override
            public void onResponse(Call<Story> call, Response<Story> response) {
                story = response.body();
                Init();
                HandleShow(false);
            }

            @Override
            public void onFailure(Call<Story> call, Throwable t) {

            }

        });


        AddAction();
    }

    private void HandleShow(boolean isShow){



            binding.actiDetailStoryBtnDel.setVisibility(View.VISIBLE);


    }

    private void Init(){
        ValuesSave.CURRENT_ID_STORY = story.get_id();
        GeneralFunc.LoadImageFromLink(story.getBackground(),binding.actiDetailStoryImgAvatar);
        binding.actiDetailStoryTvName.setText(story.getName());
        binding.actiDetailStoryTvAuthor.setText(story.getAuthor());
        binding.actiDetailStoryTvTimeRelase.setText(GeneralFunc.ConvertToStringDate(story.getTimeRelease()));
        long timeInMillis = convertDateToMillis(GeneralFunc.ConvertToStringDate(story.getTimeRelease()));
        binding.actiDetailStoryTvTimeRelase.setText(GeneralFunc.ConvertToStringDate(timeInMillis));
        binding.actiDetailStoryTvDescribeMini.setText(story.getDescribe());
        binding.actiDetailStoryRcImages.setLayoutManager(new LinearLayoutManager(this));
        StoryContentAdapter storyContentAdapter = new StoryContentAdapter();
        storyContentAdapter.SetData(story.getImages());
        binding.actiDetailStoryRcImages.setAdapter(storyContentAdapter);

        //mess
        commentList = new ArrayList<>();
        messAdapter = new MessAdapter();
        messAdapter.SetData(commentList);


    }

    private void AddAction() {


        binding.actiDetailStoryBtnReadStory.setOnClickListener(v -> ActionOnclickReadStory());
        binding.actiDetailStoryBtnDescribe.setOnClickListener(v -> ActionOnClickDescribe());
    }

    private void ActionOnclickReadStory(){
        binding.actiDetailStoryRcImages.setVisibility(View.VISIBLE);
        binding.actiDetailStoryTvDescribeMini.setVisibility(View.INVISIBLE);
        GeneralFunc.ChangeColorButton(binding.actiDetailStoryBtnReadStory,"#D9D9D9");
        GeneralFunc.ChangeColorButton(binding.actiDetailStoryBtnDescribe,"#FFFFFF");
    }

    private void ActionOnClickDescribe(){
        binding.actiDetailStoryTvDescribeMini.setVisibility(View.VISIBLE);
        binding.actiDetailStoryRcImages.setVisibility(View.INVISIBLE);

        GeneralFunc.ChangeColorButton(binding.actiDetailStoryBtnDescribe,"#D9D9D9");
        GeneralFunc.ChangeColorButton(binding.actiDetailStoryBtnReadStory,"#FFFFFF");
    }


    private long convertDateToMillis(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date dateObj = sdf.parse(date);
            if (dateObj != null) {
                return dateObj.getTime();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0; // Hoặc giá trị mặc định khác nếu cần
    }
}