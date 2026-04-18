package com.mygdx.game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class Assets {
    public static Texture playerTexture;
    public static Texture zombieTexture;
    public static Texture hunterTexture;
    public static Texture bruteTexture;
    public static Texture exploderTexture;
    public static Texture spitterTexture;
    public static Texture bulletTexture;
    public static Texture wallTexture;
    public static Texture floorTexture;
    public static Texture safeZoneTexture;
    public static Texture medkitTexture;
    public static Texture ammoTexture;
    public static Texture menuBackground;

    public static void load() {
        playerTexture = loadOrCreate("player.png", 32, 32, new Color(0.2f, 0.8f, 0.2f, 1f));
        zombieTexture = loadOrCreate("zombie.png", 28, 28, new Color(0.8f, 0.2f, 0.2f, 1f));
        hunterTexture = loadOrCreate("hunter.png", 24, 24, new Color(0.95f, 0.5f, 0.1f, 1f));
        bruteTexture = loadOrCreate("brute.png", 42, 42, new Color(0.5f, 0.1f, 0.1f, 1f));
        exploderTexture = loadOrCreate("exploder.png", 30, 30, new Color(0.95f, 0.9f, 0.2f, 1f));
        spitterTexture = loadOrCreate("spitter.png", 30, 30, new Color(0.3f, 1f, 0.3f, 1f));
        bulletTexture = loadOrCreate("bullet.png", 8, 8, Color.YELLOW);
        wallTexture = loadOrCreate("wall.png", 32, 32, Color.GRAY);
        floorTexture = loadOrCreate("floor.png", 32, 32, new Color(0.16f, 0.16f, 0.16f, 1f));
        safeZoneTexture = loadOrCreate("safezone.png", 64, 64, new Color(0.2f, 0.5f, 1f, 0.6f));
        medkitTexture = loadOrCreate("medkit.png", 24, 24, Color.PINK);
        ammoTexture = loadOrCreate("ammo.png", 24, 24, Color.ORANGE);
        menuBackground = loadOrCreate("menu_bg.png", 800, 600, new Color(0.08f, 0.08f, 0.12f, 1f));
    }

    private static Texture loadOrCreate(String path, int width, int height, Color color) {
        FileHandle file = Gdx.files.internal(path);
        if (file.exists()) {
            return new Texture(file);
        }

        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillRectangle(0, 0, width, height);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    public static void dispose() {
        disposeTexture(playerTexture);
        disposeTexture(zombieTexture);
        disposeTexture(hunterTexture);
        disposeTexture(bruteTexture);
        disposeTexture(exploderTexture);
        disposeTexture(spitterTexture);
        disposeTexture(bulletTexture);
        disposeTexture(wallTexture);
        disposeTexture(floorTexture);
        disposeTexture(safeZoneTexture);
        disposeTexture(medkitTexture);
        disposeTexture(ammoTexture);
        disposeTexture(menuBackground);
    }

    private static void disposeTexture(Texture texture) {
        if (texture != null) {
            texture.dispose();
        }
    }
}
