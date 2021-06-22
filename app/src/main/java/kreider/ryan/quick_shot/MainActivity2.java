package kreider.ryan.quick_shot;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import kreider.ryan.quick_shot.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener{
    private ImageButton startbutton;
    private static Integer sound;
    private ImageButton soundbutton;
   public String[] lines = new String[6];
   public String fname = "scoreboard.txt";
   public String result="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main2);
        startbutton = findViewById(R.id.restart);
        soundbutton=findViewById(R.id.sound);
        soundbutton.setOnClickListener(this);

        //int size_x = getResources().getDisplayMetrics().widthPixels * 3 / 8;
        startbutton.setOnClickListener(this);
        TextView tv1 = (TextView) findViewById(R.id.score1);
        TextView tv2 = (TextView) findViewById(R.id.score2);
        TextView tv3 = (TextView) findViewById(R.id.score3);
        TextView tv4 = (TextView) findViewById(R.id.score4);
        TextView tv5 = (TextView) findViewById(R.id.score5);
        TextView tv6= (TextView) findViewById(R.id.yourscore);


        String sessionId = getIntent().getStringExtra("message");
        int gamescore= Integer.parseInt(sessionId);


        File file = getBaseContext().getFileStreamPath(fname);
//reads the file
        for(int e=0;e<6;e++) { lines[e]=""; }
        try {
            FileInputStream fin = this.openFileInput(fname);
            int c;
            int w=0;
            while( (c = fin.read()) != -1){

                if(c==47){
                    w=w+1;
                }
                else {
                    lines[w] = lines[w] + Character.toString((char) c);
                }
            }
            sound=Integer.parseInt(lines[5]);
            System.out.println("read from file sound"+sound);
            if(sound==0) {
                soundbutton.setImageResource(R.drawable.sound_off);
            }
            else{
                soundbutton.setImageResource(R.drawable.sound_on);
            }
            fin.close();
        } catch(Exception e){ }

        Integer[] s = new Integer[5];
        for(int e=0;e<5;e++) {
            s[e] = Integer.parseInt(lines[e]);
        }
//sorts the scores if a new top score is made
        for(int e=0;e<5;e++){
            if(gamescore>=s[e]){
                for(int f=4;f>e;f--){
                    s[f]=s[f-1];
                }
                s[e]=gamescore;
                break;
            }

        }

        tv1.setText("1st:   "+Integer.toString(s[0]));
        tv2.setText("2nd:   "+Integer.toString(s[1]));
        tv3.setText("3rd:   "+Integer.toString(s[2]));
        tv4.setText("4th:   "+Integer.toString(s[3]));
        tv5.setText("5th:   "+Integer.toString(s[4]));
        tv6.setText("Your Score: "+Integer.toString(gamescore));

        if(s[0]==gamescore){tv1.setTextColor(Color.parseColor("#ffdb3d"));}
        else if(s[1]==gamescore){tv2.setTextColor(Color.parseColor("#ffdb3d"));}
        else if(s[2]==gamescore){tv3.setTextColor(Color.parseColor("#ffdb3d"));}
        else if(s[3]==gamescore){tv4.setTextColor(Color.parseColor("#ffdb3d"));}
        else if(s[4]==gamescore){tv5.setTextColor(Color.parseColor("#ffdb3d"));}

        for(int i=0; i<5;i++){
            result=result+Integer.toString(s[i])+"/";
        }
        String r2=result+Integer.toString(sound);
        //result=result+Integer.toString(sound);
        //result=result.substring(0,result.length()-1);
        System.out.println("the data put in file2: "+r2);

        try {
            FileOutputStream fOut = this.openFileOutput(fname, Context.MODE_PRIVATE);
            fOut.write(r2.getBytes());
            fOut.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Integer getMyData(){
        return sound;
    }
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.restart:
                    setContentView(new GameView(this));
                    break;
                case R.id.sound:
                    sound=((sound+1)%2);
                    if(sound==0) {
                        soundbutton.setImageResource(R.drawable.sound_off);
                    }
                    else{
                        soundbutton.setImageResource(R.drawable.sound_on);
                    }
                    String r2="";
                    r2=result;
                    r2=r2+Integer.toString(sound);
                    try {
                        FileOutputStream fOut = this.openFileOutput(fname, Context.MODE_PRIVATE);
                        fOut.write(r2.getBytes());
                        fOut.close();
                    } catch (Exception e) {e.printStackTrace(); }
                    break;
            }
        }

}