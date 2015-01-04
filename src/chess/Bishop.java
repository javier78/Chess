/**
 * @author Javier Martinez
 */
package chess;

public class Bishop extends ChessPiece {

	protected Bishop(String loc, char color) {
		super(loc,color);
	}

	@Override
	public boolean move(int file, int rank) {
		int currfile=getLocation().charAt(0)-'a';
		int currrank=8-Character.getNumericValue(getLocation().charAt(1));
		int filediff=Math.abs(currfile-file);
		int rankdiff=Math.abs(currrank-rank);
		if(filediff==rankdiff)
		{
			int filedir;
			int rankdir;
			if(currfile<file)
				filedir=1;
			else
				filedir=-1;
			if(currrank<rank)
				rankdir=1;
			else
				rankdir=-1;
			
			for(int x=currrank+rankdir,y=currfile+filedir;x!=rank;x+=rankdir,y+=filedir)	//Might need supposedly redundant check for y?
			{
				if(ChessBoard.board[x][y]!=null)
					return false;
			}
			if(ChessBoard.board[rank][file]==null)
			{
				this.setLocation(""+(char)(file+'a')+(8-rank));
				return true;
			}
			else if(ChessBoard.board[rank][file].getColor()==getColor())
				return false;
			this.setLocation(""+(char)(file+'a')+(8-rank));
			return true;	//If it reaches here, the bishop captured a piece.
		}
		else
			return false;
	}

	@Override
	public String toString() {
		return getColor()+"B";
	}

}
