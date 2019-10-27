import java.util.*;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;
import javafx.scene.paint.Paint;
import javafx.geometry.Pos;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;

/**
 * An American Checkers Game
 * @author Hima Velaga
 *
 */
public class Checkers {	
	  Scene scene;
	  private ArrayList<ArrayList <Circle>> board;
	  private  Rectangle[][] tiles;
	  Pane pane = new Pane();
	  int count = 0;
	  int row; //row where the user clicked
	  int col; //column where the user clicked
	  Circle player = new Circle();	
	  Circle opponent = new Circle();
	  boolean isFirstClick = true;
	  int r = 0; //row index of the player that will jump
	  int c = 0; //column index of the player that will jump
	  Circle c1 = new Circle();
	  int num = 0;
	  int redScore;
	  int blackScore;
	  Text text;
	  Text scoretext;
	  Text scoretext2;
	  Circle king = new Circle();
	  Circle opponentKing = new Circle();		
	  Image im = new Image("http://www.clker.com/cliparts/d/6/W/Y/m/b/red-crown-md.png", false);
	  ImagePattern redimg = new ImagePattern(im);	  
	  Image im2 = new Image("http://t1.rbxcdn.com/7a3b936f5eb0e84a1105a632c8f4cb4f", false);	        		 
	  ImagePattern blackimg = new ImagePattern(im2);
	  int moves;
	  boolean jump = false;
	  
	 /**
	  * Sets UI elements
	  */
	public Checkers()
	{
		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);		
		vbox.setSpacing(30);
		scene = new Scene(vbox, 660, 550);
		Button start = new Button("Start game");
		
		HBox hbox = new HBox();
		Rectangle rect = new Rectangle(100, 100, Color.LEMONCHIFFON);
		Circle circle1 = new Circle(20, Color.TOMATO);
		scoretext = new Text("" + redScore);
		scoretext.setFill(Color.WHITE);	
		StackPane stack = new StackPane(rect, circle1, scoretext);
		
		Rectangle rect2 = new Rectangle(100, 100, Color.LEMONCHIFFON);
		Circle circle2 = new Circle(20, Color.BLACK);
		scoretext2 = new Text("" + blackScore);
		scoretext2.setFill(Color.WHITE);	
		StackPane stack2 = new StackPane(rect2, circle2, scoretext2);
		hbox.getChildren().addAll(stack, pane, stack2);
		
		hbox.setAlignment(Pos.CENTER);	
		hbox.setSpacing(20);
		text = new Text("");
		vbox.getChildren().addAll(start, text, hbox);		
	
		start.setOnAction(event -> {
				pane= initializeBoardDisplay();	
								
				text.setText("Red's turn");
				DropShadow ds = new DropShadow();
				ds.setOffsetY(3.0f);
				ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
				text.setEffect(ds);
				text.setCache(true);
				text.setX(10.0f);
				text.setY(270.0f);
				text.setFill(Color.TOMATO);
				text.setFont(Font.font(null, FontWeight.BOLD, 32));		
				
			    pane.setOnMouseClicked(event2 ->{ 	
			    	jump = false;
					setPlayers();
			    	row = getCoordinate((int)event2.getY());
					col = getCoordinate((int)event2.getX());
					
					if(isInBounds(row, col) && board.get(row).get(col).getStroke().equals(player.getStroke()) && (board.get(row).get(col).getFill().equals(player.getFill()) || board.get(row).get(col).getFill().equals(king.getFill()) ))
					{
						isFirstClick = true;
					}	
					jump=false;
				
				if(isFirstClick==true )
				{	clearMoves(); //check if the player has any moves.	
					checkJumps(); //check if there are any jumps for the current player. If so, the jump is mandatory
					validJump(player, row, col); //when clicked a square, the valid moves are displayed so the player can choose the best move
				}
				else
				{	//JUMPING						
							jump();
							clearMoves();
							
							if(count%2 !=0)
							{	text.setText("Black's turn");
								text.setFill(Color.BLACK);						
							}
							else
							{	text.setText("Red's turn");
								text.setFill(Color.TOMATO);
							}
							
							scoreAndCrown();							
							if(checkMoves()==false)
							{
								scoreAndCrown();
								if(player.getFill().equals(Color.TOMATO) || king.getFill().equals(redimg))
								{
									
									text.setText("No moves for Red. Black Wins");
								}
								else
									if(player.getFill().equals(Color.BLACK) || king.getFill().equals(blackimg))
									{
										text.setText("No moves for Black. Red wins");
									}
							}
				}			
				
			    });
		});
	}
	
	/**
	 * Logic for jump move
	 */
	public void jump() 
	{
		if(isInBounds(row, col) && board.get(row).get(col).getFill().equals(Color.MEDIUMSEAGREEN) && board.get(row).get(col).getStroke().equals(Color.WHITE) ) //if stroke equals white
		{			
			 isFirstClick=true;
			 count++;
			 boolean kingHere = false;
			for(int i=0; i<8;i++)
		      {
		        for(int j=0;j<8;j++)
		        {
		          if( (board.get(i).get(j).getFill().equals(player.getFill()) || board.get(i).get(j).getFill().equals(king.getFill()) )&& (board.get(i).get(j).getStroke().equals(Color.WHITE)))
		          {
		        	  r = i;
		        	  c = j;
		          }
		          
		          if(  board.get(i).get(j).getFill().equals(king.getFill()))
		          {
		        	  kingHere = true;
		          }
		          
		        }
		      } 
			
			//JUMPING TO THE NEXT AVAILABLE SPACE
	        c1.setFill( board.get(row).get(col).getFill());
	        c1.setStroke( board.get(row).get(col).getStroke());
	        board.get(row).get(col).setFill(board.get(r).get(c).getFill());
	    	board.get(row).get(col).setStroke(board.get(r).get(c).getStroke());		    	
	    	board.get(r).get(c).setFill(c1.getFill());
	    	board.get(r).get(c).setStroke(c1.getStroke());
	       
	    	//JUMPING OVER THE OPPONENT PIECE
	    	if(Math.abs(row-r)==2 && Math.abs(col-c)==2)
	    	{
	    		if(player.getFill().equals(Color.TOMATO) || kingHere == true )
	    		{
	    			if(c>col && r>=1 && c>=1)
	    			{	board.get(r-1).get(c-1).setFill(Color.MEDIUMSEAGREEN);
	 	    			board.get(r-1).get(c-1).setStroke(Color.MEDIUMSEAGREEN);
	    			}
	    			else 
	    			{
	    				if(r>=1 && c<=6)
	    				{	board.get(r-1).get(c+1).setFill(Color.MEDIUMSEAGREEN);
	 	    				board.get(r-1).get(c+1).setStroke(Color.MEDIUMSEAGREEN);
	    				}
	    			}
	    		}
	    		if (player.getFill().equals(Color.BLACK) || kingHere == true)
	    		{
	    			if(c>col && r<=6 && c>=1)
	    			{	board.get(r+1).get(c-1).setFill(Color.MEDIUMSEAGREEN);
	 	    			board.get(r+1).get(c-1).setStroke(Color.MEDIUMSEAGREEN);
	    			}
	    			else
	    			{
	    				if(r<=6 && c<=6)
	    				{
	    					board.get(r+1).get(c+1).setFill(Color.MEDIUMSEAGREEN);
	    					board.get(r+1).get(c+1).setStroke(Color.MEDIUMSEAGREEN);
	    				}
	    			}
	    		}
	    	}
	    	setPlayers();
		} //if statement
	}
	
	/**
	 * Clear moves on the board
	 */
	public void clearMoves()
	{
		 for(int i = 0; i< 8; i++)
		  	{
		  		for(int j = 0; j< 8; j++)
		  		{
		  			if(board.get(i).get(j).getFill().equals(Color.MEDIUMSEAGREEN))
		  			{
						board.get(i).get(j).setStroke(Color.MEDIUMSEAGREEN);
		  			}
		  			else if(board.get(i).get(j).getFill().equals(Color.BLACK))
		  			{
						board.get(i).get(j).setStroke(Color.BLACK);
		  			}
		  			else if(board.get(i).get(j).getFill().equals(Color.TOMATO))
		  			{
						board.get(i).get(j).setStroke(Color.TOMATO);
		  			}
		  			else if(board.get(i).get(j).getFill().equals(redimg))
		  			{
						board.get(i).get(j).setStroke(Color.TOMATO);
		  			}
		  			else if(board.get(i).get(j).getFill().equals(blackimg))
		  			{
						board.get(i).get(j).setStroke(Color.BLACK);
		  			}
		  		}
		  	}
	}
	
	/**
	 * Check if a jump is valid. A jump is valid if jumping over opponent's piece
	 * @param player
	 * @param row
	 * @param col
	 */
	public void validJump(Circle player, int row, int col)
	{
		
		if(player.getFill().equals(Color.BLACK) || (isInBounds(row, col) && board.get(row).get(col).getFill().equals(king.getFill())))
		{
		
			if(isInBounds(row, col) && jump==false && col>=1 && row<=6 && (board.get(row).get(col).getFill().equals(player.getFill()) || board.get(row).get(col).getFill().equals(king.getFill()) ) && tiles[row+1][col-1].getFill().equals(Color.MEDIUMSEAGREEN) && board.get(row+1).get(col-1).getFill().equals(Color.MEDIUMSEAGREEN) )
			{
				board.get(row).get(col).setStroke(Color.WHITE);
				board.get(row+1).get(col-1).setStroke(Color.WHITE);
			}
			if(isInBounds(row, col) && row<=5 && col>=2 && (board.get(row+1).get(col-1).getFill().equals(opponent.getFill()) || board.get(row+1).get(col-1).getFill().equals(opponentKing.getFill()) )&& board.get(row+2).get(col-2).getFill().equals(Color.MEDIUMSEAGREEN))
			{	
				board.get(row).get(col).setStroke(Color.WHITE);
				board.get(row+2).get(col-2).setStroke(Color.WHITE);				
			}
			
			if(isInBounds(row, col) && jump==false && row<=6 && col<=6 && (board.get(row).get(col).getFill().equals(player.getFill())  || board.get(row).get(col).getFill().equals(king.getFill())  ) && tiles[row+1][col+1].getFill().equals(Color.MEDIUMSEAGREEN) && board.get(row+1).get(col+1).getFill().equals(Color.MEDIUMSEAGREEN) )
			{
				board.get(row).get(col).setStroke(Color.WHITE);
				board.get(row+1).get(col+1).setStroke(Color.WHITE);
			}
			
			if(isInBounds(row, col) && row<=5 && col<=5 && (board.get(row+1).get(col+1).getFill().equals(opponent.getFill()) || board.get(row+1).get(col+1).getFill().equals(opponentKing.getFill()) ) && board.get(row+2).get(col+2).getFill().equals(Color.MEDIUMSEAGREEN))
			{
				board.get(row).get(col).setStroke(Color.WHITE);
				board.get(row+2).get(col+2).setStroke(Color.WHITE);
			}
			
			isFirstClick = false;
		}
		 if(player.getFill().equals(Color.TOMATO) ||  (isInBounds(row, col) && board.get(row).get(col).getFill().equals(king.getFill()) ) )
		{	
			if(isInBounds(row, col)  && jump==false && row>=1 && col>=1 && (board.get(row).get(col).getFill().equals(player.getFill()) || board.get(row).get(col).getFill().equals(king.getFill())  ) && tiles[row-1][col-1].getFill().equals(Color.MEDIUMSEAGREEN) && board.get(row-1).get(col-1).getFill().equals(Color.MEDIUMSEAGREEN) )
			{
				board.get(row).get(col).setStroke(Color.WHITE);
				board.get(row-1).get(col-1).setStroke(Color.WHITE);
			}
			
			if(isInBounds(row, col) && row>= 2 && col>=2 && (board.get(row-1).get(col-1).getFill().equals(opponent.getFill()) || board.get(row-1).get(col-1).getFill().equals(opponentKing.getFill()) ) && board.get(row-2).get(col-2).getFill().equals(Color.MEDIUMSEAGREEN))
			{
				board.get(row).get(col).setStroke(Color.WHITE);
				board.get(row-2).get(col-2).setStroke(Color.WHITE);	
			}
			
			if(isInBounds(row, col)  && jump == false && row>= 1 &&  col<=6 && (board.get(row).get(col).getFill().equals(player.getFill()) || board.get(row).get(col).getFill().equals(king.getFill()) ) && tiles[row-1][col+1].getFill().equals(Color.MEDIUMSEAGREEN) && board.get(row-1).get(col+1).getFill().equals(Color.MEDIUMSEAGREEN) )
			{
				board.get(row).get(col).setStroke(Color.WHITE);
				board.get(row-1).get(col+1).setStroke(Color.WHITE);
				
			}
			
			if(isInBounds(row, col) && row>= 2 && col<=5 && (board.get(row-1).get(col+1).getFill().equals(opponent.getFill()) || board.get(row-1).get(col+1).getFill().equals(opponentKing.getFill())) && board.get(row-2).get(col+2).getFill().equals(Color.MEDIUMSEAGREEN))
			{
				board.get(row).get(col).setStroke(Color.WHITE);
				board.get(row-2).get(col+2).setStroke(Color.WHITE);	
				
			}			
			isFirstClick = false;		
		}
	}
	
	/**
	 * Display the available jums
	 */
	public void checkJumps()
	{
		
		for(int i = 0; i< 8; i++)
    	{
    		for(int j = 0; j< 8; j++)
    		{
    			if(player.getFill().equals(Color.BLACK) || (isInBounds(i, j) && board.get(i).get(j).getFill().equals(king.getFill())))
    			{    				
    				if(isInBounds(i, j) && i<=5 && j>=2 && (board.get(i).get(j).getFill().equals(player.getFill()) || board.get(i).get(j).getFill().equals(king.getFill()) ) && (board.get(i+1).get(j-1).getFill().equals(opponent.getFill()) || board.get(i+1).get(j-1).getFill().equals(opponentKing.getFill()) ) && board.get(i+2).get(j-2).getFill().equals(Color.MEDIUMSEAGREEN))
    				{	
    					text.setText("Mandatory Jump!");
    					jump = true;
    				}
    				
    				if(isInBounds(i, j) && i<=5 && j<=5 && (board.get(i).get(j).getFill().equals(player.getFill()) || board.get(i).get(j).getFill().equals(king.getFill()) ) && (board.get(i+1).get(j+1).getFill().equals(opponent.getFill()) || board.get(i+1).get(j+1).getFill().equals(opponentKing.getFill()) ) && board.get(i+2).get(j+2).getFill().equals(Color.MEDIUMSEAGREEN))
    				{
    					text.setText("Mandatory Jump!");
    					jump = true;
    				}
    				
    				
    			}
    			 if(player.getFill().equals(Color.TOMATO) ||  (isInBounds(i, j) && board.get(i).get(j).getFill().equals(king.getFill()) ) )
    			{
    				if(isInBounds(i, j) && i>= 2 && j>=2 && (board.get(i).get(j).getFill().equals(player.getFill()) || board.get(i).get(j).getFill().equals(king.getFill()) ) && (board.get(i-1).get(j-1).getFill().equals(opponent.getFill()) || board.get(i-1).get(j-1).getFill().equals(opponentKing.getFill()) ) && board.get(i-2).get(j-2).getFill().equals(Color.MEDIUMSEAGREEN))
    				{	
    					text.setText("Mandatory Jump!");
    					jump=true;
    					
    				}
    				
    				if(isInBounds(i, j) && i>= 2 && j<=5 && (board.get(i).get(j).getFill().equals(player.getFill()) || board.get(i).get(j).getFill().equals(king.getFill()) ) && (board.get(i-1).get(j+1).getFill().equals(opponent.getFill()) || board.get(i-1).get(j+1).getFill().equals(opponentKing.getFill()) ) && board.get(i-2).get(j+2).getFill().equals(Color.MEDIUMSEAGREEN))
    				{
    					text.setText("Mandatory Jump!");
    					jump = true;
    				}			
    					
    			}
    			
    		}
    	}
		
	}
	/**
	 * Check if the row and column specified are valid
	 * @param row
	 * @param col
	 * @return
	 */
	protected boolean isInBounds(int row, int col) //checks to see if row and col are within bounds
	  {
	    if(row>=0 && row< board.size() && col>=0 && col<board.get(0).size())
	    {
	      return true;
	    }
	    else
	      return false;
	  }	  
	
	/**
	 * Initialize a board 
	 */
	protected void initializeBoardArray()
    {
    	board = new ArrayList<ArrayList <Circle>>(8);
    	for(int i = 0; i< 8; i++)
    	{
    		board.add(new ArrayList<Circle>());
    		for(int j = 0; j< 8; j++)
    		{
    			Piece piece = new Piece();
    			piece.strokeCircle(null);
    			if((j%2 != 0 && i%2 == 0) ||(j%2 == 0 && i%2 != 0)  )
  	          	{
	    			piece.colorCircle(Color.MEDIUMSEAGREEN);
	    			piece.strokeCircle(Color.MEDIUMSEAGREEN);
	  	        }
    			else
    			{
    				piece.colorCircle(Color.LEMONCHIFFON);
    				piece.strokeCircle(Color.LEMONCHIFFON);
    			}    		
    			board.get(i).add(piece.getCircle());    			
    			board.get(i).get(j).setTranslateX(j*50+25);
    			board.get(i).get(j).setTranslateY(i*50+25);			
    			pane.getChildren().add(board.get(i).get(j));
    		}
    	}
    }	
	
	/**
	 * Display the board
	 * @return
	 */
	public Pane initializeBoardDisplay() //Displays the game board
	  { 
		redScore = 0;
		blackScore = 0;
		scoretext.setText("" + redScore);
		scoretext2.setText("" + blackScore);
		count = 0;
		isFirstClick = true;
		jump = false;
		
		pane.setPrefSize(400,400);
		 tiles = new Rectangle[8][8];
	      for(int i=0; i<8;i++)
	      {
	        for(int j=0;j<8;j++)
	        {
	          Tile tile =new Tile();
	          tile.setTranslateX(j*50);
	          tile.setTranslateY(i*50);
	          if((j%2 != 0 && i%2 == 0) ||(j%2 == 0 && i%2 != 0)  )
	          {
	        	 tile.colorRectangle(Color.MEDIUMSEAGREEN); 
	          }
	          tiles[i][j] = tile.getRect();
	        
	          pane.getChildren().add(tile);
	        }
	      }  
	      	initializeBoardArray();
	    	      	
	        for(int i=0; i<8;i++)
		      {
		        for(int j=0;j<8;j++)
		        {
		        	if(i==0 || i==1 || i==2)
		        	{
		        		 if((j%2 != 0 && i%2 == 0) ||(j%2 == 0 && i%2 != 0)  )
		   	          {
			      	        board.get(i).get(j).setFill(Color.BLACK);
			      	        board.get(i).get(j).setStroke(Color.BLACK);
		   	          }
		        	}		        	
		        	else if(i==5 || i==6 || i==7)
		        	{
		        		 if((j%2 != 0 && i%2 == 0) ||(j%2 == 0 && i%2 != 0)  )
			   	          {
		        			 board.get(i).get(j).setFill(Color.TOMATO);
		        			 board.get(i).get(j).setStroke(Color.TOMATO);
			   	          }
		        	}
		        }
		      }  
	        return pane;
	     }
	
	public Scene getScene()
	{
		return scene;
	}
	
	 int r1;
     int c2;
     //Tiles of the boardn
	private class Tile extends StackPane
	  {
		  Rectangle r;
		  public Tile(){
		      r =new Rectangle(50,50);
		      r.setFill(Color.LEMONCHIFFON);
		      r.setStroke(null);		      
		      setAlignment(Pos.CENTER);
		      getChildren().addAll(r);
		      
		      r1 = (int)r.getTranslateX()/50;
		      c2 = (int)r.getTranslateY()/50;          
		      }
		  
		  public void colorRectangle(Paint color)
		  {
			  r.setFill(color);
		  }
		  public Rectangle getRect()
		  {
			  return r;
		  }		  
	  }
	
	/**
	 * Gets the coordinate
	 * @param num
	 * @return
	 */
	public int getCoordinate(int num)
	{
		int z = -1;
		if(num> 0 && num <50)
		{
			z= 0;
		}
		else 
		if(num> 50 && num <100)
		{
			z = 1;
		}
		else
		if(num> 100 && num < 150)
		{
			z = 2;
		}
		else
			if(num> 150 && num < 200)
			{
				z = 3;
			}
			else
				if(num> 200 && num < 250)
				{
					z = 4;
				}
				else
					if(num> 250 && num < 300)
					{
						z = 5;
					}
					else
						if(num> 300 && num < 350)
						{
							z = 6;
						}
						else
							if(num> 350 && num < 400)
							{
								z = 7;
							}
		return z;
							
	}
	
	/**Checks scores of the player and sees if any player needs to be crowned and turned into a king. 
	 * 
	 */
	public void scoreAndCrown()
	{
		int tomato = 0;
		int black = 0;
		for(int i=0; i<8;i++)
	      {
	        for(int j=0;j<8;j++)
	        {
	        	// CHECKING FOR THE NUMBER OF PLAYER PIECES
	        	 if(board.get(i).get(j).getFill().equals(Color.TOMATO) || board.get(i).get(j).getFill().equals(redimg)) 
	        	 {
	        		 tomato++;
	        	 }
	        	 else if(board.get(i).get(j).getFill().equals(Color.BLACK)  || board.get(i).get(j).getFill().equals(blackimg))
	        	 {
	        		 black++;
	        	 }
	          
	        // CHECKING TO SEE IF A PIECE NEEDS TO BE CROWNED	 
	        	 if(i==0 && board.get(i).get(j).getFill().equals(Color.TOMATO) )
	        	 {	        		
	        		board.get(i).get(j).setStroke(Color.TOMATO);	        		
	     	        board.get(i).get(j).setFill(redimg);	
	        	 }
	        	 else if(i==7 && board.get(i).get(j).getFill().equals(Color.BLACK) )
	        	 {
	        		board.get(i).get(j).setStroke(Color.BLACK);
	     	        board.get(i).get(j).setFill(blackimg);	 
	        	 }
	        }
	      } 
		// CALCULATING SCORE OF THE TWO PLAYERS
		redScore = 12- black;
		blackScore = 12 - tomato;
		
		scoretext.setText("" + redScore);
		scoretext2.setText("" + blackScore);
		
		if(redScore==12)
		{
			text.setText("Red has won!");
			
		}
		else if(blackScore==12)
		{
			text.setText("Black has won!");
			
		}
	}
	
	/**
	 * Sets the player pieces
	 */
	public void setPlayers()
	{
		if(count%2 !=0)
		{
			jump = false;
			player.setFill(Color.BLACK);	
			player.setStroke(Color.BLACK);				
			king.setFill(blackimg);	
			king.setStroke(Color.BLACK);
			
			opponent.setFill(Color.TOMATO);
			opponent.setStroke(Color.TOMATO);
			opponentKing.setFill(redimg);
			opponentKing.setStroke(Color.TOMATO);
		}
		else
		{
			jump = false;
			player.setFill(Color.TOMATO);
			player.setStroke(Color.TOMATO);			
			king.setFill(redimg);
			king.setStroke(Color.TOMATO);
			
			opponent.setFill(Color.BLACK);
			opponent.setStroke(Color.BLACK);
			opponentKing.setFill(blackimg);
			opponentKing.setStroke(Color.BLACK);
		}
	}
	
	/**
	 * Check the moves
	 * @return
	 */
	public boolean checkMoves()
	{
		moves = 0;	
		for(int i = 0; i< 8; i++)
    	{    		
    		for(int j = 0; j< 8; j++)
    		{
		
		if(player.getFill().equals(Color.BLACK) || (isInBounds(i, j) && board.get(i).get(j).getFill().equals(king.getFill())))
		{
			if(isInBounds(i, j) && j>=1 && i<=6 && (board.get(i).get(j).getFill().equals(player.getFill()) || board.get(i).get(j).getFill().equals(king.getFill()) ) && tiles[i+1][j-1].getFill().equals(Color.MEDIUMSEAGREEN) && board.get(i+1).get(j-1).getFill().equals(Color.MEDIUMSEAGREEN) )
			{
				moves++;
			}
			if(isInBounds(i, j) && i<=5 && j>=2 && (board.get(i).get(j).getFill().equals(player.getFill()) || board.get(i).get(j).getFill().equals(king.getFill()) ) && (board.get(i+1).get(j-1).getFill().equals(opponent.getFill()) || board.get(i+1).get(j-1).getFill().equals(opponentKing.getFill()) )&& board.get(i+2).get(j-2).getFill().equals(Color.MEDIUMSEAGREEN) && tiles[i+2][j-2].getFill().equals(Color.MEDIUMSEAGREEN))
			{
				moves++;
			}
			
			if(isInBounds(i, j)&& i<=6 && j<=6 && (board.get(i).get(j).getFill().equals(player.getFill())  || board.get(i).get(j).getFill().equals(king.getFill())  ) && tiles[i+1][j+1].getFill().equals(Color.MEDIUMSEAGREEN) && board.get(i+1).get(j+1).getFill().equals(Color.MEDIUMSEAGREEN) )
			{
				moves++;
			}
			
			if(isInBounds(i, j) && i<=5 && j<=5 && (board.get(i).get(j).getFill().equals(player.getFill()) || board.get(i).get(j).getFill().equals(king.getFill()) ) &&(board.get(i+1).get(j+1).getFill().equals(opponent.getFill()) || board.get(i+1).get(j+1).getFill().equals(opponentKing.getFill()) ) && board.get(i+2).get(j+2).getFill().equals(Color.MEDIUMSEAGREEN) && tiles[i+2][j+2].getFill().equals(Color.MEDIUMSEAGREEN))
			{
				moves++;
			}
		}
		 if(player.getFill().equals(Color.TOMATO) ||  (isInBounds(i, j) && board.get(i).get(j).getFill().equals(king.getFill()) ) )
		{	
			if(isInBounds(i, j) && i>=1 && j>=1 && (board.get(i).get(j).getFill().equals(player.getFill()) || board.get(i).get(j).getFill().equals(king.getFill())  ) && tiles[i-1][j-1].getFill().equals(Color.MEDIUMSEAGREEN) && board.get(i-1).get(j-1).getFill().equals(Color.MEDIUMSEAGREEN) )
			{	
				moves++;
			}
			
			if(isInBounds(i, j) && i>= 2 && j>=2 && (board.get(i).get(j).getFill().equals(player.getFill()) || board.get(i).get(j).getFill().equals(king.getFill()) ) && (board.get(i-1).get(j-1).getFill().equals(opponent.getFill()) || board.get(i-1).get(j-1).getFill().equals(opponentKing.getFill()) ) && board.get(i-2).get(j-2).getFill().equals(Color.MEDIUMSEAGREEN)  && tiles[i-2][j-2].getFill().equals(Color.MEDIUMSEAGREEN))
			{
				moves++;
			}
			
			if(isInBounds(i, j) && i>= 1 && j<=6 && (board.get(i).get(j).getFill().equals(player.getFill()) || board.get(i).get(j).getFill().equals(king.getFill()) ) && tiles[i-1][j+1].getFill().equals(Color.MEDIUMSEAGREEN) && board.get(i-1).get(j+1).getFill().equals(Color.MEDIUMSEAGREEN) )
			{
				moves++;
			}
			
			if(isInBounds(i, j) && i>= 2 && j<=5 && (board.get(i).get(j).getFill().equals(player.getFill()) || board.get(i).get(j).getFill().equals(king.getFill()) ) && (board.get(i-1).get(j+1).getFill().equals(opponent.getFill()) || board.get(i-1).get(j+1).getFill().equals(opponentKing.getFill())) && board.get(i-2).get(j+2).getFill().equals(Color.MEDIUMSEAGREEN) && tiles[i-2][j+2].getFill().equals(Color.MEDIUMSEAGREEN))
			{
				moves++;
			}	
		}
		 
    } //forloop
	}//forloop
	
		if(moves == 0)
		{
			return false;
		}
		else 
			return true;
	
	}
		
	
}