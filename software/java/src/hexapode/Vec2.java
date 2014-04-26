package hexapode;

/**
 * Vecteur de dimension 2. Utilisé pour les positions.
 * @author pf
 *
 */

public class Vec2
{
    public int x;
    public int y;
    
    public Vec2(int x, int y)
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
}
