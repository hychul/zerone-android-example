package com.hychul.example.jumper;

import com.hychul.zerone.Music;
import com.hychul.zerone.Sound;
import com.hychul.zerone.android.ZeroneActivity;
import com.hychul.zerone.android.graphics.Font;
import com.hychul.zerone.android.graphics.Sprite;
import com.hychul.zerone.android.graphics.SpriteAnimation;
import com.hychul.zerone.android.graphics.Texture;

public class Assets {
    public static Texture background;
    public static Sprite backgroundRegion;
    
    public static Texture items;        
    public static Sprite mainMenu;
    public static Sprite pauseMenu;
    public static Sprite ready;
    public static Sprite gameOver;
    public static Sprite highScoresRegion;
    public static Sprite logo;
    public static Sprite soundOn;
    public static Sprite soundOff;
    public static Sprite arrow;
    public static Sprite pause;    
    public static Sprite spring;
    public static Sprite castle;
    public static SpriteAnimation coinAnim;
    public static SpriteAnimation bobJump;
    public static SpriteAnimation bobFall;
    public static Sprite bobHit;
    public static SpriteAnimation squirrelFly;
    public static Sprite platform;
    public static SpriteAnimation brakingPlatform;
    public static Font font;
    
    public static Music music;
    public static Sound jumpSound;
    public static Sound highJumpSound;
    public static Sound hitSound;
    public static Sound coinSound;
    public static Sound clickSound;

    public static void load(ZeroneActivity zeroneActivity) {
        background = new Texture(zeroneActivity.getFileIO(), "jumper/background.png");
        backgroundRegion = new Sprite(background, 0, 0, 320, 480);
        
        items = new Texture(zeroneActivity.getFileIO(), "jumper/items.png");
        mainMenu = new Sprite(items, 0, 224, 300, 110);
        pauseMenu = new Sprite(items, 224, 128, 192, 96);
        ready = new Sprite(items, 320, 224, 192, 32);
        gameOver = new Sprite(items, 352, 256, 160, 96);
        highScoresRegion = new Sprite(Assets.items, 0, 257, 300, 110 / 3);
        logo = new Sprite(items, 0, 352, 274, 142);
        soundOff = new Sprite(items, 0, 0, 64, 64);
        soundOn = new Sprite(items, 64, 0, 64, 64);
        arrow = new Sprite(items, 0, 64, 64, 64);
        pause = new Sprite(items, 64, 64, 64, 64);
        
        spring = new Sprite(items, 128, 0, 32, 32);
        castle = new Sprite(items, 128, 64, 64, 64);
        coinAnim = new SpriteAnimation(0.2f,
                                 new Sprite(items, 128, 32, 32, 32),
                                 new Sprite(items, 160, 32, 32, 32),
                                 new Sprite(items, 192, 32, 32, 32),
                                 new Sprite(items, 160, 32, 32, 32));
        bobJump = new SpriteAnimation(0.2f,
                                new Sprite(items, 0, 128, 32, 32),
                                new Sprite(items, 32, 128, 32, 32));
        bobFall = new SpriteAnimation(0.2f,
                                new Sprite(items, 64, 128, 32, 32),
                                new Sprite(items, 96, 128, 32, 32));
        bobHit = new Sprite(items, 128, 128, 32, 32);
        squirrelFly = new SpriteAnimation(0.2f,
                                    new Sprite(items, 0, 160, 32, 32),
                                    new Sprite(items, 32, 160, 32, 32));
        platform = new Sprite(items, 64, 160, 64, 16);
        brakingPlatform = new SpriteAnimation(0.2f,
                                     new Sprite(items, 64, 160, 64, 16),
                                     new Sprite(items, 64, 176, 64, 16),
                                     new Sprite(items, 64, 192, 64, 16),
                                     new Sprite(items, 64, 208, 64, 16));
        
        font = new Font(items, 224, 0, 16, 16, 20);
        
        music = zeroneActivity.getAudio().newMusic("jumper/music.mp3");
        music.setLooping(true);
        music.setVolume(0.5f);
        if (Settings.soundEnabled)
            music.play();
        jumpSound = zeroneActivity.getAudio().newSound("jumper/jump.ogg");
        highJumpSound = zeroneActivity.getAudio().newSound("jumper/highjump.ogg");
        hitSound = zeroneActivity.getAudio().newSound("jumper/hit.ogg");
        coinSound = zeroneActivity.getAudio().newSound("jumper/coin.ogg");
        clickSound = zeroneActivity.getAudio().newSound("jumper/click.ogg");
    }       

    public static void reload() {
        background.reload();
        items.reload();
        if (Settings.soundEnabled)
            music.play();
    }

    public static void playSound(Sound sound) {
        if (Settings.soundEnabled)
            sound.play(1);
    }
}
