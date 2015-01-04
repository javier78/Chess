/**
 * @author Javier Martinez
 */
package chess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class Chess {

	static BufferedReader in=null;
	static PrintWriter out=null;
	static boolean isWhite;
	static String choice;
	static Socket s;
	static ServerSocket ss;
	public static void main(String[] args) throws IOException
	{
		Scanner kb=new Scanner(System.in);
		System.out.println("Are you (h)osting a game, (s)earching for one, or playing (l)ocally?");
		boolean valid=false;
		choice="";
		while(!valid)
		{
			choice=kb.nextLine();
			if(!(choice.equals("h") || choice.equals("s") || choice.equals("l")))
				System.out.println("Please enter 'h' for hosting a game, or 's' to search for one");
			else
				valid=true;
		}

		if(choice.equals("h"))
		{
			isWhite=true;
			//while(true)
			{
				try{

					ss=new ServerSocket(1337);
					ss.setSoTimeout(100000);
					System.out.println("Listening on port "+ss.getLocalPort()+"...");
					s=ss.accept();
					System.out.println("Successfully connected to "+s.getRemoteSocketAddress());
					in=new BufferedReader(new InputStreamReader(s.getInputStream()));
					out=new PrintWriter(s.getOutputStream(),true);
				}
				catch(SocketTimeoutException ste)
				{
					System.out.println("Socket timed out");
					kb.close();
					return;
				}
				catch(IOException ioe)
				{
					ioe.printStackTrace();
					kb.close();
					return;
				}
			}
		}
		else if(choice.equals("s"))
		{
			isWhite=false;
			System.out.println("Enter an IP address");
			String host=kb.next();
			System.out.println("Enter a port number");
			int port=kb.nextInt();
			s=new Socket(host, port);
			in=new BufferedReader(new InputStreamReader(s.getInputStream()));
			out=new PrintWriter(s.getOutputStream(),true);
			System.out.println("Successfully connected to server");
		}
		else if(choice.equals("l"))
		{
			in=new BufferedReader(new InputStreamReader(System.in));
			out=new PrintWriter(System.out,true);
		}
		else
		{
			System.out.println("Invalid mode!");
			kb.close();
			
			return;
		}
		playChess();
		kb.close();
	}

	public static void playChess() throws IOException
	{
		ChessBoard board=new ChessBoard();
		board.draw();
		boolean gameover=false;
		boolean draw=false;
		boolean moved;
		Scanner kb=null;
		while(!gameover)
		{
			do
			{
				moved=false;
				
				String move="";
				kb=new Scanner(System.in);
				if(choice.equals("l"))
				{
					System.out.println(ChessBoard.colorTurn()+": Enter a move: ");
					move=in.readLine();
				}
				else if((ChessBoard.WhiteTurn && isWhite) || (!ChessBoard.WhiteTurn && !isWhite))	//Local player's turn
				{
					System.out.println(ChessBoard.colorTurn()+": Enter a move: ");
					move=kb.nextLine();
					out.println(move);
				}
				else if((ChessBoard.WhiteTurn && !isWhite) || (!ChessBoard.WhiteTurn && isWhite))	//Not the local player's turn, block on input
				{
					try{
						System.out.println("Waiting for "+ChessBoard.colorTurn()+" player...");
						move=in.readLine();
						System.out.println("Received input: "+move);						
					}
					catch(SocketException se)
					{
						System.out.println("Opponent disconnected!");
						kb.close();
						return;
					}
				}
				String[] parsed=move.split(" ");
				if(parsed.length>1 && parsed[0].length()==2 && parsed[1].length()==2)
				{
					if(parsed.length==4)
					{
						if(parsed[3].equals("draw?"))
							draw=true;
						moved=ChessBoard.move(parsed[0],parsed[1],parsed[2]);
						if(!moved)
							draw=false;
					}
					else if(parsed.length==3)
					{
						if(parsed[2].equals("draw?"))
							draw=true;
						moved=ChessBoard.move(parsed[0],parsed[1]);
						if(!moved)
							draw=false;
					}
					else if(parsed.length==2)
					{
						moved=ChessBoard.move(parsed[0], parsed[1]);
						draw=false;
					}
				}
				else if(parsed.length==1)
				{
					if(parsed[0].equals("draw"))
					{
						if(draw)
						{
							System.out.println("Draw accepted!");
							moved=true;
							gameover=true;
						}
						else
							System.out.println("Draw wasn't offered");
					}
					else if(parsed[0].equals("resign"))
					{
						if(ChessBoard.WhiteTurn)
							System.out.println("Black wins!");
						else
							System.out.println("White wins!");
						gameover=true;
						moved=true;
					}
				}
				if(!moved)
					System.out.println("Illegal move, try again");
			}while(!moved);
			if(ChessBoard.isCheckMate())
			{
				if(ChessBoard.WhiteTurn)
				{
					board.draw();
					System.out.println("Black wins!");
				}
				else
				{
					board.draw();
					System.out.println("White wins!");
				}
				gameover=true;
			}
			if(!gameover)
				board.draw();
		}
		kb.close();
		s.close();
	}
}