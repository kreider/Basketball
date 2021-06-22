package kreider.ryan.quick_shot;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.view.View;

public class CharacterSprite {
    public boolean active=false;
    public boolean alive=false;
    public boolean touchable=true;
    private Bitmap image;
    public boolean touch=false;
    public float ball_px, ball_py, xx,yy;
    private double xaccel;
    public double yaccel = 0;
    private boolean up=true;
    private double gravity=1.4;
    public boolean scoreable=true;
    public boolean scored=false;
    public boolean s_scored=false;

    public long timestart=0;
    public long timeend=0;
    public Bitmap Bmp;
    private GameView context;
    private int screen_w=Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screen_h=Resources.getSystem().getDisplayMetrics().heightPixels;
    private double ball_w=screen_w/6;
    public float ball_r=(float)ball_w/2;
    private double friction=-.7;
    private int bounce_c=0;
//////////////////////////////////////////////////////////
/*constructor                                           */
//////////////////////////////////////////////////////////
public CharacterSprite (Bitmap bmp, GameView context) {
        image = bmp;
        image=Bitmap.createScaledBitmap(bmp, (int)ball_w, (int)ball_w, true);
        Bmp=bmp;
        ball_px = screen_w/2;
        ball_py= screen_h*5/6;
        xx=0;
        yy=0;
        bounce_c=0;
        yaccel = 0;
        xaccel = 0;
        scored=false;
        s_scored=false;
        ball_w=screen_w/6;
        ball_r=(float)ball_w/2;
        this.context=context;

}
///////////////////////////////////////////////////////////
/*draws the ball                                         */
///////////////////////////////////////////////////////////
    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, ball_px-(float)ball_w/2, ball_py-(float)ball_w/2, null);
    }
    public boolean getscorable(){
        if(scored==true)
        {scored=false;
            return true; }
        else{return false;}
    }
//////////////////////////////////////////////////////////
/*determines if ball scores                             */
//////////////////////////////////////////////////////////
    public int score(float[] arr){//0=left 1=right 2=y 3=radius
        int num=0;
        if(ball_px<arr[1]+arr[3]&&ball_px>arr[0]+arr[3]&&ball_py>arr[2]-10&&ball_py<arr[2]+30){
            System.out.println("scored");
            scoreable=false;
            scored=true;
            s_scored=true;
             num=3;
        }


        return num;
    }

//////////////////////////////////////////////////////////
/*resets variables for the next ball when finger lifted */
//////////////////////////////////////////////////////////
    public void update(int touch_x,int touch_y) {
        xx=0;
        yy=0;
        up=true;
        xaccel=0;
        yaccel=0;
        bounce_c=0;
        scoreable=true;
        scored=false;

        ball_px=touch_x;
        ball_py=touch_y;
        ball_w=screen_w/6;
        ball_r=(float)ball_w/2;
    }
////////////////////////////////////////////////////////
/*gets the x&y positions of where finger is lifted    */
////////////////////////////////////////////////////////
    public void update2(int touch2_x,int touch2_y) {
        xx=touch2_x;
        yy=touch2_y;
    }
///////////////////////////////////////////////////////
/*calculates strengths ball ball x&y movement        */
///////////////////////////////////////////////////////
    public void update3() {
        float time=(timeend-timestart);
        time= time/1000;
        if(time<.1){time=(float).2;}
        if(time>.99){time=(float).99;}
        time=time*2;
        System.out.println(time);

        if (xx != 0 && yy != 0) {
           xaccel = (ball_px - xx) / 8;
            yaccel = (ball_py - yy)/15/time;
            System.out.println("!!!!"+yaccel);
            if (yaccel > 70) {
                yaccel = 70;
            }
            if (yaccel < 20) {
                yaccel = 20;
            }
        }
        else{
            active=false;
            alive=false;
        }

    }
////////////////////////////////////////////////////////////////
/*determines collisions, adds affect of gravity              */
////////////////////////////////////////////////////////////////
    public void gravity(float[] arr) {
        //0=left 1=right 2=y 3=radius
        //left rim collision
        if(yaccel!=0){
            double dx = ball_px - arr[0];
            double dy = ball_py - arr[2];
            double distance = Math.sqrt(dx * dx + dy * dy);
            double mindist=ball_r+arr[3];
            if (distance < arr[3] + ball_r&&yaccel<0) {
                System.out.println("left rim collision");
                double angle = Math.atan2(dy, dx);
                double targetX = ball_px + Math.cos(angle) * mindist;
                double targetY = ball_py + Math.sin(angle) * mindist;
                double ax = (targetX - arr[0])*.05;
                double ay = (targetY - arr[2])*.05;
                xaccel-=ax;
                yaccel-=ay;
                ball_px+=ax;
                ball_py+=ay;
            }
            //0=left 1=right 2=y 3=radius
            //right rim collision
            dx = ball_px - arr[1];
            dy = ball_py - arr[2];
            distance = Math.sqrt(dx * dx + dy * dy);
            mindist=ball_r+arr[3];
            if (distance < arr[3] + ball_r&&yaccel<0) {
                System.out.println("right rim collision");
                double angle = Math.atan2(dy, dx);
                double targetX = ball_px + Math.cos(angle) * mindist;
                double targetY = ball_py + Math.sin(angle) * mindist;
                double ax = (targetX - arr[1])*.05;
                double ay = (targetY - arr[2])*.05;
                xaccel-=ax;
                yaccel-=ay;
                ball_px+=ax;
                ball_py+=ay;
            }
             //gravity
            yaccel-=gravity;
             ball_px-=xaccel;
             ball_py-=yaccel;
             ball_w=ball_w-.09;
             ball_r=(float)ball_w/2;
            image=Bitmap.createScaledBitmap(Bmp, (int)ball_w, (int)ball_w, true);

            if(ball_py>screen_h-ball_w/2) {
                ball_py=screen_h-(float)ball_w/2;
                yaccel*=friction;
                xaccel*=4.2;
                bounce_c++;
             }

            if(bounce_c>5||ball_px<0-ball_w/2||ball_px>screen_w+ball_w/2) {
                alive=false;
                active=false;
                xx=0;
                yy=0;
                bounce_c=0;
                yaccel = 0;
                xaccel = 0;
                ball_w=screen_w/6;
                ball_r=(float)ball_w/2;
                image=Bitmap.createScaledBitmap(Bmp, (int)ball_w, (int)ball_w, true);
            }
        }
    }
}

