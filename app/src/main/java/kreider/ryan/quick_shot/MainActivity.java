package kreider.ryan.quick_shot;
//76323781110
//SHA1: A1:EE:D6:7B:21:94:D2:29:B9:A1:8E:FD:0B:4E:29:30:FD:40:C5:28
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
//telcom6050
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static Integer sound;
    private ImageButton startbutton;
    private ImageButton soundbutton;

    public String[] lines=new String[6];
    public String info="0/0/0/0/0/1";
   public String fname="scoreboard.txt";
   public String result="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        startbutton = findViewById(R.id.start);
        soundbutton=findViewById(R.id.sound);
        //int size_x = getResources().getDisplayMetrics().widthPixels*3/8;
        startbutton.setOnClickListener(this);
        soundbutton.setOnClickListener(this);

        File file = getBaseContext().getFileStreamPath(fname);
        //sees if the file already exists
        try {
            FileInputStream fin = this.openFileInput(fname);
            int c;
            while( (c = fin.read()) != -1) {
                result = result + Character.toString((char) c);
                System.out.println("data read from file"+result);

            }
            fin.close();
        } catch(Exception e){ }

//if file doesnt exist, it creates one and fills it with starting info
        if(result=="") {
            try {
                FileOutputStream fOut = this.openFileOutput(fname, Context.MODE_PRIVATE);
                fOut.write(info.getBytes());
                fOut.close();

            } catch (Exception e) {e.printStackTrace(); }
        }

//reads data from the file
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
                fin.close();
            } catch(Exception e){}
             result="";
            for(int i=0;i<5;i++){
                result=result+lines[i]+"/";
            }
            sound=Integer.parseInt(lines[5]);
            System.out.println("read from file sound"+sound);
            if(sound==0) {
                soundbutton.setImageResource(R.drawable.sound_off);
            }
            else{
                soundbutton.setImageResource(R.drawable.sound_on);
            }
        }

    public static Integer getMyData(){
        return sound;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.start:
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

