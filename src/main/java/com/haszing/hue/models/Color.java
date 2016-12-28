package com.haszing.hue.models;

/**
 * This represents a color using RGB values.
 */
public class Color {
    int red;
    int green;
    int blue;

    public Color(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int getRed() {
        return this.red;
    }

    public int getGreen() {
        return this.green;
    }

    public int getBlue() {
        return this.blue;
    }

    public boolean equals(Object other) {
        if (!(other instanceof Color)) {
            return false;
        }
        Color color = (Color) other;
        return this.red == color.red &&
                this.green == color.green &&
                this.blue == color.blue;
    }
}
