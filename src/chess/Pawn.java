/**
 * @author Javier Martinez
 */
package chess;

public class Pawn extends ChessPiece {
	int direction;
	//boolean hasMoved;
	int hasMoved;
	boolean passantable;
	Pawn passanted;
	protected Pawn(String loc, char color) {
		super(loc, color);
		//Direction corresponds to location in array. So a white pawn's direction is moving up in the array.
		if(color=='b')
			direction=1;
		else
			direction=-1;
		hasMoved=0;
	}

	@Override
	public boolean move(int file, int rank) 
	{
		// TODO Auto-generated method stub
		int currfile=getLocation().charAt(0)-'a';
		int currrank=8-Character.getNumericValue(getLocation().charAt(1));
		
		//Just a vertical move. Accounts for first move being 2 moves.
		if(hasMoved==0 && currfile==file)
		{
			if(currrank+direction==rank && ChessBoard.board[rank][file]==null)
			{
				hasMoved++;
				this.setLocation(""+(char)(file+'a')+(8-rank));
				return true;
			}
			else if(currrank+direction*2==rank && ChessBoard.board[rank][file]==null && ChessBoard.board[currrank+direction][file]==null)
			{
				hasMoved++;
				passantable=true;
				this.setLocation(""+(char)(file+'a')+(8-rank));
				return true;
			}
			else
				return false;
			
		}
		//Another vertical move, but it can only be over one space.
		else if(currrank+direction==rank && currfile==file && ChessBoard.board[rank][file]==null)
		{
			this.setLocation(""+(char)(file+'a')+(8-rank));
			return true;
		}
		//Diagonal move!
		else if(currrank+direction==rank)
		{
			
			if(Math.abs(currfile-file)==1)
			{
				//Determines if en passant just happened.
				if(ChessBoard.board[rank-direction][file] instanceof Pawn)	//checks for null as well.
				{
					Pawn p=(Pawn)ChessBoard.board[rank-direction][file];
					if(p.isPassantable() && p.getColor()!=getColor())
					{
						if(ChessBoard.checking)
							ChessBoard.testRemove(file, rank-direction);
						else
							ChessBoard.remove(file, rank-direction);
						this.setLocation(""+(char)(file+'a')+(8-rank));
						hasMoved++;
						return true;
					}
				}
				//If there is no piece to capture, or the piece diagonal to this piece is the same color, then the move is illegal.
				if(ChessBoard.board[rank][file]==null || ChessBoard.board[rank][file].getColor()==getColor())
					return false;
				//reaches here if it's a valid move that captures opponent piece.
				this.setLocation(""+(char)(file+'a')+(8-rank));
				hasMoved++;
				return true;
			}
			else
				return false;
		}
		return false;
	}

	@Override
	public String toString() {
		return getColor()+"p";
	}
	
	public boolean isPassantable()
	{
		return passantable;
	}
	
	public void revertLocation()
	{
		super.revertLocation();
		hasMoved--;
	}

}