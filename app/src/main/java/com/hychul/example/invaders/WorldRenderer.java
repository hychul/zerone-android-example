package com.hychul.example.invaders;

import android.opengl.GLES10;

import com.hychul.zerone.android.GLGraphics;
import com.hychul.zerone.android.graphics.Camera3D;
import com.hychul.zerone.android.graphics.Sprite;
import com.hychul.zerone.android.graphics.SpriteBatcher;
import com.hychul.zerone.android.graphics.light.AmbientLight;
import com.hychul.zerone.android.graphics.light.DirectionalLight;
import com.hychul.zerone.math.Vector3;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class WorldRenderer {
    GLGraphics glGraphics;
    Camera3D camera;
    AmbientLight ambientLight;
    DirectionalLight directionalLight;
    SpriteBatcher batcher;

    public WorldRenderer(GLGraphics glGraphics) {
        this.glGraphics = glGraphics;
        camera = new Camera3D(glGraphics, 67, glGraphics.getWidth() / (float) glGraphics.getHeight(), 0.1f, 100);
        camera.getPosition().set(0, 6, 2);
        camera.getLookAt().set(0, 0, -4);
        ambientLight = new AmbientLight();
        ambientLight.setColor(0.2f, 0.2f, 0.2f, 1.0f);
        directionalLight = new DirectionalLight();
        directionalLight.setDirection(-1, -0.5f, 0);
        batcher = new SpriteBatcher(10);
    }

    public void render(World world) {
        camera.getPosition().x = world.ship.position.x;
        camera.getLookAt().x = world.ship.position.x;
        camera.setMatrices();

        GLES10.glEnable(GL10.GL_DEPTH_TEST);
        GLES10.glEnable(GL10.GL_TEXTURE_2D);
        GLES10.glEnable(GL10.GL_LIGHTING);
        GLES10.glEnable(GL10.GL_COLOR_MATERIAL);
        ambientLight.enable();
        directionalLight.enable(GL10.GL_LIGHT0);

        renderShip(world.ship);
        renderInvaders(world.invaders);

        GLES10.glDisable(GL10.GL_TEXTURE_2D);

        renderShields(world.shields);
        renderShots(world.shots);

        GLES10.glDisable(GL10.GL_COLOR_MATERIAL);
        GLES10.glDisable(GL10.GL_LIGHTING);
        GLES10.glDisable(GL10.GL_DEPTH_TEST);
    }

    private void renderShip(Ship ship) {
        if (ship.state == Ship.SHIP_EXPLODING) {
            GLES10.glDisable(GL10.GL_LIGHTING);
            renderExplosion(ship.position, ship.stateTime);
            GLES10.glEnable(GL10.GL_LIGHTING);
        } else {
            Assets.shipTexture.bind();
            Assets.shipModel.bind();
            GLES10.glPushMatrix();
            GLES10.glTranslatef(ship.position.x, ship.position.y, ship.position.z);
            GLES10.glRotatef(ship.velocity.x / Ship.SHIP_VELOCITY * 90, 0, 0, -1);
            Assets.shipModel.draw(GL10.GL_TRIANGLES, 0,
                    Assets.shipModel.getNumVertices());
            GLES10.glPopMatrix();
            Assets.shipModel.unbind();
        }
    }

    private void renderInvaders(List<Invader> invaders) {
        Assets.invaderTexture.bind();
        Assets.invaderModel.bind();
        int len = invaders.size();
        for (int i = 0; i < len; i++) {
            Invader invader = invaders.get(i);
            if (invader.state == Invader.INVADER_DEAD) {
                GLES10.glDisable(GL10.GL_LIGHTING);
                Assets.invaderModel.unbind();
                renderExplosion(invader.position, invader.stateTime);
                Assets.invaderTexture.bind();
                Assets.invaderModel.bind();
                GLES10.glEnable(GL10.GL_LIGHTING);
            } else {
                GLES10.glPushMatrix();
                GLES10.glTranslatef(invader.position.x, invader.position.y,
                        invader.position.z);
                GLES10.glRotatef(invader.angle, 0, 1, 0);
                Assets.invaderModel.draw(GL10.GL_TRIANGLES, 0,
                        Assets.invaderModel.getNumVertices());
                GLES10.glPopMatrix();
            }
        }
        Assets.invaderModel.unbind();
    }

    private void renderShields(List<Shield> shields) {
        GLES10.glEnable(GL10.GL_BLEND);
        GLES10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        GLES10.glColor4f(0, 0, 1, 0.4f);
        Assets.shieldModel.bind();
        int len = shields.size();
        for (int i = 0; i < len; i++) {
            Shield shield = shields.get(i);
            GLES10.glPushMatrix();
            GLES10.glTranslatef(shield.position.x, shield.position.y,
                    shield.position.z);
            Assets.shieldModel.draw(GL10.GL_TRIANGLES, 0,
                    Assets.shieldModel.getNumVertices());
            GLES10.glPopMatrix();
        }
        Assets.shieldModel.unbind();
        GLES10.glColor4f(1, 1, 1, 1f);
        GLES10.glDisable(GL10.GL_BLEND);
    }

    private void renderShots(List<Shot> shots) {
        GLES10.glColor4f(1, 1, 0, 1);
        Assets.shotModel.bind();
        int len = shots.size();
        for (int i = 0; i < len; i++) {
            Shot shot = shots.get(i);
            GLES10.glPushMatrix();
            GLES10.glTranslatef(shot.position.x, shot.position.y, shot.position.z);
            Assets.shotModel.draw(GL10.GL_TRIANGLES, 0,
                    Assets.shotModel.getNumVertices());
            GLES10.glPopMatrix();
        }
        Assets.shotModel.unbind();
        GLES10.glColor4f(1, 1, 1, 1);
    }

    private void renderExplosion(Vector3 position, float stateTime) {
        Sprite frame = Assets.explosionAnim.getKeyFrame(stateTime, false);
        
        GLES10.glEnable(GL10.GL_BLEND);
        GLES10.glPushMatrix();
        GLES10.glTranslatef(position.x, position.y, position.z);
        batcher.beginBatch(Assets.explosionTexture);
        batcher.drawSprite(0, 0, 2, 2, frame);
        batcher.endBatch();
        GLES10.glPopMatrix();
        GLES10.glDisable(GL10.GL_BLEND);
    }
}
