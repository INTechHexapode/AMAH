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
    
    /**
     * Constructeur à partir des coordonnées
     * @param x
     * @param y
     */
    public Vec2(double x, double y)
    {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Constructeur à partir d'un autre Vec2
     * @param other
     */
    public Vec2(Vec2 other)
    {
        x = other.x;
        y = other.y;
    }
    
    /**
     * this -= other
     * @param other
     */
    public void sub(Vec2 other)
    {
        x -= other.x;
        y -= other.y;
    }
    
    /**
     * this += other
     * @param other
     */
    public void add(Vec2 other)
    {
        x += other.x;
        y += other.y;
    }
    
    /**
     * copy this dans other.
     * @param other
     */
    public void copy(Vec2 other)
    {
        other.x = x;
        other.y = y;
    }
    
    /**
     * Effectue le produit scalaire entre a et b
     * @param a
     * @param b
     * @return
     */
    public static double scalaire(Vec2 a, Vec2 b)
    {
        return a.x*b.x+a.y*b.y;
    }

    public double length()
    {
        return Math.sqrt(x*x+y*y);
    }

    public double squared_distance(Vec2 o)
    {
        return (x-o.x)*(x-o.x)+(y-o.y)*(y-o.y);
    }
    
    public Vec2 clone()
    {
        return new Vec2(this);
    }

    
    @Override
    public boolean equals(Object other)
    {
        if(other == null)
            return false;
        if(!(other instanceof Vec2))
            return false;
        Vec2 a = (Vec2) other;
        return x == a.x && y == a.y;
    }
    
    /**
     * Renvoie la distance du point au bord du terrain.
     * @return
     */
    public double distance_au_bord()
    {
        double min = y;
        if(Math.abs(2000-y) < min)
            min = Math.abs(2000-y);
        if(Math.abs(1500-x) < min)
            min = Math.abs(1500-x);
        if(Math.abs(1500+x) < min)
            min = Math.abs(1500+x);
        return min;
    }
    
    @Override
    public String toString()
    {
        return "("+x+","+y+")";
    }
}
