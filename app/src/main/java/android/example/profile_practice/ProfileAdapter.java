package android.example.profile_practice;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.example.profile_practice.Model.Profile;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {
    Context context;
    List<Profile> profileList;

    public static class ProfileViewHolder extends RecyclerView.ViewHolder {
        private ImageView profileImage;
        private TextView nameTv;
        private TextView ageTv;
        private TextView genderTv;

        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage_iv);
            nameTv = itemView.findViewById(R.id.name_tv);
            ageTv = itemView.findViewById(R.id.age_tv);
            genderTv = itemView.findViewById(R.id.gender_tv);
        }
    }

    public ProfileAdapter(Context context, List<Profile> profileList) {
        this.context = context;
        this.profileList = profileList;
    }

    @NonNull
    @Override
    public ProfileAdapter.ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_item, parent, false);

        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter.ProfileViewHolder holder, int position) {
        final Profile profile = profileList.get(position);
        holder.nameTv.setText(profile.getName());
        holder.ageTv.setText(String.valueOf(profile.getAge()));
        holder.genderTv.setText(showGender(profile.getGender()));
        holder.profileImage.setImageResource(R.drawable.ic_launcher_background);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditorActivity.class);
                intent.putExtra("id", profile.getId());
                context.startActivity(intent);
            }
        });
    }

    private String showGender(int gender) {
        String genderString;
        switch (gender) {
            case 0:
                genderString = "Unknown";
                return genderString;
            case 1:
                genderString = "Male";
                return genderString;
            case 2:
                genderString = "Female";
                return genderString;
        }
        return "Unknown";
    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }
}
