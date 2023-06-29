package org.moonware.client.utils;

import java.awt.*;
import java.io.InputStream;

/**
 * @deprecated Не пихай сюда новые шрифты, юзай {@link MWFont}.
 */
@Deprecated
public class FontStorage {
    public static CustomFont openSansFontRender;
    public static CustomFont lucidaConsoleFontRenderer;
    public static CustomFont buttonFontRender;
    public static CustomFont bigButtonFontRender;
    public static CustomFont verdanaFontRender;
    public static CustomFont robotoRegularFontRender;
    public static CustomFont latoFontRender;
    public static CustomFont latoBig;
    public static CustomFont ubuntuFontRender;
    public static CustomFont sfuiFontRender;
    public static CustomFont arrayListQuick;
    public static CustomFont helvetica;
    public static CustomFont notification;
    public static CustomFont calibri;
    public static CustomFont productsans;
    public static CustomFont raleway;
    public static CustomFont kollektif;
    public static CustomFont aldotheapache;
    public static CustomFont museosans;
    public static CustomFont tahoma;
    public static CustomFont circleregular;
    public static CustomFont menlo;
    public static CustomFont acherusFeral;
    public static CustomFont comfortaa19;
    public static CustomFont wexside22;
    
    public static void init() {
        comfortaa19 = load("moonware/font/comfortaa.ttf", 19.0f);
        wexside22 = load("moonware/font/wexside.ttf", 22);
        acherusFeral = load("moonware/font/acherusferal.ttf", 18);
        sfuiFontRender = load("moonware/font/comfortaa.ttf", 19.0F);
        notification = load("moonware/font/notification.ttf", 45);
        buttonFontRender = load("moonware/font/quicksand.ttf", 28);
        bigButtonFontRender = load("moonware/font/quicksand.ttf", 35);
        arrayListQuick = load("moonware/font/quicksand.ttf", 20);
        verdanaFontRender = load("moonware/font/verdana.ttf", 19);
        robotoRegularFontRender = load("moonware/font/robotoregular.ttf", 19);
        latoFontRender = load("moonware/font/lato.ttf", 19);
        latoBig = load("moonware/font/lato.ttf", 25);
        openSansFontRender = load("moonware/font/opensans.ttf", 19);
        ubuntuFontRender = load("moonware/font/ubuntu.ttf", 19);
        lucidaConsoleFontRenderer = load("moonware/font/lucidaconsole.ttf", 19);
        helvetica = load("moonware/font/helvetica.ttf", 19);
        calibri = load("moonware/font/calibri.ttf", 20);
        productsans = load("moonware/font/productsans.ttf", 20);
        raleway = load("moonware/font/raleway.ttf", 20);
        kollektif = load("moonware/font/kollektif.ttf", 20);
        aldotheapache = load("moonware/font/apache.ttf", 20);
        museosans = load("moonware/font/museo.ttf", 20);
        tahoma = load("moonware/font/tahoma.ttf", 20);
        circleregular = load("moonware/font/circle-regular.ttf", 19);
        menlo = load("moonware/font/menlo.ttf", 19);
    }

    public static CustomFont load(String path, float size) {
        try (InputStream in = FontStorage.class.getResourceAsStream("/assets/minecraft/" + path)) {
            return new CustomFont(Font.createFont(Font.TRUETYPE_FONT, in).deriveFont(size));
        } catch (Exception e) {
            throw new RuntimeException("Unable to load deprecated font " + path, e);
        }
    }
}
