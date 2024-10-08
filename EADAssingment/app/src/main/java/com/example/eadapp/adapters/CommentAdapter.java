package com.example.eadapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.eadapp.R;
import com.example.eadapp.data.Comment;
import java.util.List;
import android.util.Log;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> commentList;
    private static final String TAG = "CommentAdapter";

    public void setComments(List<Comment> comments) {
        this.commentList = comments;
        Log.i(TAG, "comments :: " + comments);
        notifyDataSetChanged(); // Refresh RecyclerView when comments are updated
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.commentText.setText(comment.getCommentText());
        holder.commentRating.setText("Rating: " + comment.getRating());
    }

    @Override
    public int getItemCount() {
        return commentList != null ? commentList.size() : 0;
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commentText, commentRating;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentText = itemView.findViewById(R.id.comment_text);
            commentRating = itemView.findViewById(R.id.comment_rating);
        }
    }
}

