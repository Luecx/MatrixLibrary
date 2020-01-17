package visuals;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.function.Function;

public enum ColorPalette {
    RAINBOW(aDouble -> {
        return Color.getHSBColor((float) (4f / 6f + 1 / 2f * aDouble), 1, 1);
    }),
    HEAT(aDouble -> {
        return Color.getHSBColor((float) (1 / 6f * aDouble), 1f, 0.8f);
    });


    private Function<Double, Color> function;

    public Color apply(double v) {
        if (v >= 0 && v <= 1)
            return function.apply(v);
        return Color.black;
    }

    ColorPalette(Function<Double, Color> function) {
        this.function = function;
    }

    public void drawColorPalette(Graphics2D graphics2D, double min, double max, int subd, int height) {
        graphics2D.setColor(Color.white);
        graphics2D.fillRect(0,0,130,height+ 60);

        int cell = (int)(height / (double)subd);
        int counter = 0;
        for(double i = 1; i >= 0; i-= 1d/(subd-1)){
            graphics2D.setColor(this.apply(i));
            graphics2D.fillRect(30,(int)(30 + (height / (double)subd) * counter)-1,40,cell);
            counter ++;
        }
        graphics2D.setColor(Color.black);

        DecimalFormat format = new DecimalFormat("#.###E0");
        Panel.draw_centered_string(graphics2D, format.format(max),90, 30, new Font("Arial", Font.PLAIN, 18));
        Panel.draw_centered_string(graphics2D, format.format(min),90, height, new Font("Arial", Font.PLAIN, 18));
    }
}

