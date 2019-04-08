/**
 * Basic package information
 */
package me.mrexplode.deployTest;

import static org.fusesource.jansi.Ansi.ansi;

import java.util.Random;

import org.fusesource.jansi.Ansi.Attribute;
import org.fusesource.jansi.Ansi.Color;
import org.fusesource.jansi.AnsiConsole;
import org.fusesource.jansi.AnsiRenderer;

/**
 * A Logger class that features colored logging.
 * 
 * @author <a href="https://mrexplode.github.io">MrExplode</a>
 *
 */
public class Logger {
    
    /**
     * No use in program, this helps me to keep this class sync in 2 projects
     */
    public static final String CLASS_VERSION = "1.4";
    
    /**
     * The FileCorrector ANSI logo, line by line
     */
    public static final String[] FNCORRECTOR_LOGO = {
            "&e,------.&a,--.&e,--.       ,--.  ,--.                          ,-----.                                    ,--.                ",
            "&e|  .---'&a`--'&e|  | ,---. |  ,'.|  | ,--,--.,--,--,--. ,---. '  .--./ ,---. ,--.--.,--.--. ,---.  ,---.,-'  '-. ,---. ,--.--.",
            "&e|  `--, ,--.|  || .-. :|  |' '  |' ,-.  ||        || .-. :|  |    | .-. ||  .--'|  .--'| .-. :| .--''-.  .-'| .-. ||  .--'",
            "&e|  |`   |  ||  |\\   --.|  | `   |\\ '-'  ||  |  |  |\\   --.'  '--'\\' '-' '|  |   |  |   \\   --.\\ `--.  |  |  ' '-' '|  |   ",
            "&e`--'    `--'`--' `----'`--'  `--' `--`--'`--`--`--' `----' `-----' `---' `--'   `--'    `----' `---'  `--'   `---' `--'   "
    };
    
    /**
     * The FileMirror ANSI logo, line by line
     */
    public static final String[] FILEMIRROR_LOGO = {
            "&e,------.&a,--.&e,--.       ,--.   ,--.&a,--.                            ",
            "&e|  .---'&a`--'&e|  | ,---. |   `.'   |&a`--'&e,--.--.,--.--. ,---. ,--.--.",
            "&e|  `--, ,--.|  || .-. :|  |'.'|  |,--.|  .--'|  .--'| .-. ||  .--'",
            "&e|  |`   |  ||  |\\   --.|  |   |  ||  ||  |   |  |   ' '-' '|  |   ",
            "&e`--'    `--'`--' `----'`--'   `--'`--'`--'   `--'    `---' `--'   "
    };

    private static boolean DEBUG = false;
    
    private Random rnd;
    
    public ColorCorrector textColor;
    /**
     * The character that marks that the following number is a color ID
     */
    public static char codeMarker = '&';
    private ColorCorrector backgroundColor;
    
    /**
     * Creates a new instance of the Logger class, and installs the AnsiConsole to the System.out
     */
    public Logger() {
        AnsiConsole.systemInstall();
        this.rnd = new Random();
        this.textColor = ColorCorrector.DEFAULT;
        this.backgroundColor = ColorCorrector.DEFAULT;
    }
    /**
     * Uninstalls the ANSI Console.
     * 
     * @see AnsiConsole#systemUninstall()
     */
    public void uninstall() {
        AnsiConsole.systemUninstall();
    }
    
    private void defaults() {
        System.out.print(ansi().reset());
        textColor.apply();
        backgroundColor.applyToBackGround();
    }
    
    /**
     * Apply the color to the background. If the color is white, the text color is set to black.
     * 
     * @param color
     */
    public void setBackgroundColor(ColorCorrector color) {
        if (color == null) {color = ColorCorrector.DEFAULT;}
        this.backgroundColor = color;
        if (backgroundColor == ColorCorrector.WHITE) {
            textColor = ColorCorrector.BLACK;
            textColor.apply();
        } else {
            textColor = ColorCorrector.DEFAULT;
            textColor.apply();
        }
        backgroundColor.applyToBackGround();
    }
    
    /**
     * Prints a colored message, which must be formatted to the {@link AnsiRenderer}
     * 
     * @deprecated Use the {@link #log(String)} method, since it can do better formatting.
     * 
     * @param msg The displayed message
     */
    @Deprecated
    public void logANSI(String msg) {
        if (!msg.contains("|@") && !msg.contains("|@")) {
            System.out.println(msg);
        } else if (msg.contains("|@") || msg.contains("|@")) {
            log("[WARNING]%% Wrong formatted AnsiRenderer text!", ColorCorrector.YELLOW, ColorCorrector.DEFAULT);
            System.out.println(msg);
        }
        System.out.println(ansi().render(msg) + textColor.asString());
    }
    
    /**
     * Prints a single colored message.
     * 
     * @param msg The displayed message
     * @param color The color of the message
     */
    public void log(String msg, ColorCorrector color) {
        color.apply();
        System.out.println(msg);
        defaults();
    }
    
    /**
     * Prints a multi-colored message, without the {@link AnsiRenderer}.<br>
     * Coloring possible by splitting the text with %% characters. Example:<br>
     * <pre>
     * log("Let's get%% this %%bread!", ColorCorrector.RED, ColorCorrector.BLUE, ColorCorrector.LIGHT_RED);
     * </pre>
     * 
     * 
     * @param msg The message
     * @param colors The colors for each separated block
     */
    public void log(String msg, ColorCorrector... colors) {
        String[] split = msg.split("%%");
        for (int i = 0; i < split.length; i++) {
            colors[i].apply();
            System.out.print(split[i]);
        }
        System.out.println();
        defaults();
    }
    
    /**
     * Logs a multi-colored message, that can be colored by chat codes.
     * 
     * You can see the formatting at {@link ColorCorrector} documentation.
     * 
     * @param msg The message
     */
    public void log(String msg) {
        int index = 0;
        ColorCorrector[] values = ColorCorrector.values();
        while (index != -1) {
            //a pretty bug handling. without that it woudl look like msg.indexOf(codeMarker, index + 1);
            //bug: if the string starts with the codemarker, without handling we skip that coloring
            index = msg.indexOf(codeMarker, index + (index == 0 ? 0 : 1));
            char operator = msg.charAt(index + 1);
            //but without this, we stuck infinite on 0 index
            index += (index == 0 ? 1 : 0);
            int enumIndex = ColorCorrector.keyString().indexOf(operator);
            if (enumIndex > -1) {
                msg = msg.replaceAll(String.valueOf(codeMarker) + String.valueOf(operator), values[enumIndex].asString());
            }
        }
        
        System.out.println(msg);
        defaults();
    }
    
    /**
     * Logs a debug message if the DEBUG set to true.
     * @param msg The message
     */
    public void debug(String msg) {
        if (DEBUG) {
            log("&e[DEBUG]&d " + msg);
        }
    }
    
    /**
     * Print the FileNameCorrector logo to the console, with the specified Color.
     * 
     * @param backg The color of the Logo
     */
    public void printFNCLogo(ColorCorrector backg) {
        setBackgroundColor(backg);
        for (int i = 0; i < FNCORRECTOR_LOGO.length; i++) {
            log(FNCORRECTOR_LOGO[i]);
        }
        setBackgroundColor(null);
        defaults();
    }
    
    /**
     * Print the FileMirror logo to the console, with the specified Color.
     * 
     * @param backg The color of the Logo
     */
    public void printFMLogo(ColorCorrector backg) {
        setBackgroundColor(backg);
        for (int i = 0; i < FILEMIRROR_LOGO.length; i++) {
            log(FILEMIRROR_LOGO[i]);
        }
        setBackgroundColor(null);
        defaults();
    }
    
    /**
     * Return a random ColorCorrector instance.
     * 
     * @return the color
     */
    public ColorCorrector randomColor() {
        return ColorCorrector.values()[rnd.nextInt(14)];
    }
}
/**
 * A color holder enum, for bright values too, used in {@link Logger} class.
 * <br>I highly suggest only using the light colors, because the normal ones looks ugly as hell (on Windows). Also, none of non-color formatting works<br>
 * <br>Here you can see the formatting:<br>
 * <table>
 * <tr>
 *      <td>Black</td><td>&0</td>
 *      </tr>
 * <tr>
 *      <td>Red</td><td>&1</td>
 *      </tr>
 * <tr>
 *      <td>Green</td><td>&2</td>
 *      </tr>
 * <tr>
 *      <td>Yellow</td><td>&3</td>
 *      </tr>
 * <tr>
 *      <td>Blue</td><td>&4</td>
 *      </tr>
 * <tr>
 *      <td>Magenta</td><td>&5</td>
 *      </tr>
 * <tr>
 *      <td>Cyan</td><td>&6</td>
 *      </tr>
 * <tr>
 *      <td>White</td><td>&7</td>
 *      </tr>
 * <tr>
 *      <td>Light Red</td><td>&a</td>
 *      </tr>
 * <tr>
 *      <td>Light Green</td><td>&c</td>
 *      </tr>
 * <tr>
 *      <td>Light Yellow</td><td>&e</td>
 *      </tr>
 * <tr>
 *      <td>Light Blue</td><td>&f</td>
 *      </tr>
 * <tr>
 *      <td>Light Magenta</td><td>&g</td>
 *      </tr>
 * <tr>
 *      <td>Light Cyan</td><td>&h</td>
 *      </tr>
 * <tr>
 *      <td>Default</td><td>&d</td>
 *      </tr>
 * <tr>
 *      <td>Bold</td><td>&b</td>
 *      </tr>
 * <tr>
 *      <td>Italic</td><td>&i</td>
 *      </tr>
 * <tr>
 *      <td>Underline</td><td>&u</td>
 *      </tr>
 * <tr>
 *      <td>Striketrough</td><td>&s</td>
 *      </tr>
 * <tr>
 *      <td>Blink slow</td><td>&r</td>
 *      </tr>
 * <tr>
 *      <td>Blink fast</td><td>&R</td>
 *      </tr>
 * </table>
 * 
 * @author <a href="https://mrexplode.github.io">MrExplode</a>
 *
 */
enum ColorCorrector {

    BLACK(Color.BLACK, "0"),
    RED(Color.RED, "1"),
    LIGHT_RED(Color.RED, "a"),
    GREEN(Color.GREEN, "2"),
    LIGHT_GREEN(Color.GREEN, "c"),
    YELLOW(Color.YELLOW, "3"),
    LIGHT_YELLOW(Color.YELLOW, "e"),
    BLUE(Color.BLUE, "4"),
    LIGHT_BLUE(Color.BLUE, "f"),
    MAGENTA(Color.MAGENTA, "5"),
    LIGHT_MAGENTA(Color.MAGENTA, "g"),
    CYAN(Color.CYAN, "6"),
    LIGHT_CYAN(Color.CYAN, "h"),
    WHITE(Color.WHITE, "7"),
    DEFAULT(Color.DEFAULT, "d"),
    
    BOLD(Attribute.INTENSITY_BOLD, "b"),
    ITALIC(Attribute.ITALIC, "i"),
    STRIKE(Attribute.STRIKETHROUGH_ON, "s"),
    UNDERLINE(Attribute.UNDERLINE, "u"),
    BLINK_SLOW(Attribute.BLINK_SLOW, "r"),
    BLINK_FAST(Attribute.BLINK_FAST, "R");
    
    private Color parentColor = null;
    private Attribute parentAttribute = null;
    public String key;
    
    ColorCorrector(Color reference, String key) {
        this.parentColor = reference;
        this.key = key;
    }
    
    ColorCorrector(Attribute reference, String key) {
        this.parentAttribute = reference;
        this.key = key;
    }
    
    /**
     * @return All keys in order.
     */
    public static String keyString() {
        ColorCorrector[] val = ColorCorrector.values();
        String k = new String();
        for (int i = 0; i < val.length; i++) {
            k = k + val[i].key;
        }
        return k;
    }
    
    public String asAlternate() {
        return Logger.codeMarker + key;
    }
    
    /**
     * Apply the formatting to the ANSI output.
     * This is necessary since the original JAnsi API don't have the color enum specified by the light level.
     */
    public void apply() {
        if (parentColor != null) {
            if (this.name().startsWith("LIGHT")) {
                System.out.print(ansi().fgBright(parentColor));
            } else {
                System.out.print(ansi().fg(parentColor));
            }
        } else {
            System.out.print(ansi().a(parentAttribute));
        }
    }
    
    /**
     * Returns the formatting as the string escape code
     * @return escape code
     */
    public String asString() {
        if (parentColor != null) {
            if (this.name().startsWith("LIGHT")) {
                return ansi().fgBright(parentColor).toString();
            } else {
                return ansi().fg(parentColor).toString();
            }
        } else {
            return ansi().a(parentAttribute).toString();
        }
    }
    
    /**
     * Apply the color to the backGround
     */
    public void applyToBackGround() {
        //no check for attribute, since who wanna have a bold background
        if (this.name().startsWith("LIGHT")) {
            System.out.print(ansi().bgBright(parentColor));
        } else {
            System.out.print(ansi().bg(parentColor));
        }
    }
}