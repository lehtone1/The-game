 package o1.game

import scala.swing._
 import scala.swing.event._
 import java.awt.Color
 import java.awt.event._
 import java.awt.{geom}
import o1.game.Wall
import o1.game.Spot
import o1.game.Player
import o1.game.Floor
import scala.collection.mutable.Buffer

object MainFrame extends SimpleSwingApplication  {
  val width = 40
  val height = 20
  val cellSize = 25
  var world: Array[Array[Spot]] = Array.fill(width, height)(Floor)
  val r = scala.util.Random
  val player = new Player(width/2, height)
  var pressedKeys = Buffer[Key.Value]() //A buffer that holds all keys that are pressed at the same time
  
  
  //Creates a row full of wall objects
  def fillWall(row: Int) = {
    for(line <- world){
      line(row) = Wall
    }
  }
  
  //Creates holes marked with Floor-objects to the row.
  def fillRow(row: Int) = {
    val randomNum = r.nextInt(width)
    var crawler = 0
    while(crawler < randomNum){
      val randomNum2 = r.nextInt(width - 1)
      world(randomNum2)(row) = Floor
      crawler += 1
    }
  }
  //Fills the "world" with holed walls
  def fillWorld = {
    for(line <- 0 to height - 1){
      fillWall(line)
      fillRow(line)
    }
  }
  
  val canvas = new GridPanel(rows0 = height, cols0 = width) {
    
  preferredSize = new Dimension(width * cellSize, height * cellSize)
  
  fillWorld
  

  
  //Paints the world again after some event
  
  override def paintComponent(g: Graphics2D) {
      for (i <- 0 until width) {
        for (k <- 0 until height) { // Loop through the world grid
          world(i)(k) match {       // Match what is found in every position
            case Wall => {          // If a wall is there, change color to black and paint a black tile representing a wall
              g.setColor(Color.BLACK)
              g.fillRect(i * cellSize, k * cellSize, cellSize, cellSize)
            }
            case Floor => {         // If a floor is there, change color to cyan and paint a cyan tile representing floor
              g.setColor(Color.WHITE)
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
    //Determines the game reactions for keyboards and mouse events
    reactions += {
      case KeyPressed(canvas, key, _, _) => { //When any key is pressed
        pressedKeys += key //the key is added to the pressed keys
        if(pressedKeys contains Key.Space){ //When space is one of the keys
          if(pressedKeys contains Key.Left){//the player moves two spaces to 
            if(world(player.x - 2)(player.y) != Wall){//chosen direction
              player.move(player.x - 2, player.y)
              repaint()
            }
          }else if(pressedKeys contains Key.Right) {
            if(world(player.x + 2)(player.y) != Wall){
              player.move(player.x + 2, player.y)
              repaint()
            }
          }else if(pressedKeys contains Key.Up) {
            if(world(player.x)(player.y - 2) != Wall){
              player.move(player.x, player.y - 2)
              repaint()
            }
          }else if(pressedKeys contains Key.Down){
            if(world(player.x)(player.y + 2) != Wall){
              player.move(player.x, player.y + 2)
              repaint()
            }
          }   
        }else if(pressedKeys contains Key.Left){
          if(world(player.x - 1)(player.y) != Wall){
            player.move(player.x - 1, player.y)
            repaint()
            }
        }else if(pressedKeys contains Key.Right) {
          if(world(player.x + 1)(player.y) != Wall){
            player.move(player.x + 1, player.y)
            repaint()
            }
        }else if(pressedKeys contains Key.Up) {
          if(world(player.x)(player.y - 1) != Wall){
            player.move(player.x, player.y - 1)
            repaint()
            }
        }else if(pressedKeys contains Key.Down){
          if(world(player.x)(player.y + 1) != Wall){
            player.move(player.x, player.y + 1)
            repaint()
          }
        }
  
      }
      
      case KeyReleased(canvas, key, _, _) => {
        pressedKeys = Buffer[Key.Value]()
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
    }

  }
}