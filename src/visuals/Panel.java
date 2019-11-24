package visuals;

import core.vector.Vector2d;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Panel extends JPanel {


    protected Vector2d center = new Vector2d(0, 0);
    protected Vector2d scale = new Vector2d(2, 2);

    protected Vector2d toScreenSpace(Vector2d vector2d) {
        return new Vector2d(
                this.getWidth() * ((vector2d.getX() - (center.getX() - scale.getX() / 2)) / (scale.getX())),
                this.getHeight() * (1 - (vector2d.getY() - (center.getY() - scale.getY() / 2)) / (scale.getY()))
        );
    }

    protected Vector2d toWorldSpace(Vector2d screenSpace) {
        return new Vector2d(
                scale.getX() * screenSpace.getX() / this.getWidth() + (center.getX() - scale.getX() / 2),
                scale.getY() * (1 - screenSpace.getY() / this.getHeight()) + (center.getY() - scale.getY() / 2)
        );
    }

    private static Vector2d midpoint(Vector2d p1, Vector2d p2) {
        return new Vector2d((int) ((p1.getX() + p2.getX()) / 2.0),
                (int) ((p1.getY() + p2.getY()) / 2.0));
    }

    public void zoom(double factor) {
        this.scale.self_scale(factor);
    }

    public Vector2d getCenter() {
        return center;
    }

    public Vector2d getScale() {
        return scale;
    }

    public void move(double percentage_x, double percentage_y) {
        this.center.self_add(new Vector2d(this.scale.getX() * percentage_x, this.scale.getY() * percentage_y));
    }

    public void setCenter(Vector2d center) {
        this.center = center;
    }

    public void setScale(Vector2d scale) {
        this.scale = scale;
    }

    public void move(Vector2d dp) {
        this.center.self_add(dp);
    }

    protected static void draw_arrow(Graphics2D g2d, Vector2d start, Vector2d end, int width, int arrow_width, int arrow_length) {
        Polygon arrowPolygon = new Polygon();

        double ptDistance = (int) end.sub(start).length();

        arrowPolygon.addPoint(-(int) (ptDistance / 2), (width / 2));
        arrowPolygon.addPoint((int) (ptDistance / 2) - arrow_length, (width / 2));
        arrowPolygon.addPoint((int) (ptDistance / 2) - arrow_length, (arrow_width / 2));
        arrowPolygon.addPoint((int) (ptDistance / 2), 0);
        arrowPolygon.addPoint((int) (ptDistance / 2) - arrow_length, -(arrow_width / 2));
        arrowPolygon.addPoint((int) (ptDistance / 2) - arrow_length, -(width / 2));
        arrowPolygon.addPoint(-(int) (ptDistance / 2), -(width / 2));


        Vector2d midPoint = midpoint(start, end);

        double rotate = Math.atan2(end.getY() - start.getY(), end.getX() - start.getX());

        AffineTransform transform = new AffineTransform();
        transform.translate(midPoint.getX(), midPoint.getY());
        transform.rotate(rotate);

        g2d.fill(transform.createTransformedShape(arrowPolygon));
    }

    protected static void draw_centered_string(Graphics2D g, String s, int x, int y, Font font) {
        FontRenderContext frc = new FontRenderContext(null, true, true);
        Rectangle2D r2D = font.getStringBounds(s, frc);

        g.setFont(font);
        g.drawString(s, x - (int) (r2D.getWidth() / 2), y + (int) (r2D.getHeight() / 3));
    }

    protected static void draw_centered_string(Graphics2D g, String s, Rectangle r, Font font) {
        FontRenderContext frc = new FontRenderContext(null, true, true);

        Rectangle2D r2D = font.getStringBounds(s, frc);
        int rWidth = (int) Math.round(r2D.getWidth());
        int rHeight = (int) Math.round(r2D.getHeight());
        int rX = (int) Math.round(r2D.getX());
        int rY = (int) Math.round(r2D.getY());

        int a = r.width / 2 - rWidth / 2 - rX;
        int b = r.height / 2 - rHeight / 2 - rY;

        g.setFont(font);
        g.drawString(s, r.x + a, r.y + b);
    }

    protected void draw_grid(Graphics2D g) {
        int power = (int) Math.floor(Math.log10(scale.getX())) - 1;
        double w_x = Math.pow(10, power);

        double start_x = Math.floor((toWorldSpace(new Vector2d(0,0)).getX()) / w_x) * w_x;
        double end_y = Math.floor((toWorldSpace(new Vector2d(0,0)).getY()) / w_x) * w_x;

        double end_x = Math.floor((toWorldSpace(new Vector2d(this.getWidth(),this.getHeight())).getX()) / w_x) * w_x;
        double start_y = Math.floor((toWorldSpace(new Vector2d(this.getWidth(),this.getHeight())).getY()) / w_x) * w_x;

//        double end_x = Math.floor((center.getX() + scale.getX() / 2) / w_x) * w_x + w_x;
//        double end_y = Math.floor((center.getY() + scale.getY() / 2) / w_x) * w_x + w_x;

        Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1, new float[]{1}, 0);
        Stroke full = new BasicStroke(2);
        g.setStroke(dashed);


        for (double x = start_x; x <= end_x; x += w_x) {
            g.setStroke(Math.abs(x) < 1E-14 ? full : dashed);
            int screen_x = (int) toScreenSpace(new Vector2d(x, 0)).getX();
            g.drawLine(screen_x, 0, screen_x, this.getHeight());
            String num = "" + BigDecimal.valueOf(x).setScale(-power, RoundingMode.HALF_UP).doubleValue();
            g.drawString(num, screen_x, this.getHeight() - 10);

        }
        for (double y = start_y; y <= end_y; y += w_x) {
            g.setStroke(Math.abs(y) < 1E-14 ? full : dashed);
            int screen_y = (int) toScreenSpace(new Vector2d(0, y)).getY();
            g.drawLine(0, screen_y, this.getWidth(), screen_y);
            String num = "" + BigDecimal.valueOf(y).setScale(-power, RoundingMode.HALF_UP).doubleValue();
            g.drawString(num, this.getWidth() - 30, screen_y);

        }

    }

}
