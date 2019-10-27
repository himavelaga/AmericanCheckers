import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 * represents the player piece represented as a circle
 * @author User
 *
 */
public class Piece extends StackPane
  {
		  Circle s;
		 
		  public Piece()
		  {
			s = new Circle(20);
			s.setFill(Color.PLUM);
			s.setStroke(Color.PLUM);
			
		  }
		  
	
		  /**
		   * Retreives the circle
		   * @return player piece
		   */
		  public Circle getCircle()
		  {
			  return s;
		  }
	
		  /**
		   * color the player piece
		   * @param color color of piece
		   */
		  public void colorCircle(Paint color){
			  s.setFill(color);
		  }
		  
		  /**
		   * set stroke of circle
		   * @param color
		   */
		  public void strokeCircle(Paint color){
			  s.setStroke(color);
		  }
		  
		  
  }
	

