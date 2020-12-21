package com.ngdroidapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import java.util.Random;
import java.util.Vector;
import java.util.function.BinaryOperator;

import istanbul.gamelab.ngdroid.base.BaseCanvas;
import istanbul.gamelab.ngdroid.util.Log;
import istanbul.gamelab.ngdroid.util.Utils;


/**
 * Created by noyan on 24.06.2016.
 * Nitra Games Ltd.
 */


public class GameCanvas extends BaseCanvas {

    Canvas canvas;

    //karakter
    Bitmap sercan[];
    int sercanx, sercany, sercanhiz, sercandurum;
    boolean saghareket, solhareket;

    //butonlar
    Bitmap sagbuton;
    int sagbutonx, sagbutony;

    Bitmap solbuton;
    int solbutonx, solbutony;

    //arkaplan
    Bitmap arkaplan;
    int arkaplanx, arkaplany;

    //yemek
    Bitmap yemek;
    Random rasgelesayi;
    Vector<Integer> yemekler;
    Vector<Vector<Integer>> yemeklistesi;
    double baslangiczamani, suankizaman;

    //Çarpışma Kontrolleri
    Rect carpisan1, carpisan2;

    //Puan çerçevesi
    Bitmap puancerceve;
    int puancercevex, puancercevey;
    //Puan yazısı
    int puanx, puany, puan;
    Paint puanrengi;
    int yemekpuani;

    //patlama efekti
    Bitmap patlamaresimleri[];
    int patlamaframesayisi, patlamax, patlamay;
    Vector<Integer> yenipatlama;
    Vector<Vector<Integer>> patlamalistesi;

    //patlama etiketkleri
    int PATLAMA_FRAME = 0, PATLAMA_X = 1, PATLAMA_Y = 2;

    //ayar butonu
    Bitmap ayarbutonu;
    int ayarbutonux, ayarbutonuy;

    //pause menu
    Bitmap pausemenu;
    int pausemenux, pausemenuy;
    int devamx, devamy, devamh, devamw;
    int yenidenx, yenideny, yenidenh, yenidenw;
    int menudonx, menudony, menudonh, menudonw;
    boolean oyundevamediyor;

    public GameCanvas(NgApp ngApp) {
        super(ngApp);
    }

    public void setup() {
        oyundevamediyor = true;
        rasgelesayi = new Random();
        this.canvas = canvas;
        baslangiczamani = System.currentTimeMillis();//1000 = 1 saniye
        LOGI("setup");
        arkaplanYukle();
        sercanYukle();
        yemekYukle();
        sagbutonYukle();
        solbutonYukle();
        puanYukle();
        patlamaYukle();
        ayarbutonuYukle();
        pausemenuYukle();
        root.oyunmuzik.setLooping(true);    //müzik bitince tekrar çal
        root.gamemuzikOynat(true);
    }
    public void update(){
        if (oyundevamediyor == true) {
            suankizaman = System.currentTimeMillis();
            LOGI("update");
            sercanHareketi();
            yemekUret();
            yemekDusmesi();
            carpismaMetodlari();
        }
    }
    public void draw(Canvas canvas) {
        this.canvas = canvas;
        canvas.scale(getWidth() / 1920.0f, getHeight() / 1080.0f);
        LOGI("draw");
        arkaplanCiz();
        sercanCiz();
        yemekCiz();
        sagbutonCiz();
        solbutonCiz();
        puanCiz();
        patlamaCiz();
        ayarbutonuCiz();
        if (oyundevamediyor == false){
            pausemenuCiz();
        }
    }
    public void touchDown(int x, int y, int id) {
        sagbutonTiklandi(x, y);
        solbutonTiklandi(x, y);
    }public void touchMove(int x, int y, int id) {
        sagbutonTiklandi2(x, y);
        solbutonTiklandi2(x, y);
    }public void touchUp(int x, int y, int id) {
        if (!(x > root.xOranla(sagbutonx)
                && y > root.yOranla(sagbutony)
                && x < root.xOranla(sagbutonx + sagbuton.getWidth())
                && y < root.yOranla(sagbutony + sagbuton.getHeight()))) {
            saghareket = false;
        }
        if (!(x > root.xOranla(solbutonx)
                && y > root.yOranla(solbutony)
                && x < root.xOranla(solbutonx + solbuton.getWidth())
                && y < root.yOranla(solbutony + solbuton.getHeight()))) {
            saghareket = false;
        }
        sercanHareket(x, y);
        ayarbutonuTiklandi(x, y);
    }

    //pause menu
    private void pausemenuYukle() {
        pausemenu = Utils.loadImage(root, "ek/pausemenu.png");
        pausemenux = 1920 / 2 - pausemenu.getWidth() / 2;
        pausemenuy = 1080 / 2 - pausemenu.getHeight() / 2;

        devamx = pausemenux + 60;
        devamy = pausemenuy + 72;
        devamw = pausemenux + 125;
        devamh = pausemenuy + 140;

        yenidenx = pausemenux + 160;
        yenideny = pausemenuy + 72;
        yenidenw = pausemenux + 230;
        yenidenh = pausemenuy + 140;

        menudonx = pausemenux + 265;
        menudony = pausemenuy + 72;
        menudonw = pausemenux + 332;
        menudonh = pausemenuy + 140;

    }
    private void pausemenuCiz() {
        canvas.drawBitmap(pausemenu, pausemenux, pausemenuy, null);
    }

    //ayarbutonu
    private void ayarbutonuYukle() {
        ayarbutonu = Utils.loadImage(root, "ek/butonlar/ayar.png");
        ayarbutonux = 10;
        ayarbutonuy = 10;
    } private void ayarbutonuCiz() {
        canvas.drawBitmap(ayarbutonu, ayarbutonux, ayarbutonuy, null);
    }
    private void ayarbutonuTiklandi(int x, int y) {
        if (x > root.xOranla(ayarbutonux)
                && y > root.yOranla(ayarbutonuy)
                && x < root.xOranla(ayarbutonux + ayarbutonu.getWidth())
                && y < root.yOranla(ayarbutonuy + ayarbutonu.getHeight())) {
            oyundevamediyor = !oyundevamediyor;
        }

        devamButonuTiklama(x, y);
        yenidenButonuTikmala(x, y);
        menudonButonuTiklama(x, y);
    }
    private void menudonButonuTiklama(int x, int y) {
        if (x >= root.xOranla(menudonx)
                &&  x < root.xOranla(menudonw)
                &&  y >= root.yOranla(menudony)
                &&  y < root.yOranla(menudonh)
                &&  oyundevamediyor == false){
            root.canvasManager.setCurrentCanvas(new MenuCanvas(root));
        }
    }
    private void yenidenButonuTikmala(int x, int y) {
        if (x >= root.xOranla(yenidenx)
                &&  x < root.xOranla(yenidenw)
                &&  y >= root.yOranla(yenideny)
                &&  y < root.yOranla(yenidenh)
                &&  oyundevamediyor == false){
            root.canvasManager.setCurrentCanvas(new GameCanvas(root));
        }
    }
    private void devamButonuTiklama(int x, int y) {
        if (x >= root.xOranla(devamx)
                &&  x < root.xOranla(devamw)
                &&  y >= root.yOranla(devamy)
                &&  y < root.yOranla(devamh)
                &&  oyundevamediyor == false){
            oyundevamediyor = true;
        }
    }

    //carpismalar
    private void carpismaMetodlari() {
        carpisan1 = new Rect();
        carpisan2 = new Rect();
        etSercanaCarptimi();
    }
    private void etSercanaCarptimi() {
        //karakterin alanı
        carpisan1.set(sercanx, sercany,
                sercanx + sercan[sercandurum].getWidth(),
                sercany + sercan[sercandurum].getHeight());
        //yemegin alanı
        for (int odcm_dusman = 0; odcm_dusman < yemeklistesi.size(); odcm_dusman++) {
            carpisan2.set(yemeklistesi.get(odcm_dusman).get(YEMEK_X),
                    yemeklistesi.get(odcm_dusman).get(YEMEK_Y),
                    yemeklistesi.get(odcm_dusman).get(YEMEK_X)
                            + yemek.getWidth(),
                    yemeklistesi.get(odcm_dusman).get(YEMEK_Y)
                            + yemek.getHeight());

            if (carpisan1.intersect(carpisan2) == true) {
                sercandurum = 1;
                puan += yemekpuani;
                patlamax = yemeklistesi.get(odcm_dusman).get(YEMEK_X);
                patlamay = yemeklistesi.get(odcm_dusman).get(YEMEK_Y);
                patlamaUret(patlamax, patlamay);
                yemeklistesi.remove(odcm_dusman);
                patlamaUret(patlamax, patlamay);
                break;
            }
            sercandurum = 0;
        }
    }

    //arayuz
    private void puanYukle() {
        puanrengi = new Paint();
        puanrengi.setTextSize(30);//Punto
        puanrengi.setTextAlign(Paint.Align.LEFT);//Sola hizala(Hiza)
        puanrengi.setTypeface(Typeface.DEFAULT_BOLD); //Kalın yazı
        puanrengi.setARGB(255, 225, 255, 50); //A:saydamlık(255 kapalı), R:KIRMIZI, G:YEŞİL, B:MAVİ
        //Puan çerçevesi yükle
        puancerceve = Utils.loadImage(root, "puanpanel.png");
        puancercevex = 1920 - puancerceve.getWidth() - 100;
        puancercevey = 30;
        //puan başlangıç değerleri
        puan = 0;
        puanx = puancercevex + 80;
        puany = puancercevey + 50;
    }
    private void puanCiz() {
        //Çerçeveyi çiz
        canvas.drawBitmap(puancerceve, puancercevex, puancercevey, null);
        //Puanı çiz
        canvas.drawText("" + puan, puanx, puany, puanrengi);
    }

    //patlama
    private void patlamaYukle() {
        patlamaframesayisi = 4;
        patlamaresimleri = new Bitmap[patlamaframesayisi];
        for (int i = 0; i < patlamaframesayisi; i++) {
            patlamaresimleri[i] = Utils.loadImage(root,
                    "Explode" + (i+1) + ".png");
        }
        patlamalistesi = new Vector<Vector<Integer>>();
    }
    private void patlamaUret(int patlamax, int patlamay){
        yenipatlama = new Vector<Integer>();
        yenipatlama.add(0); //frame
        yenipatlama.add(patlamax); //x
        yenipatlama.add(patlamay); //y
        patlamalistesi.add(yenipatlama);
        root.sesOynat(root.SE_PATLAMA);
    }
    private void patlamaCiz() {
        for (int i = 0; i < patlamalistesi.size(); i++) {
            canvas.drawBitmap(patlamaresimleri[patlamalistesi.get(i).get(PATLAMA_FRAME)],
                    patlamalistesi.get(i).get(PATLAMA_X), patlamalistesi.get(i).get(PATLAMA_Y),
                    null);
            //frame +1 işlemi
            if (patlamalistesi.get(i).get(PATLAMA_FRAME) < patlamaframesayisi - 1) {
                patlamalistesi.get(i).set(PATLAMA_FRAME,
                        patlamalistesi.get(i).get(PATLAMA_FRAME) + 1);
            }else{
                patlamalistesi.remove(i);
            }
        }
    }

    //yemek
    private void yemekYukle() {
        yemeklistesi = new Vector<Vector<Integer>>();
        yemek = Utils.loadImage(root, "yemek.png");
    }
    private void yemekCiz() {
        for (int i=0; i < yemeklistesi.size(); i++){
            canvas.drawBitmap(yemek, yemeklistesi.get(i).get(YEMEK_X), yemeklistesi.get(i).get(YEMEK_Y), null);
        }
    }
    int YEMEK_X = 0, YEMEK_Y = 1, YEMEK_HIZ = 2;
    private void yemekUret() {
        yemekpuani = rasgelesayi.nextInt(100);
        if (Math.abs(baslangiczamani - suankizaman) < 1000) return;

        yemekler = new Vector<Integer>();
        yemekler.add(rasgelesayi.nextInt(1920 - yemek.getHeight())); //0
        yemekler.add(0); //1
        yemekler.add(30); //2

        yemeklistesi.add(yemekler);
        baslangiczamani = System.currentTimeMillis();
    }
    private void yemekDusmesi() {
        for (int i = 0; i < yemeklistesi.size(); i++) {
            if (yemeklistesi.get(i).get(YEMEK_Y)
                    > -yemek.getHeight()) {
                // X = HIZ DEĞİL!!!  X = X + HIZ
                yemeklistesi.get(i).set(YEMEK_Y,
                        yemeklistesi.get(i).get(YEMEK_Y)
                                + yemeklistesi.get(i).get(YEMEK_HIZ));
            } else {
                yemeklistesi.remove(i);
            }
        }
    }

    //arkaplan
    private void arkaplanYukle() {
        arkaplan = Utils.loadImage(root, "arkaplan.png");
        arkaplanx = 0;
        arkaplany = 0;
    }
    private void arkaplanCiz() {
        canvas.drawBitmap(arkaplan, arkaplanx, arkaplany, null);
    }

    //sercan
    private void sercanYukle() {
        sercandurum = 0;
        saghareket = false;
        solhareket = false;
        sercanhiz = 50;
        sercan = new Bitmap[2];
        sercan[0] = Utils.loadImage(root, "sercan.png");
        sercan[1] = Utils.loadImage(root, "sercan1.png");
        sercanx = 1920 /2 - sercan[0].getWidth();
        sercany = 1080 - sercan[0].getHeight();
    }
    private void sercanCiz() {
        canvas.drawBitmap(sercan[sercandurum], sercanx, sercany, null);
    }
    private void sercanHareketi() {
        if (saghareket == true && sercanx + sercan[sercandurum].getWidth() < 1920){
            sercanx += sercanhiz;
        }
        if (solhareket == true && sercanx > 0){
            sercanx -= sercanhiz;
        }
    }
    private void sercanHareket(int x, int y) {
        if (saghareket == true
                && x > root.xOranla(sagbutonx)
                && y > root.yOranla(sagbutony)
                && x < root.xOranla(sagbutonx + sagbuton.getWidth())
                && y < root.yOranla(sagbutony + sagbuton.getHeight())) {
            saghareket = false;
        }

        if (solhareket == true
                && x > root.xOranla(solbutonx)
                && y > root.yOranla(solbutony)
                && x < root.xOranla(solbutonx + solbuton.getWidth())
                && y < root.yOranla(solbutony + solbuton.getHeight())) {
            solhareket = false;
        }
    }

    //sag buton
    private void sagbutonYukle() {
        sagbuton = Utils.loadImage(root,"sag.png");
        sagbutonx = 1920 - sagbuton.getWidth() - 100;
        sagbutony = 1080 - sagbuton.getHeight() - 20;
    }
    private void sagbutonCiz() {
        canvas.drawBitmap(sagbuton, sagbutonx, sagbutony, null);
    }
    private void sagbutonTiklandi(int x, int y) {
        if (sercanx == 1920){
            saghareket = false;
        }else {
            if (sercanx < 1920
                    && x > root.xOranla(sagbutonx)
                    && y > root.yOranla(sagbutony)
                    && x < root.xOranla(sagbutonx + sagbuton.getWidth())
                    && y < root.yOranla(sagbutony + sagbuton.getHeight())) {
                saghareket = true;
            }
        }
    }
    private void sagbutonTiklandi2(int x, int y){
        if (!(x > root.xOranla(sagbutonx)
                && y > root.yOranla(sagbutony)
                && x < root.xOranla(sagbutonx + sagbuton.getWidth())
                && y < root.yOranla(sagbutony + sagbuton.getHeight()))) {
            saghareket = false;
        }
    }

    //sol buton
    private void solbutonYukle() {
        solbuton = Utils.loadImage(root,"sol.png");
        solbutonx = 20;
        solbutony = 1080 - solbuton.getHeight() - 20;
    }
    private void solbutonCiz() {
        canvas.drawBitmap(solbuton, solbutonx, solbutony, null);
    }
    private void solbutonTiklandi(int x, int y) {
        if (sercanx == 0){
            solhareket = false;
        }else {
            if (sercanx > 0
                    && x > root.xOranla(solbutonx)
                    && y > root.yOranla(solbutony)
                    && x < root.xOranla(solbutonx + solbuton.getWidth())
                    && y < root.yOranla(solbutony + solbuton.getHeight())) {
                solhareket = true;
            }
        }
    }
    private void solbutonTiklandi2(int x, int y) {
        if (!(x > root.xOranla(solbutonx)
                && y > root.yOranla(solbutony)
                && x < root.xOranla(solbutonx + solbuton.getWidth())
                && y < root.yOranla(solbutony + solbuton.getHeight()))) {
            saghareket = false;
        }
    }



    public void keyPressed(int key) {

    }

    public void keyReleased(int key) {

    }

    public boolean backPressed() {
        return true;
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
