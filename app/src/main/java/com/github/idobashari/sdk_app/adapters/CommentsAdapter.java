package com.github.idobashari.sdk_app.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.idobashari.rating_sdk.models.Comment;
import com.github.idobashari.sdk_app.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {
    private static final String TAG = "CommentsAdapter";
    private final List<Comment> comments = new ArrayList<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    private String currentUserId;
    private CommentActionListener actionListener;

    // ממשק לטיפול בפעולות על תגובות
    public interface CommentActionListener {
        void onEditComment(Comment comment);
        void onDeleteComment(Comment comment);
    }

    // setters
    public void setCurrentUserId(String userId) {
        this.currentUserId = userId;
        notifyDataSetChanged();
    }

    public void setActionListener(CommentActionListener listener) {
        this.actionListener = listener;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.bind(comment);
        Log.d(TAG, "Binding comment at position " + position);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void setComments(List<Comment> newComments) {
        comments.clear();
        if (newComments != null) {
            comments.addAll(newComments);
        }
        notifyDataSetChanged();
        Log.d(TAG, "Updated comments list. Size: " + comments.size());
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        private final TextView contentView;
        private final TextView dateView;
        private final ImageButton editButton;
        private final ImageButton deleteButton;
        private final View actionButtons;

        CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            contentView = itemView.findViewById(R.id.commentContent);
            dateView = itemView.findViewById(R.id.commentDate);
            editButton = itemView.findViewById(R.id.editCommentButton);
            deleteButton = itemView.findViewById(R.id.deleteCommentButton);
            actionButtons = itemView.findViewById(R.id.actionButtons);
        }

        void bind(Comment comment) {
            contentView.setText(comment.getContent());

            if (comment.getCreatedAt() != null) {
                dateView.setText(dateFormat.format(comment.getCreatedAt()));
            }

            // בדיקה האם התגובה שייכת למשתמש הנוכחי
            boolean isCurrentUserComment = currentUserId != null &&
                    currentUserId.equals(comment.getUserId());
            actionButtons.setVisibility(isCurrentUserComment ? View.VISIBLE : View.GONE);

            if (isCurrentUserComment) {
                editButton.setOnClickListener(v -> {
                    if (actionListener != null) {
                        actionListener.onEditComment(comment);
                    }
                });

                deleteButton.setOnClickListener(v -> {
                    if (actionListener != null) {
                        actionListener.onDeleteComment(comment);
                    }
                });
            }

            Log.d(TAG, "Comment bound. User ID: " + comment.getUserId() +
                    ", Current User: " + currentUserId +
                    ", Is Owner: " + isCurrentUserComment);
        }
    }
}