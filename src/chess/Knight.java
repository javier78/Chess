/**
 * @author Javier Martinez
 */
package chess;

public class Knight extends ChessPiece {

	protected Knight(String loc, char color) {
		super(loc, color);
	}

	@Override
	public boolean move(int file, int rank) 
	{
		int currfile=getLocation().charAt(0)-'a';
		int currrank=8-(Character.getNumericValue(getLocation().charAt(1)));
		int filediff=Math.abs(currfile-file);
		int rankdiff=Math.abs(currrank-rank);
		if((filediff==2 && rankdiff==1) || (filediff==1 && rankdiff==2))
		{
			if(ChessBoard.board[rank][file]==null)
			{
				this.setLocation(""+(char)(file+'a')+(8-rank));
				return true;
			}
			else if(ChessBoard.board[rank][file].getColor()==getColor())
				return false;
			this.setLocation(""+(char)(file+'a')+(8-rank));
			return true;	//If it reaches here, the knight captured a piece.
		}
		else
			return false;
	}

	@Override
	public String toString() {
		return getColor()+"N";
	}

}
