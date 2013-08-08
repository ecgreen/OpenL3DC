/*
 * @author: Ethan Green
 *
 * This code will take a finger and return a value from 0 to 1 
 * that is the relative position of the user's finger.
 *
 * Values are normalized around: 
 * x from 0 to 400    practical range
 * y from 40 to 400   practical range
 *
 *
 * THE POSITION (0,0) IS NORMALIZED TO THE BOTTOM LEFT CORNER OF THE SCREEN
 *
 */

import com.leapmotion.leap.*;

public class RelativeScreenPosition {
    private int Xsensitivity = 200;
    private int Ysensitivity = 100;
    private int centerPositionX = 200;
    private int centerPositionY = 100;
    Screen screen;
    Controller controller = new Controller();
    
    RelativeScreenPosition(){
        ScreenList screenList = controller.calibratedScreens();
        this.screen = screenList.get(0);
    }
    
    
    /*
     * MUST ACCEPT A VALUE BETWEEN 50 AND 300 INCLUSIVE
     * RETURNS TRUE IF THE VALUES WERE CHANGED AND FALSE
     * IF EITHER OF THE VALUES WERE INVALID.
     */
    public boolean setCenterPositions(int centerX, int centerY){
        if( ! (centerX < 50 || centerX > 300 || centerY < 50 || centerY > 300) ) {
            this.centerPositionX = centerX;
            this.centerPositionY = centerY;
            return true;
        }
        return false;
    }
    
    /*
     * MUST ACCEPT A VALUE BETWEEN 50 AND 400 INCLUSIVE
     * RETURNS TRUE IF THE VALUES WERE CHANGED AND FALSE
     * IF EITHER OF THE VALUES WERE INVALID.
     */
    public boolean setSensitivties(int sensitivityX, int sensitivityY){
        if( ! (sensitivityX < 50 || sensitivityX > 400 || sensitivityY < 50 || sensitivityY > 400) ) {
            this.Xsensitivity = sensitivityX;
            this.Ysensitivity = sensitivityY;
            return true;
        }
        return false;
    }
    
    
    /*
     * PASS IN A FINGER AND THE METHOD WILL RETURN THE RELATIVE 
     * SCREEN POSITION (0 TO 1) BASED ON THE SENSITIVITY.
     */
    
    public double relativeX(Finger f){
        int normalizedX = (int) normalizeXValue(f.tipPosition().getX());
        return bounds((int) (normalizedX / maxX()));
    }
    
    public double relativeY(Finger f){
        int normalizedY = (int) normalizeYValue(f.tipPosition().getY());
        return bounds((int) (invertY(normalizedY)));
    }
    
    public int pixelLocationX(Finger f){
        return (int) (screen.widthPixels() * relativeX(f));
    }
    
    public int pixedLocationY(Finger f){
        return (int) (screen.heightPixels() * relativeY(f));
    }
    
    
    
    
    /*
     * PRIVATE METHODS
     */
    
    private double invertY(int uninvertedY){
        return ( -1 * ( (uninvertedY/maxY() ) - 1));
    }
    
    private int normalizeXValue(float position){
        return (int) position + Xsensitivity/2;
    }
    
    private int normalizeYValue(float position){
        return (int) position - Ysensitivity/2;
    }
    
    
    
    
    
    private int minX(){
        return centerPositionX - Xsensitivity/2;
    }
    
    private double maxX(){
        return centerPositionX + Xsensitivity/2;
    }
    
    
    /*
     * WHEN THESE METHODS ARE USED Y'S VALUES NEED TO BE
     * FLIPPED FROM 0,0 AT TOP LEFT TO 0,0 BOTTOM LEFT
     */
    
    private int minY(){
        return centerPositionY - Ysensitivity/2;
    }
    
    private double maxY(){
        return centerPositionY + Ysensitivity/2;
    }
    
    
    /*
     * CREATES A BOUNDS CHECK SO THAT THE MAX RANGE
     * IS AT POSITION MAXX OR MAXY. THE SAME IS
     * TRUE FOR MIN
     */
    
    private int bounds(int positionToCheck){
        if(positionToCheck > 1) return 1;
        else if(positionToCheck < 0) return 0;
        else return positionToCheck;
    }


    
}