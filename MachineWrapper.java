import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;

public class MachineWrapper 
{
    private static int MACHINE_WIDTH=150;
    private static int MACHINE_HEIGHT=55;
    private static Color COLOR = Color.BLUE;
    private static Color DETAIL_COLOR = Color.RED;

    private static int defaultMachineNumber = 1;

    private Machine machine;
    private int x, y;
    private Color color;
    private boolean showDetails;
    private int width, height;

    public MachineWrapper( String name, int x, int y )
    {
        if ( name == null )
        {
            name = "M " + defaultMachineNumber;
            defaultMachineNumber++;
        }
        this.machine = new Machine( name );
        this.x = x;
        this.y = y;
        this.color = MachineWrapper.COLOR;
        this.showDetails = false;
        this.width = MACHINE_WIDTH;
        this.height = MACHINE_HEIGHT;
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    public void setX( int x )
    {
        this.x = x;
    }

    public void setY( int y )
    {
        this.y = y;
    }

    public Machine getMachine()
    {
        return this.machine;
    }

    public int getWidth()
    {
        return this.width;
    }

    public int getHeight()
    {
        return this.height;
    }

    public void setColor( Color c )
    {
        this.color = c;
    }

    public void showDetails( boolean value )
    {
        this.showDetails = value;
    }

    public void toggleDetails()
    {
        this.showDetails = !this.showDetails;
    }

    private void stateInfo( Graphics g )
    {
        Packet[] trans = this.machine.getTransitPackets();
        Packet[] mine = this.machine.getMyPackets();
        int width = this.width / 2;

        g.setColor( Color.WHITE );
        g.fillRect( this.x, this.y, this.width, 4 );

        if ( trans.length > 0 )
        {
            g.setColor( Color.GRAY );
            g.fillRect( this.x, this.y, width, 4 );
            g.setColor( Color.BLACK );
            double fill = width * trans.length /
                          this.machine.getBufferSize();
            if ( fill > width ) fill = width;
            g.fillRect( this.x, this.y, (int)fill, 4 );
        }

        if ( mine.length > 0 )
        {
            int absoluteTotal = 0;
            Map<Machine,Boolean> tally = new HashMap<>();
            for ( Packet each : mine )
                if ( !tally.containsKey( each.getSource() ) )
                {
                    tally.put( each.getSource(), true );
                    absoluteTotal += each.getTotalPackets() - 1;
                }
            g.setColor( Color.RED );
            g.fillRect( this.x + width, this.y, width, 4 );
            g.setColor( Color.GREEN );
            double fill = (this.width - width - 1) * 
                mine.length / absoluteTotal;
            fill = Math.min( fill, this.width - width - 1 );
            g.fillRect( this.x + width, this.y, (int)fill, 4 );
        }
        g.setColor( Color.BLACK );
        g.drawRect( this.x, this.y, this.width - width, 4 );
        g.drawRect( this.x + width, this.y, this.width - width - 1, 4 );
    }

    private void detailedView( Graphics g )
    {
        this.stateInfo( g );
        Packet[] trans = this.getMachine().getTransitPackets() ;
        Packet[] fin = this.getMachine().getMyPackets() ;

        this.height = 25 + 15 * (trans.length + fin.length);
        g.setColor( DETAIL_COLOR );
        g.fillRect( this.x, this.y + 5, this.width, this.height );

        g.setColor( Color.GRAY );
        g.fillRect( this.x + 2, this.y + 20, this.width - 4, trans.length * 15 );
        g.setColor( Color.BLACK );
        g.drawString( this.machine.getName(), this.x + 5, this.y + 15 );
        int line = 35;
        for ( Packet each : trans )
        {
            g.drawString( 
                String.format("%s (TTL: %d)", each.getPayload(), each.getTimeToLive() ) ,
                this.x + 10, this.y + line
            );
            line += 15;
        }
        g.setColor( Color.WHITE );
        g.fillRect( this.x + 2, this.y + line-15, this.width - 4, fin.length * 15 );
        g.setColor( Color.BLACK );
        if ( fin.length > 0 )
        {
            for ( Packet each : fin )
            {
                g.drawString( 
                    String.format("%s (%s# %d/%d)", 
                        each.getPayload(), each.getSource().getName(), each.getOrder(), each.getTotalPackets() - 1 ) ,
                    this.x + 10, this.y + line
                );
                line += 15;
            }
        }
    }

    private void networkView( Graphics g )
    {
        this.width = MachineWrapper.MACHINE_WIDTH;
        this.height = MachineWrapper.MACHINE_HEIGHT;
        this.stateInfo( g );
        g.setColor( this.color );
        g.fillRect( this.x, this.y + 5, this.width, this.height );
        g.setColor( Color.WHITE );
        String display = this.machine.getName();
        if ( this.machine.hasMessage() ) display += "*";
        g.drawString( display, this.x + 5, this.y + 15 );
        int transitPackets = this.machine.getTransitPacketCount();
        g.drawString( "In Transit: " + transitPackets, 
            this.x + 10, this.y + 30 );
        int myPackets = this.machine.getMyPacketCount();
        g.drawString( "My Packets: " + myPackets, 
            this.x + 10, this.y + 45 );
    }

    public void paint( Graphics g, boolean justShadows )
    {
        if ( justShadows )
        {
            g.setColor( new Color( 180, 180, 255 ) );
            g.fillRect( this.x + 5, this.y + 5, this.width, this.height + 5 );
        }
        else
        {
            if ( this.showDetails ) detailedView( g );
            else networkView( g );
        }
    }
}
