package visuals;

import core.vector.Vector2d;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;

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

    public static void draw_arrow(Graphics2D g2d, Vector2d start, Vector2d end, int width, int arrow_width, int arrow_length) {
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

    public static void draw_centered_string(Graphics2D g, String s, int x, int y, Font font) {
        FontRenderContext frc = new FontRenderContext(null, true, true);
        Rectangle2D r2D = font.getStringBounds(s, frc);

        g.setFont(font);
        g.drawString(s, x - (int) (r2D.getWidth() / 2), y + (int) (r2D.getHeight() / 3));
    }

    public static Rectangle2D draw_centered_string(Graphics2D g, String s, Rectangle r, Font font) {
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
        return r2D;
    }

    public static Rectangle2D get_centered_string_bounds(String s, Font font){
        FontRenderContext frc = new FontRenderContext(null, true, true);

        Rectangle2D r2D = font.getStringBounds(s, frc);
        return r2D;
    }

    public static void draw_triangle_gradient(Graphics2D g2d, Polygon triangle, Color color1, Color color2, Color color3){

        Vector2d p1 = new Vector2d(triangle.xpoints[0], triangle.ypoints[0]);
        Vector2d p2 = new Vector2d(triangle.xpoints[1], triangle.ypoints[1]);
        Vector2d p3 = new Vector2d(triangle.xpoints[2], triangle.ypoints[2]);
        Vector2d t1 = getPerpendicularPointOnLine(p2,p3,p1);
        Vector2d t2 = getPerpendicularPointOnLine(p3,p1,p2);
        Vector2d t3 = getPerpendicularPointOnLine(p1,p2,p3);

//        t1 = t1.add(t1.sub(p1).self_scale(-0.3));
//        t2 = t2.add(t2.sub(p2).self_scale(-0.3));
//        t3 = t3.add(t3.sub(p3).self_scale(-0.3));
        Color empty = new Color(0,0,0,0);

        GradientPaint gradientPaint1 = new GradientPaint(
                (int)p1.getX(), (int)p1.getY(), color1,
                (int)t1.getX(), (int)t1.getY(), empty);
        GradientPaint gradientPaint2 = new GradientPaint(
                (int)p2.getX(), (int)p2.getY(), color2,
                (int)t2.getX(), (int)t2.getY(), empty);
        GradientPaint gradientPaint3 = new GradientPaint(
                (int)p3.getX(), (int)p3.getY(), color3,
                (int)t3.getX(), (int)t3.getY(), empty);

//        Composite old = g2d.getComposite();
//        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 1));
        g2d.setColor(new Color(
                (color1.getRed() + color2.getRed() + color3.getRed())/3,
                (color1.getGreen() + color2.getGreen() + color3.getGreen())/3,
                (color1.getBlue() + color2.getBlue() + color3.getBlue())/3,
                255
        ));
        g2d.fillPolygon(triangle);
        //g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

        g2d.setPaint(gradientPaint1);
        g2d.fillPolygon(triangle);
        g2d.setPaint(gradientPaint2);
        g2d.fillPolygon(triangle);
        g2d.setPaint(gradientPaint3);
        g2d.fillPolygon(triangle);
    }

    public static Vector2d getPerpendicularPointOnLine(Vector2d lineP1, Vector2d lineP2, Vector2d point){
        double k = ((lineP2.getY()-lineP1.getY()) * (point.getX()-lineP1.getX()) -
                (lineP2.getX()-lineP1.getX()) * (point.getY()-lineP1.getY())) /
                ((lineP2.getY()-lineP1.getY()) * (lineP2.getY()-lineP1.getY()) +
                        (lineP2.getX()-lineP1.getX()) * (lineP2.getX()-lineP1.getX()));
        double x4 = point.getX() - k * (lineP2.getY()-lineP1.getY());
        double y4 = point.getY() + k * (lineP2.getX()-lineP1.getX());
        return new Vector2d(x4,y4);
    }

    protected void draw_grid(Graphics2D g) {
        int power = (int) Math.floor(Math.log10(scale.getX())) - 1;
        double w_x = Math.pow(10, power);

        double start_x = Math.floor((toWorldSpace(new Vector2d(0,0)).getX()) / w_x) * w_x;
        double end_y = Math.floor((toWorldSpace(new Vector2d(0,0)).getY()) / w_x) * w_x;

        double end_x = Math.floor((toWorldSpace(new Vector2d(this.getWidth(),this.getHeight())).getX()) / w_x) * w_x;
        double start_y = Math.floor((toWorldSpace(new Vector2d(this.getWidth(),this.getHeight())).getY()) / w_x) * w_x;

//        double end_x = Math.floor((center.getX() + self_scale.getX() / 2) / w_x) * w_x + w_x;
//        double end_y = Math.floor((center.getY() + self_scale.getY() / 2) / w_x) * w_x + w_x;

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

    public static void main(String[] args) {

        for(int i = 0; i < 1; i++){
            int finalI = i;
            Panel panel = new Panel(){
                @Override
                protected void paintComponent(Graphics g) {
                    g.clearRect(0,0,this.getWidth(), this.getHeight());
                    g.setColor(finalI == 0 ? Color.red:Color.red);
//                    g.fillRect(0,0,this.getWidth(), this.getHeight());
//                    g.setColor(Color.black);
//                    g.drawLine(0,300,1000,400);

                    Polygon polygon = new Polygon();
                    polygon.addPoint(100,100);
                    polygon.addPoint(700,400);
                    polygon.addPoint(400,700);
                    polygon.addPoint(100,100);
                    draw_triangle_gradient((Graphics2D) g, polygon, Color.RED, Color.GREEN, Color.BLUE);

                    Polygon polygon2 = new Polygon();
                    polygon2.addPoint(100,100);
                    polygon2.addPoint(700,400);
                    polygon2.addPoint(800,30);
                    polygon2.addPoint(100,100);
                    draw_triangle_gradient((Graphics2D) g, polygon2, Color.RED, Color.GREEN, Color.BLUE);
                }
            };


            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(2);
            frame.setLayout(new BorderLayout());
            frame.add(panel);
            frame.setSize(800,800);
            frame.setVisible(true);
        }

    }

}
