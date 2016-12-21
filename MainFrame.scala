 package o1.game

import scala.swing._
import scala.swing.event._
import java.awt.Color
import java.awt.event._
import com.sun.glass.events.MouseEvent
import java.awt.{geom}


object MainFrame extends SimpleSwingApplication  {
  val width = 40
  val height = 20
  val cellSize = 25
  var world: Array[Array[Spot]] = Array.fill(width, height)(Floor)
  val r = scala.util.Random
  val player = new Player(1, 1)
  
  val canvas = new GridPanel(rows0 = height, cols0 = width) {
    
  preferredSize = new Dimension(width * cellSize, height * cellSize)
  
  override def paintComponent(g: Graphics2D) {
      for (i <- 0 until width) {
        for (k <- 0 until height) { // Loop through the world grid
          world(i)(k) match {       // Match what is found in every position
            case Wall => {          // If a wall is there, change color to black and paint a black tile representing a wall
              g.setColor(Color.BLACK)
              g.fillRect(i * cellSize, k * cellSize, cellSize, cellSize)
            }
            case Floor => {         // If a floor is there, change color to cyan and paint a cyan tile representing floor
              g.setColor(Color.CYAN)
              g.fillRect(i * cellSize, k * cellSize, cellSize, cellSize)
            }
          }
        }
      }
      g.setColor(Color.ORANGE)      // Set color for the player to be drawn
      g.fillOval(player.x * cellSize, player.y * cellSize, cellSize, cellSize) // Draw player to its location
      g.setColor(Color.GRAY)
    }
  }
  
  
  def top = new MainFrame {
    title = "Game"
    preferredSize = new Dimension(width * cellSize, height * cellSize)

    contents = canvas
    
    listenTo(canvas.mouse.clicks)
    listenTo(canvas.keys)
    canvas.focusable = true
    canvas.requestFocus
    reactions += {
      case KeyPressed(canvas, Key.Left, _, _) => {
        if(world(player.x - 1)(player.y) != Wall){
          player.move(player.x - 1, player.y)
          repaint()
        }
      }
      case KeyPressed(canvas, Key.Right, _, _) => {
        if(world(player.x + 1)(player.y) != Wall){
          player.move(player.x + 1, player.y)
          repaint()
        }
      }
      case KeyPressed(canvas, Key.Up, _, _) => {
        if(world(player.x)(player.y - 1) != Wall){
          player.move(player.x, player.y - 1)
          repaint()
        }
      }
      case KeyPressed(canvas, Key.Down, _, _) => {
        if(world(player.x)(player.y + 1) != Wall){
          player.move(player.x, player.y + 1)
          repaint()
        }
      }
      case MouseClicked(canvas, point, _, clicks, _) => {       
        if(clicks == 2){
          world(point.x/cellSize)(point.y/cellSize) = Floor
          repaint()
        }else{
          world(point.x/cellSize)(point.y/cellSize) = Wall
          repaint()
        }
      }
      case MouseDragged(canvas, point, _) => {
        lineTo(point)
      }

    }
    var path = new geom.GeneralPath
    def lineTo(p: Point) { path.lineTo(p.x, p.y); repaint() }
    

  }
}