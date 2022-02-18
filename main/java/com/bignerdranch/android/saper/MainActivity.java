package com.bignerdranch.android.saper;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageView;
import android.content.Context;
import android.content.Intent;
import android.widget.RelativeLayout;


import java.util.ArrayDeque;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


        private int n = 8, indentLeft = 80, indentTop = 400, count = 0, bombAmount = 10;
        private int imagesID[] = new int[12];
        private int fieldValues[][] = new int[n][n];

        private ArrayList<Integer> listX;
        private ArrayList<Integer> listY;

        private AlertDialog.Builder ad;

        private State states[][];

        private ArrayDeque<Integer> arrayOfActions;

        private CellValue field[][] = new CellValue[n][n];

        private boolean firstTurn = true;

        private ImageView imageViews[][] = new ImageView[n][n];

        private RelativeLayout relative_layout;

        private final int ID_BAD_END_OF_THE_GAME = 0, ID_GOOD_END_OF_THE_GAME = 1, CELL_DISTANCE = 110, TOP_MARGIN = 400, LEFT_MARGIN = 80;


    enum CellValue
    {
        NUMBER,
        UNDEFINED,
        BOMB
    }

    enum State
    {
        CLOSED,
        OPENED,
        MARKED
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RelativeLayout relative_layout = (RelativeLayout) findViewById(R.id.relativeLayout);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View myView = inflater.inflate(R.layout.activity_main, null);

        listX = new ArrayList<>();
        listY = new ArrayList<>();

        arrayOfActions = new ArrayDeque<>();

        String again = "Play again";


        states = new State[n][n];

        relative_layout = new RelativeLayout(this);
        relative_layout = (RelativeLayout) findViewById(R.id.relativeLayout);


        restart();


        int bombCount = 0;



        imagesID[0] = R.drawable.cell0;
        imagesID[1] = R.drawable.cell1;
        imagesID[2] = R.drawable.cell2;
        imagesID[3] = R.drawable.cell3;
        imagesID[4] = R.drawable.cell4;
        imagesID[5] = R.drawable.cell5;
        imagesID[6] = R.drawable.cell6;
        imagesID[7] = R.drawable.cell7;
        imagesID[8] = R.drawable.cell8;
        imagesID[9] = R.drawable.cell;
        imagesID[10] = R.drawable.cell_flag;
        imagesID[11] = R.drawable.bomba;


//        for(int i = 0; i < n; i++)
//        {
//            for(int j = 0; j < n; j++)
//            {
//                imageViews[i][j].setId(count);
//                count++;
//            }
//        }



        int i, j;
        for(i = 0; i < n; i++)
        {
            for(j = 0; j < n; j++)
            {
                imageViews[i][j] = new ImageView(this);
                final int p = count;
                imageViews[i][j].setId(p);
                imageViews[i][j].setOnClickListener(mOnClickListener);
                imageViews[i][j].setOnLongClickListener(mOnLongClickListener);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(indentLeft, indentTop, 10, 10);
                imageViews[i][j].setImageResource(imagesID[9]);

//                ImageView.OnClickListener onClickListener = new ImageView.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        field[i][j] = CellValue.BOMB;
//                    }
//                };
//                imageViews[i][j].setOnClickListener(onClickListener);

                relative_layout.addView(imageViews[i][j], layoutParams);
                indentLeft += CELL_DISTANCE;
                count++;
            }
            indentLeft = LEFT_MARGIN;
            indentTop += CELL_DISTANCE;
        }
        indentLeft = LEFT_MARGIN;
        indentTop = TOP_MARGIN;
    }


    void fillBombs()
    {
        int bombCount = 0;
        int bombsPos[] = new int[bombAmount];
        for(int i = 0 ; i < bombAmount; i++)
        {
            bombsPos[i] = -1;
        }

        while (bombCount < bombAmount)
        {
            int cellBomb = (int)(Math.random()*64);
            int k = 0;
            for(int i = 0; i < bombAmount; i++)
            {
                if(cellBomb == bombsPos[i])
                {
                    k++;
                }
            }
            if(k == 0)
            {
                bombsPos[bombCount] = cellBomb;
                field[cellBomb/n][cellBomb%n] = CellValue.BOMB;
                fieldValues[cellBomb/n][cellBomb%n] = -1;
                bombCount++;
            }
        }

        int k = 0;
        for(int i = 0; i < n; i++)
        {
            for(int j = 0; j < n; j++)
            {
                if(field[i][j] == CellValue.UNDEFINED)
                {
                    if(i == 0 && j == 0)
                    {
                        if(field[i][j+1] == CellValue.BOMB)
                        {
                            k++;
                        }
                        if(field[i+1][j] == CellValue.BOMB)
                        {
                            k++;
                        }
                        if(field[i+1][j+1] == CellValue.BOMB)
                        {
                            k++;
                        }
                    }
                    else if(i == 0 && j == n-1)
                    {
                        if(field[i][j-1] == CellValue.BOMB)
                        {
                            k++;
                        }
                        if(field[i+1][j] == CellValue.BOMB)
                        {
                            k++;
                        }
                        if(field[i+1][j-1] == CellValue.BOMB)
                        {
                            k++;
                        }
                    }
                    else if(i == n-1 && j == n-1)
                    {
                        if(field[i][j-1] == CellValue.BOMB)
                        {
                            k++;
                        }
                        if(field[i-1][j] == CellValue.BOMB)
                        {
                            k++;
                        }
                        if(field[i-1][j-1] == CellValue.BOMB)
                        {
                            k++;
                        }
                    }
                    else if(i == n-1 && j == 0)
                    {
                        if(field[i][j+1] == CellValue.BOMB)
                        {
                            k++;
                        }
                        if(field[i-1][j] == CellValue.BOMB)
                        {
                            k++;
                        }
                        if(field[i-1][j+1] == CellValue.BOMB)
                        {
                            k++;
                        }
                    }
                    else if(i == 0 && j != 0 && j != n-1)
                    {
                        for (int i1 = i; i1 < i + 2; i1++) {
                            for (int j1 = j - 1; j1 < j + 2; j1++) {
                                if (i1 == i && j1 == j) continue;
                                else {
                                    if (field[i1][j1] == CellValue.BOMB) {
                                        k++;
                                    }
                                }
                            }
                        }
                    }
                    else if(j == n-1 && i != n-1 && i != 0)
                    {
                        for (int i1 = i - 1; i1 < i + 2; i1++) {
                            for (int j1 = j - 1; j1 < j + 1; j1++) {
                                if (i1 == i && j1 == j) continue;
                                else {
                                    if (field[i1][j1] == CellValue.BOMB) {
                                        k++;
                                    }
                                }
                            }
                        }
                    }
                    else if(i == n-1 && j != n-1 && j != 0)
                    {
                        for (int i1 = i - 1; i1 < i + 1; i1++) {
                            for (int j1 = j - 1; j1 < j + 2; j1++) {
                                if (i1 == i && j1 + 1 == j + 1) continue;
                                else {
                                    if (field[i1][j1] == CellValue.BOMB) {
                                        k++;
                                    }
                                }
                            }
                        }
                    }
                    else if(j == 0 && i != 0 && i != n-1)
                    {
                        for (int i1 = i - 1; i1 < i + 2; i1++) {
                            for (int j1 = j; j1 < j + 2; j1++) {
                                if (i1 == i && j1 == j) continue;
                                else {
                                    if (field[i1][j1] == CellValue.BOMB) {
                                        k++;
                                    }
                                }
                            }
                        }
                    }
                    else if(i != 0 && i != n-1 && j != 0 && j != n-1){
                        for (int i1 = i - 1; i1 < i + 2; i1++) {
                            for (int j1 = j - 1; j1 < j + 2; j1++) {
                                if (i1 == i && j1 == j) continue;
                                else {
                                    if (field[i1][j1] == CellValue.BOMB) {
                                        k++;
                                    }
                                }
                            }
                        }
                    }
                    field[i][j] = CellValue.NUMBER;
                    fieldValues[i][j] = k;
                    k = 0;
                }
            }
        }
    }


    public void onClick1(View v)
    {
        if(v.getId() == R.id.IW1) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(indentLeft, indentTop, 10, 10);
                    if (field[i][j] == CellValue.BOMB) {
                        states[i][j] = State.OPENED;
                        imageViews[i][j].setImageResource(imagesID[11]);
                    } else if (field[i][j] == CellValue.NUMBER) {
                        states[i][j] = State.OPENED;
                        imageViews[i][j].setImageResource(imagesID[fieldValues[i][j]]);
                    }
                    //relative_layout.addView(imageViews[i][j], layoutParams);
                    indentLeft += CELL_DISTANCE;
                }
                indentLeft = LEFT_MARGIN;
                indentTop += CELL_DISTANCE;
            }
            indentLeft = LEFT_MARGIN;
            indentTop = TOP_MARGIN;
            paint();
        }
    }

    public void openCells()
    {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(indentLeft, indentTop, 10, 10);
                if (field[i][j] == CellValue.BOMB) {
                    states[i][j] = State.OPENED;
                    imageViews[i][j].setImageResource(imagesID[11]);
                } else if (field[i][j] == CellValue.NUMBER) {
                    states[i][j] = State.OPENED;
                    imageViews[i][j].setImageResource(imagesID[fieldValues[i][j]]);
                }
                //relative_layout.addView(imageViews[i][j], layoutParams);
                indentLeft += CELL_DISTANCE;
            }
            indentLeft = LEFT_MARGIN;
            indentTop += CELL_DISTANCE;
        }
        indentLeft = LEFT_MARGIN;
        indentTop = TOP_MARGIN;
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.IW1) {
                openCells();
                paint();
            } else {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(indentLeft, indentTop, 10, 10);
                int i11 = 0, j11 = 0;
                if (firstTurn == true) {
                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < n; j++) {
                            if (imageViews[i][j].getId() == v.getId()) {
                                fillBombs();
                                while (field[i][j] == CellValue.BOMB) {
                                    restart();
                                    fillBombs();
                                }
                                if(fieldValues[i][j] == 0) {
                                    arrayOfActions.addLast(i);
                                    arrayOfActions.addLast(j);
                                    while (arrayOfActions.size() != 0) {
                                        checkFields(arrayOfActions.poll(), arrayOfActions.poll());
                                    }
                                    paint();
                                }
                                else {
                                    states[i][j] = State.OPENED;
                                    paint();
                                }
                            }
                        }
                    }
                    firstTurn = false;
                } else {
                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < n; j++) {
                            if (imageViews[i][j].getId() == v.getId()) {
                                if (field[i][j] == CellValue.BOMB || fieldValues[i][j] == -1) {
                                    if(states[i][j] != State.MARKED) {
                                        for (int i1 = 0; i1 < n; i1++) {
                                            for (int j1 = 0; j1 < n; j1++) {
                                                if (field[i1][j1] == CellValue.BOMB) {
                                                    states[i1][j1] = State.OPENED;
                                                } else if (field[i1][j1] == CellValue.NUMBER) {
                                                    states[i1][j1] = State.OPENED;
                                                }
                                                //relative_layout.addView(imageViews[i][j], layoutParams);
                                            }
                                        }

                                        paint();
                                        showDialog(ID_BAD_END_OF_THE_GAME);
                                    }
                                } else {
                                    if (states[i][j] == State.CLOSED) {
                                        if (fieldValues[i][j] == 0) {
                                            //emptyField(fieldValues,i,j);


                                            arrayOfActions.addLast(i);
                                            arrayOfActions.addLast(j);
                                            while (arrayOfActions.size() != 0) {
                                                checkFields(arrayOfActions.poll(), arrayOfActions.poll());
                                            }
                                            //states[i][j] = State.OPENED;
                                            paint();
                                        } else {
                                            states[i][j] = State.OPENED;
                                            paint();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    };

    void checkFields(int i, int j)
    {
        states[i][j] = State.OPENED;
        if(j != 0) {
            if (states[i][j-1] == State.CLOSED) {
                if (field[i][j - 1] == CellValue.NUMBER) {
                    if (fieldValues[i][j - 1] != 0) {
                        states[i][j - 1] = State.OPENED;
                    } else {
                        arrayOfActions.addLast(i);
                        arrayOfActions.addLast(j - 1);
                    }
                }
            }
        }
        if(j != n-1) {
            if (states[i][j+1] == State.CLOSED) {
                if (field[i][j + 1] == CellValue.NUMBER) {
                    if (fieldValues[i][j + 1] != 0) {
                        states[i][j + 1] = State.OPENED;
                    } else {
                        arrayOfActions.addLast(i);
                        arrayOfActions.addLast(j + 1);
                    }
                }
            }
        }
        if(i != 0) {
            if (states[i-1][j] == State.CLOSED) {
                if (field[i - 1][j] == CellValue.NUMBER) {
                    if (fieldValues[i - 1][j] != 0) {
                        states[i - 1][j] = State.OPENED;
                    } else {
                        arrayOfActions.addLast(i - 1);
                        arrayOfActions.addLast(j);
                    }
                }
            }
        }
        if(i != n-1) {
            if (states[i+1][j] == State.CLOSED) {
                if (field[i + 1][j] == CellValue.NUMBER) {
                    if (fieldValues[i + 1][j] != 0) {
                        states[i + 1][j] = State.OPENED;
                    } else {
                        arrayOfActions.addLast(i + 1);
                        arrayOfActions.addLast(j);
                    }
                }
            }
        }
    }

    View.OnLongClickListener mOnLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            for(int i = 0; i < n; i++)
            {
                for(int j = 0; j < n; j++)
                {
                    if(v.getId() == imageViews[i][j].getId())
                    {
                        if(states[i][j] == State.CLOSED)
                        {
                            states[i][j] = State.MARKED;
                            paint();
                        }
                        else if(states[i][j] == State.MARKED)
                        {
                            states[i][j] = State.CLOSED;
                            paint();
                        }
                    }
                }
            }
            return true;
        }
    };


    public boolean isWin()
    {
        int k = 0;
        for(int i = 0; i < n; i++)
        {
            for(int j = 0; j < n; j++)
            {
                if(field[i][j] == CellValue.BOMB) {
                    if (states[i][j] == State.MARKED) k++;
                }
            }
        }
        if(k == bombAmount) {
            showDialog(ID_GOOD_END_OF_THE_GAME);
            return true;
        }
        else
            return false;
    }


//    @Override
//    public boolean onTouchEvent(MotionEvent event)
//    {
//        int x = (int)event.getX();
//        int y = (int)event.getY();
//        State st = State.CLOSED;
//        ImageView img = (ImageView) findViewById(R.id.IW1);
//        if(event.getAction() == MotionEvent.ACTION_DOWN)
//        {
//            if (x > 50 && y > 150 && x < 530 && y < 630)
//            {
//                for (int i = 0; i < n; i++) {
//                    for (int j = 0; j < n; j++)
//                    {
//                        int xCell = arrayX[i][j], yCell = arrayY[i][j];
//                        if (xCell > i * 60 + 50 && xCell < (i + 1) * 60 + 50 && yCell > j * 60 + 50 && (j + 1) * 60 + 50 > yCell)
//                        {
//                            if (states[i][j] == State.CLOSED)
//                            {
//                                states[i][j] = State.OPENED;
//                                st = states[i][j];
//                                paint();
//                            }
//                        }
//                    }
//                }
//                Toast.makeText(getApplicationContext(), x + " " + y, Toast.LENGTH_LONG).show();
//                if (st == State.OPENED)
//                    Toast.makeText(getApplicationContext(), "OPENED", Toast.LENGTH_LONG).show();
//                else if (st == State.CLOSED)
//                    Toast.makeText(getApplicationContext(), "CLOSED", Toast.LENGTH_LONG).show();
//            }
//        }
//        return super.onTouchEvent(event);
//    }


    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder builder;
        switch (id) {
            case ID_BAD_END_OF_THE_GAME:
                builder = new AlertDialog.Builder(this);
                builder.setMessage("Your life was ended, congratulations!")
                        .setCancelable(false)
                        .setPositiveButton("Play again",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        Intent mStartActivity = new Intent(getApplicationContext(), MainActivity.class);
                                        int mPendingIntentId = 123456;
                                        PendingIntent mPendingIntent = PendingIntent.getActivity(getApplicationContext(), mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                                        AlarmManager mgr = (AlarmManager)getApplication().getSystemService(Context.ALARM_SERVICE);
                                        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                                        finish();
                                        dialog.cancel();
                                    }
                                })
                        .setNegativeButton("Pounce in window",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                        finish();
                                    }
                                });

                return builder.create();
            case ID_GOOD_END_OF_THE_GAME:
                builder = new AlertDialog.Builder(this);
                builder.setMessage("You won!")
                        .setCancelable(false)
                        .setPositiveButton("Play again",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        Intent mStartActivity = new Intent(getApplicationContext(), MainActivity.class);
                                        int mPendingIntentId = 123456;
                                        PendingIntent mPendingIntent = PendingIntent.getActivity(getApplicationContext(), mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                                        AlarmManager mgr = (AlarmManager)getApplication().getSystemService(Context.ALARM_SERVICE);
                                        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                                        finish();
                                        dialog.cancel();
                                    }
                                })
                        .setNegativeButton("Exit",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                        finish();
                                    }
                                });

                return builder.create();
            default:
                return null;
        }
    }


    void restart()
    {
        for(int i = 0; i < n; i++)
        {
            for(int j = 0; j < n; j++)
            {
                states[i][j] = State.CLOSED;
                field[i][j] = CellValue.UNDEFINED;
                fieldValues[i][j] = -2;
            }
        }
    }

    public void paint()
    {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(indentLeft, indentTop, 10, 10);
                if(states[i][j] == State.OPENED)
                {
                    if(fieldValues[i][j] != -1)
                    imageViews[i][j].setImageResource(imagesID[fieldValues[i][j]]);
                    else
                        imageViews[i][j].setImageResource(imagesID[11]);
                }
                else if(states[i][j] == State.MARKED)
                {
                    imageViews[i][j].setImageResource(imagesID[10]);
                }
                else if(states[i][j] == State.CLOSED)
                {
                    if(fieldValues[i][j] != -1)
                        imageViews[i][j].setImageResource(imagesID[9]);
                    else imageViews[i][j].setImageResource(imagesID[9]);
                }
                //relative_layout.addView(imageViews[i][j], layoutParams);
                indentLeft += CELL_DISTANCE;
            }
            indentLeft = LEFT_MARGIN;
            indentTop += CELL_DISTANCE;
        }
        indentLeft = LEFT_MARGIN;
        indentTop = TOP_MARGIN;
        if(isWin())
        {
            openCells();
        }
    }

    public void emptyField(int a[][], int i, int j)
    {
        int k = 0;
        states[i][j] = State.OPENED;
            if (i == 0 && j == 0) {
                if (a[i][j + 1] == 0) {
                    emptyField(a,i,j+1);
                    k++;
                }
                else {
                    states[i][j+1] = State.OPENED;
                }
                if (a[i + 1][j] == 0) {
                    emptyField(a,i+1,j);
                    k++;
                }
                else {
                    states[i+1][j] = State.OPENED;
                }
                if (a[i + 1][j + 1] == 0) {
                    emptyField(a,i+1,j+1);
                    k++;
                }
                else {
                    states[i+1][j+1] = State.OPENED;
                }
                paint();
            } else if (i == 0 && j == n - 1) {
                if (a[i][j - 1] == 0) {
                    emptyField(a,i,j-1);
                    k++;
                }
                else {
                    states[i][j-1] = State.OPENED;
                }
                if (a[i + 1][j] == 0) {
                    emptyField(a,i+1,j);
                    k++;
                }
                else {
                    states[i+1][j] = State.OPENED;
                }
                if (a[i + 1][j - 1] == 0) {
                    emptyField(a,i+1,j-1);
                    k++;
                }
                else {
                    states[i+1][j-1] = State.OPENED;
                }
                paint();
            } else if (i == n - 1 && j == n - 1) {
                if (a[i][j - 1] == 0) {
                    emptyField(a,i,j-1);
                    k++;
                }
                else {
                    states[i][j-1] = State.OPENED;
                }
                if (a[i - 1][j] == 0) {
                    emptyField(a,i-1,j);
                    k++;
                }
                else {
                    states[i-1][j] = State.OPENED;
                }
                if (a[i - 1][j - 1] == 0) {
                    emptyField(a,i-1,j-1);
                    k++;
                }
                else {
                    states[i-1][j-1] = State.OPENED;
                }
                paint();
            } else if (i == n - 1 && j == 0) {
                if (a[i][j + 1] == 0) {
                    emptyField(a,i,j+1);
                    k++;
                }
                else {
                    states[i][j+1] = State.OPENED;
                }
                if (a[i - 1][j] == 0) {
                    emptyField(a,i-1,j);
                    k++;
                }
                else {
                    states[i-1][j] = State.OPENED;
                }
                if (a[i - 1][j + 1] == 0) {
                    emptyField(a,i-1,j+1);
                    k++;
                }
                else {
                    states[i-1][j+1] = State.OPENED;
                }
            } else if (i == 0 && j != 0 && j != n - 1) {
                for (int i1 = i; i1 < i + 2; i1++) {
                    for (int j1 = j - 1; j1 < j + 2; j1++) {
                        if (i1 == i && j1 == j) continue;
                        else {
                            if (a[i1][j1] == 0) {
                                emptyField(a,i1,j1);
                                k++;
                            }
                            else {
                                states[i1][j1] = State.OPENED;
                            }
                        }
                    }
                }
                paint();
            } else if (j == n - 1 && i != n - 1 && i != 0) {
                for (int i1 = i - 1; i1 < i + 2; i1++) {
                    for (int j1 = j - 1; j1 < j + 1; j1++) {
                        if (i1 == i && j1 == j) continue;
                        else {
                            if (a[i1][j1] == 0) {
                                emptyField(a,i1,j1);
                                k++;
                            }
                            else {
                                states[i1][j1] = State.OPENED;
                            }
                        }
                    }
                }
                paint();
            } else if (i == n - 1 && j != n - 1 && j != 0) {
                for (int i1 = i - 1; i1 < i + 1; i1++) {
                    for (int j1 = j - 1; j1 < j + 2; j1++) {
                        if (i1 == i && j1 == j) continue;
                        else {
                            if (a[i1][j1] == 0) {
                                emptyField(a,i1,j1);
                                k++;
                            }
                            else {
                                states[i1][j1] = State.OPENED;
                            }
                        }
                    }
                }
                paint();
            } else if (j == 0 && i != 0 && i != n - 1) {
                for (int i1 = i - 1; i1 < i + 2; i1++) {
                    for (int j1 = j; j1 < j + 2; j1++) {
                        if (i1 == i && j1 == j) continue;
                        else {
                            if (a[i1][j1] == 0) {
                                emptyField(a,i1,j1);
                                k++;
                            }
                            else {
                                states[i1][j1] = State.OPENED;
                            }
                        }
                    }
                }
                paint();
            } else if (i != 0 && i != n - 1 && j != 0 && j != n - 1) {
                for (int i1 = i - 1; i1 < i + 2; i1++) {
                    for (int j1 = j - 1; j1 < j + 2; j1++) {

                        if (i1 == i && j1 == j) continue;
                        else {
                            if (a[i1][j1] == 0) {
                                emptyField(a,i1,j1);
                                k++;
                            }
                            else {
                                states[i1][j1] = State.OPENED;
                            }
                        }
                    }
                }
                paint();
            }
    }
}