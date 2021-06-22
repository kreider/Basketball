package kreider.ryan.quick_shot;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;


public class Basket {
    private Bitmap hoop,net,netl1,netl2,netl3,nets1,nets2,nets3,background;
    private int x_gravity=5;
    private int y_gravity=5;
    private int screen_w= Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screen_h=Resources.getSystem().getDisplayMetrics().heightPixels;
    private int basket_w=screen_w/3;
    private int basket_h=screen_w/4;
    private int net_w=screen_w/4;
    private int net_h=screen_w/5;
    private int basket_px = screen_w/2;
    private int basket_py= screen_h*2/7;
    private float rim_r=basket_h/60;
    private float left=basket_px-net_w/2+rim_r/2;
    private float right=basket_px+net_w/2-rim_r/2;
    private float rim_y=basket_py+basket_h/2;

boolean animationrunning=false;
int animationtimer=0;

    public Basket (Bitmap bmp,Bitmap bmp2, Bitmap bmp3, Bitmap bmp4, Bitmap bmp5, Bitmap bmp6,Bitmap bmp7,Bitmap bmp8,Bitmap bmp9) {

        hoop=Bitmap.createScaledBitmap(bmp, basket_w, basket_h, true);
        net=Bitmap.createScaledBitmap(bmp2, net_w, net_h, true);
        netl1=Bitmap.createScaledBitmap(bmp3, net_w, net_h, true);
        netl2=Bitmap.createScaledBitmap(bmp4, net_w, net_h, true);
        netl3=Bitmap.createScaledBitmap(bmp5, net_w, net_h, true);
        nets1=Bitmap.createScaledBitmap(bmp6, net_w, net_h, true);
        nets2=Bitmap.createScaledBitmap(bmp7, net_w, net_h, true);
        nets3=Bitmap.createScaledBitmap(bmp8, net_w, net_h, true);
        background=Bitmap.createScaledBitmap(bmp9, screen_w, screen_h, true);

    }

    public void gravity(int displaytime){
        if(displaytime<53) {
            basket_px += x_gravity;
            if (basket_px > screen_w-basket_w/2 || basket_px < 0+basket_w/2) {
                Random rand = new Random(); //instance of random class
                int upperbound = 4;
                int r = rand.nextInt(upperbound) + 4;
                if(x_gravity>0){x_gravity=r*-1;basket_px=screen_w-basket_w/2;}
                else{ x_gravity=r;basket_px=0+basket_w/2;}
            }
                if(displaytime<35){
                    basket_py+=y_gravity;
                    if(basket_py<0+basket_h/2||basket_py>screen_h/2-basket_h/2){
                        Random rand = new Random(); //instance of random class
                        int upperbound = 4;
                        int r = rand.nextInt(upperbound) + 4;
                        if(y_gravity>0){y_gravity=r*-1;basket_py=screen_h/2-basket_h/2;}
                        else{ y_gravity=r;basket_py=0+basket_h/2;}
                    }
                }
            left = basket_px - basket_w / 2 + rim_r * 15;
            right = basket_px + basket_w / 2 - rim_r * 15;
            rim_y=basket_py+basket_h/2;
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(background, 0, 0, null);
        canvas.drawBitmap(hoop, basket_px-basket_w/2, basket_py-basket_h/2, null);
    }
    public void draw2(Canvas canvas, boolean s) {

        if(animationrunning==false&&s==true) {
            animationrunning=true;
            animationtimer=0;
        }
        if(animationrunning==true){
            if(animationtimer<=1&&animationtimer>=0){
                canvas.drawBitmap(netl1, basket_px - net_w / 2, basket_py - net_h / 2 + basket_h * 2 / 3, null); }
            else if(animationtimer<=2&&animationtimer>1){
                canvas.drawBitmap(netl2, basket_px - net_w / 2, basket_py - net_h / 2 + basket_h * 2 / 3, null); }
            else if(animationtimer<=3&&animationtimer>2){
                canvas.drawBitmap(netl3, basket_px - net_w / 2, basket_py - net_h / 2 + basket_h * 2 / 3, null); }
            else if(animationtimer<=4&&animationtimer>3){
                canvas.drawBitmap(netl2, basket_px - net_w / 2, basket_py - net_h / 2 + basket_h * 2 / 3, null); }
            else if(animationtimer<=5&&animationtimer>4){
                canvas.drawBitmap(netl1, basket_px - net_w / 2, basket_py - net_h / 2 + basket_h * 2 / 3, null); }
            else if(animationtimer<=6&&animationtimer>5){
                canvas.drawBitmap(net, basket_px - net_w / 2, basket_py - net_h / 2 + basket_h * 2 / 3, null); }
            else if(animationtimer<=7&&animationtimer>6){
                canvas.drawBitmap(nets1, basket_px - net_w / 2, basket_py - net_h / 2 + basket_h * 2 / 3, null); }
            else if(animationtimer<=8&&animationtimer>7){
                canvas.drawBitmap(nets2, basket_px - net_w / 2, basket_py - net_h / 2 + basket_h * 2 / 3, null); }
            else if(animationtimer<=9&&animationtimer>8){
                canvas.drawBitmap(nets3, basket_px - net_w / 2, basket_py - net_h / 2 + basket_h * 2 / 3, null); }
            else if(animationtimer<=10&&animationtimer>9){
                canvas.drawBitmap(nets2, basket_px - net_w / 2, basket_py - net_h / 2 + basket_h * 2 / 3, null); }
            else if(animationtimer<=11&&animationtimer>10){
                canvas.drawBitmap(nets1, basket_px - net_w / 2, basket_py - net_h / 2 + basket_h * 2 / 3, null); }
            animationtimer++;
            if(animationtimer>11)
            {
                animationrunning=false;
            }
        }
        else {
            canvas.drawBitmap(net, basket_px - net_w / 2, basket_py - net_h / 2 + basket_h * 2 / 3, null);
        }
    }


    public float[] getradius(){
      float []arr= new float[4];
      arr[0]=left;
      arr[1]=right;
      arr[2]=rim_y;
      arr[3]=rim_r;
      return arr;
    }

}
