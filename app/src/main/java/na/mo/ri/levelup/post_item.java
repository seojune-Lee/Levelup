package na.mo.ri.levelup;
import java.util.ArrayList;
import java.util.Date;

import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import static java.lang.Math.toIntExact;


public class post_item extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<comment_item> comment_listArrayList;
    private String groupname;
    private int heart_cnt=0;
    private int comment_cnt=1;
    private String title;
    private int icon=0; // 글쓴이 아이콘 ( 글에 저장되어있음)
    private String name;
    private TextView name_textview;
    private TextView date_dateview;
    private TextView title_textview;
    private TextView post_comment_count;
    private int content_image;// 글의 이미지
    private String date;
    private TextView post_content;
    private TextView post_heart_count;
    private Button heartbutton;
    private Button comment_submitbutton;
    private String comment_mycontent;
    private String comment_content;
    private ImageView content_imageview;
    private ListView comment_list;
    CommentAdapter commentListAdapter;

    DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("community").child("1").child("post").child("1");
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("community").child("1").child("post").child("1");
    //DatabaseReference postRef=myRef.child("1");//"test는 글제목dlfknrnskd
    ValueEventListener postListener = new ValueEventListener() {
        @Override

        public void onDataChange(DataSnapshot dataSnapshot) {
            // Get Post object and use the values to update the U
            //System.out.println(dataSnapshot.child("comment").child("1").child("comment_content").getValue());
            date=(String)dataSnapshot.child("date").getValue();
            comment_cnt=toIntExact(dataSnapshot.child("comment").getChildrenCount());
            icon=Integer.parseInt((String)dataSnapshot.child("icon").getValue());
            title_textview.setText((String)dataSnapshot.child("title").getValue());
            name=(String)dataSnapshot.child("name").getValue();
            date_dateview.setText(date);
            heart_cnt=Integer.parseInt((String)dataSnapshot.child("heart_count").getValue());
            post_content.setText((String)dataSnapshot.child("content").getValue());


            for(int i=1;i<=comment_cnt;i++){
                comment_item temp=new comment_item(1,"temp");
                temp.setComment_content((String)dataSnapshot.child("comment").child(Integer.toString(i)).child("comment_content").getValue());
                temp.setComment_image(Integer.parseInt((String)dataSnapshot.child("comment").child(Integer.toString(i)).child("comment_img").getValue()));
                comment_listArrayList.add(temp);
                /*
                comment_listArrayList.add(new comment_item(
                        dataSnapshot.child("comment").child("comment_img").getValue().toString(),
                        dataSnapshot.child("comment").child("comment_context").getValue().toString(),
                        dataSnapshot.child("comment").child("comment_id").getValue().));
                        */
            }

            // ...

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w("post_item", "loadPost:onCancelled", databaseError.toException());
            // ...
        }
    };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);
        //postRef.child("comment").child("3").push().setValue(new comment_item(R.mipmap.ic_launcher, "fdsfa@navet.com", "dddddddd"));
        name="fdsa";
        icon=0;
        postRef.addListenerForSingleValueEvent(postListener);
        groupname="Testgroup";
        comment_list=(ListView)findViewById(R.id.comment_list);
        post_comment_count=(TextView)findViewById(R.id.post_comment_cnt);
        commentListAdapter = new CommentAdapter(post_item.this,comment_listArrayList);
        title_textview=(TextView)findViewById(R.id.post_title);
        content_imageview=(ImageView)findViewById(R.id.post_item_image);
        date_dateview=(TextView)findViewById(R.id.post_date);
        post_content=(TextView)findViewById(R.id.post_content);
        heartbutton = (Button)findViewById(R.id.heart_button);
        comment_submitbutton=(Button)findViewById(R.id.comment_submit_button);
        heartbutton.setOnClickListener(this);
        comment_submitbutton.setOnClickListener(this);
        post_heart_count=(TextView)findViewById(R.id.post_heart_cnt);
        post_content.setText("post안의 content text 설정");
        date_dateview.setText("날짜");
        comment_listArrayList = new ArrayList<comment_item>();

      /*  comment_listArrayList.add(new comment_item(R.mipmap.ic_launcher,"ㅇㅇ진짜로"));
        comment_listArrayList.add(new comment_item(R.mipmap.ic_launcher,"열심히 안하냐"));
        comment_listArrayList.add(new comment_item(R.mipmap.ic_launcher,"구라아닌거 같은데?"));
        comment_listArrayList.add(new comment_item(R.mipmap.ic_launcher,"에휴 나도 해야하는데 부럽다."));
        */
        commentListAdapter = new CommentAdapter(post_item.this,comment_listArrayList);
        comment_list.setAdapter(commentListAdapter);
        comment_cnt=comment_listArrayList.size();
        post_heart_count.setText(Integer.toString(heart_cnt));





    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.heart_button:
                heart_cnt++;//ddddd
                System.out.println("$$$");
                post_heart_count.setText(Integer.toString(heart_cnt));
                postRef.child("heart_count").setValue(Integer.toString(heart_cnt));
                //myRef.child("community").child(groupname).child("post").get
                break;
            case R.id.comment_submit_button:
                EditText editText = (EditText)findViewById(R.id.comment_mycomment);
                System.out.println("ok");
                comment_mycontent = editText.getText().toString();
                comment_cnt++;
                postRef.child("comment").child(Integer.toString(comment_cnt)).push().setValue(comment_mycontent);//이부분 에서는 댓글쓴이의 아이콘 입력
                postRef.child("comment").child(Integer.toString(comment_cnt)).push().setValue(GetUserData.picLink);
                /*
                comment_listArrayList.add(new comment_item(R.mipmap.ic_launcher,comment_mycontent));
                comment_cnt=comment_listArrayList.size();
                post_comment_count.setText(comment_cnt);
                */
                //myRef.child("community").child(groupname).child("post").push().setValue(new comment_item(R.mipmap.ic_launcher, "fdsfa@navet.com", comment_mycontent));
                break;
        }
    }












}
