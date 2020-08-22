package com.neo.androidkeyboardinputs;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.neo.androidkeyboardinputs.models.Message;
import com.neo.androidkeyboardinputs.models.User;
import com.neo.androidkeyboardinputs.util.Messages;

import java.util.ArrayList;


public class MessagesRecyclerViewAdapter extends RecyclerView.Adapter<MessagesRecyclerViewAdapter.ViewHolder> implements Filterable {

    private static final String TAG = "ConnectionsAdapter";

    //vars
    private ArrayList<User> mUsers = new ArrayList<>();                             // list of all users
    private ArrayList<Message> mFilteredMessages = new ArrayList<>();               // holds list of messages and users assoc with it based on searchQuery if no query ret all messages
    private ArrayList<Message> mMessages = new ArrayList<>();                       // holds list of messages and users assoc with it
    //    private ArrayList<User> mFilteredUsers = new ArrayList<>();                   // list of filtered users based on searchQuery if any else same as all users
    private Context mContext;
    private IMainActivity mInterface;


    public MessagesRecyclerViewAdapter(Context context, ArrayList<User> users) {
        mContext = context;
        mUsers = users;

        setMessages();
    }


    /**
     * sets the messages to the list of message field
     */
    private void setMessages() {
        for (int i = 0; i < mUsers.size(); i++) {
            // create a message for each user and assoc user with the msg
            mMessages.add(new Message(mUsers.get(i), Messages.MESSAGES[i]));
        }
        mFilteredMessages = mMessages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_messages_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        final User user = mFilteredMessages.get(position).getUser();
        final String message = mFilteredMessages.get(position).getMessage();

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background);

        Glide.with(mContext)
                .load(user.getProfile_image())
                .apply(requestOptions)
                .into(holder.image);

        holder.name.setText(user.getName());
        holder.message.setText(message);                //generate a message, that's associated with user in focus

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + user.getName());

                mInterface.onMessageSelected(new Message(user, Messages.MESSAGES[position]));
            }
        });
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mFilteredMessages = mMessages;
                } else {
                    ArrayList<Message> filteredList = new ArrayList<>();
                    for(int i = 0; i < mMessages.size(); i++){
                        // checks if the message in focus or user in focus has char sequence passed in search, and filter by user or message
                        if(mMessages.get(i).getMessage().toLowerCase().contains(charString.toLowerCase())){
                            filteredList.add(mMessages.get(i));
                        } else if(mUsers.get(i).getName().toLowerCase().contains(charString.toLowerCase())){
                            filteredList.add(mMessages.get(i));
                        }
                    }
                    mFilteredMessages = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredMessages;
                return filterResults;                                   // after ret filterResults with val, publishResults is called
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredMessages = (ArrayList<Message>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mInterface = (IMainActivity) mContext;
    }

    @Override
    public int getItemCount() {
        return mFilteredMessages.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name;
        TextView message;
        TextView reply;
        RelativeLayout parent;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            message = itemView.findViewById(R.id.message);
            reply = itemView.findViewById(R.id.reply);
            parent = itemView.findViewById(R.id.parent_view);
        }
    }
}