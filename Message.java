public class Message
{
    private Machine source;
    private Machine destination;
    private String text;

    public Message( String message )
    {
        this.source = null;
        this.destination = null;
        this.text = message;
    }

    public String getText()
    {
        return this.text;
    }

    public Machine getSource( )
    {
        return this.source;
    }

    public Machine getDestination()
    {
        return this.destination;
    }

    public void setSource( Machine m )
    {
        this.source = m;
    }

    public void setDestination( Machine m )
    {
        this.destination = m;
    }

    public void send()
    {
        Packet[] packets = Packet.send( this );
    }
}
