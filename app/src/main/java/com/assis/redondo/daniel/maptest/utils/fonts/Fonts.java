package com.assis.redondo.daniel.maptest.utils.fonts;


import com.assis.redondo.daniel.maptest.utils.Typefaces;

/**
 * Created by diego on 4/2/15.
 */
public enum Fonts implements Typefaces.Font {
    RB_REGULAR("Roboto-Regular.ttf"),
    RB_BLACK_REGULAR("Roboto-Black.ttf"),
    RB_REGULAR_ITALIC("Roboto-BlackItalic.ttf"),
    RB_BOLD("Roboto-Bold.ttf"),
    RB_BOLD_ITALIC("Roboto-BoldItalic.ttf"),
    RB_ITALIC("Roboto-Italic.ttf"),
    RB_LIGHT("Roboto-Light.ttf"),
    RB_LIGHT_ITALIC("Roboto-LightItalic.ttf"),
    RB_MEDIUM("Roboto-Medium.ttf"),
    RB_MEDIUM_ITALIC("Roboto-MediumItalic.ttf"),
    RB_THIN("Roboto-Thin.ttf"),
    RB_THIN_ITALIC("Roboto-ThinItalic.ttf"),
    RB_CONDENSED_BOLD("RobotoCondensed-Bold.ttf"),
    RB_CONDENSED_BOLD_ITALIC("RobotoCondensed-BoldItalic.ttf"),
    RB_CONDENSED_ITALIC("RobotoCondensed-Italic.ttf"),
    RB_CONDENSED_LIGHT("RobotoCondensed-Light.ttf"),
    RB_CONDENSED_LIGHT_ITALIC("RobotoCondensed-LightItalic.ttf"),
    RB_CONDENSED_REGULAR("RobotoCondensed-Regular.ttf"),;


    private String name;

    Fonts(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
