/**
 * @author Javier Martinez
 */
package chess;

public class Rook extends ChessPiece {
	boolean hasMoved;
	public Rook(String loc, char color) {
		super(loc,color);
		hasMoved=false;
	}
	
	public Rook(String loc, char color, boolean moved)
	{
		super(loc,color);
		hasMoved=moved;
	}

	@Override
	public boolean move(int file, int rank) {
		int currfile=getLocation().charAt(0)-'a';
		int currrank=8-Character.getNumericValue(getLocation().charAt(1));
		int dir;
		if(currfile==file)	//preliminary checks guarantee that both statements aren't true.
		{
			if(currrank<rank)
				dir=1;
			else
				dir=-1;
			for(int x=currrank+dir;x!=rank;x+=dir)
			{
				if(ChessBoard.board[x][file]!=null)
					return false;
			}
			if(ChessBoard.board[rank][file]==null)
			{
				this.setLocation(""+(char)(file+'a')+(8-rank));
				hasMoved=true;
				return true;
			}
			else if(ChessBoard.board[rank][file].getColor()==getColor())
				return false;
			hasMoved=true;
			this.setLocation(""+(char)(file+'a')+(8-rank));
			return true;	//If it reaches here, the rook captured a piece.
		}
		else if(currrank==rank)
		{
			if(currfile<file)
				dir=1;
			else
				dir=-1;
			for(int x=currfile+dir;x!=file;x+=dir)
			{
				if(ChessBoard.board[rank][x]!=null)
					return false;
			}
			if(ChessBoard.board[rank][file]==null)
			{
				this.setLocation(""+(char)(file+'a')+(8-rank));
				hasMoved=true;
				return true;
			}
			else if(ChessBoard.board[rank][file].getColor()==getColor())
				return false;
			this.setLocation(""+(char)(file+'a')+(8-rank));
			hasMoved=true;
			return true;	//If it reaches here, the rook captured a piece.
		}
		return false;
	}

	@Override
	public String toString() {
		return getColor()+"R";
	}
}