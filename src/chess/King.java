/**
 * @author Javier Martinez
 */
package chess;

public class King extends ChessPiece
{
	boolean hasMoved;
	public King(String loc,char color) 
	{
		super(loc,color);
		hasMoved=false;
	}

	@Override
	public boolean move(int file, int rank) 
	{
		int currfile=getLocation().charAt(0)-'a';
		int currrank=8-Character.getNumericValue(getLocation().charAt(1));
		int filediff=Math.abs(currfile-file);
		int rankdiff=Math.abs(currrank-rank);
		if((rankdiff==1 && filediff==1) || (rankdiff==0 && filediff==1) || (rankdiff==1 && filediff==0))
		{
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
			return true;
		}
		//Castling!
		else if(!hasMoved && filediff==2 && rankdiff==0 && isCheck()==null)
		{
			int dir;
			if(currfile<file)
				dir=1;
			else
				dir=-1;
			try
			{
				for(int x=currfile+dir;x!=file+dir;x+=dir)
				{
					if(ChessBoard.board[rank][x]==null)		//checking for check at the end is redundant.
					{
						ChessBoard.board[rank][x]=ChessBoard.board[rank][x-dir];
						ChessBoard.board[rank][x-dir]=null;
						if(isCheck()!=null)
						{
							ChessBoard.board[currrank][currfile]=ChessBoard.board[rank][x];
							ChessBoard.board[rank][x]=null;
							return false;
						}
							
					}
					else
						return false;
				}
				ChessBoard.board[currrank][currfile]=ChessBoard.board[rank][file];	//Moves king back so he can be moved.
				if((ChessBoard.board[rank][currfile+(3*dir)] instanceof Rook) && (ChessBoard.board[rank][currfile+(3*dir)].getColor()==getColor()))
				{
					Rook r=(Rook)ChessBoard.board[rank][currfile+(3*dir)];
					if(!r.hasMoved)
					{
						ChessBoard.board[rank][file-dir]=ChessBoard.board[rank][currfile+(3*dir)];	//Moves rook.
						ChessBoard.board[rank][currfile+(3*dir)]=null;
						setLocation(""+(char)(file+'a')+(8-rank));
						hasMoved=true;
						r.setLocation(""+(char)((file-dir)+'a')+(8-rank));
						r.hasMoved=true;
						return true;
					}
				}
				else if((ChessBoard.board[rank][currfile+(4*dir)] instanceof Rook) && (ChessBoard.board[rank][currfile+(4*dir)].getColor()==getColor()))
				{
					Rook r=(Rook)ChessBoard.board[rank][currfile+(4*dir)];
					if(!r.hasMoved)
					{
						ChessBoard.board[rank][file-dir]=ChessBoard.board[rank][currfile+(4*dir)];
						ChessBoard.board[rank][currfile+(4*dir)]=null;
						hasMoved=true;
						setLocation(""+(char)(file+'a')+(8-rank));
						r.setLocation(""+(char)((file-dir)+'a')+(8-rank));
						r.hasMoved=true;
						return true;
					}
				}
				else
					return false;
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				return false;
			}
		}
		return false;
	}
	
	public Attack isCheck()
	{
		int file=getLocation().charAt(0)-'a';
		int rank=8-Character.getNumericValue(getLocation().charAt(1));
		//Check for queens, bishops, and rooks in one blow! Maybe extend to kings and pawns!
		//Diagonal northwest
		ChessPiece p;
		for(int x=rank-1,y=file-1;x>=0 && y>=0;x--,y--)
		{
			p=ChessBoard.board[x][y];
			if(p!=null && p.getColor()==getColor())
				break;
			if(p!=null && (p instanceof Queen || p instanceof Bishop))
				return new Attack(p,"nw");
			else if(p!=null && x==rank-1 && y==file-1 && ((p instanceof Pawn && p.getColor()=='b') || p instanceof King))	//merge with above?
				return new Attack(p,"nw");
			else if(p!=null)
				break;
		}
		
		//Diagonal northeast
		for(int x=rank-1,y=file+1;x>=0 && y<=7;x--,y++)
		{
			p=ChessBoard.board[x][y];
			if(p!=null && p.getColor()==getColor())
				break;
			if(p!=null && (p instanceof Queen || p instanceof Bishop))
				return new Attack(p,"ne");
			else if(p!=null && x==rank-1 && y==file+1 && ((p instanceof Pawn && p.getColor()=='b') || p instanceof King))	//merge with above?
				return new Attack(p,"ne");
			else if(p!=null)
				break;
		}
		
		//Diagonal southwest
		for(int x=rank+1,y=file-1;x<=7 && y>=0;x++,y--)
		{
			p=ChessBoard.board[x][y];
			if(p!=null && p.getColor()==getColor())
				break;
			if(p!=null && (p instanceof Queen || p instanceof Bishop))
				return new Attack(p,"sw");
			else if(p!=null && x==rank+1 && y==file-1 && ((p instanceof Pawn && p.getColor()=='w') || p instanceof King))	//merge with above?
				return new Attack(p,"sw");
			else if(p!=null)
				break;
		}
		
		//Diagonal southeast
		for(int x=rank+1,y=file+1;x<=7 && y<=7;x++,y++)
		{
			p=ChessBoard.board[x][y];
			if(p!=null && p.getColor()==getColor())
				break;
			if(p!=null && (p instanceof Queen || p instanceof Bishop))
				return new Attack(p,"se");
			else if(p!=null && x==rank+1 && y==file+1 && ((p instanceof Pawn && p.getColor()=='w') || p instanceof King))	//merge with above?
				return new Attack(p,"se");
			else if(p!=null)
				break;
		}
		
		//Now checking for rooks and queens
		//North
		for(int x=rank-1;x>=0;x--)
		{
			p=ChessBoard.board[x][file];
			if(p!=null && p.getColor()==getColor())
				break;
			
			if(p!=null && (p instanceof Queen || p instanceof Rook))
				return new Attack(p,"n");
			else if(p!=null && x==rank-1 && p instanceof King)	//merge with above?
				return new Attack(p,"n");
			else if(p!=null)
				break;
		}
		
		//South
		for(int x=rank+1;x<=7;x++)
		{
			p=ChessBoard.board[x][file];
			if(p!=null && p.getColor()==getColor())
				break;
			if(p!=null && (p instanceof Queen || p instanceof Rook))
				return new Attack(p,"s");
			else if(p!=null && x==rank+1 && p instanceof King)	//merge with above?
				return new Attack(p,"s");
			else if(p!=null)
				break;
		}
		
		//East
		for(int y=file+1;y<=7;y++)
		{
			p=ChessBoard.board[rank][y];
			if(p!=null && p.getColor()==getColor())
				break;
			if(p!=null && (p instanceof Queen || p instanceof Rook))
				return new Attack(p,"e");
			else if(p!=null && y==file+1 && p instanceof King)
				return new Attack(p,"e");
			else if(p!=null)
				break;
		}
		
		//West
		for(int y=file-1;y>=0;y--)
		{
			p=ChessBoard.board[rank][y];
			if(p!=null && p.getColor()==getColor())
				break;
			if(p!=null && (p instanceof Queen || p instanceof Rook))
				return new Attack(p,"w");
			if(p!=null && y==file-1 && p instanceof King)
				return new Attack(p,"w");
			else if(p!=null)
				break;
			
		}
		//Now check for knights!
		if(checkKnight(rank,file,2,1)!=null)
			return new Attack(checkKnight(rank,file,2,1),"");
		if(checkKnight(rank,file,-2,1)!=null)
			return new Attack(checkKnight(rank,file,-2,1),"");
		if(checkKnight(rank,file,2,-1)!=null)
			return new Attack(checkKnight(rank,file,2,-1),"");
		if(checkKnight(rank,file,-2,-1)!=null)
			return new Attack(checkKnight(rank,file,-2,-1),"");
		if(checkKnight(rank,file,1,2)!=null)
			return new Attack(checkKnight(rank,file,1,2),"");
		if(checkKnight(rank,file,-1,2)!=null)
			return new Attack(checkKnight(rank,file,-1,2),"");
		if(checkKnight(rank,file,1,-2)!=null)
			return new Attack(checkKnight(rank,file,1,-2),"");
		if(checkKnight(rank,file,-1,-2)!=null)
			return new Attack(checkKnight(rank,file,-1,-2),"");
		return null;
	}
	
	private ChessPiece checkKnight(int rank, int file,int rankoffset,int fileoffset)
	{
		if(rank+rankoffset<=7 && file+fileoffset<=7 && file+fileoffset>=0 && rank+rankoffset>=0)
		{
			ChessPiece p;
			p=ChessBoard.board[rank+rankoffset][file+fileoffset];
			if(p!=null && p.getColor()!=getColor())
			{
				if(p instanceof Knight)
					return p;
			}
		}
		return null;
		
	}
	
	public String toString()
	{
		return getColor()+"K";
	}
}
