/**
 * @author Javier Martinez
 */
package chess;

public class Attack 
{
	ChessPiece cp;
	String dir;
	public Attack(ChessPiece p,String dir)
	{
		cp=p;
		this.dir=dir;
	}
}
