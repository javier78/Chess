/**
 * @author Javier Martinez
 */
package chess;

import java.util.ArrayList;

public class ChessBoard 
{
	public static ChessPiece[][] board;
	static boolean WhiteTurn;
	static ChessPiece WhitePrev;
	static ChessPiece BlackPrev;
	static King WK,BK;
	static ChessPiece LastRemoved,LastTestRemoved;
	static boolean removed,checking;
	static ArrayList<ChessPiece> whites;
	static ArrayList<ChessPiece> blacks;
	public ChessBoard()
	{
		whites=new ArrayList<ChessPiece>();
		blacks=new ArrayList<ChessPiece>();
		board=new ChessPiece[8][8];
		
		board[7][0]=new Rook("a1",'w');
		board[7][1]=new Knight("b1",'w');
		board[7][2]=new Bishop("c1",'w');
		board[7][3]=new Queen("d1",'w');
		board[7][4]=new King("e1",'w');
		board[7][5]=new Bishop("f1",'w');
		board[7][6]=new Knight("g1",'w');
		board[7][7]=new Rook("h1",'w');
		
		WK=(King)board[7][4];
		char file='a';
		for(int x=0;x<8;x++,file++)
		{
			board[6][x]=new Pawn(""+file+2,'w');
			board[1][x]=new Pawn(""+file+7,'b');
		}
		board[0][0]=new Rook("a8", 'b');
		board[0][1]=new Knight("b8",'b');
		board[0][2]=new Bishop("c8",'b');
		board[0][3]=new Queen("d8",'b');
		board[0][4]=new King("e8",'b');
		board[0][5]=new Bishop("f8",'b');
		board[0][6]=new Knight("g8",'b');
		board[0][7]=new Rook("h8",'b');
		
		BK=(King)board[0][4];
		
		for(int x=0;x<8;x++)
		{
			whites.add(board[7][x]);
			whites.add(board[6][x]);
			blacks.add(board[1][x]);
			blacks.add(board[0][x]);
		}
		
		checking=false;
		WhiteTurn=true;
	}
	
	public static boolean move(String piece, String destination, String promo)
	{
		//move first, then promote!
		int file=piece.charAt(0)-'a';
		int rank=8-Character.getNumericValue(piece.charAt(1));
		int desFile=destination.charAt(0)-'a';
		int desRank=8-Character.getNumericValue(destination.charAt(1));
		ArrayList<ChessPiece> toPromote;
		if(WhiteTurn)
			toPromote=whites;
		else
			toPromote=blacks;
		if(board[rank][file] instanceof Pawn && moveNP(piece, destination) && ((WhiteTurn && desRank==0) || (!WhiteTurn && desRank==7)))	//these checks may not be needed.
		{
			ChessPiece cp = null;
			switch(promo)
			{
				case "R":
					cp=new Rook(destination,board[desRank][desFile].getColor(),true);
					break;
				case "N":
					cp=new Knight(destination,board[desRank][desFile].getColor());
					break;
				case "B":
					cp=new Bishop(destination,board[desRank][desFile].getColor());
					break;
				case "Q":
					cp=new Queen(destination,board[desRank][desFile].getColor());
					break;
				default:
					return false;
			}
			board[desRank][desFile]=cp;
			toPromote.remove(board[desRank][desFile]);
			toPromote.add(cp);
			return true;
		}
		return false;
		
	}
	
	/**
	 * The move method that should be called if a promo isn't specified. It checks if a promotion wasn't specified, but must be performed.
	 * @param piece
	 * @param destination
	 * @return
	 */
	public static boolean move(String piece, String destination)
	{
		int file=piece.charAt(0)-'a';
		int rank=8-Character.getNumericValue(piece.charAt(1));
		int desRank=8-Character.getNumericValue(destination.charAt(1));
		if(board[rank][file] instanceof Pawn && ((WhiteTurn && desRank==0) || (!WhiteTurn && desRank==7)))
			return move(piece, destination, "Q");
		else
			return moveNP(piece,destination);
	}
	
	/**
	 * The main move method, doesn't check if a pawn is being promoted.
	 * @param piece
	 * @param destination
	 * @return true if the move was legal, false otherwise.
	 */
	public static boolean moveNP(String piece, String destination)
	{
		if(WhiteTurn && WhitePrev instanceof Pawn)
		{
			Pawn p=(Pawn) WhitePrev;
			p.passantable=false;
		}
		else if(!WhiteTurn && BlackPrev instanceof Pawn)
		{
			Pawn p=(Pawn) BlackPrev;
			p.passantable=false;
		}
		
		int file=piece.charAt(0)-'a';
		int rank=8-Character.getNumericValue(piece.charAt(1));
		if((file<0 || file>7) || (rank<0 || rank>7))
			return false;
		if(board[rank][file]!=null && ((board[rank][file].getColor()=='b' && !WhiteTurn) || (board[rank][file].getColor()=='w' && WhiteTurn)))
		{
			int desFile=destination.charAt(0)-'a';
			int desRank=8-Character.getNumericValue(destination.charAt(1));
			if(desRank==rank && desFile==file)
				return false;
			
			if((desRank<0 || desRank>7) || (desFile<0 || desFile>7))
				return false;
			
			if(board[rank][file].move(desFile, desRank))
			{
				boolean enpassant=false;
				if(board[desRank][desFile]!=null)	//This has to be null if en passant occurred, so LastRemoved is preserved.
				{
					remove(desFile,desRank);
				}
				else if(board[desRank][desFile]==null && removed)	//en passant occurred
				{
					enpassant=true;
				}
					
				board[desRank][desFile]=board[rank][file];
				board[rank][file]=null;
				Attack a=isCheck();
				if(a!=null)
				{
					board[desRank][desFile].revertLocation();
					board[rank][file]=board[desRank][desFile];
					if(removed)
					{
						undoRemove();
						if(enpassant)
							board[desRank][desFile]=null;
					}
						
					else
						board[desRank][desFile]=null;
					removed=false;
					return false;
				}
				else
				{
					if(WhiteTurn)
						WhitePrev=board[desRank][desFile];
					else
						BlackPrev=board[desRank][desFile];
					WhiteTurn=!WhiteTurn;
					removed=false;
					return true;
				}
			}
			else
				return false;
		}
		else
			return false;
	}
	
	public static boolean canMove(String piece, int desFile, int desRank)
	{
		int file=piece.charAt(0)-'a';
		int rank=8-Character.getNumericValue(piece.charAt(1));
		if((desRank<0 || desRank>7) || (desFile<0 || desFile>7))
			return false;
		checking=true;
		if(board[rank][file].move(desFile, desRank))
		{
			boolean enpassant=false;
			checking=false;
			
			if(board[desRank][desFile]!=null)	//This has to be null if en passant occurred, so LastRemoved is preserved.
				testRemove(desFile,desRank);
			
			else if(board[desRank][desFile]==null && LastTestRemoved!=null)
				enpassant=true;
			board[desRank][desFile]=board[rank][file];	
			board[rank][file]=null;
			if(isCheck()!=null)
			{
				board[desRank][desFile].revertLocation();
				board[rank][file]=board[desRank][desFile];
				if(LastTestRemoved!=null)
				{
					undoTestRemove();
					if(enpassant)
						board[desRank][desFile]=null;
				}
				else
					board[desRank][desFile]=null;
				LastTestRemoved=null;
				return false;
			}
			else
			{
				board[desRank][desFile].revertLocation();
				board[rank][file]=board[desRank][desFile];
				if(LastTestRemoved!=null)
				{
					undoTestRemove();
					if(enpassant)
						board[desRank][desFile]=null;
				}
				else
					board[desRank][desFile]=null;
				LastTestRemoved=null;
				return true;
			}
		}
		else
			return false;
	}
	
	public void draw()
	{
		boolean isDarkSpace=false;
		for(int x=0;x<board.length;x++)
		{
			for(int y=0;y<board[0].length;y++)
			{
				if(isDarkSpace && board[x][y]==null)
					System.out.print("## ");
				else if(!isDarkSpace && board[x][y]==null)
					System.out.print("   ");
				else
					System.out.print(board[x][y]+" ");
				isDarkSpace=!isDarkSpace;
			}
			isDarkSpace=!isDarkSpace;
			System.out.println(8-x);
		}
		System.out.print(" ");
		for(char x='a';x<='g';x++)
			System.out.print(x+"  ");
		System.out.println('h');
	}
	
	public static Attack isCheck()
	{
		if(WhiteTurn)
			return WK.isCheck();
		else
			return BK.isCheck();
	}
	
	public static boolean isCheckMate()
	{
		Attack a=isCheck();
		if(a==null)
			return false;
			
		//Let's see if the king can run away.
		King k;
		ArrayList<ChessPiece> iter;
		if(WhiteTurn)
		{
			k=WK;
			iter=whites;
		}
		else
		{
			k=BK;
			iter=blacks;
		}
		int file=k.getLocation().charAt(0)-'a';
		int rank=8-Character.getNumericValue(k.getLocation().charAt(1));
		for(int x=-1;x<=1;x++)
		{
			for(int y=-1;y<=1;y++)
			{
				if(canMove(k.getLocation(),file+y,rank+x))
					return false;	//king can run away.
			}
		}
		//Let's try attacking with a piece then.
		ChessPiece p=a.cp;
		int threatfile=p.getLocation().charAt(0)-'a';
		int threatrank=8-Character.getNumericValue(p.getLocation().charAt(1));
		for(ChessPiece cp:iter)
		{
			if(cp!=null && canMove(cp.getLocation(),threatfile,threatrank))
				return false;	//A piece can attack the threat.
		}
		//Can't attack? Then block!
		int rankdir=0;
		int filedir=0;
		switch(a.dir)
		{
			case "nw":
				rankdir=-1;
				filedir=-1;
				break;
			case "ne":
				rankdir=-1;
				filedir=1;
				break;
			case "n":
				rankdir=-1;
				break;
			case "e":
				filedir=1;
				break;
			case "w":
				filedir=-1;
				break;
			case "sw":
				rankdir=1;
				filedir=-1;
				break;
			case "se":
				rankdir=1;
				filedir=1;
				break;
			case "s":
				rankdir=1;
				break;
			case "":	//Already accounted for in attack?
				break;
		}
		if(rankdir!=0 && filedir!=0)
		{
			for(int x=rank+rankdir;x!=threatrank;x+=rankdir)
			{
				for(int y=file+filedir;y!=threatfile;y+=filedir)
				{
					for(ChessPiece cp:iter)
					{
						if(cp!=null && canMove(cp.getLocation(), y, x))		//null check for debugging only!!
							return false;	//A piece CAN block the threatening piece
					}
				}
			}			
		}
		else if(rankdir!=0)
		{
			for(int x=rank+rankdir;x!=threatrank;x+=rankdir)
			{
				for(ChessPiece cp:iter)
				{
					if(cp!=null && canMove(cp.getLocation(),file,x))
						return false;
				}
			}
		}
		else if(filedir!=0)
		{
			for(int y=file+filedir;y!=threatfile;y+=filedir)
			{
				for(ChessPiece cp:iter)
				{
					if(cp!=null && canMove(cp.getLocation(),y,rank))
						return false;
				}
			}
		}
		return true;	//Nothing the player can do, checkmate.
	}
	
	public static void testRemove(int file, int rank)
	{
		LastTestRemoved=board[rank][file];
		board[rank][file]=null;
	}
	
	public static void undoTestRemove()
	{
		if(LastTestRemoved!=null)
		{
			int file=LastTestRemoved.getLocation().charAt(0)-'a';
			int rank=8-Character.getNumericValue(LastTestRemoved.getLocation().charAt(1));
			board[rank][file]=LastTestRemoved;
		}
	}
	
	/**
	 * Properly removes a piece from the board, keeping track of the previous one removed.
	 * @param file the file of the piece to delete.
	 * @param rank the rank of the piece to delete.
	 */
	public static void remove(int file, int rank)
	{
		LastRemoved=board[rank][file];
		board[rank][file]=null;
		if(WhiteTurn)
			blacks.remove(LastRemoved);			
		else
			whites.remove(LastRemoved);
		removed=true;
	}
	
	public static void undoRemove()
	{
		if(LastRemoved!=null && removed)
		{
			int file=LastRemoved.getLocation().charAt(0)-'a';
			int rank=8-Character.getNumericValue(LastRemoved.getLocation().charAt(1));
			board[rank][file]=LastRemoved;
			if(WhiteTurn)
				blacks.add(LastRemoved);
			else
				whites.add(LastRemoved);
			LastRemoved=null;
		}
	}
	
	public static String colorTurn()
	{
		if(WhiteTurn)
			return "White";
		else
			return "Black";
	}
}

/*
bR bN bB bQ bK bB bN bR 8
bp bp bp bp bp bp bp bp 7
   ##    ##    ##    ## 6
##    ##    ##    ##    5
   ##    ##    ##    ## 4
##    ##    ##    ##    3 
wp wp wp wp wp wp wp wp 2
wR wN wB wQ wK wB wN wR 1
 a  b  c  d  e  f  g  h
*/