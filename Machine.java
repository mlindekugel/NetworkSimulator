import java.util.*;

public class Machine
{
    private static int bufferSize = 50;

    public static void setBufferSize( int bufferSize )
    {
        Machine.bufferSize = bufferSize;
    }

    public static int getBufferSize()
    {
        return Machine.bufferSize;
    }

    private String name;
    private List<Machine> network;
    private List<Packet> packets;
    private List<Packet> myPackets;
    private Message message;

    public Machine( String name )
    {
        this.setName( name );
        this.network = new ArrayList<Machine>();
        this.packets = new ArrayList<Packet>();
        this.myPackets = new ArrayList<Packet>();
        this.network.add( this );
    }

    public void clear()
    {
        this.packets.clear();
        this.myPackets.clear();
    }

    @Override
    public boolean equals( Object o )
    {
        if (! (o instanceof Machine) ) return false;
        return this.name.equals( ((Machine)o).name );
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder( this.getClass().getName() );
        sb.append( String.format( " Name: %s\n", this.name ) );
        sb.append( "\nNetwork:\n" );
        for ( Machine each : this.network)
            sb.append( "    " ) .append( each.getName() );
        sb.append( "\nPackets in Transit:\n" );
        for ( Packet each : this.packets )
            sb.append( each );
        sb.append( "\nPackets Received:\n" );
        for ( Packet each : this.myPackets )
            sb.append( each );
        sb.append( "\n" );
        return sb.toString();
    }

    final public void setName( String name )
    {
        this.name = name;
    }

    public String getName( )
    {
        return this.name;
    }

    public void addMessage( Message m )
    {
        this.message = m;
    }

    public Message getMessage()
    {
        return this.message;
    }

    public void connectTo( Machine machine )
    {
        if ( this.network.indexOf( machine ) >= 0 ) return;
        this.network.add( machine );
        machine.connectTo( this );
    }

    public void breakConnection( Machine machine )
    {
        if ( this.network.indexOf( machine ) >= 0 )
        {
            this.network.remove( machine );
            machine.breakConnection( this );
        }
    }

    public void breakAllConnections()
    {
        for ( Machine each : this.network )
            breakConnection( each );
    }

    public void removePacket( Packet p )
    {
        this.packets.remove( p );
        p.setCurrentMachine( null );
    }

    public void addPacket( Packet p )
    {
        if ( p.getDestination().equals( this ) ) 
        {
            this.myPackets.add( p );
            p.setCurrentMachine( this );
        }
        else if ( this.packets.size() < bufferSize )
        {
            this.packets.add( p );
            p.setCurrentMachine( this );
        }
        else p.dropped( "Buffer overflowed; too much traffic through " + this.getName() );
    }

    public boolean hasMessage()
    {
        return this.message != null;
    }

    public Machine[] getNetwork()
    {
        return this.network.toArray( new Machine[0] );
    }

    public Machine getRandomNeighbor( Packet p )
    {
        if ( p != null && this.network.indexOf( p.getDestination() ) > -1 )
            return p.getDestination() ;
        List<Machine> eligible = new ArrayList<>();
        Machine current = p.getCurrent();
        Machine source = p.getSource();
        for ( Machine each : this.network )
            if ( each != current && each != source && each != this )
                eligible.add( each );
        if ( eligible.size() < 2 ) 
            eligible = this.network;
        return eligible.get( (int)( Math.random() * eligible.size() ) );
    }

    public Machine getRandomNeighbor( )
    {
        // If not connected to anything, return null;
        if ( this.network.size() < 2 ) return null;
        // If network has more than two nodes, avoid bouncing back
        // to the excluded network.
        int max = this.network.size() ;
        int rIdx = (int)(Math.random() * max);
        if ( this.network.get( rIdx ) == this )
            return this.getRandomNeighbor();
        return this.network.get( rIdx );
    }

    public int getMyPacketCount()
    {
        return this.myPackets.size();
    }

    public int getTransitPacketCount()
    {
        return this.packets.size();
    }

    public Packet[] getTransitPackets()
    {
        return this.packets.toArray( new Packet[0] );
    }

    public Packet[] getMyPackets()
    {
        return this.myPackets.toArray( new Packet[0] );
    }

}
