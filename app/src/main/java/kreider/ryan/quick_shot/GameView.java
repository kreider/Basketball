package kreider.ryan.quick_shot;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import androidx.fragment.app.Fragment;

import kreider.ryan.quick_shot.R;

import java.util.Random;
public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    public int score = 0;
    private int screen_w = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screen_h = Resources.getSystem().getDisplayMetrics().heightPixels;
    private int numball = 20;
    private int ballcount = 0;
    public boolean touchable=false;
    private MainThread thread;
    private CharacterSprite[] characterSprite = new CharacterSprite[numball];
    private Basket basket;
    Context mycontext;
    private int gametime=62000;
    final MediaPlayer swish;
    final MediaPlayer b_sound;
    public final long interval = 1 * 1000; // 1 sec interval
    public CountDownTimer countDownTimer; // Reference to the class
    public boolean timerRunning = false;
    public long displayTime=-1; // To display the time on the screen
    public Integer sound;
////////////////////////////////////////////////////////////////////////
/*constructor                                                         */
////////////////////////////////////////////////////////////////////////
    public GameView(Context context) {
        super(context);
        mycontext = context;
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        setFocusable(true);
        swish = MediaPlayer.create(mycontext,R.raw.swish);
        b_sound = MediaPlayer.create(mycontext,R.raw.background_sound);
        b_sound.setLooping(true);
        if(MainActivity2.getMyData()!=null) {
            sound = MainActivity2.getMyData();
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
        else{
            sound = MainActivity.getMyData();
            System.out.println("the first sound variable" + sound);
        }
        thread.sound=sound;
        countDownTimer = new MyCountDownTimer(gametime, interval);
    }
///////////////////////////////////////////////////////////////////////
/*changes the touchable variable to the variable c                   */
///////////////////////////////////////////////////////////////////////
    public void changetouchable(boolean c){
        touchable=c;
    }
//////////////////////////////////////////////////////////////////////
/*starts the countdown for the game                                 */
//////////////////////////////////////////////////////////////////////
public void start_countdown() {
    countDownTimer.start(); // Start the time running
    timerRunning = true;
}
//////////////////////////////////////////////////////////////////////
/*  doesnt do anything                                              */
//////////////////////////////////////////////////////////////////////
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }
//////////////////////////////////////////////////////////////////////
/*initalizes the images for the balls and starts thread             */
//////////////////////////////////////////////////////////////////////
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        basket = new Basket(BitmapFactory.decodeResource(getResources(), R.drawable.hoop),
                BitmapFactory.decodeResource(getResources(), R.drawable.net),
                BitmapFactory.decodeResource(getResources(), R.drawable.netlong1),
                BitmapFactory.decodeResource(getResources(), R.drawable.netlong2),
                BitmapFactory.decodeResource(getResources(), R.drawable.netlong3),
                BitmapFactory.decodeResource(getResources(), R.drawable.netshort1),
                BitmapFactory.decodeResource(getResources(), R.drawable.netshort2),
                BitmapFactory.decodeResource(getResources(), R.drawable.netshort3),
                BitmapFactory.decodeResource(getResources(), R.drawable.background));
        for (int i = 0; i < numball; i++) {
            characterSprite[i] = new CharacterSprite(BitmapFactory.decodeResource(getResources(), R.drawable.bball), this);
        }
        System.out.println("sound variable is "+sound);

        characterSprite[0].active = true;
        characterSprite[0].alive = true;
        thread.setRunning(true);
        thread.start();
    }
////////////////////////////////////////////////////////////////////////
/*stops the thread                                                    */
////////////////////////////////////////////////////////////////////////
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
        b_sound.stop();
    }

////////////////////////////////////////////////////////////////////////
/*calls the gravity functions of other classes                        */
////////////////////////////////////////////////////////////////////////
    public void gravity() {
        basket.gravity((int) displayTime);
        float[] arr = basket.getradius();
        for (int i = 0; i < numball; i++) {
            if (characterSprite[i].yaccel != 0) {
                characterSprite[i].gravity(arr);
            }
        }
    }

public void b_sound() {
b_sound.start();
}
//////////////////////////////////////////////////////////////////////////
/*makes the swish sound                                                 */
//////////////////////////////////////////////////////////////////////////
public void sound() {
int n=0;
    for (int i = 0; i < numball; i++) {
        if (characterSprite[i].s_scored==true)
        {
            n=1;
            characterSprite[i].s_scored=false;
            break;
        }
    }
    if(n==1)
    {
        swish.start();
    }
}
////////////////////////////////////////////////////////////////////////
/*adds up the score                                                   */
////////////////////////////////////////////////////////////////////////
    public void score() {
        float[] arr = basket.getradius();
        for (int i = 0; i < numball; i++) {
            if (characterSprite[i].yaccel != 0 && characterSprite[i].yaccel < 0 && characterSprite[i].scoreable == true) {
                score += characterSprite[i].score(arr);
            }
        }
    }
///////////////////////////////////////////////////////////////////////
/*draws the countdown at the beginning of game                       */
///////////////////////////////////////////////////////////////////////
    public void drawtimer(Canvas canvas, int num) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.rgb(0, 0, 0));
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(600);
        canvas.drawText(Integer.toString( num), screen_w / 2-150, screen_h / 2, paint);
    }
 //////////////////////////////////////////////////////////////////////
 /*draws the balls, net, and basket                                  */
 //////////////////////////////////////////////////////////////////////
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            basket.draw(canvas);
            for (int i = 0; i < numball; i++) {
                if (characterSprite[i].alive != false && characterSprite[i].yaccel <= 0) {
                    characterSprite[i].draw(canvas);
                }
            }
            boolean s=false;
            for (int i = 0; i < numball; i++) {
                 s = characterSprite[i].getscorable();
                 if(s==true){
                     break;
                 }
            }
            basket.draw2(canvas,s);
            for (int i = 0; i < numball; i++) {
                if (characterSprite[i].alive != false && characterSprite[i].yaccel > 0) {
                    characterSprite[i].draw(canvas);
                }
            }
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.rgb(0, 0, 0));
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(300);
            if(displayTime==0) {
                canvas.drawText("TIME!", screen_w / 2 - 400, screen_h / 2, paint);
            }
        }
    }
///////////////////////////////////////////////////////////////////////
/*draws the time and score at the top of the screen                  */
///////////////////////////////////////////////////////////////////////
    public void draw_time_score(Canvas canvas){

        if (canvas != null) {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.rgb(250, 250, 250));
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(100);
            canvas.drawText(Integer.toString(score), screen_w * 7 / 9, screen_h / 19, paint);
            canvas.drawText(Integer.toString((int) displayTime), screen_w / 9, screen_h / 19, paint);
        }
    }

///////////////////////////////////////////////////////////////////////
/*registers the touch and movements of the ball                      */
///////////////////////////////////////////////////////////////////////
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(touchable==true) {
            float touch_x = (int) event.getX();
            float touch_y = (int) event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    characterSprite[ballcount].timestart = System.currentTimeMillis();
                    double dx = characterSprite[ballcount].ball_px - touch_x;
                    double dy = characterSprite[ballcount].ball_py - touch_y;
                    double distance = Math.sqrt(dx * dx + dy * dy);
                    if (distance < 1 + characterSprite[ballcount].ball_r) {
                        characterSprite[ballcount].touch = true;
                    } else {
                        characterSprite[ballcount].touch = false;
                    }
                    characterSprite[ballcount].active = true;
                    characterSprite[ballcount].alive = true;
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (characterSprite[ballcount].touch == true) {
                        characterSprite[ballcount].update2((int) touch_x, (int) touch_y);
                    }
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    if (characterSprite[ballcount].touch == true) {
                        characterSprite[ballcount].timeend=System.currentTimeMillis();
                        characterSprite[ballcount].update3();
                        characterSprite[ballcount].active = false;
                        ballcount++;
                        if (ballcount == numball) {
                            ballcount = 0;
                        }
                        Random rand = new Random(); //instance of random class
                        int upperbound = 5;
                        int r = rand.nextInt(upperbound) + 1;
                        characterSprite[ballcount].alive = true;
                        characterSprite[ballcount].update(screen_w * r / 6, screen_h * 5 / 6);
                    }
                    invalidate();
                    break;
            }
        }
        return true;
    }
//////////////////////////////////////////////////////////////////////
/*countdown class that calculates the time left                      */
//////////////////////////////////////////////////////////////////////
    private class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }
        public void onFinish() {
            timerRunning = false;
            touchable=false;
            try{ Thread.sleep(3000); }
            catch (Exception e){}
            finally{}
            countDownTimer.cancel();
            Intent intent = new Intent(mycontext, MainActivity2.class);
            String s=Integer.toString(score);
            intent.putExtra("message", s);
            mycontext.startActivity(intent);
            ((Activity)mycontext).finish();
        }

        public void onTick(long millisUntilFinished) {
            long dTime = (millisUntilFinished / 1000);
            displayTime = dTime;
        }
    }
}
