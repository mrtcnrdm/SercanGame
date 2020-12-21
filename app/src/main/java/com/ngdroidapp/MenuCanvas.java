package com.ngdroidapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import istanbul.gamelab.ngdroid.base.BaseCanvas;
import istanbul.gamelab.ngdroid.util.Log;
import istanbul.gamelab.ngdroid.util.Utils;

/**
 * Created by noyan on 27.06.2016.
 * Nitra Games Ltd.
 */

public class MenuCanvas extends BaseCanvas {
    Canvas canvas;

    //oynabutonu
    Bitmap oynabuton[];
    int oynabutonx, oynabutony, oynabutondurumu;

    //arkaplan
    Bitmap arkaplan;


    public MenuCanvas(NgApp ngApp) {
        super(ngApp);
    }

    public void setup() {
        arkaplanYukle();
        oynabutonYukle();
        root.menumuzikOynat(true);
    }
    public void update() {
    }

    public void draw(Canvas canvas) {
        this.canvas = canvas;
        canvas.scale(getWidth() / 1920.0f, getHeight() / 1080.0f);
        arkaplanCiz();
        oynabutonCiz();
    }

    public void touchDown(int x, int y, int id) {
        oynabutonTiklama(x, y);
    }

    public void touchMove(int x, int y, int id) {
    }

    public void touchUp(int x, int y, int id) {
        oynabutonDurumuDuzelt(x, y);
        root.menumuzik.stop();
    }

    //------OBJELER------
    //arkaplan
    private void arkaplanYukle() {
        arkaplan = Utils.loadImage(root, "menuarkaplan.png");
    }
    private void arkaplanCiz() {
        canvas.drawBitmap(arkaplan, 0, 0,null);
    }

    //oynabutonu
    private void oynabutonCiz() {
        canvas.drawBitmap(oynabuton[oynabutondurumu], oynabutonx, oynabutony, null);
    }
    private void oynabutonYukle() {
        oynabutondurumu = 0;
        oynabuton = new Bitmap[2];
        oynabuton[0]= Utils.loadImage(root, "butonlar/girisbutonu1.png");
        oynabuton[1]= Utils.loadImage(root, "butonlar/girisbutonu2.png");
        oynabutonx = 1920 / 2 - oynabuton[oynabutondurumu].getWidth() / 2;
        oynabutony = 1080 / 2 - oynabuton[oynabutondurumu].getHeight() / 2;
    }
    private void oynabutonTiklama(int x, int y) {
        if (x > root.xOranla(oynabutonx)
                && y > root.yOranla(oynabutony)
                && x < root.xOranla(oynabutonx) + oynabuton[oynabutondurumu].getWidth()
                && y < root.yOranla(oynabutony) + oynabuton[oynabutondurumu].getHeight()) {
            oynabutondurumu = 1;
            root.sesOynat(root.SE_BUTON);
            oynaButonKonumunuDuzelt();
        }
    }
    private void oynaButonKonumunuDuzelt() {
        oynabutonx = 1920 / 2 - oynabuton[oynabutondurumu].getWidth() / 2;
        oynabutony = 1080 / 2 - oynabuton[oynabutondurumu].getHeight() / 2;
    }
    private void oynabutonDurumuDuzelt(int x, int y) {
        if (x > root.xOranla(oynabutonx)
                && y > root.yOranla(oynabutony)
                && x < root.xOranla(oynabutonx) + oynabuton[0].getWidth()
                && y < root.yOranla(oynabutony) + oynabuton[0].getHeight()) {
            root.canvasManager.setCurrentCanvas(new GameCanvas(root));
        }
        if (oynabutondurumu ==1){
            oynabutondurumu = 0;
        }
    }


    public void keyPressed(int key) {
    }

    public void keyReleased(int key) {
    }

    public boolean backPressed() {
        return false;
    }

    public void surfaceChanged(int width, int height) {
    }

    public void surfaceCreated() {
    }

    public void surfaceDestroyed() {
    }

    public void pause() {
    }

    public void resume() {
    }

    public void reloadTextures() {
    }

    public void showNotify() {
    }

    public void hideNotify() {
    }

}
