package com.hychul.example.invaders;

import com.hychul.zerone.Music;
import com.hychul.zerone.Sound;
import com.hychul.zerone.android.ZeroneActivity;
import com.hychul.zerone.android.graphics.Font;
import com.hychul.zerone.android.graphics.ObjLoader;
import com.hychul.zerone.android.graphics.Sprite;
import com.hychul.zerone.android.graphics.SpriteAnimation;
import com.hychul.zerone.android.graphics.Texture;
import com.hychul.zerone.android.graphics.Vertices3;

public class Assets {
    public static Texture background;
    public static Sprite backgroundRegion;
    public static Texture items;
    public static Sprite logoRegion;
    public static Sprite menuRegion;
    public static Sprite gameOverRegion;
    public static Sprite pauseRegion;
    public static Sprite settingsRegion;
    public static Sprite touchRegion;
    public static Sprite accelRegion;
    public static Sprite touchEnabledRegion;
    public static Sprite accelEnabledRegion;
    public static Sprite soundRegion;
    public static Sprite soundEnabledRegion;
    public static Sprite leftRegion;
    public static Sprite rightRegion;
    public static Sprite fireRegion;
    public static Sprite pauseButtonRegion;
    public static Font font;

    public static Texture explosionTexture;
    public static SpriteAnimation explosionAnim;
    public static Vertices3 shipModel;
    public static Texture shipTexture;
    public static Vertices3 invaderModel;
    public static Texture invaderTexture;
    public static Vertices3 shotModel;
    public static Vertices3 shieldModel;

    public static Music music;
    public static Sound clickSound;
    public static Sound explosionSound;
    public static Sound shotSound;

    public static void load(ZeroneActivity zeroneActivity) {
        background = new Texture(zeroneActivity.getFileIO(), "invaders/background.jpg", true);
        backgroundRegion = new Sprite(background, 0, 0, 480, 320);
        items = new Texture(zeroneActivity.getFileIO(), "invaders/items.png", true);
        logoRegion = new Sprite(items, 0, 256, 384, 128);
        menuRegion = new Sprite(items, 0, 128, 224, 64);
        gameOverRegion = new Sprite(items, 224, 128, 128, 64);
        pauseRegion = new Sprite(items, 0, 192, 160, 64);
        settingsRegion = new Sprite(items, 0, 160, 224, 32);
        touchRegion = new Sprite(items, 0, 384, 64, 64);
        accelRegion = new Sprite(items, 64, 384, 64, 64);
        touchEnabledRegion = new Sprite(items, 0, 448, 64, 64);
        accelEnabledRegion = new Sprite(items, 64, 448, 64, 64);
        soundRegion = new Sprite(items, 128, 384, 64, 64);
        soundEnabledRegion = new Sprite(items, 190, 384, 64, 64);
        leftRegion = new Sprite(items, 0, 0, 64, 64);
        rightRegion = new Sprite(items, 64, 0, 64, 64);
        fireRegion = new Sprite(items, 128, 0, 64, 64);
        pauseButtonRegion = new Sprite(items, 0, 64, 64, 64);
        font = new Font(items, 224, 0, 16, 16, 20);

        explosionTexture = new Texture(zeroneActivity.getFileIO(), "invaders/explode.png", true);
        Sprite[] keyFrames = new Sprite[16];
        int frame = 0;
        for (int y = 0; y < 256; y += 64) {
            for (int x = 0; x < 256; x += 64) {
                keyFrames[frame++] = new Sprite(explosionTexture, x, y, 64, 64);
            }
        }
        explosionAnim = new SpriteAnimation(0.1f, keyFrames);

        shipTexture = new Texture(zeroneActivity.getFileIO(), "invaders/ship.png", true);
        shipModel = ObjLoader.load(zeroneActivity.getFileIO(), "invaders/ship.obj");
        invaderTexture = new Texture(zeroneActivity.getFileIO(), "invaders/invader.png", true);
        invaderModel = ObjLoader.load(zeroneActivity.getFileIO(), "invaders/invader.obj");
        shieldModel = ObjLoader.load(zeroneActivity.getFileIO(), "invaders/shield.obj");
        shotModel = ObjLoader.load(zeroneActivity.getFileIO(), "invaders/shot.obj");

        music = zeroneActivity.getAudio().newMusic("invaders/music.mp3");
        music.setLooping(true);
        music.setVolume(0.5f);
        if (Settings.soundEnabled)
            music.play();

        clickSound = zeroneActivity.getAudio().newSound("invaders/click.ogg");
        explosionSound = zeroneActivity.getAudio().newSound("invaders/explosion.ogg");
        shotSound = zeroneActivity.getAudio().newSound("invaders/shot.ogg");
    }

    public static void reload() {
        background.reload();
        items.reload();
        explosionTexture.reload();
        shipTexture.reload();
        invaderTexture.reload();
        if (Settings.soundEnabled)
            music.play();
    }

    public static void playSound(Sound sound) {
        if (Settings.soundEnabled)
            sound.play(1);
    }
}

