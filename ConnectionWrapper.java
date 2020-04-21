import java.awt.*;
import java.util.*;

public class ConnectionWrapper
{
    private static java.util.List<ConnectionWrapper> connections;

    static
    {
        ConnectionWrapper.connections = new ArrayList<ConnectionWrapper>();
    }

    public static void breakConnectionsWith( Machine m )
    {
        for ( int i = ConnectionWrapper.connections.size() - 1; i >= 0; i-- )
        {
            ConnectionWrapper conn = ConnectionWrapper.connections.get( i );
            if ( conn.machines[0].getMachine().equals( m ) || 
                 conn.machines[1].getMachine().equals( m ) ) 
            {
                conn.machines[0].getMachine().breakConnection( conn.machines[1].getMachine() );
                ConnectionWrapper.connections.remove( conn );
            }
        }
    }

    public static boolean isOnConnection()
    {
        for ( ConnectionWrapper each : ConnectionWrapper.connections )
            if ( each.highlight )
                return true;
        return false;
    }

    public static void breakHighlightedConnection()
    {
        for ( int i = ConnectionWrapper.connections.size() - 1; i >= 0; i-- )
        {
            ConnectionWrapper conn = ConnectionWrapper.connections.get( i );
            if ( conn.highlight )
            {
                conn.machines[0].getMachine().breakConnection( conn.machines[1].getMachine() );
                ConnectionWrapper.connections.remove( conn );
            }
        }
    }

    public static void highlight( int x, int y )
    {
        for ( ConnectionWrapper each : ConnectionWrapper.connections )
        {
           if ( each.pointOnConnection( x, y ) ) 
              each.highlight = true;
           else 
              each.highlight = false;
        }
    }

    public static void drawAll( Graphics g )
    {
        for ( ConnectionWrapper each : ConnectionWrapper.connections )
            each.draw( g );
    }

    public static ConnectionWrapper get(MachineWrapper one, MachineWrapper two)
    {
        for ( ConnectionWrapper each : ConnectionWrapper.connections )
            if ( each.isSameAs( one, two ) ) 
                return each;
        return new ConnectionWrapper( one, two );
    }

    private MachineWrapper[] machines;
    private boolean highlight;

    private ConnectionWrapper( MachineWrapper one, MachineWrapper two )
    {
        this.machines = new MachineWrapper[]{ one, two };
        ConnectionWrapper.connections.add( this );
        one.getMachine().connectTo( two.getMachine() );
    }

    public boolean isSameAs( MachineWrapper one, MachineWrapper two )
    {
        if ( this.machines[0].equals( one ) && this.machines[1].equals(two) )
            return true;
        if ( this.machines[0].equals( two ) && this.machines[1].equals(one) )
            return true;
        return false;
    }
    public boolean pointOnConnection( int x, int y )
    {
        MachineWrapper one = this.machines[0];
        MachineWrapper two = this.machines[1];

        double[] a = 
            {one.getX() + one.getWidth()/2., one.getY() + one.getHeight()/2.};
        double[] b = 
            {two.getX() + one.getWidth()/2., two.getY() + two.getHeight()/2.};
        double[] c = { x, y };

        double result = Math.abs( 
            ( c[1] - a[1] ) * ( b[0] - a[0] ) - 
            ( b[1] - a[1] ) * ( c[0] - a[0] ) ) ;

        boolean itsOn = false;
        if ( result < 1000. )
        {
            double minX = Math.min( a[0], b[0] );
            double maxX = Math.max( a[0], b[0] );
            double minY = Math.min( a[1], b[1] );
            double maxY = Math.max( a[1], b[1] );
            if ( c[0] > minX && c[0] < maxX && c[1] > minY && c[1] < maxY )
                itsOn = true;
        }
        return itsOn;
    }
    private void draw( Graphics g )
    {
        int startX = this.machines[0].getX() + this.machines[0].getWidth()/2;
        int startY = this.machines[0].getY() + this.machines[0].getHeight()/2;
        int endX = this.machines[1].getX() + this.machines[1].getWidth()/2;
        int endY = this.machines[1].getY() + this.machines[1].getHeight()/2;
        g.setColor( Color.BLACK );
        if ( this.highlight ) g.setColor( Color.ORANGE );
        g.drawLine( startX, startY, endX, endY );
    }
}
