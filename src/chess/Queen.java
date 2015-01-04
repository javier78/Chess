/**
 * @author Javier Martinez
 */
package chess;

public class Queen extends ChessPiece {

	public Queen(String loc,char color) {
		super(loc,color);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean move(int file, int rank) 
	{
		// TODO Auto-generated method stub
		int currfile=getLocation().charAt(0)-'a';
		int currrank=8-Character.getNumericValue(getLocation().charAt(1));
		int filediff=Math.abs(currfile-file);
		int rankdiff=Math.abs(currrank-rank);
		int dir;
		//Moving diagonally
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
			return true;	//If it reaches here, the queen captured a piece.
		}
		
		else if(currfile==file)	//preliminary checks guarantee that both statements aren't true.
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
				return true;
			}
			else if(ChessBoard.board[rank][file].getColor()==getColor())
				return false;
			this.setLocation(""+(char)(file+'a')+(8-rank));
			return true;	//If it reaches here, the queen captured a piece.
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
				return true;
			}
			else if(ChessBoard.board[rank][file].getColor()==getColor())
				return false;
			this.setLocation(""+(char)(file+'a')+(8-rank));
			return true;	//If it reaches here, the queen captured a piece.
		}
		return false;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getColor()+"Q";
	}

}
