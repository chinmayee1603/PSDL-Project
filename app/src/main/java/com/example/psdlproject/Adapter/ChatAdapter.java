package com.example.psdlproject.Adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.psdlproject.Models.MessageModel;
import com.example.psdlproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Context;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter{

    ArrayList<MessageModel> messagemodels;
    Context context;
    int SENDER_VIEW_TYPE=1;
    int RECEIVER_VIEW_TYPE=2;
    String recId;
    public ChatAdapter(ArrayList<MessageModel> messagemodels, Context context) {
        this.messagemodels = messagemodels;
        this.context = context;
    }

    public ChatAdapter(ArrayList<MessageModel> messagemodels, Context context, String recId) {
        this.messagemodels = messagemodels;
        this.context = context;
        this.recId = recId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == SENDER_VIEW_TYPE){
            View vieww= LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
            return new SenderViewHolder(vieww);
        }else{
            View vieww= LayoutInflater.from(context).inflate(R.layout.sample_receiver,parent,false);
            return new SenderViewHolder(vieww);
        }
    }
    @Override
    public int getItemViewType(int position) {
        if(messagemodels.get(position).getuId().equals(FirebaseAuth.getInstance().getUid())){
            return SENDER_VIEW_TYPE;
        }else{
            return RECEIVER_VIEW_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            MessageModel messageModel=messagemodels.get(position);

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    new AlertDialog.Builder(context).setTitle("Delete")
                            .setMessage("Are you sure you want to delete this message?")
                            .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    FirebaseDatabase database=FirebaseDatabase.getInstance();
                                    String senderRoom=FirebaseAuth.getInstance().getUid()+recId;
                                    database.getReference().child("chats").child(senderRoom).child(messageModel.getMessageId()).setValue(null);
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();


                    return false;
                }
            });
            if(holder.getClass()==SenderViewHolder.class){
                ((SenderViewHolder)holder).senderMsg.setText(messageModel.getMessage());
            }else{
                ((ReceiverViewHolder)holder).receiverMsg.setText(messageModel.getMessage());
            }
    }

    @Override
    public int getItemCount() {
        return messagemodels.size();
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder{
        TextView receiverMsg,receiverTime;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverMsg=itemView.findViewById(R.id.receivertext);
            receiverTime=itemView.findViewById(R.id.receiverTime);

        }
    }
    public class SenderViewHolder extends RecyclerView.ViewHolder{
        TextView senderMsg,senderTime;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMsg=itemView.findViewById(R.id.sendertext);
            senderTime=itemView.findViewById(R.id.sendertime);

        }
    }
}
