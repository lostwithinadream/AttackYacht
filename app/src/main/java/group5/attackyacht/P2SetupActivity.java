/*
********************************************************************************
*** P2SetupActivity.java
*** Group 5
********************************************************************************
*** Purpose:
*** Player 2 choosed where they wish to place their ships pre-game, which then
*** carries over to P1TurnActivity & P2TurnActivity
********************************************************************************
*** Date:
*** 11/19/15
********************************************************************************
*** Change Log:
*** 11/19/15 - CS - Created onCreate
*** 11/23/15 - CS - Created onClick
*** 11/23/15 - CS - Created displayMessage
*** 11/23/15 - CS - Created getFriendlyWaters
*** 11/xx/15 - xx -
***
********************************************************************************
*/

// Project Package
package group5.attackyacht;

// Imported libraries
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class P2SetupActivity extends AppCompatActivity {

    // Class-wide variable
    static private int ROW = 7;
    static private int COL = 12;
    static private Ship[][] friendlyWaters = new Ship[ROW][COL];
    private int pieceLimit = 15;
    static private int numPieces = 0;

/*
********************************************************************************
*** onCreate
*** Group 5
********************************************************************************
*** Purpose:
*** Displays the setup screen, gets user input
*** Inputs:
*** Bundle savedInstanceState
*** Outputs:
*** n/a
********************************************************************************
*** Date
*** 11/19/15
********************************************************************************
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p2_setup);

        TableLayout table = (TableLayout) findViewById(R.id.shipSetupGridP2);
        for (int i = 0; i < ROW; i++){
            TableRow row = new TableRow(this);
            for (int j = 0; j < COL; j++){
                ImageView image = new ImageView (this);
                int imageID;
                if(friendlyWaters[i][j] == null){
                    friendlyWaters[i][j] = new Ship("water", i, j);
                    imageID = getResources().getIdentifier("ship_water", "drawable", getPackageName());
                }
                else{
                    imageID = getResources().getIdentifier(("ship_" + (friendlyWaters[i][j]).getType()), "drawable", getPackageName());
                }
                image.setImageDrawable(ContextCompat.getDrawable(this, imageID));
                row.addView(image, 100, 100);
                image.setOnClickListener(onClick(image, i, j));
            }
            table.addView(row);
        }

        Button buttonReady = (Button) findViewById(R.id.button_readyP2);
        buttonReady.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                displayMessage("Pass the phone!");

                // Begin player 1's first turn
                Intent intent = new Intent(P2SetupActivity.this, P1TurnActivity.class);
                startActivity(intent);
            }
        });
    }

    /*
********************************************************************************
*** onClick
*** Group 5
********************************************************************************
*** Purpose:
*** Adds functional to the selection grid
*** Inputs:
*** final ImageView im, final int row, final int col
*** Outputs:
*** View.OnClickListener
********************************************************************************
*** Date
*** 11/23/15
********************************************************************************
*/
    View.OnClickListener onClick(final ImageView im, final int row, final int col)  {
        return new View.OnClickListener() {
            public void onClick(View v) {

                ImageView im = (ImageView) v;
                String type = (friendlyWaters[row][col]).getType();
                String left, right, above, below;

                if(type.equalsIgnoreCase("water")){

                    if(numPieces == pieceLimit){
                        displayMessage("Max Number of Ship Pieces = " + pieceLimit);
                        return;
                    }
                    else{ numPieces++; }

                    if(row != 0){ below = (friendlyWaters[row - 1][col]).getType(); }
                    else{ below = "water"; }

                    if(row != ROW-1){ above = (friendlyWaters[row + 1][col]).getType(); }
                    else{ above = "water"; }

                    if(col != 0){ left = (friendlyWaters[row][col - 1]).getType(); }
                    else{ left = "water"; }

                    if(col != COL-1){ right = (friendlyWaters[row][col + 1]).getType(); }
                    else{ right = "water"; }

                    boolean b = false, a = false, l = false, r = false;

                    if(!(below.equalsIgnoreCase("water"))){
                        (friendlyWaters[row-1][col]).setType("top");
                        (friendlyWaters[row][col]).setType("bottom");
                        b = true;
                        im.setImageDrawable(ContextCompat.getDrawable(P2SetupActivity.this, R.drawable.ship_bottom));
                    }
                    if(!(above.equalsIgnoreCase("water"))){
                        (friendlyWaters[row+1][col]).setType("bottom");
                        (friendlyWaters[row][col]).setType("top");
                        a = true;
                        im.setImageDrawable(ContextCompat.getDrawable(P2SetupActivity.this, R.drawable.ship_top));
                    }
                    if(!(left.equalsIgnoreCase("water"))){
                        (friendlyWaters[row][col-1]).setType("left");
                        (friendlyWaters[row][col]).setType("right");
                        l = true;
                        im.setImageDrawable(ContextCompat.getDrawable(P2SetupActivity.this, R.drawable.ship_right));
                    }
                    if(!(right.equalsIgnoreCase("water"))){
                        (friendlyWaters[row][col+1]).setType("right");
                        (friendlyWaters[row][col]).setType("left");
                        r = true;
                        im.setImageDrawable(ContextCompat.getDrawable(P2SetupActivity.this, R.drawable.ship_left));
                    }
                    if(b & a){
                        (friendlyWaters[row][col]).setType("middle_v");
                        im.setImageDrawable(ContextCompat.getDrawable(P2SetupActivity.this, R.drawable.ship_middle_v));
                    }
                    if(l & r){
                        (friendlyWaters[row][col]).setType("middle_h");
                        im.setImageDrawable(ContextCompat.getDrawable(P2SetupActivity.this, R.drawable.ship_middle_h));
                    }
                    if((!b) & (!a) & (!l) & (!r)){
                        (friendlyWaters[row][col]).setType("one");
                        im.setImageDrawable(ContextCompat.getDrawable(P2SetupActivity.this, R.drawable.ship_one));
                    }
                }
                else {
                    (friendlyWaters[row][col]).setType("water");
                    im.setImageDrawable(ContextCompat.getDrawable(P2SetupActivity.this, R.drawable.ship_water));
                    numPieces--;
                }
            }
        };
    }

    /*
********************************************************************************
*** displayMessage
*** Group 5
********************************************************************************
*** Purpose:
*** Displays a message to the player
*** Inputs:
*** Bundle savedInstanceState
*** Outputs:
*** n/a
********************************************************************************
*** Date
*** 11/23/15
********************************************************************************
*/
    private void displayMessage(String message){
        // Get context and set duration of message
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        // Actually display the message
        Toast toast = Toast.makeText(context, message, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /*
********************************************************************************
*** getFriendlyWaters
*** Group 5
********************************************************************************
*** Purpose:
*** Returns friendlyWaters
*** Inputs:
*** n/a
*** Outputs:
*** ship [][] friendlyWaters
********************************************************************************
*** Date
*** 11/23/15
********************************************************************************
*/
    static public Ship[][] getFriendlyWaters() {
        return friendlyWaters;
    }

}
