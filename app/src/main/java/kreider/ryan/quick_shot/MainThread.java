package kreider.ryan.quick_shot;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.view.SurfaceHolder;
public class MainThread extends Thread {
    private GameView gameView;
    private SurfaceHolder surfaceHolder;
    private boolean running;
    public static Canvas canvas;
    private int targetFPS = 30;
    private double averageFPS;
    private int c_inc=5;
    public int sound=0;
    public MainThread(SurfaceHolder surfaceHolder, GameView gameView) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
    }

    @Override
    public void run()
    {
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount =0;
        long targetTime = 1000/targetFPS;

        while(running) {
            startTime = System.nanoTime();
            canvas = null;

            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    if(c_inc<0) {
                        this.gameView.gravity();
                        this.gameView.score();
                        this.gameView.draw(canvas);
                        this.gameView.draw_time_score(canvas);

                    }  else {

                        this.gameView.draw(canvas);
                        this.gameView.drawtimer(canvas, c_inc);
                        c_inc--;
                        Thread.sleep(1000);

                        if(c_inc==0)
                        {
                            this.gameView.changetouchable(true);
                            this.gameView.start_countdown();
                            if(sound==1) {
                                this.gameView.b_sound();
                            }
                        }
                    }
                    if(sound==1) {
                        this.gameView.sound();
                    }
                }
            } catch (Exception e) {
            }
            finally{
                if(canvas!=null)
                {
                    try { surfaceHolder.unlockCanvasAndPost(canvas); }
                    catch(Exception e){e.printStackTrace();}
                }
            }
            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime-timeMillis;
            try{ this.sleep(waitTime); }catch(Exception e){}
            totalTime += System.nanoTime()-startTime;
            frameCount++;
            if(frameCount == targetFPS)
            {
                averageFPS = 1000/((totalTime/frameCount)/1000000);
                frameCount =0;
                totalTime = 0;
               // System.out.println(averageFPS);
            }
        }
    }
    public void setRunning(boolean isRunning) {
        running = isRunning;
        System.out.println("setrunning was called");
    }
}
