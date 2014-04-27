package hexapode;

/**
 * Vecteur de dimension 2. Utilisé pour les positions.
 * @author pf
 *
 */

public class Vec2
{
    public double x;
    public double y;
    
    public Vec2(double x, double y)
    {
        this.x = x;
        this.y = y;
    }
    
    public Vec2(Vec2 other)
    {
        x = other.x;
        y = other.y;
    }
    
    public void sub(Vec2 other)
    {
        x -= other.x;
        y -= other.y;
    }
    
    public void add(Vec2 other)
    {
        x += other.x;
        y += other.y;
    }
    
    public void copy(Vec2 other)
    {
        other.x = x;
        other.y = y;
    }
    
    public static double scalaire(Vec2 a, Vec2 b)
    {
        return a.x*b.x+a.y*b.y;
    }
    
    @Override
    public String toString()
    {
        return "("+x+","+y+")";
    }
}
