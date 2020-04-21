import java.util.*;

public class Packet
{
    private static List<Packet> packets;
    private static int TTL;
    private static int packetSize;

    static 
    {
        Packet.TTL = 50;
        Packet.packetSize = 10;
        Packet.packets = new ArrayList<Packet>();
    }

    public static void clear()
    {
        for ( Packet each : Packet.packets )
            each.dropped = true;
        deleteDropped();
    }

    public static Packet[] send( Message message )
    {
        List<Packet> packets = new ArrayList<>();
        int idx = 0;
        int order = 1;
        String msg = message.getText(); 
        while ( idx < msg.length() )
        {
            int endIdx = idx + Packet.packetSize;
            if ( endIdx >= msg.length() )
                endIdx = msg.length();
            packets.add( new Packet( 
                message.getSource(), message.getDestination(), 
                order, msg.substring( idx, endIdx )
            ) );
            idx = endIdx;
            order++;
        }
        for ( Packet each : packets )
            each.setTotalPackets( order );
        return packets.toArray(new Packet[0]);
    }

    public static int deleteDropped()
    {
        int count = 0;
        for ( int i = Packet.packets.size() - 1; i >= 0; i-- )
        {
            Packet p = Packet.packets.get(i);
            if ( p.dropped )
            {
                count++;
                if ( p.current != null ) p.current.removePacket( p );
                Packet.packets.remove( p );
            }
        }
        return count;
    }

    public static void shufflePackets()
    {
        for ( int i = 0; i < Packet.packets.size(); i++ )
        {
            int rIdx = (int)(Math.random() * Packet.packets.size());
            Packet temp = Packet.packets.get( i );
            Packet.packets.set( i, Packet.packets.get( rIdx ) );
            Packet.packets.set( rIdx, temp );
        }
    }

    public static void routeAll()
    {
        shufflePackets();
        for ( Packet each : Packet.packets )
            each.route();
    }

    public static void setTTL( int ttl )
    {
        Packet.TTL = ttl;
    }

    public static int getTTL( )
    {
        return Packet.TTL;
    }

    public static void setPacketSize( int size )
    {
        Packet.packetSize = size;
    }

    public static int getPacketSize()
    {
        return Packet.packetSize;
    }

    public Machine current, source, destination;
    private int ttl, order, totalPackets;
    private String payload;
    private boolean dropped;

    private Packet( Machine source, Machine destination, 
                    int order, String payload )
    {
        this.dropped = false;
        this.ttl = Packet.TTL;
        this.source = source;
        this.destination = destination;
        this.current = this.source;
        this.order = order;
        this.payload = payload;
        this.source.addPacket( this );
        Packet.packets.add( this );
    }

    @Override 
    public String toString()
    {
        StringBuilder sb = new StringBuilder( this.getClass().getName() );
        sb.append( String.format( " Payload: %s\n", this.payload ) );
        sb.append( "    TTL: " ).append( this.ttl );
        sb.append( "\n    Order: " ).append( this.order );
        sb.append("\n");
        
        return sb.toString();
    }

    public Machine getCurrent()
    {
        return this.current;
    }

    public Machine getSource()
    {
        return this.source;
    }

    public Machine getDestination()
    {
        return this.destination;
    }

    private void setTotalPackets( int tp )
    {
        this.totalPackets = tp;
    }

    public int getTotalPackets( )
    {
        return this.totalPackets;
    }

    public void route()
    {
        if ( this.dropped ) return;
        if ( this.current == null ) 
        {
            dropped( "ERROR." );
            return;
        }
        if ( this.current.equals( this.destination ) ) return;
        this.ttl--;
        if ( this.ttl == 0 )
            this.dropped("TTL ran out");
        Machine next = this.current.getRandomNeighbor( this );
        this.current.removePacket( this );
        if ( next != null ) next.addPacket( this );
    }

    public void setCurrentMachine( Machine machine )
    {
        this.current = machine;
    }

    public void dropped( String reason )
    {
        System.out.println( "Packet dropped... " + reason );
        System.out.println( this );
        this.dropped = true;
    }

    public int getOrder()
    {
        return this.order;
    }

    public int getTimeToLive()
    {
        return this.ttl;
    }

    public String getPayload()
    {
        return this.payload;
    }
}
