/**
 * @author Javier Martinez
 */
package chess;

public abstract class ChessPiece {
	private String loc;
	private final char color;
	private String PrevLoc;
	protected ChessPiece(String loc,char color)
	{
		this.loc=loc;
		this.color=color;
	}
	
	public abstract boolean move(int file, int rank);
	
	public char getColor()
	{
		return color;
	}
	
	public String getLocation()
	{
		return loc;
	}
	
	public void setLocation(String loc)
	{
		PrevLoc=this.loc;
		this.loc=loc;
	}
	
	public abstract String toString();
	
	public void revertLocation()
	{
		this.loc=PrevLoc;
	}
	
	public boolean equals(Object o)
	{
		if(o instanceof ChessPiece)
		{
			ChessPiece p=(ChessPiece)o;
			return p.loc.equals(this.loc);
		}
		return false;
	}
}
